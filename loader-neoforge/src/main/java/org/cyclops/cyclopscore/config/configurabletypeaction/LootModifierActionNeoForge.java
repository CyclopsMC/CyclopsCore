package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import org.cyclops.cyclopscore.config.extendedconfig.LootModifierConfigNeoForge;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

/**
 * The action used for {@link LootModifierConfigNeoForge}.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
public class LootModifierActionNeoForge<T extends IGlobalLootModifier> extends ConfigurableTypeActionRegistry<LootModifierConfigNeoForge<T>, MapCodec<T>, ModBaseNeoForge<?>> {

}
