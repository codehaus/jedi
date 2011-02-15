package jedi.annotation.processor.model;

import java.io.File;

public interface MemberDeclaration {
	String getPackage();
	String getDeclaringTypeWithUnboundedGenerics();
	String getQualifiedNameOfDeclaringType();
	String getSimpleNameOfDeclaringType();

	String getOriginalName();
	String getName(boolean simplified);
	String getSimpleName();
	String getDeclaredType();
	String getBoxedDeclaredType();
	boolean isVoid();
	boolean isBoolean();

	File getFile();
	int getLine();
	int getColumn();
}
