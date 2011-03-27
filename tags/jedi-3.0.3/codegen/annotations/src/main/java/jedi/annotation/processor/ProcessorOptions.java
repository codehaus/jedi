package jedi.annotation.processor;

public class ProcessorOptions {
	private final ProcessorOptionAccessor accessor;

	public ProcessorOptions(ProcessorOptionAccessor accessor) {
		this.accessor = accessor;
	}

	public boolean includeAccessorVerbs() {
		return !accessor.isOption("jediSuppressAccessorVerbs");
	}

	public boolean includeSuffixes() {
		return !accessor.isOption("jediSuppressSuffixes");
	}

	public boolean includeClosureTypeSuffix() {
		return !accessor.isOption("jediSuppressClosureTypeSuffix");
	}

	public boolean includeProxySuffix() {
		return !accessor.isOption("jediSuppressProxySuffix");
	}
}
