package org.cyclops.cyclopscore.command.argument;

import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.ArgumentTypeConfig;

public class ArgumentTypeEnumConfig extends ArgumentTypeConfig<ArgumentTypeEnum<?>, ArgumentTypeEnum.Info.Template<?>> {
    public ArgumentTypeEnumConfig() {
        super(CyclopsCore._instance, "enum", new ArgumentTypeEnum.Info(), (Class<ArgumentTypeEnum<?>>) (Class) ArgumentTypeEnum.class);
    }
}
