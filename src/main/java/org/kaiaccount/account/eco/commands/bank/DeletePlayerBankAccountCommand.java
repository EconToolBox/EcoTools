package org.kaiaccount.account.eco.commands.bank;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kaiaccount.AccountInterface;
import org.kaiaccount.account.eco.commands.argument.account.PlayerBankArgument;
import org.kaiaccount.account.eco.message.Messages;
import org.kaiaccount.account.eco.message.messages.error.SourceOnlyCommandMessage;
import org.kaiaccount.account.eco.permission.Permissions;
import org.kaiaccount.account.inter.type.named.bank.player.PlayerBankAccount;
import org.kaiaccount.account.inter.type.player.PlayerAccount;
import org.mose.command.ArgumentCommand;
import org.mose.command.CommandArgument;
import org.mose.command.arguments.operation.ExactArgument;
import org.mose.command.arguments.simple.text.StringArgument;
import org.mose.command.context.CommandContext;
import org.mose.command.arguments.operation.permission.PermissionOrArgument;
import java.util.List;
import java.util.Optional;

public class DeletePlayerBankAccountCommand implements ArgumentCommand {

    public static final ExactArgument CLOSE = new ExactArgument("close");
    public static final PlayerBankArgument BANK = new PermissionOrArgument<>("bank",
            sender -> (sender.hasPermission(
                    Permissions.BALANCE_OTHER.getPermissionNode())),
            PlayerBankArgument.allPlayerBanks("bank"), PlayerBankArgument.senderBanks("bank"));

    @Override
    public @NotNull List<CommandArgument<?>> getArguments() {
        return List.of(CLOSE, BANK);
    }

    @Override
    public @NotNull String getDescription() {
        return "Deletes a bank account";
    }

    @Override
    public @NotNull Optional<String> getPermissionNode() {
        return Optional.of(Permissions.CREATE_BANK_ACCOUNT.getPermissionNode());
    }

    @Override
    public boolean run(CommandContext commandContext, String... strings) {
        PlayerBankAccount bank = commandContext.getArgument(this, BANK);
        if (!(commandContext.getSource() instanceof OfflinePlayer player)) {
            commandContext.getSource()
                    .sendMessage(Messages.SOURCE_ONLY.getProcessedMessage(SourceOnlyCommandMessage.PLAYER_SOURCE));
            return true;
        }
        if (!bank.isPresent()) {
            commandContext.getSource().sendMessage("Bank does not exist");
            return true;
        }
        PlayerAccount<?> account = bank.getAccountHolder();
        BigDecimal oldBalance = account.getBalance();
        Payment payment = new PaymentBuilder()
        .setAmount(account.getBalance(AccountInterface.getManager().getDefaultCurrency()))
        .setCurrency(AccountInterface.getManager().getDefaultCurrency())
        .build(EcoToolPlugin.getPlugin());

        this.deposit(payment);
        BigDecimal newBalance = account.getBalance();

        if (newBalance.compareTo(oldBalance) == 0) {
            commandContext.getSource().sendMessage("Bank account deletion failed");
            return true;
        }
		account.deleteBankAccount(bank);

        commandContext.getSource().sendMessage("Deleted " + bank.getAccountName());
        return true;
    }
}
