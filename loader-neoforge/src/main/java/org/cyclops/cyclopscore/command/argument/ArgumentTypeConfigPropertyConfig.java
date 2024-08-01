package org.cyclops.cyclopscore.command.argument;

import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.ArgumentTypeConfig;

public class ArgumentTypeConfigPropertyConfig extends ArgumentTypeConfig<ArgumentTypeConfigProperty, ArgumentInfoMod<ArgumentTypeConfigProperty>.Template> {
    public ArgumentTypeConfigPropertyConfig() {
        super(CyclopsCore._instance, "blur", new ArgumentInfoMod<>(), ArgumentTypeConfigProperty.class);
    }
}
