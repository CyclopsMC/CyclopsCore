package org.cyclops.cyclopscore.block.multi;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;

import java.util.List;

/**
 * Holder class for the allowed blocks in a structure.
 * @author rubensworks
 *
 */
public class AllowedBlock {
	
	private Block block;
	private final List<IBlockCountValidator> countValidators = Lists.newLinkedList();
	
	/**
	 * Make a new instance.
	 * @param block The allowed block.
	 */
	public AllowedBlock(Block block) {
		this.block = block;
	}
	
	/**
	 * Add a count validator
	 * @param countValidator The count validator.
	 * @return This instance.
	 */
	public AllowedBlock addCountValidator(IBlockCountValidator countValidator) {
		this.countValidators.add(countValidator);
		return this;
	}

	/**
	 * @return The count validators
	 */
	public List<IBlockCountValidator> getCountValidators() {
		return countValidators;
	}

	/**
	 * @return the block
	 */
	public Block getBlock() {
		return block;
	}
	
	@Override
	public boolean equals(Object object) {
		return object instanceof AllowedBlock && getBlock().equals(((AllowedBlock)object).getBlock());
	}
	
}