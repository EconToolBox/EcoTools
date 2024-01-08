package org.kaiaccount.account.eco.account.named;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.io.Serializer;
import org.kaiaccount.account.inter.type.named.NamedAccountBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NamedAccountSerializer implements Serializer<EcoNamedAccount> {
    @Override
    public void serialize(@NotNull YamlConfiguration configuration, @NotNull EcoNamedAccount value) {
        value.getBalances().forEach(((currency, amount) -> configuration.set("balance." + currency.getPlugin().getName() + "." + currency.getKeyName(), amount.doubleValue())));
        configuration.set("name", value.getAccountName());
    }

    @Override
    public EcoNamedAccount deserialize(@NotNull YamlConfiguration configuration) throws IOException {
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
        String accountName = configuration.getString("name");
        if (accountName == null) {
            throw new IOException("Account is missing from file: " + configuration.getName());
        }
        return new EcoNamedAccount(new NamedAccountBuilder().setAccountName(accountName).setInitialBalance(amount));
    }
}
