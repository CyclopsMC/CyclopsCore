package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.serialization.MapCodec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeModifierConfigForge;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * The action used for {@link org.cyclops.cyclopscore.config.extendedconfig.BiomeModifierConfigForge}.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
public class BiomeModifierActionForge<T extends BiomeModifier> extends ConfigurableTypeActionCommon<BiomeModifierConfigForge<T>, MapCodec<T>, ModBaseForge<?>> {
    @Override
    public void onRegisterModInit(BiomeModifierConfigForge<T> eConfig) {
        super.onRegisterModInit(eConfig);
        eConfig.getMod().getModEventBus().addListener((RegisterEvent event) -> {
            if (event.getRegistryKey() == ForgeRegistries.BIOME_MODIFIER_SERIALIZERS.getKey()) {
                eConfig.getRegistryForge().register(ConfigHandlerCommon.getConfigId(eConfig), eConfig.getInstance());
            }
        });
    }
}
