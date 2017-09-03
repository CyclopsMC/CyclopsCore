package org.cyclops.cyclopscore.config.configurable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.cyclopscore.item.DamageIndicatedItemFluidContainer;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Item food that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableDamageIndicatedItemFluidContainer extends DamageIndicatedItemFluidContainer implements IConfigurable {

    protected ExtendedConfig<ItemConfig, Item> eConfig = null;

    protected boolean canPickUp = true;
    private boolean placeFluids = false;

    /**
     * Make a new blockState instance.
     * @param eConfig Config for this item.
     * @param capacity The capacity for the fluid container this item should have.
     * @param fluid The fluid this container should be able to hold.
     */
    protected ConfigurableDamageIndicatedItemFluidContainer(ExtendedConfig<ItemConfig, Item> eConfig, int capacity, Fluid fluid) {
        super(capacity, fluid);
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
    }

    private void setConfig(ExtendedConfig<ItemConfig, Item> eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<ItemConfig, Item> getConfig() {
        return eConfig;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        IFluidHandlerItemCapacity fluidHandler = FluidHelpers.getFluidHandlerItemCapacity(itemStack);
        FluidStack fluidStack = FluidUtil.getFluidContained(itemStack);
        FluidStack drained = fluidHandler.drain(Fluid.BUCKET_VOLUME, false);
        Block block = getFluid().getBlock();

        boolean hasBucket = drained != null
                && (drained.amount == Fluid.BUCKET_VOLUME);
        boolean hasSpace = fluidStack == null
                || (fluidStack.amount + Fluid.BUCKET_VOLUME <= fluidHandler.getCapacity());
        RayTraceResult movingobjectpositionDrain = this.rayTrace(world, player, false);
        RayTraceResult movingobjectpositionFill = this.rayTrace(world, player, true);

        if (movingobjectpositionDrain != null && movingobjectpositionFill != null) {
            if (isPickupFluids() && movingobjectpositionFill.typeOfHit == RayTraceResult.Type.BLOCK) {
                // Fill the container and remove fluid blockState
                BlockPos blockPos = movingobjectpositionFill.getBlockPos();
                if (!world.canMineBlockBody(player, blockPos)) {
                    return MinecraftHelpers.successAction(itemStack);
                }

                /*if (!player.canPlayerEdit(blockPos, movingobjectpositionFill.sideHit, itemStack)) {
                    return itemStack;
                }*/
                if (world.getBlockState(blockPos).getBlock() == block && world.getBlockState(blockPos).getValue(BlockLiquid.LEVEL) == 0) {
                    if(hasSpace) {
                        world.setBlockToAir(blockPos);
                        fluidHandler.fill(new FluidStack(getFluid(), Fluid.BUCKET_VOLUME), true);
                    }
                    return MinecraftHelpers.successAction(itemStack);
                }
            }

            // Drain container and place fluid blockState
            if (hasBucket && isPlaceFluids() && movingobjectpositionDrain.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockPos = movingobjectpositionFill.getBlockPos();
                if (!world.canMineBlockBody(player, blockPos)) {
                    return MinecraftHelpers.successAction(itemStack);
                }

                EnumFacing direction = movingobjectpositionDrain.sideHit;
                blockPos = blockPos.add(direction.getDirectionVec());

                /*if (!player.canPlayerEdit(blockPos, direction, itemStack)) {
                    return itemStack;
                }*/

                if (this.tryPlaceContainedLiquid(world, blockPos, block, true)) {
                    fluidHandler.drain(Fluid.BUCKET_VOLUME, true);
                    return MinecraftHelpers.successAction(itemStack);
                }
            }
        }
        return MinecraftHelpers.successAction(itemStack);
    }

    private boolean tryPlaceContainedLiquid(World world, BlockPos blockPos, Block block, boolean hasBucket) {
        if (!hasBucket) {
            return false;
        } else {
            IBlockState blockState = world.getBlockState(blockPos);
            Material material = blockState.getMaterial();

            if (!world.isAirBlock(blockPos) && material.isSolid()) {
                return false;
            } else {
                if (!world.isRemote && !material.isSolid() && !material.isLiquid()) {
                    world.destroyBlock(blockPos, true);
                }

                world.setBlockState(blockPos, block.getDefaultState(), 3);

                return true;
            }
        }
    }

    @Override
    public boolean canItemEditBlocks() {
        return true;
    }

    /**
     * If this item can place fluids when right-clicking (non-sneaking).
     * The fluid will only be placed if the container has at least 1000 mB inside of it
     * and will drain that accordingly.
     * @return If it can place fluids.
     */
    public boolean isPlaceFluids() {
        return placeFluids;
    }

    /**
     * If this item can pick up fluids when right-clicking (non-sneaking).
     * @return If it can pick up fluids.
     */
    public boolean isPickupFluids() {
        return canPickUp;
    }

    /**
     * Set whether or not this item should be able to place fluids in the world
     * when right-clicking (non-sneaking).
     * The fluid will only be placed if the container has at least 1000 mB inside of it
     * and will drain that accordingly.
     * @param placeFluids If it can place fluids.
     */
    public void setPlaceFluids(boolean placeFluids) {
        this.placeFluids = placeFluids;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag) {
    	L10NHelpers.addOptionalInfo(list, getUnlocalizedName());
        super.addInformation(itemStack, world, list, flag);
    }

    protected FluidStack drainFromOthers(int amount, ItemStack itemStack, Fluid fluid, EntityPlayer player, boolean doDrain) {
        PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
        FluidStack drained = null;
        while(it.hasNext() && amount > 0) {
            ItemStack current = it.next();
            if(current != null && current != itemStack && FluidUtil.getFluidHandler(current) != null) {
                IFluidHandler containerItem = FluidUtil.getFluidHandler(current);
                FluidStack totalFluid = FluidUtil.getFluidContained(current);
                if(totalFluid != null && totalFluid.getFluid() == fluid) {
                    FluidStack thisDrained = containerItem.drain(amount, doDrain);
                    if (thisDrained != null && thisDrained.getFluid() == fluid) {
                        if (drained == null) {
                            drained = thisDrained;
                        } else {
                            drained.amount += drained.amount;
                        }
                        amount -= drained.amount;
                    }
                }
            }
        }
        if(drained != null && drained.amount == 0) {
            drained = null;
        }
        return drained;
    }

    /**
     * If this container can consume a given fluid amount.
     * Will also check other containers inside the player inventory.
     * @param amount The amount to drain.
     * @param itemStack The fluid container.
     * @param player The player.
     * @return If the given amount can be drained.
     */
    public boolean canConsume(int amount, ItemStack itemStack, @Nullable EntityPlayer player) {
        if(canDrain(amount, itemStack)) return true;
        int availableAmount = 0;
        if(FluidUtil.getFluidContained(itemStack) != null) {
            availableAmount = FluidUtil.getFluidContained(itemStack).amount;
        }
        return player != null && drainFromOthers(amount - availableAmount, itemStack, getFluid(), player, false) != null;
    }

    /**
     * Consume a given fluid amount.
     * Will also check other containers inside the player inventory.
     * @param amount The amount to drain.
     * @param itemStack The fluid container.
     * @param player The player.
     * @return The fluid that was drained.
     */
    public FluidStack consume(int amount, ItemStack itemStack, @Nullable EntityPlayer player) {
        boolean doDrain = player == null || (!player.capabilities.isCreativeMode && !player.world.isRemote);
        if (amount == 0) return null;
        FluidStack drained = FluidUtil.getFluidHandler(itemStack).drain(amount, doDrain);
        if (drained != null && drained.amount == amount) return drained;
        int drainedAmount = (drained == null ? 0 : drained.amount);
        int toDrain = amount - drainedAmount;
        FluidStack otherDrained = player == null ? null : drainFromOthers(toDrain, itemStack, getFluid(), player, doDrain);
        if (otherDrained == null) return drained;
        otherDrained.amount += drainedAmount;
        return otherDrained;
    }

}
