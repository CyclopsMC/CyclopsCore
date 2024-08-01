package org.cyclops.cyclopscore.helper;

import org.hamcrest.Matcher;

import java.util.Iterator;

/**
 * @author rubensworks
 */
public class CyclopsMatchers {

    public static <T extends Iterator<?>> Matcher<T> isIterator(T value) {
        return new IsEqualIterator<T>(value);
    }

}
