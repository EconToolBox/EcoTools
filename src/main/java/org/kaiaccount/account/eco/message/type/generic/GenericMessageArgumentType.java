package org.kaiaccount.account.eco.message.type.generic;

import org.jetbrains.annotations.NotNull;
import org.kaiaccount.account.eco.message.type.MessageArgumentType;

import java.util.function.Function;

public class GenericMessageArgumentType<T> implements MessageArgumentType<T> {

	private final Function<T, String> toString;
	private final Class<T> type;
	private final @NotNull String argumentHolder;

	public GenericMessageArgumentType(@NotNull String defaultArgumentHolder, @NotNull Class<T> type) {
		this(defaultArgumentHolder, type, Object::toString);
	}

	public GenericMessageArgumentType(@NotNull String defaultArgumentHolder, @NotNull Class<T> type,
			@NotNull Function<T, String> toString) {
		this.toString = toString;
		this.argumentHolder = defaultArgumentHolder;
		this.type = type;
	}

	@NotNull
	@Override
	public String getDefaultArgumentHandler() {
		return this.argumentHolder;
	}

	@NotNull
	@Override
	public String apply(T input) {
		return this.toString.apply(input);
	}

	@NotNull
	@Override
	public Class<T> getClassType() {
		return this.type;
	}
}
