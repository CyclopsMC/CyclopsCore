package org.cyclops.cyclopscore.client.render.model;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author rubensworks
 */
public record ItemDynamicItemAndBlockModel(DynamicItemAndBlockModel resolvedModel, ModelRenderProperties modelRenderProperties, @Nullable RenderType renderType) implements ItemModel {

    public ItemDynamicItemAndBlockModel(DynamicItemAndBlockModel resolvedModel, ModelRenderProperties modelRenderProperties) {
        this(resolvedModel, modelRenderProperties, null);
    }

    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver itemModelResolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        new BlockModelWrapper(List.of(), resolvedModel.handleItemState(stack, level, owner), modelRenderProperties)
                .update(renderState, stack, itemModelResolver, displayContext, level, owner, seed);
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
            ModelBaker modelbaker = bakingContext.blockModelBaker();
            DynamicItemAndBlockModel resolvedModel = (DynamicItemAndBlockModel) modelbaker.getModel(this.model);
            TextureSlots textureslots = resolvedModel.getTopTextureSlots();
//            List<BakedQuad> quads = resolvedModel.bakeTopGeometry(textureslots, modelbaker, BlockModelRotation.X0_Y0).getAll();
            ModelRenderProperties modelRenderProperties = ModelRenderProperties.fromResolvedModel(modelbaker, resolvedModel, textureslots);
            return new ItemDynamicItemAndBlockModel(resolvedModel, modelRenderProperties);
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            resolver.markDependency(model);
        }
    }
}
