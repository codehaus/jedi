package jedi.annotation;

public @interface JediCut {
	String name() default "";

	String[] parameters() default {};
}
