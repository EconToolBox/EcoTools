package org.kaiaccount.account.eco.commands.balance;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.eco.message.Messages;
import org.kaiaccount.account.eco.permission.Permissions;
import org.kaiaccount.account.eco.utils.CommonUtils;
import org.kaiaccount.account.inter.currency.Currency;
import org.kaiaccount.account.inter.type.Account;
import org.mose.command.ArgumentCommand;
import org.mose.command.CommandArgument;
import org.mose.command.context.CommandContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class CheckBalanceCommand implements ArgumentCommand {
	@Override
	public @NotNull List<CommandArgument<?>> getArguments() {
		return Collections.emptyList();
	}

	@Override
	public @NotNull String getDescription() {
		return "Check your balance";
	}

	@Override
	public @NotNull Optional<String> getPermissionNode() {
		return Optional.of(Permissions.BALANCE_SELF.getPermissionNode());
	}

	@Override
	public boolean hasPermission(CommandSender source) {
		if (!(source instanceof OfflinePlayer)) {
			return false;
		}
		return ArgumentCommand.super.hasPermission(source);
	}

	@Override
	public boolean run(CommandContext commandContext, String... strings) {
		return Bukkit.dispatchCommand(commandContext.getSource(), "balance player");
	}

	static boolean displayInfo(@NotNull CommandSender sender, @NotNull Account account) {
		Map<Currency<?>, BigDecimal> balances = account.getBalances();
		balances.forEach(((currency, balance) -> sender
				.sendMessage("  " + currency.formatSymbol(balance))));

		Currency<?> defaultCurrency;
		try {
			defaultCurrency = AccountInterface.getManager().getDefaultCurrency();
		} catch (RuntimeException e) {
			sender.sendMessage("Worth cannot be calculated: " + e.getMessage());
			return true;
		}
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
			String message = Messages.TOTAL_WORTH.getProcessedMessage(sender, worth);
			sender.sendMessage(message);
		}
		return true;
	}
}
