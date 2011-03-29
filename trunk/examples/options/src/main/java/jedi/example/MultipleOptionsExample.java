package jedi.example;

import jedi.annotation.JediFunctor;
import jedi.option.Option;
import jedi.option.Options;

import static jedi.example.MultipleOptionsExampleStaticClosureFactory.appendProxyFunctor2;
import static jedi.functional.FunctionalPrimitives.flatten;
import static jedi.functional.FunctionalPrimitives.fold;
import static jedi.option.Options.option;

public class MultipleOptionsExample {

    public void runExamples() {
        Option<String> x = option("Hi");
        Option<String> y = option(" ");
        Option<String> z = option("there");

        // use 'for' to work with values if they ALL exist
        for (String xv : x)
            for (String yv : y)
                for (String zv : z)
                    System.out.println("All values are something: " + xv + yv + zv);

        // iterate over all values excluding Nones
        for (String v : flatten(x,y,z, Options.<String>none())) System.out.println(v);

        // Fold excluding Nones, eg concatenate them:
        System.out.println(fold("Concatenate: ", flatten(x, y, z), appendProxyFunctor2(this)));
    }

    @JediFunctor
    public String append(String a, String b) {
        return a + b;
    }

    public static void main(String[] args) {
        new MultipleOptionsExample().runExamples();
    }
}
