package com.nurkiewicz.typeof;

import java.util.function.Consumer;

/**
 * @author Tomasz Nurkiewicz
 * @since 22.09.13, 21:29
 */
public class TerminalThen<S> extends Then<S> {

	public TerminalThen() {
		super(null);
	}

	@Override
	public <T> ThenIs<S, T> is(Class<T> type) {
		return new TerminalThenIs<>(this, null, null);
	}

	@Override
	public void orElse(Consumer<S> orElseBlock) {
		//no-op
	}
}
