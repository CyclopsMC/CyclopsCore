package org.cyclops.cyclopscore.tileentity;

import lombok.experimental.Delegate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.cyclopscore.persist.nbt.NBTProviderComponent;

/**
 * A base class for all the tile entities.
 * This tile does not tick, use the TickingEvilCraftTileEntity variant for that.
 * @author rubensworks
 *
 */
public class CyclopsTileEntity extends TileEntity implements INBTProvider {
    
    @NBTPersist
    private Boolean rotatable = false;
    private EnumFacing rotation = EnumFacing.NORTH;
    @Delegate
    private INBTProvider nbtProviderComponent = new NBTProviderComponent(this);
    
    /**
     * Called when the blockState of this tile entity is destroyed.
     */
    public void destroy() {
        invalidate();
    }
    
    /**
     * If this entity is interactable with a player.
     * @param entityPlayer The player that is checked.
     * @return If the given player can interact.
     */
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        writeGeneratedFieldsToNBT(tag);
        
        // Separate action for direction
        tag.setString("rotation", rotation.getName());
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        readGeneratedFieldsFromNBT(tag);
        
        // Separate action for direction
        EnumFacing foundRotation = EnumFacing.byName(tag.getString("rotation"));
        if(foundRotation != null) {
        	rotation = foundRotation;
        }
        onLoad();
    }

    /**
     * When the tile is loaded or created.
     */
    public void onLoad() {

    }
    
    /**
     * Get the NBT tag for this tile entity.
     * @return The NBT tag that is created with the
     * {@link CyclopsTileEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)} method.
     */
    public NBTTagCompound getNBTTagCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return tag;
    }

    /**
     * If the blockState this tile entity has can be rotated.
     * @return If it can be rotated.
     */
    public boolean isRotatable() {
        return this.rotatable;
    }

    /**
     * Set whether or not the blockState that has this tile entity can be rotated.
     * @param rotatable If it can be rotated.
     */
    public void setRotatable(boolean rotatable) {
        this.rotatable = rotatable;
    }

    /**
     * Get the current rotation of this tile entity.
     * Default is {@link net.minecraft.util.EnumFacing#NORTH}.
     * @return The rotation.
     */
    public EnumFacing getRotation() {
        return rotation;
    }

    /**
     * Set the rotation of this tile entity.
     * Default is {@link net.minecraft.util.EnumFacing#NORTH}.
     * @param rotation The new rotation.
     */
    public void setRotation(EnumFacing rotation) {
        this.rotation = rotation;
    }
    
    /**
     * Get the blockState type this tile entity is defined for.
     * @return The blockState instance.
     */
    public ConfigurableBlockContainer getBlock() {
        return (ConfigurableBlockContainer) this.getBlockType();
    }

}
