package jedi.example;

import static jedi.functional.Coercions.*;
import static jedi.functional.Comparables.*;
import static jedi.functional.FirstOrderLogic.*;
import static jedi.functional.FunctionalPrimitives.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jedi.annotation.JediFunctor;

// This class shows a few uses of the Jedi code. See the Stormtrooper class for the annotations which
// generated the StormtrooperStaticClosureFactory referenced in the code below.
public class Empire {
	@JediFunctor
    private Set<Stormtrooper> stormtroopers = new HashSet<Stormtrooper>();
    
      // This is the 'conventional' way of writing the method of the same name below. No doubt the
      // obvious sections would be extracted into private methods or something.
//    public void attackDiscriminately(RebelBase base, int requiredNumberOfStormtroopers) {
//        Collection<Stormtrooper> availableStormtroopers = new HashSet<Stormtrooper>();
//        for (Stormtrooper stormtrooper : stormtroopers) {
//            if (stormtrooper.isWithinAttackingRange(base)) {
//                availableStormtroopers.add(stormtrooper);
//            }
//        }
//        
//        if (availableStormtroopers.size() < requiredNumberOfStormtroopers) {
//            return;
//        }
//        
//        for (Stormtrooper stormtrooper : availableStormtroopers) {
//            stormtrooper.attack(planet);
//        }
//    }
    
    // Only attack if there are sufficient Stormtroopers close to the given planet
    public void attackDiscriminately(RebelBase base, int requiredNumberOfStormtroopers) {
        Collection<Stormtrooper> availableStormtroopers = select(stormtroopers, StormtrooperStaticClosureFactory.isWithinRangeFilter(base));
        
        if (availableStormtroopers.size() >= requiredNumberOfStormtroopers) {
            forEach(availableStormtroopers, StormtrooperStaticClosureFactory.attackCommand(base));
        }
    }
    
      // This is the 'conventional' way of writing the method of the same name below. Notice how
      // the meaning is obscured by the looping code, whereas the Jedi version below is more
      // like a repetition of the method name.
//    public boolean areAnyStormtroopersWithinRange(RebelBase base) {
//        for (Stormtrooper stormtrooper : stormtroopers) {
//            if (stormtrooper.isWithinRange(base)) {
//                return true;
//            }
//        }
//        
//        return false;
//    }

    public boolean areAnyStormtrooperWithinRange(RebelBase base) {
        return exists(stormtroopers, StormtrooperStaticClosureFactory.isWithinRangeFilter(base));
    }
    
      // This is the 'conventional' way of writing the method of the same name below. Notice how
      // the meaning is obscured by comparator. Even if we extract the anonymous comparator class
      // It still requires more code to be written than seems necessary.
//    public void attackWithClosestStormtroopers(final RebelBase base, int numberOfStormtroopersToUse) {
//        List<Stormtrooper> sortedStormtroopers = new ArrayList<Stormtrooper>(stormtroopers);
//        Collections.sort(sortedStormtroopers, new Comparator<Stormtrooper>() {
//            public int compare(Stormtrooper s1, Stormtrooper s2) {
//                return s1.getDistance(base) - s2.getDistance(base);
//            }
//        });
//        
//        final List<Stormtrooper> closestStormtroopers = sortedStormtroopers.subList(0, numberOfStormtroopersToUse);
//        for (Stormtrooper stormtrooper : closestStormtroopers) {
//            stormtrooper.attack(base);
//        }
//    }
    
    public void attackWithClosestStormtroopers(RebelBase base, int numberOfStormtroopersToUse) {
       List<Stormtrooper> sortedStormtroopers = sortInPlace(asList(stormtroopers), StormtrooperStaticClosureFactory.getDistanceFunctor(base));
       forEach(take(numberOfStormtroopersToUse, sortedStormtroopers), StormtrooperStaticClosureFactory.attackCommand(base));
    }
}
