package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.item.equipment.ArmorMaterial;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for armor materials.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public class ArmorMaterialConfigCommon<M extends IModBase> extends ExtendedConfigCommon<ArmorMaterialConfigCommon<M>, ArmorMaterial, M> {

    public ArmorMaterialConfigCommon(M mod, String namedId, Function<ArmorMaterialConfigCommon<M>, ArmorMaterial> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "armormaterial." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.ARMOR_MATERIAL;
    }
}
