package org.kaiaccount.account.eco.message;

import org.kaiaccount.account.eco.message.messages.balance.TotalWorthMessage;

public final class Messages {

	public static final TotalWorthMessage TOTAL_WORTH = new TotalWorthMessage(null);

	private Messages() {
		throw new RuntimeException("Dont do that");
	}
}
