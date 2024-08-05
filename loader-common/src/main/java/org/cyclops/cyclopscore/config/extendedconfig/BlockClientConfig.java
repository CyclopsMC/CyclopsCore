package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 * @param <M> The mod type
 */
public class BlockClientConfig<M extends IModBase> {

    private final BlockConfigCommon<M> blockConfig;

    public ModelResourceLocation dynamicBlockVariantLocation;
    public ModelResourceLocation dynamicItemVariantLocation;

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
    public Pair<ModelResourceLocation, ModelResourceLocation> registerDynamicModel() {
        ResourceLocation blockName = ResourceLocation.fromNamespaceAndPath(getBlockConfig().getMod().getModId(), getBlockConfig().getNamedId());
        ModelResourceLocation blockLocation = new ModelResourceLocation(blockName, "");
        ModelResourceLocation itemLocation = new ModelResourceLocation(blockName, "inventory");
        return Pair.of(blockLocation, itemLocation);
    }

    /**
     * @return An optional color handler for the block instance.
     */
    @Nullable
    public BlockColor getBlockColorHandler() {
        return null;
    }
}
