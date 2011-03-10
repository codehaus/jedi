package jedi.annotation.processor5;

import jedi.annotation.processor.ProcessorOptionAccessor;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;

public class OptionAccessor5 implements ProcessorOptionAccessor {
	private final AnnotationProcessorEnvironment environment;

	public OptionAccessor5(AnnotationProcessorEnvironment environment) {
		this.environment = environment;
	}

	@Override
	public boolean isOption(String key) {
		return environment.getOptions().containsKey(key);
	}
}
