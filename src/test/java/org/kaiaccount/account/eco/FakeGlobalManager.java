package org.kaiaccount.account.eco;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterfaceManager;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.currency.ToCurrency;
import org.kaiaccount.account.inter.type.bank.player.ToBankAccount;
import org.kaiaccount.account.inter.type.player.PlayerAccount;
import org.kaiaccount.account.inter.type.player.ToPlayerAccount;

import java.util.Collection;
import java.util.LinkedList;

public class FakeGlobalManager implements AccountInterfaceManager {

	public final Collection<Currency<?>> currencies = new LinkedList<>();
	public ToCurrency toCurrency;
	public ToBankAccount toBankAccount;
	public final Collection<PlayerAccount<?>> accounts = new LinkedList<>();

	@Override
	public Plugin getVaultPlugin() {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public ToCurrency getToCurrencies() {
		return this.toCurrency;
	}

	@Override
	public ToBankAccount getToBankAccount() {
		return this.toBankAccount;
	}

	@NotNull
	@Override
	public Collection<Currency<?>> getCurrencies() {
		return this.currencies;
	}

	@NotNull
	@Override
	public Collection<PlayerAccount<?>> getPlayerAccounts() {
		return this.accounts;
	}

	@Override
	public ToPlayerAccount getToPlayerAccount() {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void registerPlayerAccount(@NotNull PlayerAccount<?> account) {
		throw new RuntimeException("Not implemented yet");

	}

	@Override
	public PlayerAccount<?> loadPlayerAccount(@NotNull OfflinePlayer player) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void deregisterPlayerAccount(@NotNull PlayerAccount<?> account) {
		throw new RuntimeException("Not implemented yet");

	}

	@Override
	public void registerCurrency(@NotNull Currency<?> currency) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void deregisterCurrency(@NotNull Currency<?> currency) {
		throw new RuntimeException("Not implemented yet");
	}
}
