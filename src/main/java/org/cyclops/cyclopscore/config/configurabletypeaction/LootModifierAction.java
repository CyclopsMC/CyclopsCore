package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import org.cyclops.cyclopscore.config.extendedconfig.LootModifierConfig;

/**
 * The action used for {@link LootModifierConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class LootModifierAction<T extends IGlobalLootModifier> extends ConfigurableTypeActionForge<LootModifierConfig<T>, Codec<T>> {

}
