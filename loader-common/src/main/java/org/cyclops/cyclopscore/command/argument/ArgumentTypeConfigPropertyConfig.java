package org.cyclops.cyclopscore.command.argument;

import org.cyclops.cyclopscore.config.extendedconfig.ArgumentTypeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

public class ArgumentTypeConfigPropertyConfig<M extends IModBase> extends ArgumentTypeConfigCommon<ArgumentTypeConfigProperty, ArgumentInfoMod<ArgumentTypeConfigProperty>.Template, M> {
    public ArgumentTypeConfigPropertyConfig(M mod) {
        super(mod, "configprop", new ArgumentInfoMod<>(), ArgumentTypeConfigProperty.class);
    }
}
