package org.cyclops.cyclopscore.block.multi;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.algorithm.Dimension;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Detector of cubes in a world.
 * @author rubensworks
 *
 */
public class CubeDetector {
	
	private static Vec3i NULL_SIZE = Vec3i.NULL_VECTOR;
	
	private Collection<AllowedBlock> allowedBlocks = Sets.newHashSet();
	private Map<Block, AllowedBlock> blockInfo = Maps.newHashMap();
	private List<? extends IDetectionListener> listeners;
	private List<ISizeValidator> sizeValidators = Lists.newLinkedList();
	
	private Map<Block, Integer> blockOccurences;

	/**
	 * Make a new instance.
	 * @param allowedBlocks The blocks that are allowed in this cube.
	 * @param listeners Listeners for detections. 
	 */
	public CubeDetector(AllowedBlock[] allowedBlocks, List<? extends IDetectionListener> listeners) {
		addAllowedBlocks(allowedBlocks);
		this.listeners = listeners;
	}
	
	/**
	 * @return the allowed blocks
	 */
	public Collection<AllowedBlock> getAllowedBlocks() {
		return allowedBlocks;
	}

	/**
	 * @param allowedBlocks The allowed blocks
	 */
	public void addAllowedBlocks(AllowedBlock[] allowedBlocks) {
		for(AllowedBlock block : allowedBlocks) {
			blockInfo.put(block.getBlock(), block);
			this.allowedBlocks.add(block);
		}
	}
	
	/**
	 * @return the size validators
	 */
	public List<ISizeValidator> getSizeValidators() {
		return sizeValidators;
	}

	/**
	 * An optional size validator to add.
	 * @param sizeValidator The validator object to check the size
	 * @return this instance.
	 */
	public CubeDetector addSizeValidator(ISizeValidator sizeValidator) {
		this.sizeValidators.add(sizeValidator);
		return this;
	}
	
	/**
	 * @return the listeners
	 */
	public List<? extends IDetectionListener> getListeners() {
		return listeners;
	}

	protected void notifyListeners(World world, BlockPos location, Vec3i size, boolean valid, BlockPos originCorner) {
		for(IDetectionListener listener : getListeners()) {
			listener.onDetect(world, location, size, valid, originCorner);
		}
	}
	
	protected L10NHelpers.UnlocalizedString isValidLocation(World world, BlockPos location, IValidationAction action, BlockPos excludeLocation) {
		IBlockState blockState = world.getBlockState(location);
		Block block = blockState.getBlock();
		boolean contains = location.equals(excludeLocation) || blockInfo.containsKey(block);
		L10NHelpers.UnlocalizedString error;
        if(action != null && blockInfo.containsKey(block) && (error = action.onValidate(location, blockState)) != null) {
			return error;
		}
        return contains ? null : new L10NHelpers.UnlocalizedString("multiblock.cyclopscore.error.invalidBlock",
				LocationHelpers.toCompactString(location), new L10NHelpers.UnlocalizedString(block.getUnlocalizedName() + ".name"));
	}

    protected L10NHelpers.UnlocalizedString isValidLocation(World world, BlockPos location, BlockPos excludeLocation) {
        return isValidLocation(world, location, null, excludeLocation);
    }
	
	protected boolean isAir(World world, BlockPos location) {
		return world.isAirBlock(location);
	}
	
	/**
	 * Find the border of valid/non-valid locations in one given dimension for just one direction.
	 * @param world The world to look in.
	 * @param startLocation The location to start looking from.
	 * @param dimension The dimension to navigate in.
     * @param direction the distance to go by.
     * @param excludeLocation The location of the block that is being removed, used for invalidating, null for validating.
	 * @return The found border.
	 */
	protected BlockPos navigateToBorder(World world, BlockPos startLocation, int dimension, int direction, BlockPos excludeLocation) {
		BlockPos loopLocation = LocationHelpers.copyLocation(startLocation);
		
		// Loop until we find a non-valid location.
		while(isValidLocation(world, loopLocation, excludeLocation) == null) {
			loopLocation = LocationHelpers.addToDimension(loopLocation, dimension, direction);
		}
		
		// Because we went one increment too far.
		loopLocation = LocationHelpers.addToDimension(loopLocation, dimension, -direction);
		
		return loopLocation;
	}
	
	/**
	 * Find the border of valid/non-valid locations in one given dimension (both directions).
	 * @param world The world to look in.
	 * @param startLocation The location to start looking from.
	 * @param dimension The dimension to navigate in.
	 * @param max If the direction to look in for the given dimension should be positive
     * @param excludeLocation The location of the block that is being removed, used for invalidating, null for validating.
	 * otherwise negative.
	 * @return The found border.
	 */
	protected BlockPos navigateToBorder(World world, BlockPos startLocation, int dimension, boolean max, BlockPos excludeLocation) {
		return navigateToBorder(world, startLocation, dimension, max ? 1 : -1, excludeLocation);
	}
	
