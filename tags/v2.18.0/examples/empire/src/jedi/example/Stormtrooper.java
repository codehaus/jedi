package jedi.example;

import jedi.annotation.JediCommand;
import jedi.annotation.JediFilter;
import jedi.annotation.JediFunctor;

// This interface shows how to use some annotations. Look at the Empire class to
// see how the generated closure factories are used.
public interface Stormtrooper {
    @JediFilter
    boolean isWithinRange(RebelBase base);
    
    @JediCommand
    void attack(RebelBase base);
    
    @JediFunctor
    int getDistance(RebelBase base);
}
