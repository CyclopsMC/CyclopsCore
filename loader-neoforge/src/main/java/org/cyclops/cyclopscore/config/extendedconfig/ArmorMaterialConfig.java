package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.item.ArmorMaterial;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for armor materials.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public class ArmorMaterialConfig extends ArmorMaterialConfigCommon<ModBase<?>>{
    public ArmorMaterialConfig(ModBase<?> mod, String namedId, Function<ArmorMaterialConfigCommon<ModBase<?>>, ArmorMaterial> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }
}
