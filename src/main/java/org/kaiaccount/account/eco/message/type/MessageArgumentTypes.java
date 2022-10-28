package org.kaiaccount.account.eco.message.type;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public final class MessageArgumentTypes {

	private static final List<MessageArgumentType<?>> cache = new LinkedList<>();

	public static <T> Collection<MessageArgumentType<T>> getArgumentTypes(Class<T> clazz) {
		return getArgumentTypes().parallelStream()
				.filter(type -> type.getClassType().isAssignableFrom(clazz))
				.map(type -> (MessageArgumentType<T>) type)
				.collect(Collectors.toUnmodifiableSet());
	}

	public static Collection<MessageArgumentType<?>> getArgumentTypes() {
		if (!cache.isEmpty()) {
			return Collections.unmodifiableCollection(cache);
		}
		Collection<MessageArgumentType<?>> collection = Arrays.stream(MessageArgumentTypes.class.getDeclaredFields())
				.parallel()
				.filter(field -> Modifier.isPublic(field.getModifiers()))
				.filter(field -> Modifier.isStatic(field.getModifiers()))
				.filter(field -> Modifier.isFinal(field.getModifiers()))
				.filter(field -> field.getType().isAssignableFrom(MessageArgumentType.class))
				.map(field -> {
					try {
						return (MessageArgumentType<?>) field.get(null);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						return null;
					}
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
		cache.addAll(collection);
		return Collections.unmodifiableCollection(collection);
	}

	private MessageArgumentTypes() {
		throw new RuntimeException("Dont do that");
	}
}
