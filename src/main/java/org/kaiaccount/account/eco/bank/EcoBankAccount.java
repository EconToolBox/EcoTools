package org.kaiaccount.account.eco.bank;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.eco.io.EcoSerializers;
import org.kaiaccount.account.inter.io.Serializable;
import org.kaiaccount.account.inter.io.Serializer;
import org.kaiaccount.account.inter.type.bank.player.AbstractPlayerBankAccount;
import org.kaiaccount.account.inter.type.bank.player.PlayerBankAccount;
import org.kaiaccount.account.inter.type.bank.player.PlayerBankAccountBuilder;

import java.io.File;

public class EcoBankAccount extends AbstractPlayerBankAccount<EcoBankAccount>
		implements PlayerBankAccount<EcoBankAccount>, Serializable<EcoBankAccount> {


	public EcoBankAccount(@NotNull PlayerBankAccountBuilder builder) {
		super(builder);
	}

	@Override
	public Serializer<EcoBankAccount> getSerializer() {
		return EcoSerializers.BANK;
	}

	@Override
	public File getFile() {
		return new File("plugins/eco/players/"
				+ this.getAccountHolder().getPlayer().getUniqueId()
				+ "/"
				+ this.getBankAccountName()
				+ ".yml");

	}
}
