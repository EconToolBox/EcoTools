package org.kaiaccount.account.eco.commands.balance;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.eco.permission.Permissions;
import org.kaiaccount.account.eco.utils.CommonUtils;
import org.kaiaccount.account.inter.currency.Currency;
import org.mose.command.ArgumentCommand;
import org.mose.command.CommandArgument;
import org.mose.command.arguments.collection.source.UserArgument;
import org.mose.command.arguments.operation.OptionalArgument;
import org.mose.command.arguments.operation.permission.PermissionOrArgument;
import org.mose.command.context.CommandContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CheckBalanceCommand implements ArgumentCommand {

	public static final CommandArgument<OfflinePlayer> USER = new OptionalArgument<>(new PermissionOrArgument<>(
			"user",
			(sender) -> sender.hasPermission(Permissions.BALANCE_OTHER.getPermissionNode()),
			new UserArgument("user", (user) -> true)));

	@Override
	public @NotNull List<CommandArgument<?>> getArguments() {
		return List.of(USER);
	}

	@Override
	public @NotNull String getDescription() {
		return "Checks the balance of either yourself or another player";
	}

	@Override
	public @NotNull Optional<String> getPermissionNode() {
		return Optional.of(Permissions.BALANCE_SELF.getPermissionNode());
	}

	@Override
	public boolean run(CommandContext commandContext, String... strings) {
		OfflinePlayer player = commandContext.getArgument(this, USER);
		if (player == null) {
			if (!(commandContext.getSource() instanceof OfflinePlayer)) {
				commandContext.getSource().sendMessage("You are required to specify a player");
				return false;
			}
			player = (OfflinePlayer) commandContext.getSource();
		}
		Map<Currency<?>, BigDecimal> balances = AccountInterface.getGlobal().getPlayerAccount(player).getBalances();
		balances.forEach(((currency, balance) -> commandContext.getSource()
				.sendMessage("  " + currency.formatSymbol(balance))));

		Currency<?> defaultCurrency = AccountInterface.getGlobal().getDefaultCurrency();
		if (defaultCurrency.getWorth().isPresent()) {
			Collection<BigDecimal> collection = balances.entrySet()
					.parallelStream()
					.filter(entry -> entry.getKey().getWorth().isPresent())
					.map(entry -> entry.getValue()
							.divide(entry.getKey().getWorth().get(), RoundingMode.DOWN)
							.multiply(defaultCurrency.getWorth()
									.get()))
					.collect(Collectors.toSet());

			BigDecimal worth = CommonUtils.sumOf(collection.iterator());
			commandContext.getSource()
					.sendMessage("Total Worth: " + defaultCurrency.formatSymbol(worth));
		}
		return true;
	}
}
