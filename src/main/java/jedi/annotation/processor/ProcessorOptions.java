package jedi.annotation.processor;

public class ProcessorOptions {
	private final ProcessorOptionAccessor accessor;

	public ProcessorOptions(ProcessorOptionAccessor accessor) {
		this.accessor = accessor;
	}

	public boolean includeAccessorVerbs() {
		return !accessor.isOption("-AjediSuppressAccessorVerbs");
	}

	public boolean includeSuffixes() {
		return !accessor.isOption("-AjediSuppressSuffixes");
	}

	public boolean includeClosureTypeSuffix() {
		return !accessor.isOption("-AjediSuppressClosureTypeSuffix");
	}

	public boolean includeProxySuffix() {
		return !accessor.isOption("-AjediSuppressProxySuffix");
	}
}
