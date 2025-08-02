package org.cyclops.cyclopscore.helper;

import net.minecraft.util.ProblemReporter;

/**
 * @author rubensworks
 */
public class DummyPathElement implements ProblemReporter.PathElement {
    @Override
    public String get() {
        return "dummy";
    }
}
