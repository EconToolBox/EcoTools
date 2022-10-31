package org.kaiaccount.account.eco.commands.argument.bank;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.inter.type.bank.player.PlayerBankAccount;
import org.kaiaccount.account.inter.type.player.PlayerAccount;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

public class PlayerBankArgument implements CommandArgument<PlayerBankAccount<?>> {

	private final String id;
	private final BiFunction<CommandContext, CommandArgumentContext<PlayerBankAccount<?>>,
			Collection<PlayerBankAccount<?>>>
			function;

	public PlayerBankArgument(@NotNull String id,
			BiFunction<CommandContext, CommandArgumentContext<PlayerBankAccount<?>>, Collection<PlayerBankAccount<?>>> function) {
		this.id = id;
		this.function = function;
	}

	@Override
	public @NotNull String getId() {
		return this.id;
	}


	@Override
	public @NotNull CommandArgumentResult<PlayerBankAccount<?>> parse(@NotNull CommandContext commandContext,
			@NotNull CommandArgumentContext<PlayerBankAccount<?>> commandArgumentContext) throws IOException {
		Collection<PlayerBankAccount<?>> banks = this.function.apply(commandContext, commandArgumentContext);
		String peek = commandArgumentContext.getFocusArgument().toLowerCase();
		String playerOwner = null;
		String bankName = peek;
		if (peek.contains(".")) {
			String[] split = peek.split(Pattern.quote("."));
			if (split.length > 1) {
				playerOwner = split[0];
				bankName = split[1];
			}
		}

		String finalPlayerOwner = playerOwner;
		String finalBankName = bankName;

		Optional<PlayerBankAccount<?>> opBank = banks.parallelStream()
				.filter(name -> name.getBankAccountName().toLowerCase().equals(finalBankName))
				.filter(name -> {
					if (finalPlayerOwner == null) {
						return true;
					}
					String playerName = name.getAccountHolder().getPlayer().getName();
					if (playerName == null) {
						return true;
					}
					return playerName.equals(finalPlayerOwner);
				})
				.findAny();
		if (opBank.isEmpty()) {
			throw new IOException("No bank by that name");
		}
		return CommandArgumentResult.from(commandArgumentContext, opBank.get());
	}

	@Override
	public @NotNull Collection<String> suggest(@NotNull CommandContext commandContext,
			@NotNull CommandArgumentContext<PlayerBankAccount<?>> commandArgumentContext) {
		Collection<PlayerBankAccount<?>> banks = this.function.apply(commandContext, commandArgumentContext);
		String peek = commandArgumentContext.getFocusArgument().toLowerCase();
		return banks.parallelStream()
				.filter(name -> name.getBankAccountName().toLowerCase().startsWith(peek) || (name.getAccountHolder()
						.getPlayer()
						.getName() + "." + name.getBankAccountName()).toLowerCase().startsWith(peek))
				.flatMap(name -> Arrays.asList(name.getBankAccountName(),
								name.getAccountHolder().getPlayer().getName() + "." + name.getBankAccountName())
						.parallelStream())
				.toList();
	}

	public static @NotNull PlayerBankArgument allPlayerBanks(@NotNull String id) {
		return new PlayerBankArgument(id, (context, argument) -> AccountInterface.getManager()
				.getPlayerAccounts()
				.parallelStream()
				.flatMap(player -> player.getBanks().parallelStream())
				.toList());
	}

	public static @NotNull PlayerBankArgument senderBanks(@NotNull String id) {
		return new PlayerBankArgument(id, (context, argument) -> {
			if (!(context.getSource() instanceof OfflinePlayer player)) {
				return Collections.emptySet();
			}
			PlayerAccount<?> account = AccountInterface.getManager().getPlayerAccount(player);
			return account.getBanks();
		});
	}
}
