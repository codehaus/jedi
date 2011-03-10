package jedi.annotation;

public @interface SithMethods {
	SithCommand[] commands() default {};

	SithFilter[] filters() default {};

	SithFunctor[] functors() default {};
}
