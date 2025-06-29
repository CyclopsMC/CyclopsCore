package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestInstance;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for game test instance types.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
public class GameTestInstanceTypeConfigCommon extends ExtendedConfigRegistry<GameTestInstanceTypeConfigCommon, MapCodec<? extends GameTestInstance>, IModBase> {

    public GameTestInstanceTypeConfigCommon(IModBase mod, String namedId, Function<GameTestInstanceTypeConfigCommon, MapCodec<? extends GameTestInstance>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "gametestinstancetype." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.GAME_TEST_INSTANCE_TYPE;
    }

    @Override
    public Registry<MapCodec<? extends GameTestInstance>> getRegistry() {
        return BuiltInRegistries.TEST_INSTANCE_TYPE;
    }
}
