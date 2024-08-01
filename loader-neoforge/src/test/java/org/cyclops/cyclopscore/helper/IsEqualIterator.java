package org.cyclops.cyclopscore.helper;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Iterator;
import java.util.Objects;

/**
 * @author rubensworks
 */
public class IsEqualIterator<T extends Iterator<?>> extends BaseMatcher<T> {

    private final T expectedValue;

    public IsEqualIterator(T equalArg) {
        expectedValue = equalArg;
    }

    @Override
    public boolean matches(Object item) {
        T actualValue = (T) item;
        while (expectedValue.hasNext()) {
            if (!actualValue.hasNext()) {
                return false;
            }
            if (!Objects.equals(expectedValue.next(), actualValue.next())) {
                return false;
            }
        }
        if (actualValue.hasNext()) {
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expectedValue);
    }
}
