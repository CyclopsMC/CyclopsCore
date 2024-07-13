package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorMaterial;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for armor materials.
 * @author rubensworks
 * @see ExtendedConfig
 */
public class ArmorMaterialConfig extends ExtendedConfigForge<ArmorMaterialConfig, ArmorMaterial>{

    public ArmorMaterialConfig(ModBase mod, String namedId, Function<ArmorMaterialConfig, ArmorMaterial> elementConstructor) {
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
