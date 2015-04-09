package org.cyclops.cyclopscore.datastructure;

import lombok.Data;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * A simple data class for a block position inside a world.
 * @author rubensworks
 */
@Data(staticConstructor = "of")
public class DimPos {

    private final World world;
    private final BlockPos blockPos;

}
