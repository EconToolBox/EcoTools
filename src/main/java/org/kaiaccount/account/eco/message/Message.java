package org.kaiaccount.account.eco.message;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kaiaccount.account.eco.message.type.MessageArgument;

import java.util.Collection;
import java.util.Optional;

public interface Message {

	@NotNull
	String getDefaultMessage();

	@NotNull
	Optional<String> getOverridingMessage();

	void setOverridingMessage(@Nullable String message);

	@NotNull
	Collection<MessageArgument<?>> getArguments();

	default Optional<MessageArgument<?>> getArgument(@NotNull String argumentName) {
		return this.getArguments()
				.parallelStream()
				.filter(message -> message.getArgumentHandler().equalsIgnoreCase(argumentName))
				.findAny();
	}

	default @NotNull String getOverridingMessageElse() {
		return this.getOverridingMessage().orElseGet(this::getDefaultMessage);
	}
}
