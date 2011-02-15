package jedi.annotation;

public @interface SithMethod {
	String factoryName() default "";

	String name();

	Class<?>[] parameterTypes() default {};
}
