package jedi.annotation.processor5;

import jedi.annotation.processor.ProcessorOptionAccessor;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;

public class OptionAccessor implements ProcessorOptionAccessor {
	private final AnnotationProcessorEnvironment environment;

	public OptionAccessor(AnnotationProcessorEnvironment environment) {
		this.environment = environment;
	}

	@Override
	public boolean isOption(String key) {
		return environment.getOptions().containsKey(key);
	}
}
