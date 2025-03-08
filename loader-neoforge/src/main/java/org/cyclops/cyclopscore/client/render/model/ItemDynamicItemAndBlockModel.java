package org.cyclops.cyclopscore.client.render.model;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author rubensworks
 */
public record ItemDynamicItemAndBlockModel(DynamicItemAndBlockModel model) implements ItemModel {
    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver itemModelResolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        new BlockModelWrapper(this.model.handleItemState(stack, level, entity), List.of(new Constant(-1)))
                .update(renderState, stack, itemModelResolver, displayContext, level, entity, seed);
    }

    public static record Unbaked(ResourceLocation model) implements ItemModel.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                ResourceLocation.CODEC.fieldOf("model").forGetter(Unbaked::model)
                        )
                        .apply(instance, Unbaked::new)
        );

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ItemModel bake(BakingContext bakingContext) {
            return new ItemDynamicItemAndBlockModel((DynamicItemAndBlockModel) bakingContext.bake(model));
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            resolver.resolve(model);
        }
    }
}
