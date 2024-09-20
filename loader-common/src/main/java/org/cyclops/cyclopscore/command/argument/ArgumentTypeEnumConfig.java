package org.cyclops.cyclopscore.command.argument;

import org.cyclops.cyclopscore.config.extendedconfig.ArgumentTypeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

public class ArgumentTypeEnumConfig<M extends IModBase> extends ArgumentTypeConfigCommon<ArgumentTypeEnum<?>, ArgumentTypeEnum.Info.Template<?>, M> {
    public ArgumentTypeEnumConfig(M mod) {
        super(mod, "enum", new ArgumentTypeEnum.Info(), (Class<ArgumentTypeEnum<?>>) (Class) ArgumentTypeEnum.class);
    }
}
