package jedi.annotation.processor.model;

import java.io.File;
import java.util.Collection;

public interface MemberDeclaration {
	String getPackage();

	String getDeclaringTypeWithUnboundedGenerics();

	String getQualifiedNameOfDeclaringType();

	String getSimpleNameOfDeclaringType();

	String getOriginalName();

	String getSimpleName();

	String getDeclaredType();

	String getBoxedDeclaredType();

	Collection<Attribute> getParameters();

	boolean isVoid();

	boolean isBoolean();

	String renderGenericTypeParameters();

	File getFile();

	int getLine();

	int getColumn();
}
