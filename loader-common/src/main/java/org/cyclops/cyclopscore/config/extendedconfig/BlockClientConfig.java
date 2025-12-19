package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 * @param <M> The mod type
 */
public class BlockClientConfig<M extends IModBase> {

    private final BlockConfigCommon<M> blockConfig;

    public BlockState dynamicBlockVariantLocation;
    public Identifier dynamicItemVariantLocation;

    public BlockClientConfig(BlockConfigCommon<M> blockConfig) {
        this.blockConfig = blockConfig;
    }

    public BlockConfigCommon<M> getBlockConfig() {
        return blockConfig;
    }

    /**
     * Register default block and item models for this block.
     * This should only be used when registering dynamic models.
     * @return The pair of block resource location and item resource location.
     */
    public Pair<BlockState, Identifier> registerDynamicModel() {
        Identifier blockName = Identifier.fromNamespaceAndPath(getBlockConfig().getMod().getModId(), getBlockConfig().getNamedId());
        return Pair.of(getBlockConfig().getInstance().defaultBlockState(), blockName);
    }

    /**
     * @return An optional color handler for the block instance.
     */
    @Nullable
    public BlockColor getBlockColorHandler() {
        return null;
    }

    /**
     * @return An optional dynamic model element
     */
    @Nullable
    public IDynamicModelElementCommon getDynamicModelElement() {
        return null;
    }
}
