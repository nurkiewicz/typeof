package com.blogspot.nurkiewicz.typeof;

import java.util.function.Function;

class TerminalReturnIs<S, T, R> extends ReturnIs<S, T, R> {

	private final R result;

	public TerminalReturnIs(S object, R result) {
		super(object, null);
		this.result = result;
	}

	@Override
	public ThenReturn<S, R> thenReturn(Function<T, R> resultFun) {
		return new TerminalThenReturn<>(object, result);
	}

    @Override
    public ThenReturn<S, R> thenReturn(R result) {
        return new TerminalThenReturn<>(object, this.result);
    }
}
