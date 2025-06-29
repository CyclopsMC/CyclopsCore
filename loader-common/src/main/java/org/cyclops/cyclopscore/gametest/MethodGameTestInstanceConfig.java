package org.cyclops.cyclopscore.gametest;

import org.cyclops.cyclopscore.config.extendedconfig.GameTestInstanceTypeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * @author rubensworks
 */
public class MethodGameTestInstanceConfig extends GameTestInstanceTypeConfigCommon {
    public MethodGameTestInstanceConfig(IModBase mod) {
        super(mod, "method", (eConfig) -> MethodGameTestInstance.CODEC);
    }
}
