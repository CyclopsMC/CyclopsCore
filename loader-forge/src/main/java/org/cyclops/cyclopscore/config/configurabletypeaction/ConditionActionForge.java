package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.serialization.MapCodec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ConditionConfigForge;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * The action used for {@link ConditionConfigForge}.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
public class ConditionActionForge<T extends ICondition> extends ConfigurableTypeActionCommon<ConditionConfigForge<T>, MapCodec<T>, ModBaseForge<?>> {
    @Override
    public void onRegisterModInit(ConditionConfigForge<T> eConfig) {
        super.onRegisterModInit(eConfig);
        eConfig.getMod().getModEventBus().addListener((RegisterEvent event) -> {
            if (event.getRegistryKey() == ForgeRegistries.CONDITION_SERIALIZERS.getKey()) {
                eConfig.getRegistryForge().register(ConfigHandlerCommon.getConfigId(eConfig), eConfig.getInstance());
            }
        });
    }
}
