package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.BlockItem;
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
import org.cyclops.cyclopscore.item.ItemInformationProvider;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Config for blocks.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class BlockConfig extends ExtendedConfigForge<BlockConfig, Block> implements IModelProviderConfig {

    private final BiFunction<BlockConfig, Block, ? extends Item> itemConstructor;

    @Nullable
    private Item itemInstance;

    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation dynamicBlockVariantLocation;
    @OnlyIn(Dist.CLIENT)
    public ModelResourceLocation dynamicItemVariantLocation;

    public BlockConfig(ModBase mod, String namedId, Function<BlockConfig, ? extends Block> blockConstructor,
                       @Nullable BiFunction<BlockConfig, Block, ? extends Item> itemConstructor) {
        super(mod, namedId, blockConstructor);
        this.itemConstructor = itemConstructor;
    }

    protected static BiFunction<BlockConfig, Block, ? extends BlockItem> getDefaultItemConstructor(ModBase mod) {
        return (eConfig, block) -> new BlockItem(block, new Item.Properties().group(mod.getDefaultItemGroup()));
    }

    public BiFunction<BlockConfig, Block, ? extends Item> getItemConstructor() {
        return itemConstructor;
    }

    @Nullable
    public Item getItemInstance() {
        return itemInstance;
    }

    public void setItemInstance(@Nullable Item itemInstance) {
        this.itemInstance = itemInstance;
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
    public void onForgeRegistered() {
        super.onForgeRegistered();

        // Add item to information provider
        Item item = getItemInstance();
        if (item != null) {
            ItemInformationProvider.registerItem(item);
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
