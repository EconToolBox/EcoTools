package org.kaiaccount.account.eco.commands;

import org.kaiaccount.account.eco.commands.balance.CheckBalanceCommand;
import org.kaiaccount.account.eco.commands.currency.AddCurrencyCommand;
import org.kaiaccount.account.eco.commands.currency.RemoveCurrencyCommand;
import org.kaiaccount.account.eco.commands.currency.SetDefaultCurrencyCommand;
import org.kaiaccount.account.eco.commands.currency.SetExchangeValueCommand;
import org.kaiaccount.account.eco.commands.ecotools.GiveEcoCommand;
import org.kaiaccount.account.eco.commands.ecotools.InfoCommand;
import org.kaiaccount.account.eco.commands.exchange.ExchangeCommand;
import org.kaiaccount.account.eco.commands.pay.PayPlayerCommand;

public final class Commands {

	public static final CheckBalanceCommand CHECK_BALANCE = new CheckBalanceCommand();
	public static final AddCurrencyCommand ADD_CURRENCY = new AddCurrencyCommand();
	public static final RemoveCurrencyCommand REMOVE_CURRENCY = new RemoveCurrencyCommand();
	public static final SetDefaultCurrencyCommand SET_DEFAULT_CURRENCY = new SetDefaultCurrencyCommand();

	public static final SetExchangeValueCommand SET_EXCHANGE_CURRENCY = new SetExchangeValueCommand();
	public static final InfoCommand INFO = new InfoCommand();
	public static final GiveEcoCommand GIVE_ECO = new GiveEcoCommand();
	public static final ExchangeCommand EXCHANGE = new ExchangeCommand();

	public static final PayPlayerCommand PAY = new PayPlayerCommand();

	private Commands() {
		throw new RuntimeException("Dont do that");
	}
}
