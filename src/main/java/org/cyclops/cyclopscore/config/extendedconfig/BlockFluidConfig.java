package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
        if (MinecraftHelpers.isClientSide()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @Override
    public BlockFluidClassic getBlockInstance() {
        return (BlockFluidClassic) super.getBlockInstance();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelRegistryLoad(ModelRegistryEvent event) {
        // Handle registration for fluid rendering
        BlockFluidClassic blockInstance = getBlockInstance();
        Item fluid = Item.getItemFromBlock(blockInstance);

        ModelBakery.registerItemVariants(fluid, fluidLocation);

        ModelLoader.setCustomStateMapper(blockInstance, new StateMapperBase() {

            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return fluidLocation;
            }
        });
    }
}
