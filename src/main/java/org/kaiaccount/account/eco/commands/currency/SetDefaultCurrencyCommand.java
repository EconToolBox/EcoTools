package org.kaiaccount.account.eco.commands.currency;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.eco.commands.argument.currency.CurrencyArgument;
import org.kaiaccount.account.eco.permission.Permissions;
import org.kaiaccount.account.inter.currency.Currency;
import org.mose.command.ArgumentCommand;
import org.mose.command.CommandArgument;
import org.mose.command.arguments.operation.ExactArgument;
import org.mose.command.context.CommandContext;

import java.util.List;
import java.util.Optional;

public class SetDefaultCurrencyCommand implements ArgumentCommand {

	public static final CommandArgument<String> SET = new ExactArgument("set");
	public static final CommandArgument<String> DEFAULT = new ExactArgument("default");
	public static final CommandArgument<Currency<?>> CURRENCY = new CurrencyArgument("currency");

	@Override
	public @NotNull List<CommandArgument<?>> getArguments() {
		return List.of(SET, DEFAULT, CURRENCY);
	}

	@Override
	public @NotNull String getDescription() {
		return "Sets the default currency";
	}

	@Override
	public @NotNull Optional<String> getPermissionNode() {
		return Optional.of(Permissions.SET_DEFAULT_CURRENCY.getPermissionNode());
	}

	@Override
	public boolean run(CommandContext commandContext, String... args) {
		Currency<?> newDefault = commandContext.getArgument(this, CURRENCY);
		Currency<?> previousDefault = AccountInterface.getGlobal().getDefaultCurrency();
		newDefault.setDefault(true);
		previousDefault.setDefault(false);
		commandContext.getSource()
				.sendMessage("Default currency swapped from "
						+ previousDefault.getKeyName()
						+ " to "
						+ newDefault.getKeyName());
		return true;
	}
}
