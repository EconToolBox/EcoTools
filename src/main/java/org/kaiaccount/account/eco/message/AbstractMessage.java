package org.kaiaccount.account.eco.message;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kaiaccount.account.eco.message.type.MessageArgument;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractMessage implements Message {

	private @Nullable String message;

	public AbstractMessage(@Nullable String message) {
		this.message = message;
	}

	protected @NotNull String getProcessedMessage(@NotNull Map<Object, String> values) {
		return this.getProcessedMessage(this.getOverridingMessageElse(), values);
	}

	protected @NotNull String getProcessedMessage(@NotNull CharSequence message, @NotNull Map<Object, String> values) {
		StringBuilder builder = new StringBuilder();
		String argumentBuffer = null;
		for (int i = 0; i < message.length(); i++) {
			char at = message.charAt(i);
			if (at != '%') {
				if (argumentBuffer == null) {
					builder.append(at);
					continue;
				}
				argumentBuffer = argumentBuffer + at;
				continue;
			}
			if (argumentBuffer == null) {
				argumentBuffer = at + "";
				continue;
			}
			String withoutPercent = argumentBuffer.substring(1);
			Optional<MessageArgument<?>> opArgument = this.getArgument(withoutPercent);
			if (opArgument.isEmpty()) {
				builder.append(argumentBuffer);
				continue;
			}
			Collection<Object> possibleValues = values.entrySet()
					.parallelStream()
					.filter(entry -> opArgument.get().getClassType().isInstance(entry.getKey()))
					.map(
							Map.Entry::getKey)
					.toList();
			Object value;
			if (possibleValues.size() > 1) {
				Optional<Object> opValue = values.entrySet()
						.parallelStream()
						.filter(entry -> withoutPercent.startsWith(entry.getValue()))
						.findAny()
						.map(Map.Entry::getValue);
				if (opValue.isEmpty()) {
					i++;
					continue;
				}
				value = opValue.get();
			} else if (possibleValues.isEmpty()) {
				i++;
				continue;
			} else {
				value = possibleValues.iterator().next();
			}
			String valueString = process(opArgument.get(), value);
			builder.append(valueString);
			argumentBuffer = null;
		}
		return builder.toString();
	}

	private <T> @NotNull String process(MessageArgument<T> argument, Object value) {
		return argument.apply((T) value);
	}

	@NotNull
	@Override
	public Optional<String> getOverridingMessage() {
		return Optional.ofNullable(this.message);
	}

	@Override
	public void setOverridingMessage(@Nullable String message) {
		this.message = message;
	}
}
