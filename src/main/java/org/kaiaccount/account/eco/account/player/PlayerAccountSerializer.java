package org.kaiaccount.account.eco.account.player;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.io.Serializer;
import org.kaiaccount.account.inter.type.player.PlayerAccountBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerAccountSerializer implements Serializer<EcoPlayerAccount> {


	@Override
	public void serialize(@NotNull YamlConfiguration configuration, @NotNull EcoPlayerAccount value) {
		value.getBalances()
				.forEach((currency, amount) -> configuration.set(
						"balance." + currency.getPlugin().getName() + "." + currency.getKeyName(),
						amount.doubleValue()));
		configuration.set("id", value.getPlayer().getUniqueId().toString());
	}

	@Override
	public EcoPlayerAccount deserialize(@NotNull YamlConfiguration configuration) throws IOException {
		Map<Currency<?>, BigDecimal> amount =
				AccountInterface.getManager()
						.getCurrencies()
						.parallelStream()
						.map(currency -> {
							double value = configuration.getDouble(
									"balance." + currency.getPlugin().getName() + "." + currency.getKeyName());
							return new AbstractMap.SimpleImmutableEntry<>(currency, value);
						})
						.filter(entry -> entry.getValue() != 0.0)
						.collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey,
								value -> BigDecimal.valueOf(value.getValue())));
		String accountId = configuration.getString("id");
		if (accountId == null) {
			throw new IOException("Account is missing from file: " + configuration.getName());
		}
		OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(UUID.fromString(accountId));

		return new EcoPlayerAccount(new PlayerAccountBuilder().setPlayer(player).setInitialBalance(amount));
	}
}
