package org.kaiaccount.account.eco;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.AccountInterfaceManager;
import org.kaiaccount.account.eco.account.EcoPlayerAccount;
import org.kaiaccount.account.eco.commands.BukkitCommands;
import org.kaiaccount.account.eco.io.EcoSerializers;
import org.kaiaccount.account.inter.io.Serializable;
import org.kaiaccount.account.inter.io.Serializer;
import org.kaiaccount.account.inter.type.player.PlayerAccount;
import org.kaiaccount.account.inter.vault.VaultEmulationUtils;
import org.mose.command.BukkitCommandWrapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class EcoToolPlugin extends JavaPlugin {

	private static EcoToolPlugin plugin;

	public EcoToolPlugin() {
		plugin = this;
	}

	@Override
	public void onLoad() {
		Bukkit.getServicesManager()
				.register(AccountInterfaceManager.class, new EcoManager(), this, ServicePriority.Normal);
		Plugin vaultPlugin = this;
		try {
			vaultPlugin = VaultEmulationUtils.loadVault();
		} catch (IOException | NoSuchFieldException | IllegalAccessException e) {
			this.getLogger()
					.warning(
							"Could not load Vault as a plugin. This may cause some plugins that require vault to "
									+ "break:");
			this.getLogger().warning("\t- " + e.getMessage());
		}
		VaultEmulationUtils.loadService(vaultPlugin);

		loadCurrencies();

	}

	@Override
	public void onEnable() {
		loadPlayerAccounts();
		loadBankAccounts();
		registerCommand("balance", BukkitCommands.BALANCE);
		registerCommand("currency", BukkitCommands.CURRENCY);
		registerCommand("ecotools", BukkitCommands.ECOTOOLS);
		registerCommand("exchange", BukkitCommands.EXCHANGE);
		registerCommand("pay", BukkitCommands.PAY);
	}

	private void registerCommand(@NotNull String command, @NotNull BukkitCommandWrapper wrapper) {
		PluginCommand bCommand = this.getCommand(command);
		if (bCommand == null) {
			throw new RuntimeException("Unknown command of " + command);
		}
		bCommand.setExecutor(wrapper);
		bCommand.setTabCompleter(wrapper);
	}

	private void loadBankAccounts() {
		for (PlayerAccount<?> account : AccountInterface.getManager().getPlayerAccounts()) {
			File folder = new File("plugins/eco/players/" + account.getPlayer().getUniqueId() + "/");
			load(folder, EcoSerializers.BANK, account::registerBank);
		}
	}

	private boolean loadPlayerAccounts() {
		File folder = new File("plugins/eco/players/");
		return load(folder, EcoSerializers.PLAYER,
				(player) -> AccountInterface.getManager().registerPlayerAccount(player));
	}

	public EcoPlayerAccount loadPlayerAccount(@NotNull UUID player) {
		File file = new File("plugins/eco/players/" + player + ".yml");
		try {
			return loadSingle(file, EcoSerializers.PLAYER);
		} catch (IOException e) {
			throw new RuntimeException("load error", e);
		}
	}

	private boolean loadCurrencies() {
		File folder = new File("plugins/eco/currencies/" + this.getName() + "/");
		return load(folder, EcoSerializers.CURRENCY, (c) -> AccountInterface.getManager().registerCurrency(c));
	}

	@SuppressWarnings("SameParameterValue")
	private <T extends Serializable<T>> T loadSingle(@NotNull File file, @NotNull Serializer<T> serializer) throws
			IOException {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		return serializer.deserialize(config);
	}

	private <T extends Serializable<T>> boolean load(File folder, Serializer<T> serializer, Consumer<T> onEach) {
		File[] files = folder.listFiles();
		if (files == null) {
			return false;
		}
		Arrays
				.stream(files)
				.parallel()
				.map(file -> {
					YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
					try {
						return serializer.deserialize(config);
					} catch (IOException e) {
						e.printStackTrace();
						//noinspection ReturnOfNull
						return null;
					}
				})
				.filter(Objects::nonNull)
				.forEach(onEach);
		return true;
	}

	public static EcoToolPlugin getPlugin() {
		return plugin;
	}
}
