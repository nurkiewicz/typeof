package com.blogspot.nurkiewicz.typeof;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

public class ThenReturn<S, R> {

	final S object;

	public ThenReturn(S object) {
		this.object = object;
	}

	public <T> ReturnIs<S, T, R> is(Class<T> expectedType) {
		return new ReturnIs<>(object, expectedType);
	}

	public R get() {
		throw new NoSuchElementException(Objects.toString(object));
	}

	public R orElse(Function<S, R> resultFun) {
		return resultFun.apply(object);
	}

	public R orElse(R result) {
		return result;
	}
}
