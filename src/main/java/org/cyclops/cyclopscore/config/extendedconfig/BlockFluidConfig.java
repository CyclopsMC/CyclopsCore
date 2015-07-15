package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for BlockFluids.
 * @author immortaleeb
 * @see ExtendedConfig
 */
public abstract class BlockFluidConfig extends BlockConfig {

    @SideOnly(Side.CLIENT)
    private ModelResourceLocation fluidLocation;

    /**
     * Make a new instance.
     *
     * @param mod     The mod instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public BlockFluidConfig(ModBase mod, boolean enabled, String namedId, String comment, Class<? extends BlockFluidClassic> element) {
        super(mod, enabled, namedId, comment, element);
        if (MinecraftHelpers.isClientSide()) {
            fluidLocation = new ModelResourceLocation(mod.getModId() + ":" + getNamedId(), "fluid");
        }
    }

    @Override
    public BlockFluidClassic getBlockInstance() {
        return (BlockFluidClassic) super.getBlockInstance();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onRegistered() {
        super.onRegistered();

        // Handle registration for fluid rendering
        BlockFluidClassic blockInstance = getBlockInstance();
        Item fluid = Item.getItemFromBlock(blockInstance);

        ModelBakery.addVariantName(fluid);
        ModelLoader.setCustomMeshDefinition(fluid, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return fluidLocation;
            }
        });

        ModelLoader.setCustomStateMapper(blockInstance, new StateMapperBase() {

            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return fluidLocation;
            }
        });
    }
}
