package org.kaiaccount.account.eco.commands.argument.account;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.inter.type.Account;
import org.mose.command.CommandArgument;
import org.mose.command.CommandArgumentResult;
import org.mose.command.arguments.collection.source.UserArgument;
import org.mose.command.arguments.operation.MappedArgumentWrapper;
import org.mose.command.context.CommandArgumentContext;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedTransferQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountArgument<A extends Account> implements CommandArgument<A> {

    private final String id;
    private final Collection<CommandArgument<? extends A>> arguments = new LinkedTransferQueue<>();

    @Deprecated
    public AccountArgument(String id) {
        throw new RuntimeException("Arguments must be passed in");
    }

    public AccountArgument(String id, CommandArgument<? extends A>... arguments) {
        this(id, List.of(arguments));
    }

    public AccountArgument(String id, Collection<CommandArgument<? extends A>> argument) {
        if (argument.isEmpty()) {
            throw new RuntimeException("Arguments must be passed in");
        }
        this.id = id;
        this.arguments.addAll(argument);
    }

    public static AccountArgument<Account> allAccounts(String id) {
        return new AccountArgument<>(id,
                new NamedAccountArgument("account"),
                PlayerBankArgument.allPlayerBanks("bank"),
                new MappedArgumentWrapper<>(
                        new UserArgument("player", u -> true),
                        user -> AccountInterface.getManager().getPlayerAccount(user)));
    }

    @Override
    public @NotNull String getId() {
        return this.id;
    }

    @Override
    public @NotNull CommandArgumentResult<A> parse(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<A> commandArgumentContext) throws IOException {
        if (commandContext.getCommand().length < commandArgumentContext.getFirstArgument() + 1) {
            throw new IOException("Not enough arguments");
        }

        String peek = commandArgumentContext.getFocusArgument();

        for (CommandArgument<? extends A> argument : this.arguments) {
            if (!argument.getId().equalsIgnoreCase(peek)) {
                continue;
            }
            CommandArgumentResult<? extends A> result = argumentParse(argument, commandContext, commandArgumentContext);
            return new CommandArgumentResult<>(result.getPosition(), result.value());
        }
        throw new IOException("Unknown account type of " + peek);
    }

    @Override
    public @NotNull Collection<String> suggest(@NotNull CommandContext commandContext, @NotNull CommandArgumentContext<A> commandArgumentContext) {
        String[] remaining = commandArgumentContext.getRemainingArguments();
        String peek = commandArgumentContext.getFocusArgument().toLowerCase();

        if (remaining.length == 1) {
            return this.arguments.stream().map(CommandArgument::getId).filter(id -> id.toLowerCase().startsWith(peek)).collect(Collectors.toSet());
        }
        Optional<CommandArgument<? extends A>> opArgument = this.arguments.parallelStream().filter(argument -> argument.getId().equalsIgnoreCase(peek)).findAny();
        if (opArgument.isEmpty()) {
            return Collections.emptyList();
        }
        return argumentSuggest(opArgument.get(), commandContext, commandArgumentContext).collect(Collectors.toSet());
    }

    private <B> Stream<String> argumentSuggest(CommandArgument<B> argument, CommandContext commandContext, CommandArgumentContext<A> argumentContext) {
        return argument.suggest(commandContext, new CommandArgumentContext<>(argument, argumentContext.getFirstArgument() + 1, commandContext.getCommand())).stream();
    }

    private <B> CommandArgumentResult<B> argumentParse(CommandArgument<B> argument, CommandContext commandContext, CommandArgumentContext<A> argumentContext) throws IOException {
        return argument.parse(commandContext, new CommandArgumentContext<>(argument, argumentContext.getFirstArgument() + 1, commandContext.getCommand()));
    }
}
