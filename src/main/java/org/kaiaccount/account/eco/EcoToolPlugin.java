package org.kaiaccount.account.eco;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.eco.commands.BukkitCommands;
import org.kaiaccount.account.inter.vault.VaultEmulationUtils;
import org.mose.command.BukkitCommandWrapper;

import java.io.IOException;

public class EcoToolPlugin extends JavaPlugin {

	private static EcoToolPlugin plugin;

	public EcoToolPlugin() {
		plugin = this;
	}

	@Override
	public void onLoad() {
		AccountInterface.setGlobal(new EcoManager());
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
	}

	@Override
	public void onEnable() {
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

	public static EcoToolPlugin getPlugin() {
		return plugin;
	}
}
