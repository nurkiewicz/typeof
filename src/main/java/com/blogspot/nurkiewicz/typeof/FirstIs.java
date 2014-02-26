package com.blogspot.nurkiewicz.typeof;

import java.util.function.Consumer;
import java.util.function.Function;

public class FirstIs<S, T> {

	final Then<S> parent;
	private final S object;
	private final Class<T> expectedType;

	FirstIs(Then<S> parent, S object, Class<T> expectedType) {
		this.parent = parent;
		this.object = object;
		this.expectedType = expectedType;
	}

	public Then<S> then(Consumer<T> thenBlock) {
		if (matchingType()) {
			thenBlock.accept(castObject());
			return new TerminalThen<>();
		}
		return parent;
	}

	public <R> ThenReturn<S, R> thenReturn(Function<T, R> result) {
		if (matchingType()) {
			return new TerminalThenReturn<>(object, result.apply(castObject()));
		}
		return new ThenReturn<>(object);
	}

	public <R> ThenReturn<S, R> thenReturn(R result) {
		if (matchingType()) {
			return new TerminalThenReturn<>(object, result);
		}
		return new ThenReturn<>(object);
	}

	private T castObject() {
		return (T) object;
	}

	private boolean matchingType() {
		return object != null && expectedType.isAssignableFrom(object.getClass());
	}
}
