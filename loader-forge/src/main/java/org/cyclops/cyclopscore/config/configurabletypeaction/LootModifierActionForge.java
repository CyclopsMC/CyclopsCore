package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.serialization.MapCodec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.config.extendedconfig.LootModifierConfigForge;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * The action used for {@link LootModifierConfigForge}.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
public class LootModifierActionForge<T extends IGlobalLootModifier> extends ConfigurableTypeActionCommon<LootModifierConfigForge<T>, MapCodec<T>, ModBaseForge<?>> {
    @Override
    public void onRegisterModInit(LootModifierConfigForge<T> eConfig) {
        super.onRegisterModInit(eConfig);
        eConfig.getMod().getModEventBus().addListener((RegisterEvent event) -> {
            if (event.getRegistryKey() == ForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS.getKey()) {
                eConfig.getRegistryForge().register(ConfigHandlerCommon.getConfigId(eConfig), eConfig.getInstance());
            }
        });
    }
}
