package org.cyclops.cyclopscore.block.multi;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorldReader;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;

import java.util.List;

/**
 * Detector of hollow cubes in a world.
 * @author rubensworks
 *
 */
public class HollowCubeDetector extends CubeDetector {
	
	/**
	 * Make a new instance.
	 * @param allowedBlocks The blocks that are allowed in this cube.
	 * @param listeners Listeners for detections. 
	 */
	public HollowCubeDetector(AllowedBlock[] allowedBlocks, List<? extends IDetectionListener> listeners) {
		super(allowedBlocks, listeners);
	}
	
	@Override
	protected void postValidate(IWorldReader world, final Vec3i size, final int[][] dimensionEgdes, final boolean valid, final BlockPos originCorner, final BlockPos excludeLocation) {
		coordinateRecursion(world, dimensionEgdes, new BlockPosAction() {

			@Override
			public boolean run(IWorldReader world, BlockPos location) {
				if(isEdge(world, dimensionEgdes, location) && isValidLocation(world, location, excludeLocation) == null) {
					notifyListeners(world, location, size, valid, originCorner);
				}
				return true;
			}
			
		});
	}
	
	@Override
	protected L10NHelpers.UnlocalizedString validateLocationInStructure(IWorldReader world, int[][] dimensionEgdes, BlockPos location, IValidationAction action, BlockPos excludeLocation) {
		// Validate edge or air.
		L10NHelpers.UnlocalizedString error;
		if (isEdge(world, dimensionEgdes, location)) {
			if ((error = isValidLocation(world, location, action, excludeLocation)) != null) {
				//System.out.println("No edge at " + location);
				return error;
			}
		} else {
			if (!isAir(world, location)) {
				//System.out.println("No air at " + location);
				return new L10NHelpers.UnlocalizedString("multiblock.cyclopscore.error.hollow.air",
						LocationHelpers.toCompactString(location));
			}
		}
		return null;
	}

}
