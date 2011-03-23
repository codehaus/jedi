package jedi.example;

import jedi.functional.Functor2;
import jedi.option.Option;

import static jedi.functional.Coercions.list;
import static jedi.functional.FunctionalPrimitives.flatten;
import static jedi.functional.FunctionalPrimitives.fold;
import static jedi.option.Options.option;

public class MultipleOptionsExample {

    public static void main(String[] args) {
        Option<String> x = option("Hi");
        Option<String> y = option(" ");
        Option<String> z = option("there");

        // use 'for' to work with values if they ALL exist
        for (String xv : x)
            for (String yv : y)
                for (String zv : z)
                    System.out.println("All values are something: " + xv + yv + zv);

        // iterate over all values of non-None options
        for (String v : flatten(list(x,y,z))) System.out.println(v);

        // or process all non-None options, eg concatenate them:
        System.out.println(
                fold("Concatenate: ", flatten(list(x, y, z)), new Appender()));
    }

    private static class Appender implements Functor2<String, String, String> {
        @Override
        public String execute(String s1, String s2) {
            return s1 + s2;
        }
    }
}