	/**
	 * Navigate to a corner from a given startlocation for the given dimensions.
	 * If you would want to navigate to a corner in a 3D world, you would only need 2 dimensions.
	 * @param world The world to look in.
	 * @param startLocation The location to start looking from.
	 * @param dimensions The dimension to navigate in.
	 * @param max If the maximum distance from the startLocation should be looked for.
     * @param excludeLocation The location of the block that is being removed, used for invalidating, null for validating.
	 * @return The corner location.
	 */
	protected BlockPos navigateToCorner(World world, BlockPos startLocation, int[] dimensions, boolean max, BlockPos excludeLocation) {
		BlockPos navigateLocation = LocationHelpers.copyLocation(startLocation);
		for(int dimension : dimensions) {
			navigateLocation = navigateToBorder(world, navigateLocation, dimension, max, excludeLocation);
		}
		return navigateLocation;
	}
	
	protected boolean isEdge(World world, int[][] dimensionEgdes, BlockPos location) {
        int[] c = LocationHelpers.toArray(location);
		for (int i = 0; i < dimensionEgdes.length; i++) {
			for (int j = 0; j < dimensionEgdes[i].length; j++) {
				if(dimensionEgdes[i][j] == c[i]) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Check if a given location is valid, taking into account the edges of the structure, so that
	 * we know which locations should be borders, and which ones should be air.
	 * @param world The world.
	 * @param dimensionEgdes The edges per dimension. [dimension][start=0 | stop=1]
	 * @param location The location to check.
     * @param action The action to execute when a location has been validated.
     * @param excludeLocation The location of the block that is being removed, used for invalidating, null for validating.
	 * @return If the location was valid.
	 */
	protected L10NHelpers.UnlocalizedString validateLocationInStructure(World world, int[][] dimensionEgdes, BlockPos location,
                                                  IValidationAction action, BlockPos excludeLocation) {
		return isValidLocation(world, location, action, excludeLocation);
	}
	
	/**
	 * Run the {@link BlockPosAction} for all the possible locations within this structure.
	 * @param world The world.
	 * @param dimensionEgdes The edges per dimension. [dimension][start=0 | stop=1]
	 * @param locationAction The runnable that will be called for each location in the structure.
	 * @return If the structure is valid for the given edges.
	 */
	protected boolean coordinateRecursion(World world, int[][] dimensionEgdes, BlockPosAction locationAction) {
		return coordinateRecursion(world, dimensionEgdes, new int[]{}, locationAction);
	}
	
	/**
	 * Run the {@link BlockPosAction} for all the possible locations within this structure.
	 * When the accumulatedCoordinates size equals the desired amount of dimensions,
	 * this recursion will enter a leaf and check the conditions for that location.
	 * @param world The world.
	 * @param dimensionEgdes The edges per dimension. [dimension][start=0 | stop=1]
	 * @param accumulatedCoordinates The accumulated coordinates up until now.
	 * @param locationAction The runnable that will be called for each location in the structure.
	 * @return If the structure is valid for the given edges.
	 */
	protected boolean coordinateRecursion(World world, int[][] dimensionEgdes, int[] accumulatedCoordinates,
                                          BlockPosAction locationAction) {
		if(accumulatedCoordinates.length == dimensionEgdes.length) { // Leaf of recursion
			BlockPos location = LocationHelpers.fromArray(accumulatedCoordinates);
			if(!locationAction.run(world, location)) {
				return false;
			}
		} else { // Enter new recursion
			int dimension = accumulatedCoordinates.length;
			for(int i = dimensionEgdes[dimension][0]; i <= dimensionEgdes[dimension][1]; i++) {
				// Append the current dimension coordinate to the coordinate list.
				int[] newAccumulatedCoordinates = Arrays.copyOf(accumulatedCoordinates, accumulatedCoordinates.length + 1);
				newAccumulatedCoordinates[accumulatedCoordinates.length] = i;
				if(!coordinateRecursion(world, dimensionEgdes, newAccumulatedCoordinates, locationAction)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Validate if the blockState at the given location conforms with the {@link AllowedBlock}
	 * conditions.
	 * @param world The world.
	 * @param location The location.
	 * @return Null if the size is valid, otherwise the error message.
	 */
	protected L10NHelpers.UnlocalizedString validateAllowedBlockConditions(World world, BlockPos location) {
		Block block = world.getBlockState(location).getBlock();
		if(blockInfo.containsKey(block)) {
			int occurences = blockOccurences.get(block);
			AllowedBlock allowed = blockInfo.get(block);

			for(IBlockCountValidator validator : allowed.getCountValidators()) {
				L10NHelpers.UnlocalizedString error;
				if((error = validator.isValid(occurences, false, allowed.getBlock())) != null) {
					return error;
				}
			}
			
			blockOccurences.put(block, occurences + 1);
		}
		return null;
	}
	
	/**
	 * This will validate if the given structure has full borders and is hollow at the middle.
	 * To initiate the recursion, call this with an empty accumulatedCoordinates array, it will
	 * then simulate for loop for every dimension between the start and stop coordinate for that
	 * dimension. When the accumulatedCoordinates size equals the desired amount of dimensions,
	 * this recursion will enter a leaf and check the conditions for that location.
	 * @param world The world.
	 * @param dimensionEgdes The edges per dimension. [dimension][start=0 | stop=1]
	 * @param valid True if the structure should be validated, false if it should be invalidated.
     * @param action The action to execute when a location has been validated.
     * @param excludeLocation The location of the block that is being removed, used for invalidating, null for validating.
	 * @return Null the structure is valid for the given edges, the error otherwise.
	 */
	protected L10NHelpers.UnlocalizedString validateDimensionEdges(World world, final int[][] dimensionEgdes,
			final boolean valid, final IValidationAction action, final BlockPos excludeLocation) {
		// Init the blockState occurences counter on zero for all blocks.
		blockOccurences = Maps.newHashMap();
		for(AllowedBlock block : allowedBlocks) {
			blockOccurences.put(block.getBlock(), 0);
		}
		
		// Loop over all dimensions
		final List<L10NHelpers.UnlocalizedString> errors = Lists.newLinkedList();
		boolean minimumValid = coordinateRecursion(world, dimensionEgdes, new BlockPosAction() {

			@Override
			public boolean run(World world, BlockPos location) {
				// Only check the allowed blockState conditions if in validation mode,
				// normally this 'valid' check is not needed, but bugs are always possible...
				if(!valid) return true;
				L10NHelpers.UnlocalizedString allowedBlocks = validateAllowedBlockConditions(world, location);
				L10NHelpers.UnlocalizedString allowedLocation = validateLocationInStructure(world, dimensionEgdes,
						location, action, excludeLocation);
				if(allowedBlocks != null) errors.add(allowedBlocks);
				if(allowedLocation != null) errors.add(allowedLocation);
				return allowedBlocks == null && allowedLocation == null;
			}
			
		});

		if(minimumValid) {
			for(AllowedBlock allowed : allowedBlocks) {
				int occurences = blockOccurences.get(allowed.getBlock());
				for(IBlockCountValidator validator : allowed.getCountValidators()) {
					L10NHelpers.UnlocalizedString error;
					if((error = validator.isValid(occurences, true, allowed.getBlock())) != null) {
						return valid ? error : null;
					}
				}
			}
		}
		return minimumValid ? null : (errors.isEmpty() ? null : errors.get(0));
	}

	protected void postValidate(World world, final Vec3i size, int[][] dimensionEgdes,
			final boolean valid, final BlockPos originCorner, final BlockPos excludeLocation) {
		coordinateRecursion(world, dimensionEgdes, new BlockPosAction() {

			@Override
			public boolean run(World world, BlockPos location) {
				notifyListeners(world, location, size, valid, originCorner);
				return true;
			}
			
		});
	}

    /**
     * Detect a structure at the given start location.
     * @param world The world to look in.
     * @param startLocation The starting location.
     * @param excludeLocation The location of the block that is being removed, used for invalidating, null for validating.
     * @param changeState If the post-validate actions should be called, and thus potentially change the world blockState
     *                    states.
     * @return The size of the found structure. Note that a size in a dimension
     * here starts counting from 0, so a 1x1x1 structure (=1 blockState) will return a
     * size of 0 in each dimension.
     */
    public DetectionResult detect(World world, BlockPos startLocation, BlockPos excludeLocation, boolean changeState) {
        return detect(world, startLocation, excludeLocation, null, changeState);
    }
	
	/**
	 * Detect a structure at the given start location.
	 * @param world The world to look in.
	 * @param startLocation The starting location.
	 * @param excludeLocation The location of the block that is being removed, used for invalidating, null for validating.
     * @param action The action to execute when a location has been validated.
     * @param changeState If the post-validate actions should be called, and thus potentially change the world blockState
     *                    states.
	 * @return The size of the found structure. Note that a size in a dimension
	 * here starts counting from 0, so a 1x1x1 structure (=1 blockState) will return a
	 * size of 0 in each dimension.
	 */
	public DetectionResult detect(World world, BlockPos startLocation, BlockPos excludeLocation, IValidationAction action, boolean changeState) {
		// Next to the origin, we only need one corner for each dimension,
		// we can easily derive if the structure is valid with these 4 corners.
		L10NHelpers.UnlocalizedString error;

		// First detect if the given location is a valid block.
		if((error = isValidLocation(world, startLocation, excludeLocation)) != null) {
			return new DetectionResult(error);
		}

		// Find a corner that can be used as an origin for the structure.
		// We use a temp origin corner to first go to the completely opposite direction
		// in each dimension to ensure we eventually find the actual origin.
		BlockPos tempOriginCorner = navigateToCorner(world, startLocation, new int[]{2, 1, 0}, true, excludeLocation);
		BlockPos originCorner = navigateToCorner(world, tempOriginCorner, new int[]{0, 1, 2}, false, excludeLocation);

		// Find corners in each dimension starting from the origin.
		BlockPos[] corners = new BlockPos[Dimension.DIMENSIONS.length];
		for(int i = 0; i < corners.length; i++) {
			corners[i] = navigateToCorner(world, originCorner, new int[]{i}, true, excludeLocation);
		}

		// Measure the size of the cube with the found corners.
		// Also save each start and stop coordinate for each dimension,
		// we'll use this in the validation of the cube.
		int[] distances = new int[corners.length];
		int[][] dimensionEgdes = new int[corners.length][2]; // [dimension][start | stop]
        int[] cOriginCorner = LocationHelpers.toArray(originCorner);
		for(int i = 0; i < corners.length; i++) {
            int[] cCorner = LocationHelpers.toArray(corners[i]);

			// Distance measurement
            Vec3i sizeDifference = LocationHelpers.subtract(corners[i], originCorner);
			distances[i] = LocationHelpers.toArray(sizeDifference)[i];

			// Start and stop coordinate measurement.
			int addIndex = 0;
			if(cOriginCorner[i] > cCorner[i]) {
				addIndex = 1;
			}
			dimensionEgdes[i][(0 + addIndex) % 2] = cOriginCorner[i];
			dimensionEgdes[i][(1 + addIndex) % 2] = cCorner[i];
		}

		// Loop over each blockState of the cube and check if they have valid blocks or are air.
		if((error = validateDimensionEdges(world, dimensionEgdes, excludeLocation == null, action, excludeLocation)) != null) {
			return new DetectionResult(error);
		}

		Vec3i size = LocationHelpers.fromArray(distances);
		for(ISizeValidator validator : getSizeValidators()) {
			// Check if the size iis valid.
			// If it is invalid we immediately return a null-size, but if we are in invalidation-mode,
			// we skip this step because we always want to be able to invalidate existing structures.
			// TODO: the 'valid' condition might be impossible to have influence if all structure
			// handling goes according to plan.
			if ((error = validator.isSizeValid(size)) != null && excludeLocation == null) {
				return new DetectionResult(error);
			}
		}
        if(changeState) {
			postValidate(world, size, dimensionEgdes, excludeLocation == null, originCorner, excludeLocation);
        }
		return new DetectionResult(size);
	}
	
	/**
	 * Listener for detections.
	 * @author rubensworks
	 *
	 */
	public interface IDetectionListener {
		
		/**
		 * Called when a new structure has been detected.
		 * @param world The world.
		 * @param location The location of a blockState of the structure.
		 * @param size The size of the structure.
		 * @param valid True if the structure should be validated, false if it should be invalidated.
		 * @param originCorner The origin corner block of the structure.
		 */
		public void onDetect(World world, BlockPos location, Vec3i size, boolean valid, BlockPos originCorner);
		
	}
	
	protected interface BlockPosAction {
		
		/**
		 * An action for {@link CubeDetector#coordinateRecursion(World, int[][], BlockPosAction)}.
		 * @param world The world.
		 * @param location The location.
		 * @return If the recursion should continue. If one is false, the full
		 * {@link CubeDetector#coordinateRecursion(World, int[][], BlockPosAction)} will return false.
		 */
		public boolean run(World world, BlockPos location);
		
	}

    public static interface IValidationAction {

        /**
         * An action to execute when a location has been validated.
         * @param location The location that was successfully validated.
         * @param blockState The blockState on that location.
		 * @return Null if the location is valid, otherwise the error message.
         */
        public L10NHelpers.UnlocalizedString onValidate(BlockPos location, IBlockState blockState);

    }

}
