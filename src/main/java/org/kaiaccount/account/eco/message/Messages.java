package org.kaiaccount.account.eco.message;

import org.kaiaccount.account.eco.message.messages.balance.TotalWorthMessage;
import org.kaiaccount.account.eco.message.messages.error.SourceOnlyCommandMessage;

public final class Messages {

	public static final TotalWorthMessage TOTAL_WORTH = new TotalWorthMessage(null);
	public static final SourceOnlyCommandMessage SOURCE_ONLY = new SourceOnlyCommandMessage(null);

	private Messages() {
		throw new RuntimeException("Dont do that");
	}
}
