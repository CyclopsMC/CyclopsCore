package org.cyclops.commoncapabilities.api.ingredient.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.common.extensions.ILevelExtension;

import javax.annotation.Nullable;

/**
 * Abstraction over Items, Entities, and Levels.
 * @author rubensworks
 */
public interface ICapabilityGetter<C> {

    @Nullable
    <T> T getCapability(BaseCapability<T, C> capability, @Nullable C context);

    public boolean canHandleCapabilityType(BaseCapability<?, ?> capability);

    public static <C> ICapabilityGetter<C> forBlock(ILevelExtension level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity blockEntity) {
        return new ICapabilityGetter<>() {
            @Nullable
            @Override
            public <T> T getCapability(BaseCapability<T, C> capability, @Nullable C context) {
                return (T) level.getCapability((BlockCapability<?, C>) capability, pos, state, blockEntity, context);
            }

            @Override
            public boolean canHandleCapabilityType(BaseCapability<?, ?> capability) {
                return capability instanceof BlockCapability;
            }
        };
    }

    public static <C> ICapabilityGetter<C> forBlockEntity(BlockEntity blockEntity) {
        return new ICapabilityGetter<>() {
            @Nullable
            @Override
            public <T> T getCapability(BaseCapability<T, C> capability, @Nullable C context) {
                return (T) blockEntity.getLevel().getCapability((BlockCapability<?, C>) capability, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, context);
            }

            @Override
            public boolean canHandleCapabilityType(BaseCapability<?, ?> capability) {
                return capability instanceof BlockCapability;
            }
        };
    }

    public static <C> ICapabilityGetter<C> forEntity(Entity entity) {
        return new ICapabilityGetter<>() {
            @Nullable
            @Override
            public <T> T getCapability(BaseCapability<T, C> capability, @Nullable C context) {
                return (T) entity.getCapability((EntityCapability<?, C>) capability, context);
            }

            @Override
            public boolean canHandleCapabilityType(BaseCapability<?, ?> capability) {
                return capability instanceof EntityCapability;
            }
        };
    }

    public static <C> ICapabilityGetter<C> forItem(ItemStack itemStack) {
        return new ICapabilityGetter<>() {
            @Nullable
            @Override
            public <T> T getCapability(BaseCapability<T, C> capability, @Nullable C context) {
                return (T) itemStack.getCapability((ItemCapability<?, C>) capability, context);
            }

            @Override
            public boolean canHandleCapabilityType(BaseCapability<?, ?> capability) {
                return capability instanceof ItemCapability;
            }
        };
    }

}
