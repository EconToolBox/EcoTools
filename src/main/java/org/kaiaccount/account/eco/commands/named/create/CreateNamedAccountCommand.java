package org.kaiaccount.account.eco.commands.named.create;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.eco.account.named.EcoNamedAccount;
import org.kaiaccount.account.eco.permission.Permissions;
import org.kaiaccount.account.inter.type.named.NamedAccount;
import org.kaiaccount.account.inter.type.named.NamedAccountBuilder;
import org.mose.command.ArgumentCommand;
import org.mose.command.CommandArgument;
import org.mose.command.arguments.operation.ExactArgument;
import org.mose.command.arguments.simple.text.StringArgument;
import org.mose.command.context.CommandContext;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CreateNamedAccountCommand implements ArgumentCommand {

    public static final StringArgument NAME = new StringArgument("name");

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return List.of(new ExactArgument("create"), NAME);
    }

    @Override
    public @NotNull String getDescription() {
        return "Creates a named account";
    }

    @Override
    public @NotNull Optional<String> getPermissionNode() {
        return Optional.of(Permissions.CREATE_NAMED_ACCOUNT.getPermissionNode());
    }

    @Override
    public boolean run(CommandContext commandContext, String... strings) {
        String accountName = commandContext.getArgument(this, NAME);
        Optional<NamedAccount> opFoundAccount = AccountInterface.getManager().getNamedAccount(accountName);
        if (opFoundAccount.isPresent()) {
            commandContext.getSource().sendMessage("Could not create: " + accountName + " is already present");
            return false;
        }
        EcoNamedAccount namedAccount = new EcoNamedAccount(new NamedAccountBuilder().setAccountName(accountName));
        try {
            namedAccount.save();
            AccountInterface.getManager().registerNamedAccount(namedAccount);
            commandContext.getSource().sendMessage("Created " + accountName + "");
        } catch (IOException e) {
            commandContext.getSource().sendMessage("Technical error: Could not create: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }
}
