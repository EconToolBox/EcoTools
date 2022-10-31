package org.kaiaccount.account.eco.message.messages.error;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kaiaccount.account.eco.message.AbstractMessage;
import org.kaiaccount.account.eco.message.type.MessageArgument;
import org.kaiaccount.account.eco.message.type.generic.GenericMessageArgumentType;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourceOnlyCommandMessage extends AbstractMessage {

	public static final String PLAYER_SOURCE = "Player";
	public static final String LOCATABLE = "Player or Command Block";
	public static final String CONSOLE_SOURCE = "Console";

	private static final MessageArgument<String> SOURCE_TYPE =
			new MessageArgument<>(new GenericMessageArgumentType<>("source type", String.class));

	public SourceOnlyCommandMessage(@Nullable String message) {
		super(message);
	}

	public @NotNull String getProcessedMessage(@NotNull String sourceType) {
		Map<Object, String> map = new HashMap<>();
		map.put(sourceType, "");
		return this.getProcessedMessage(map);
	}

	@NotNull
	@Override
	public String getDefaultMessage() {
		return "This is a %source type% only command";
	}

	@NotNull
	@Override
	public Collection<MessageArgument<?>> getArguments() {
		return List.of(SOURCE_TYPE);
	}
}
