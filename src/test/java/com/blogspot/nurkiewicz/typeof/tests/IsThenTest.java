package com.blogspot.nurkiewicz.typeof.tests;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.function.Consumer;

import static com.blogspot.nurkiewicz.typeof.TypeOf.whenTypeOf;
import static org.mockito.Mockito.*;

/**
 * @author Tomasz Nurkiewicz
 * @since 22.09.13, 20:35
 */
public class IsThenTest {

	@Mock
	private Consumer<Integer> mock;

	@BeforeMethod(alwaysRun = true)
	public void injectMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testWithSingleNonMatchingClause() {
		whenTypeOf(42).
				is(String.class).then(s -> mock.accept(1));

		//then
		verifyZeroInteractions(mock);
	}

	@Test
	public void testWithSingleMatchingClause() {
		//when
		whenTypeOf(42).
				is(Integer.class).then(x -> mock.accept(2));

		//then
		verify(mock).accept(2);
	}

	@Test
	public void testWithSingleMatchOfUpperClass() {
		//given
		whenTypeOf(42).
				is(Number.class).then(x -> mock.accept(2));

		//then
		verify(mock).accept(2);
	}

	@Test
	public void testShouldPickFirstMatchingAndNotFallThroughRemaining() {
		//given
		whenTypeOf(42).
				is(Date.class).then(d -> mock.accept(2)).
				is(Number.class).then(x -> mock.accept(3)).
				is(Integer.class).then(x -> mock.accept(4)).
				is(Float.class).then(x -> mock.accept(5)).
				is(Byte.class).then(x -> mock.accept(6));

		//then
		verify(mock).accept(3);
		verifyNoMoreInteractions(mock);
	}

	@Test
	public void testLastClauseMatchingOutOfMany() {
		//given
		whenTypeOf(42).
				is(Date.class).then(d -> mock.accept(7)).
				is(Float.class).then(x -> mock.accept(8)).
				is(Byte.class).then(x -> mock.accept(9)).
				is(Integer.class).then(x -> mock.accept(10));

		//then
		verify(mock).accept(10);
		verifyNoMoreInteractions(mock);
	}

	@Test
	public void testLastClauseNotMatching() {
		//given
		whenTypeOf(42).
				is(Date.class).then(d -> mock.accept(7)).
				is(Float.class).then(x -> mock.accept(8)).
				is(Byte.class).then(x -> mock.accept(9)).
				is(String.class).then(x -> mock.accept(10));

		//then
		verifyZeroInteractions(mock);
	}

	@Test
	public void testShouldCatchObject() {
		//given
		whenTypeOf(42).
				is(Date.class).then(d -> mock.accept(7)).
				is(Float.class).then(x -> mock.accept(8)).
				is(Byte.class).then(x -> mock.accept(9)).
				is(String.class).then(x -> mock.accept(10)).
				is(Object.class).then(x -> mock.accept(11));

		//then
		verify(mock).accept(11);
		verifyNoMoreInteractions(mock);
	}

	@Test
	public void testShouldRunOrElseBlock() {
		//given
		whenTypeOf(42).
				is(Date.class).then(d -> mock.accept(7)).
				is(Float.class).then(x -> mock.accept(8)).
				is(Byte.class).then(x -> mock.accept(9)).
				is(String.class).then(x -> mock.accept(10)).
				orElse(mock::accept);

		//then
		verify(mock).accept(42);
		verifyNoMoreInteractions(mock);
	}

	@Test
	public void testShouldNotFailWhenNullPassedAndSingleClause() {
		//given
		whenTypeOf(null).
				is(Date.class).then(d -> mock.accept(7)).
				orElse(obj -> mock.accept(2));

		//then
		verify(mock).accept(2);
		verifyNoMoreInteractions(mock);
	}

	@Test
	public void shouldNotFailWhenNullPassedAndMultipleClauses() {
		//given
		whenTypeOf(null).
				is(Date.class).then(d -> mock.accept(7)).
				is(Float.class).then(x -> mock.accept(8)).
				is(Byte.class).then(x -> mock.accept(9)).
				is(String.class).then(x -> mock.accept(10)).
				orElse(obj -> mock.accept(2));

		//then
		verify(mock).accept(2);
		verifyNoMoreInteractions(mock);
	}
}
