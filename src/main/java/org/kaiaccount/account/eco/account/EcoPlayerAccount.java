package org.kaiaccount.account.eco.account;

import org.kaiaccount.account.eco.io.EcoSerializers;
import org.kaiaccount.account.inter.io.Serializable;
import org.kaiaccount.account.inter.io.Serializer;
import org.kaiaccount.account.inter.type.player.AbstractPlayerAccount;
import org.kaiaccount.account.inter.type.player.PlayerAccountBuilder;

import java.io.File;

public class EcoPlayerAccount extends AbstractPlayerAccount<EcoPlayerAccount>
		implements Serializable<EcoPlayerAccount> {
	public EcoPlayerAccount(PlayerAccountBuilder builder) {
		super(builder);
	}

	@Override
	public Serializer<EcoPlayerAccount> getSerializer() {
		return EcoSerializers.PLAYER;
	}

	@Override
	public File getFile() {
		return new File("plugins/eco/players/" + this.getPlayer().getUniqueId() + ".yml");
	}
}
