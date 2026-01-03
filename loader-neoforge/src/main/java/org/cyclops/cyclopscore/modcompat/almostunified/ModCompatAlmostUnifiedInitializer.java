package org.cyclops.cyclopscore.modcompat.almostunified;

import org.cyclops.cyclopscore.modcompat.ICompatInitializer;

public class ModCompatAlmostUnifiedInitializer implements ICompatInitializer {
    @Override
    public void initialize() {
        AlmostUnifiedAdapter.enabled = true;
    }
}
