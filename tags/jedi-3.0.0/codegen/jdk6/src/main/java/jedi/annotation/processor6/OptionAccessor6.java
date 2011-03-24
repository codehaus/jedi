package jedi.annotation.processor6;

import javax.annotation.processing.ProcessingEnvironment;

import jedi.annotation.processor.ProcessorOptionAccessor;

public class OptionAccessor6 implements ProcessorOptionAccessor {
	private final ProcessingEnvironment environment;

	public OptionAccessor6(ProcessingEnvironment processingEnv) {
		this.environment = processingEnv;
	}

	@Override
	public boolean isOption(String key) {
		return environment.getOptions().containsKey(key);
	}

}
