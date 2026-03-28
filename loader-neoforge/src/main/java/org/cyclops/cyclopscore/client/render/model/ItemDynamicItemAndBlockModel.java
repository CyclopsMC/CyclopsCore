package org.cyclops.cyclopscore.client.render.model;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.*;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

import java.util.List;

/**
 * @author rubensworks
 */
public record ItemDynamicItemAndBlockModel(DynamicItemAndBlockModel resolvedModel, ModelRenderProperties modelRenderProperties) implements ItemModel {

    @Override
    public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver itemModelResolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        List<BakedQuad> quads = resolvedModel.handleItemState(stack, level, owner);
        QuadCollection.Builder quadBuilder = new QuadCollection.Builder();
        for (BakedQuad quad : quads) {
            quadBuilder.addUnculledFace(quad);
        }
        new CuboidItemModelWrapper(List.of(), quadBuilder.build(), modelRenderProperties, new Matrix4f())
                .update(renderState, stack, itemModelResolver, displayContext, level, owner, seed);
    }

    public static record Unbaked(Identifier model) implements ItemModel.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Identifier.CODEC.fieldOf("model").forGetter(Unbaked::model)
                        )
                        .apply(instance, Unbaked::new)
        );

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ItemModel bake(BakingContext bakingContext, Matrix4fc matrix4fc) {
            ModelBaker modelbaker = bakingContext.blockModelBaker();
            DynamicItemAndBlockModel resolvedModel = (DynamicItemAndBlockModel) modelbaker.getModel(this.model);
            TextureSlots textureslots = resolvedModel.getTopTextureSlots();
            ModelRenderProperties modelRenderProperties = ModelRenderProperties.fromResolvedModel(modelbaker, resolvedModel, textureslots);
            return new ItemDynamicItemAndBlockModel(resolvedModel, modelRenderProperties);
        }

        @Override
        public void resolveDependencies(Resolver resolver) {
            resolver.markDependency(model);
        }
    }
}
