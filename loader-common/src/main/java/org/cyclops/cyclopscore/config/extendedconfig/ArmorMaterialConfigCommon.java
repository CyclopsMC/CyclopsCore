package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorMaterial;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for armor materials.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfig
 */
public class ArmorMaterialConfigCommon<M extends IModBase> extends ExtendedConfigForge<ArmorMaterialConfigCommon<M>, ArmorMaterial, M>{

    public ArmorMaterialConfigCommon(M mod, String namedId, Function<ArmorMaterialConfigCommon<M>, ArmorMaterial> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "armormaterial." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.ARMOR_MATERIAL;
    }

    @Override
    public Registry<? super ArmorMaterial> getRegistry() {
        return BuiltInRegistries.ARMOR_MATERIAL;
    }
}
