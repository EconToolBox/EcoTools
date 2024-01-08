package org.kaiaccount.account.eco.commands.balance;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.eco.commands.argument.account.NamedAccountArgument;
import org.kaiaccount.account.eco.permission.Permissions;
import org.kaiaccount.account.inter.type.named.NamedAccount;
import org.mose.command.ArgumentCommand;
import org.mose.command.CommandArgument;
import org.mose.command.arguments.operation.ExactArgument;
import org.mose.command.context.CommandContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CheckNamedAccountBalanceCommand implements ArgumentCommand {

    public static final CommandArgument<String> NAMED = new ExactArgument("named");
    public static final CommandArgument<NamedAccount> NAMED_ACCOUNT = new NamedAccountArgument("account", (cmdContext, argContext) -> AccountInterface.getManager().getNamedAccounts());

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return Arrays.asList(NAMED, NAMED_ACCOUNT);
    }

    @Override
    public @NotNull String getDescription() {
        return "See the balance of a named account";
    }

    @Override
    public @NotNull Optional<String> getPermissionNode() {
        return Optional.of(Permissions.BALANCE_OTHER.getPermissionNode());
    }

    @Override
    public boolean run(CommandContext commandContext, String... strings) {
        NamedAccount account = commandContext.getArgument(this, NAMED_ACCOUNT);
        return CheckBalanceCommand.displayInfo(commandContext.getSource(), account);
    }
}
