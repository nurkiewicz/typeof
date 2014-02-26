package com.blogspot.nurkiewicz.typeof.tests;

import org.testng.annotations.Test;

import java.util.Date;
import java.util.NoSuchElementException;

import static com.blogspot.nurkiewicz.typeof.TypeOf.whenTypeOf;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * @author Tomasz Nurkiewicz
 * @since 22.09.13, 20:35
 */
public class IsThenReturnTest {

	@Test
	public void testReturnFirstMatchingClause() {
		//when
		final int result = whenTypeOf(42).
				is(Integer.class).thenReturn(i -> i + 1).
				get();

		//then
		assertThat(result).isEqualTo(43);
	}

	@Test
	public void testReturnFirstMatchingClauseOfSuperClass() {
		//when
		final int result = whenTypeOf(42).
				is(Number.class).thenReturn(n -> n.intValue() + 1).
				is(Integer.class).thenReturn(i -> i - 1).
				is(Object.class).thenReturn(obj -> -1).
				get();

		//then
		assertThat(result).isEqualTo(42 + 1);
	}

	@Test
	public void testReturnSubsequent() {
		//when
		final int result = whenTypeOf(42).
				is(String.class).thenReturn(String::length).
				is(Date.class).thenReturn(d -> (int) d.getTime()).
				is(Integer.class).thenReturn(i -> i - 1).
				is(Object.class).thenReturn(obj -> -1).
				get();

		//then
		assertThat(result).isEqualTo(42 - 1);
	}

	@Test
	public void testOrElseBlockWithClosure() {
		//when
		final int result = whenTypeOf(42).
				is(String.class).thenReturn(String::length).
				is(Date.class).thenReturn(d -> (int) d.getTime()).
				is(Float.class).thenReturn(Float::intValue).
				orElse(x -> x + 1);

		//then
		assertThat(result).isEqualTo(42 + 1);
	}

	@Test
	public void testOrElseBlockWithFixedValue() {
		//when
		final int result = whenTypeOf(42).
				is(String.class).thenReturn(String::length).
				is(Date.class).thenReturn(d -> (int) d.getTime()).
				is(Float.class).thenReturn(Float::intValue).
				orElse(17);

		//then
		assertThat(result).isEqualTo(17);
	}

	@Test
	public void testThrowWhenGetCalledButSingleClauseNotMatching() {
		try {
			whenTypeOf(42).
					is(String.class).thenReturn(String::length).
					get();
			failBecauseExceptionWasNotThrown(NoSuchElementException.class);
		} catch (NoSuchElementException e) {
			assertThat(e).hasMessage("42");
		}
	}

	@Test
	public void testThrowWhenGetCalledButNeitherClausesWorked() {
		try {
			whenTypeOf(42).
					is(String.class).thenReturn(String::length).
					is(Date.class).thenReturn(d -> (int) d.getTime()).
					is(Float.class).thenReturn(Float::intValue).
					get();
			failBecauseExceptionWasNotThrown(NoSuchElementException.class);
		} catch (NoSuchElementException e) {
			assertThat(e).hasMessage("42");
		}
	}

	@Test
	public void shouldNotFailWhenNullPassedAndClosureOrElseResult() {
		//when
		final int result = whenTypeOf(null).
				is(String.class).thenReturn(String::length).
				is(Date.class).thenReturn(d -> (int) d.getTime()).
				is(Float.class).thenReturn(Float::intValue).
				orElse(x -> x != null? -1 : 1);

		//then
		assertThat(result).isEqualTo(1);
	}

	@Test
	public void shouldNotFailWhenNullPassed() {
		//when
		final int result = whenTypeOf(null).
				is(String.class).thenReturn(String::length).
				is(Date.class).thenReturn(d -> (int) d.getTime()).
				is(Float.class).thenReturn(Float::intValue).
				orElse(17);

		//then
		assertThat(result).isEqualTo(17);
	}
}
