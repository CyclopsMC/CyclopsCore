package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import org.cyclops.cyclopscore.config.extendedconfig.LootModifierConfig;

/**
 * The action used for {@link LootModifierConfig}.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
// TODO: append NeoForge to name in next major
public class LootModifierAction<T extends IGlobalLootModifier> extends ConfigurableTypeActionForge<LootModifierConfig<T>, MapCodec<T>> {

}
