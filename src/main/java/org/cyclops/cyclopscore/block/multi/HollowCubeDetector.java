package org.cyclops.cyclopscore.block.multi;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
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
	protected ITextComponent validateLocationInStructure(IWorldReader world, int[][] dimensionEgdes, BlockPos location, IValidationAction action, BlockPos excludeLocation) {
		// Validate edge or air.
		ITextComponent error;
		if (isEdge(world, dimensionEgdes, location)) {
			if ((error = isValidLocation(world, location, action, excludeLocation)) != null) {
				//System.out.println("No edge at " + location);
				return error;
			}
		} else {
			if (!isAir(world, location)) {
				//System.out.println("No air at " + location);
				return new TranslationTextComponent("multiblock.cyclopscore.error.hollow.air",
						LocationHelpers.toCompactString(location));
			}
		}
		return null;
	}

}
