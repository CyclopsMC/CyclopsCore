package org.cyclops.cyclopscore.tileentity;

import net.minecraft.nbt.NBTTagCompound;

/**
 * This is a temporary storage for NBT data when {@link org.cyclops.cyclopscore.tileentity.CyclopsTileEntity}s are destroyed.
 * In the dropped blocks method this tag should then be used to add to the dropped blockState.
 * @author rubensworks
 *
 */
public final class TileEntityNBTStorage {

    private TileEntityNBTStorage() {}

    /**
     * The temporary tag storage for dropped NBT data from {@link org.cyclops.cyclopscore.tileentity.CyclopsTileEntity}.
     */
    public static NBTTagCompound TAG = null;
    /**
     * The temporary tag storage for dropped custom name from {@link org.cyclops.cyclopscore.tileentity.CyclopsTileEntity}.
     */
    public static String NAME = null;
    
}
