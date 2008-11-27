package jedi.annotation.writer.method;

import static jedi.functional.FirstOrderLogic.*;
import static jedi.functional.FunctionalPrimitives.*;

import java.util.Set;

import jedi.annotation.jedi.Annotateable;
import jedi.annotation.writer.JavaWriter;
import jedi.annotation.writer.factorytype.FactoryType;
import jedi.functional.Coercions;
import jedi.functional.Filter;

public class CompositeFactoryMethodWriter implements FactoryMethodWriter {
	private final Set<? extends FactoryMethodWriter> children;

	public CompositeFactoryMethodWriter(FactoryMethodWriter... children) {
		this.children = Coercions.set(children);
	}

	public boolean canHandle(Annotateable method) {
		return exists(children, createHandlerFilter(method));
	}

	public void execute(Annotateable method) {
		for (FactoryMethodWriter child : select(children, createHandlerFilter(method))) {
			child.execute(method);
		}
	}

	public void initialise(JavaWriter writer, FactoryType factoryType) {
		for (FactoryMethodWriter child : children) {
			child.initialise(writer, factoryType);
		}
	}

	private Filter<FactoryMethodWriter> createHandlerFilter(final Annotateable method) {
		return new Filter<FactoryMethodWriter>() {
			public Boolean execute(FactoryMethodWriter value) {
				return value.canHandle(method);
			}
		};
	}
}
