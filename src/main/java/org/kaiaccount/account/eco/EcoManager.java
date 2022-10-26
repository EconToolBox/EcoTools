package org.kaiaccount.account.eco;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterfaceManager;
import org.kaiaccount.account.eco.account.EcoPlayerAccount;
import org.kaiaccount.account.eco.bank.EcoBankAccount;
import org.kaiaccount.account.eco.currency.EcoCurrency;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.currency.ToCurrency;
import org.kaiaccount.account.inter.type.bank.player.ToBankAccount;
import org.kaiaccount.account.inter.type.player.PlayerAccount;
import org.kaiaccount.account.inter.type.player.ToPlayerAccount;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public class EcoManager implements AccountInterfaceManager {
	private final Collection<Currency<?>> currencies = new LinkedTransferQueue<>();
	private final Collection<PlayerAccount<?>> playerAccounts = new LinkedBlockingQueue<>();

	@Override
	public @NotNull EcoToolPlugin getVaultPlugin() {
		return EcoToolPlugin.getPlugin();
	}

	@Override
	public ToCurrency getToCurrencies() {
		return EcoCurrency::new;
	}

	@Override
	public ToBankAccount getToBankAccount() {
		return EcoBankAccount::new;
	}

	@Override
	public ToPlayerAccount getToPlayerAccount() {
		return EcoPlayerAccount::new;
	}

	@Override
	public @NotNull Collection<Currency<?>> getCurrencies() {
		return Collections.unmodifiableCollection(this.currencies);
	}

	@Override
	public @NotNull Collection<PlayerAccount<?>> getPlayerAccounts() {
		return Collections.unmodifiableCollection(this.playerAccounts);
	}

	@Override
	public void registerPlayerAccount(@NotNull PlayerAccount<?> account) {
		this.playerAccounts.add(account);
	}

	@Override
	public PlayerAccount<?> loadPlayerAccount(@NotNull OfflinePlayer player) {
		PlayerAccount<?> account = EcoToolPlugin.getPlugin().loadPlayerAccount(player.getUniqueId());
		this.registerPlayerAccount(account);
		return account;
	}

	@Override
	public void deregisterPlayerAccount(@NotNull PlayerAccount<?> account) {
		this.playerAccounts.remove(account);
	}

	@Override
	public void registerCurrency(@NotNull Currency<?> currency) {
		this.currencies.add(currency);
	}

	@Override
	public void deregisterCurrency(@NotNull Currency<?> currency) {
		this.currencies.remove(currency);
	}
}
