package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.client.model.IDynamicModelElement;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.configurabletypeaction.BlockAction;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.item.ItemBlockMetadata;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Config for blocks.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class BlockConfig extends ExtendedConfigForge<BlockConfig, Block> implements IModelProviderConfig {

    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation dynamicBlockVariantLocation;
    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation dynamicItemVariantLocation;

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public BlockConfig(ModBase mod, String namedId, Function<BlockConfig, ? extends Block> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getModelName(ItemStack itemStack) {
        return getNamedId();
    }

    @Override
	public String getTranslationKey() {
		return "blocks." + getMod().getModId() + "." + getNamedId();
	}

    @Override
    public String getFullTranslationKey() {
        return "tile." + getTranslationKey() + ".name";
    }
    
    @Override
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.BLOCK;
	}
    
    /**
     * If hasSubTypes() returns true this method can be overwritten to define another ItemBlock class
     * @return the ItemBlock class to use for the target blockState.
     */
    public Class<? extends Item> getItemBlockClass() {
        return ItemBlockMetadata.class;
    }

    /**
     * Get the item corresponding to the block.
     * Will return Items.AIR rather than null if there isn't one.
     * @return The item.
     */
    @Nonnull
    public Item getItemInstance() {
        return Item.getItemFromBlock(getInstance());
    }

    /**
     * Register default block and item models for this block.
     * This should only be used when registering dynamic models.
     * @return The pair of block resource location and item resource location.
     */
    @OnlyIn(Dist.CLIENT)
    public Pair<ModelResourceLocation, ModelResourceLocation> registerDynamicModel() {
        String blockName = getMod().getModId() + ":" + getNamedId();
        final ModelResourceLocation blockLocation = new ModelResourceLocation(blockName, "normal");
        ModelResourceLocation itemLocation = new ModelResourceLocation(blockName, "inventory");
        // TODO: implement statemapping if still needed
//        ModelLoader.setCustomStateMapper(getBlockInstance(), new StateMapperBase() {
//            @Override
//            protected ModelResourceLocation getModelResourceLocation(BlockState blockState) {
//                return blockLocation;
//            }
//        });
//        ModelLoader.setCustomModelResourceLocation(getItemInstance(), 0, itemLocation);
        return Pair.of(blockLocation, itemLocation);
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        if(MinecraftHelpers.isClientSide() && getInstance() instanceof IDynamicModelElement &&
                ((IDynamicModelElement) getInstance()).hasDynamicModel()) {
            BlockAction.handleDynamicBlockModel(this);
        }
    }

    @Override
    public IForgeRegistry<Block> getRegistry() {
        return ForgeRegistries.BLOCKS;
    }

    /**
     * @return An optional color handler for the block instance.
     */
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public IBlockColor getBlockColorHandler() {
        return null;
    }

}
