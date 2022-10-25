package org.kaiaccount.account.eco.commands;

import org.mose.command.BukkitCommandWrapper;

public final class BukkitCommands {

	public static final BukkitCommandWrapper BALANCE = new BukkitCommandWrapper(Commands.CHECK_BALANCE);
	public static final BukkitCommandWrapper CURRENCY =
			new BukkitCommandWrapper(Commands.ADD_CURRENCY, Commands.SET_DEFAULT_CURRENCY);

	public static final BukkitCommandWrapper ECOTOOLS = new BukkitCommandWrapper(Commands.INFO, Commands.GIVE_ECO);
	public static final BukkitCommandWrapper EXCHANGE = new BukkitCommandWrapper(Commands.EXCHANGE);
	public static final BukkitCommandWrapper PAY = new BukkitCommandWrapper(Commands.PAY);

	private BukkitCommands() {
		throw new RuntimeException("Should not run");
	}
}
