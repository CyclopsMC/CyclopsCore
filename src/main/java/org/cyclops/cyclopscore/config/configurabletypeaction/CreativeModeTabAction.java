package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.cyclops.cyclopscore.config.extendedconfig.CreativeModeTabConfig;

/**
 * The action used for {@link CreativeModeTabConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class CreativeModeTabAction extends ConfigurableTypeAction<CreativeModeTabConfig, CreativeModeTab> {

    @Override
    public void onRegisterForgeFilled(CreativeModeTabConfig eConfig) {
        super.onRegisterForgeFilled(eConfig);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(eConfig.getMod().getModId(), eConfig.getNamedId()), eConfig.getInstance());
    }
}
