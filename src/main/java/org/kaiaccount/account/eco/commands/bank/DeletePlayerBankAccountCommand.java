package org.kaiaccount.account.eco.commands.bank;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.eco.message.Messages;
import org.kaiaccount.account.eco.message.messages.error.SourceOnlyCommandMessage;
import org.kaiaccount.account.inter.type.named.bank.player.PlayerBankAccount;
import org.kaiaccount.account.inter.type.player.PlayerAccount;
import org.mose.command.ArgumentCommand;
import org.mose.command.CommandArgument;
import org.mose.command.arguments.operation.ExactArgument;
import org.mose.command.arguments.simple.text.StringArgument;
import org.mose.command.context.CommandContext;

import java.util.List;
import java.util.Optional;

public class DeletePlayerBankAccountCommand implements ArgumentCommand {

    public static final ExactArgument CREATE = new ExactArgument("close");
    public static final StringArgument NAME = new StringArgument("name");

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return List.of(CREATE, NAME);
    }

    @Override
    public @NotNull String getDescription() {
        return "Deletes a bank account";
    }

    @Override
    public @NotNull Optional<String> getPermissionNode() {
        return Optional.empty();
    }

    @Override
    public boolean run(CommandContext commandContext, String... strings) {
        String bankName = commandContext.getArgument(this, NAME);
        if (!(commandContext.getSource() instanceof OfflinePlayer player)) {
            commandContext.getSource()
                    .sendMessage(Messages.SOURCE_ONLY.getProcessedMessage(SourceOnlyCommandMessage.PLAYER_SOURCE));
            return true;
        }
        PlayerAccount<?> account = AccountInterface.getManager().getPlayerAccount(player);
        if (!account.getBank(newBankName).isPresent()) {
            commandContext.getSource().sendMessage("Bank does not exist");
            return true;
        }
        PlayerBankAccount bankAccount = account.deleteBankAccount(bankName);
        commandContext.getSource().sendMessage("Created " + bankAccount.getAccountName());
        return true;
    }
}
