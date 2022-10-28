package org.kaiaccount.account.eco.message.balance;

import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kaiaccount.account.eco.message.Messages;
import org.mockito.Mockito;

import java.math.BigDecimal;

public class TotalWorthMessageTests {

	@Test
	public void CanProcessMessage() {
		CommandSender mockedSender = Mockito.mock(CommandSender.class);
		BigDecimal worth = BigDecimal.TEN;

		//run
		String result = Messages.TOTAL_WORTH.getProcessedMessage(mockedSender, worth);

		//assert
		Assertions.assertEquals("Total Worth: 10", result);
	}
}
