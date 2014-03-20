package com.blogspot.nurkiewicz.typeof;

/**
 * @author Tomasz Nurkiewicz
 * @since 21.09.13, 22:22
 */
class TerminalThenReturn<S, R> extends ThenReturn<S, R> {

	private final R result;

	public TerminalThenReturn(S object, R result) {
		super(object);
		this.result = result;
	}

	@Override
	public <T> ReturnIs<S, T, R> is(Class<T> expectedType) {
		return new TerminalReturnIs<>(object, result);
	}

	@Override
	public R get() {
		return result;
	}

	@Override
	public R orElse(R result) {
		return this.result;
	}
}
