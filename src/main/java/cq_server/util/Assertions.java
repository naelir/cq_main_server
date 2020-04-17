package cq_server.util;

import java.text.MessageFormat;
import java.util.Collection;

public class Assertions {
    
    public static void atLeast(Collection<?> c, int size) {
        if (c.size() < size) {
            throw new IllegalArgumentException(MessageFormat.format("collection should be at least {0} elements", size));
        }
    }

}
