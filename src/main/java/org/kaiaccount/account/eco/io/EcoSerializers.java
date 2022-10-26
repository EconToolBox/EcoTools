package org.kaiaccount.account.eco.io;

import org.kaiaccount.account.eco.account.PlayerAccountSerializer;
import org.kaiaccount.account.eco.bank.BankSerializer;
import org.kaiaccount.account.eco.currency.EcoCurrencySerializer;

public final class EcoSerializers {

	public static final BankSerializer BANK = new BankSerializer();
	public static final EcoCurrencySerializer CURRENCY = new EcoCurrencySerializer();
	public static final PlayerAccountSerializer PLAYER = new PlayerAccountSerializer();

	private EcoSerializers() {
		throw new RuntimeException("Dont do that");
	}
}
