package jedi.annotation.jedi.newprocessor;


import jedi.functional.FunctionalPrimitives;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;

import static jedi.functional.FunctionalPrimitives.first;

@SupportedAnnotationTypes(value = {"jedi.annotation.JediFunctor"})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class JediProcessor extends AbstractProcessor  {

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment roundEnvironment) {
        try {
            if (typeElements.isEmpty()) return true;
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(first(typeElements));
        if (elements.isEmpty()) return true;
        Element element = first(elements);
            while(element.getKind() != ElementKind.CLASS) element = element.getEnclosingElement();
            System.out.println(element);
            String name = element.getSimpleName() + "Factory";
            Filer filer = processingEnv.getFiler();
            JavaFileObject fileObject = filer.createSourceFile(name);
            PrintWriter writer = new PrintWriter(fileObject.openWriter());
            writer.println("public class " + name + "{}");
            writer.close();
            System.out.println("Wrote generated code");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}