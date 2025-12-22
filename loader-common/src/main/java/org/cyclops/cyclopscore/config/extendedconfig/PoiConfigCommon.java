package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for POIs.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class PoiConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<PoiConfigCommon<M>, PoiType, M> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public PoiConfigCommon(M mod, String namedId, Function<PoiConfigCommon<M>, ? extends PoiType> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "entity.poi." + getNamedId();
    }

    @Override
    public String getFullTranslationKey() {
        return "entity.poi." + getNamedId();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.POI;
    }

    @Override
    public Registry<? super PoiType> getRegistry() {
        return BuiltInRegistries.POINT_OF_INTEREST_TYPE;
    }
}
