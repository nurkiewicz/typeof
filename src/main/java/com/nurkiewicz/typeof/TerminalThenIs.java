package com.nurkiewicz.typeof;

import java.util.function.Consumer;

class TerminalThenIs<S, T> extends ThenIs<S, T> {

	TerminalThenIs(Then<S> parent, S object, Class<T> expectedType) {
		super(parent, object, expectedType);
	}

	@Override
	public Then<S> then(Consumer<T> thenBlock) {
		return parent;
	}
}
