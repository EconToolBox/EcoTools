package org.kaiaccount.account.eco.commands.currency;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.eco.commands.argument.currency.CurrencyArgument;
import org.kaiaccount.account.eco.permission.Permissions;
import org.kaiaccount.account.inter.currency.Currency;
import org.mose.command.ArgumentCommand;
import org.mose.command.CommandArgument;
import org.mose.command.arguments.operation.ExactArgument;
import org.mose.command.arguments.operation.OptionalArgument;
import org.mose.command.arguments.simple.number.DoubleArgument;
import org.mose.command.context.CommandContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class SetExchangeValueCommand implements ArgumentCommand {

	public static final CommandArgument<String> SET = new ExactArgument("set");
	public static final CommandArgument<String> EXCHANGE = new ExactArgument("exchange");
	public static final CommandArgument<Currency<?>> CURRENCY = new CurrencyArgument("currency");
	public static final CommandArgument<Double> VALUE = new OptionalArgument<>(new DoubleArgument("value"), 0.0);

	@Override
	public @NotNull List<CommandArgument<?>> getArguments() {
		return List.of(SET, EXCHANGE, CURRENCY, VALUE);
	}

	@Override
	public @NotNull String getDescription() {
		return "Sets the exchange value of the specified currency";
	}

	@Override
	public @NotNull Optional<String> getPermissionNode() {
		return Optional.of(Permissions.SET_EXCHANGE_CURRENCY.getPermissionNode());
	}

	@Override
	public boolean run(CommandContext commandContext, String... strings) {
		Currency<?> currency = commandContext.getArgument(this, CURRENCY);
		double amount = commandContext.getArgument(this, VALUE);
		if (amount == 0) {
			currency.removeWorth();
			commandContext.getSource().sendMessage("Removed the exchange value of " + currency.getSymbol());
			return true;
		}
		if (amount < 0) {
			commandContext.getSource().sendMessage("Exchange value must be above 0");
			return false;
		}
		currency.setWorth(BigDecimal.valueOf(amount));
		commandContext.getSource().sendMessage("Set exchange value of " + currency.getSymbol() + " to " + amount);
		return true;
	}
}
