package org.kaiaccount.account.eco.message.type;

import org.jetbrains.annotations.NotNull;

public interface MessageArgumentType<I> {

	@NotNull
	String getDefaultArgumentHandler();

	@NotNull
	String apply(I input);

	@NotNull
	Class<I> getClassType();

}
