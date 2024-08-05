package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Config for blocks.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public abstract class BlockConfig extends BlockConfigCommon<ModBase<?>> {

    @Deprecated // TODO: Use BlockClientConfig instead; remove in next major
    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation dynamicBlockVariantLocation;
    @Deprecated // TODO: Use BlockClientConfig instead; remove in next major
    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation dynamicItemVariantLocation;

    public BlockConfig(ModBase<?> mod, String namedId, Function<BlockConfigCommon<ModBase<?>>, ? extends Block> blockConstructor,
                       @Nullable BiFunction<BlockConfigCommon<ModBase<?>>, Block, ? extends Item> itemConstructor) {
        super(mod, namedId, blockConstructor, itemConstructor);
        if(mod.getModHelpers().getMinecraftHelpers().isClientSide()) {
            dynamicBlockVariantLocation = null;
            dynamicItemVariantLocation = null;
        }
    }
    /**
     * Register default block and item models for this block.
     * This should only be used when registering dynamic models.
     * @return The pair of block resource location and item resource location.
     */
    @OnlyIn(Dist.CLIENT)
    @Deprecated // TODO: Use BlockClientConfig instead; remove in next major
    public Pair<ModelResourceLocation, ModelResourceLocation> registerDynamicModel() {
        ResourceLocation blockName = ResourceLocation.fromNamespaceAndPath(getMod().getModId(), getNamedId());
        ModelResourceLocation blockLocation = new ModelResourceLocation(blockName, "");
        ModelResourceLocation itemLocation = new ModelResourceLocation(blockName, "inventory");
        return Pair.of(blockLocation, itemLocation);
    }

    /**
     * @return An optional color handler for the block instance.
     */
    @OnlyIn(Dist.CLIENT)
    @Nullable
    @Deprecated // TODO: Use BlockClientConfig instead; remove in next major
    public BlockColor getBlockColorHandler() {
        return null;
    }

}
