package com.nurkiewicz.typeof;

import java.util.function.Consumer;

/**
 * @author Tomasz Nurkiewicz
 * @since 22.09.13, 21:28
 */
public class Then<S> {

	private final S object;

	Then(S object) {
		this.object = object;
	}

	public <T> ThenIs<S, T> is(Class<T> type) {
		return new ThenIs<>(this, object, type);
	}

	public void orElse(Consumer<S> orElseBlock) {
		orElseBlock.accept(object);
	}
}
