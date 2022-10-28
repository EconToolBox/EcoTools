package org.kaiaccount.account.eco.message.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MessageArgument<T> {

	private final @Nullable String argumentName;
	private final @NotNull MessageArgumentType<T> type;

	public MessageArgument(@NotNull MessageArgumentType<T> type) {
		this(type, null);
	}

	public MessageArgument(@NotNull MessageArgumentType<T> type, @Nullable String argumentHandler) {
		this.argumentName = argumentHandler;
		this.type = type;
	}

	public @NotNull Class<T> getClassType() {
		return this.type.getClassType();
	}

	public @NotNull String getArgumentHandler() {
		if (this.argumentName == null) {
			return this.type.getDefaultArgumentHandler();
		}
		return this.argumentName;
	}

	public @NotNull String apply(T value) {
		return this.type.apply(value);
	}

}
