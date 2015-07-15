package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.client.model.IDynamicModelBlock;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.item.ItemBlockMetadata;

/**
 * Config for blocks.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class BlockConfig extends ExtendedConfig<BlockConfig> implements IModelProviderConfig {

    @SideOnly(Side.CLIENT) public ModelResourceLocation dynamicBlockVariantLocation;
    @SideOnly(Side.CLIENT) public ModelResourceLocation dynamicItemVariantLocation;

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public BlockConfig(ModBase mod, boolean enabled, String namedId, String comment, Class<? extends Block> element) {
        super(mod, enabled, namedId, comment, element);
        if(MinecraftHelpers.isClientSide()) {
            dynamicBlockVariantLocation = null;
            dynamicItemVariantLocation  = null;
        }
    }

    @Override
    public String getModelName(ItemStack itemStack) {
        return getNamedId();
    }

    @Override
	public String getUnlocalizedName() {
		return "blocks." + getMod().getModId() + "." + getNamedId();
	}

    @Override
    public String getFullUnlocalizedName() {
        return "tile." + getUnlocalizedName() + ".name";
    }
    
    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.BLOCK;
	}
    
    /**
     * If hasSubTypes() returns true this method can be overwritten to define another ItemBlock class
     * @return the ItemBlock class to use for the target blockState.
     */
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockMetadata.class;
    }
    
    /**
     * If the IConfigurable is registered in the OreDictionary, use this name to identify it.
     * @return the name this IConfigurable is registered with in the OreDictionary.
     */
    public String getOreDictionaryId() {
        return null;
    }
    
    /**
     * If this blockState should enable Forge Multiparts and BC facades.
     * @return If that should be enabled for this blockState.
     */
    public boolean isMultipartEnabled() {
        return false;
    }

    /**
     * Get the casted instance of the blockState.
     * @return The blockState.
     */
    public Block getBlockInstance() {
        return (Block) super.getSubInstance();
    }

    /**
     * Get the creative tab for this item.
     * @return The creative tab, by default the value in {@link org.cyclops.cyclopscore.init.ModBase#getDefaultCreativeTab()}.
     */
    public CreativeTabs getTargetTab() {
        return getMod().getDefaultCreativeTab();
    }

    /**
     * Register default block and item models for this block.
     * This should only be used when registering dynamic models.
     * @return The pair of block resource location and item resource location.
     */
    @SideOnly(Side.CLIENT)
    protected Pair<ModelResourceLocation, ModelResourceLocation> registerDynamicModel() {
        String blockName = getMod().getModId() + ":" + getNamedId();
        final ModelResourceLocation blockLocation = new ModelResourceLocation(blockName, "normal");
        ModelResourceLocation itemLocation = new ModelResourceLocation(blockName, "inventory");
        ModelLoader.setCustomStateMapper(getBlockInstance(), new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState blockState) {
                return blockLocation;
            }
        });
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(getBlockInstance()), 0, itemLocation);
        return Pair.of(blockLocation, itemLocation);
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        if(MinecraftHelpers.isClientSide() && getBlockInstance() instanceof IDynamicModelBlock &&
                ((IDynamicModelBlock) getBlockInstance()).hasDynamicModel()) {
            Pair<ModelResourceLocation, ModelResourceLocation> resourceLocations = registerDynamicModel();
            this.dynamicBlockVariantLocation = resourceLocations.getLeft();
            this.dynamicItemVariantLocation  = resourceLocations.getRight();
        }
    }

}
