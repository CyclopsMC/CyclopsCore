package org.cyclops.cyclopscore.config.extendedconfig;

import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockDoor;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.item.ItemDoorMetadata;

import net.minecraft.block.BlockDoor;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Config for doors.
 * @author josephcsible
 * @see ExtendedConfig
 */
public abstract class BlockDoorConfig extends BlockConfig {
    /**
     * @see org.cyclops.cyclopscore.config.extendedconfig.BlockConfig#BlockConfig(ModBase, boolean, String, String, Class)
     */
    public BlockDoorConfig(ModBase mod, boolean enabled, String namedId, String comment, Class<? extends ConfigurableBlockDoor> element) {
        super(mod, enabled, namedId, comment, element);
        if(MinecraftHelpers.isClientSide())
            MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public Class<? extends Item> getItemBlockClass() {
        return ItemDoorMetadata.class;
    }

    @Override
    public Item getItemInstance() {
        return ((ConfigurableBlockDoor)getBlockInstance()).item;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelRegistryLoad(ModelRegistryEvent event) {
        ModelLoader.setCustomStateMapper(getBlockInstance(), (new StateMap.Builder()).ignore(BlockDoor.POWERED).build());
    }
}
