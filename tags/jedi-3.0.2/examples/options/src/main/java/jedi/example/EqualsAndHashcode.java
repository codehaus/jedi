package jedi.example;

import java.util.Date;

import static jedi.option.Options.option;

public class EqualsAndHashcode {
    private String f;
    private Date d;

    @Override
    public int hashCode() {
        return option(f).hashCode() ^ option(d).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || !(obj instanceof EqualsAndHashcode)) return false;

        EqualsAndHashcode o = (EqualsAndHashcode) obj;
        // no need for null checks. Note that Some(x).equals((Some(y)) == x.equals(y) and None == None
        return option(f).equals(option(o.f)) && option(d).equals(option(o.d));
    }
}
