# instanceof operator and *Visitor* pattern replacement in Java 8

I had a dream where `instanceof` operator and downcasting were no longer needed but without clumsiness and verbosity of [*visitor pattern*](http://nurkiewicz.blogspot.com/2009/03/wzorzec-visitor-realny-przykad.html). So I came up with the following DSL syntax:

    Object msg = //...

	whenTypeOf(msg).
		is(Date.class).    then(date -> println(date.getTime())).
		is(String.class).  then(str -> println(str.length())).
		is(Number.class).  then(num -> println(num.intValue())).
		                 orElse(obj -> println("Unknown " + obj));

No downcasting, clean syntax, strong-typed and... perfectly achievable in Java 8. Using lambdas and a little bit of generics I created a [tiny library called `typeof`](https://github.com/nurkiewicz/typeof) that is clean, easy to use and more robust than `instanceof` and *Visitor* pattern taken together. Advantages include:

* no explicit downcasting
* avoids `instanceof`
* clean and easy to use
* strongly typed
* works with classes that we have no control over, including JDK

This small utility was developed with [Akka and Java API](http://doc.akka.io/docs/akka/2.2.1/java/untyped-actors.html) in mind to limit the usage of `instanceof` operator, but it's much more general. Similarly you can return something depending on the runtime type:

	int result = whenTypeOf(obj).
		is(String.class).thenReturn(String::length).
		is(Date.class).thenReturn(d -> (int) d.getTime()).
		is(Number.class).thenReturn(Number::intValue).
		is(TimeZone.class).thenReturn(tz -> tz.getRawOffset() / 1000).
		is(MyType.class).thenReturn(7).
		get();

The library examines every `is()` clause from top to bottom and stops if it finds first matching class, including parent classes - so `is(Number.class)` will match both `Integer` and `Float`. If none of the conditions matched, calling `get` will fail with exception. You can override this behaviour using `orElse()` (easier to read than equivalent `is(Object.class)`):

	int result = whenTypeOf(obj).
		is(String.class).thenReturn(String::length).
		//...
		orElse(42);

DSL takes advantage of static typing in Java, making it nearly impossible to use the library incorrectly - most mistakes are caught immediately during compilation. All code snippets below won't even compile:

	//ERROR - two subsequent is()
	whenTypeOf(obj).
		is(Foo.class).is(Bar.class)

	//ERROR - then() without prior is()
	whenTypeOf(obj).
		then(x -> println(x))

	//ERROR - mixing then() and thenReturn()
	whenTypeOf(obj).
		is(Foo.class).then(foo -> println(foo)).
		is(Bar.class).thenReturn(bar -> bar.getB());

Basically you start by typing `whenTypeOf()` and `Ctrl` + `space` will tell you precisely what is allowed. The key to designing type-safe and robust DSLs in statically typed languages is to limit the API as much as possible so that invalid states and calls are avoided at compile time. You will end up with a [proliferation of tiny classes](https://github.com/nurkiewicz/typeof/tree/master/src/main/java/com/blogspot/nurkiewicz/typeof), but that's OK, your users won't see this. For example check out [`FirstIs.java`](https://github.com/nurkiewicz/typeof/blob/master/src/main/java/com/blogspot/nurkiewicz/typeof/FirstIs.java) - an object returned after first `is()` invocation:

    public class FirstIs<S, T> {
		final Then<S> parent;
		private final S object;
		private final Class<T> expectedType;

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

		//...

	}

Writing DSLs is much harder than using them, but it's quite rewarding in the end. Notice how different return types are used ([`Then`](https://github.com/nurkiewicz/typeof/blob/master/src/main/java/com/blogspot/nurkiewicz/typeof/Then.java) vs. [`ThenReturn`](https://github.com/nurkiewicz/typeof/blob/master/src/main/java/com/blogspot/nurkiewicz/typeof/ThenReturn.java)) just to make sure only valid methods are accessible at each stage. An alternative is to implement run-time checks (for example that you don't write `is(...).is(...).then(...)`) - but why bother if compiler can do it for us?

I hope you enjoyed this article, let me know if you are willing to try this utility in your project.
