package jedi.filters;

import jedi.functional.Filter;

public class AllPassFilter  <T> implements Filter<T> {
    
    public Boolean execute(T value) {
        return true;
    }
}
