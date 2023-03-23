package org.cyclops.cyclopscore.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.GuiHelpers;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * An item renderer that can handle stack sizes larger than 64.
 * @author rubensworks
 */
@OnlyIn(Dist.CLIENT)
public class RenderItemExtendedSlotCount extends ItemRenderer {

    private static RenderItemExtendedSlotCount instance;

    private final ItemRenderer renderItemInner;

    protected RenderItemExtendedSlotCount(Minecraft mc) {
        this(mc, mc.getTextureManager(), mc.getItemRenderer().getItemModelShaper().getModelManager(), mc.getItemColors(), mc.getItemRenderer().getBlockEntityRenderer(), mc.getItemRenderer());
    }

    protected RenderItemExtendedSlotCount(Minecraft mc, TextureManager textureManager, ModelManager modelManager,
                                          ItemColors itemColors, BlockEntityWithoutLevelRenderer blockEntityRenderer,
                                          ItemRenderer renderItemInner) {
        super(mc,
                Objects.requireNonNull(textureManager),
                Objects.requireNonNull(modelManager),
                Objects.requireNonNull(itemColors),
                Objects.requireNonNull(blockEntityRenderer));
        this.renderItemInner = renderItemInner;
    }

    public static RenderItemExtendedSlotCount getInstance() {
        return instance;
    }

    public static void initialize() {
        instance = new RenderItemExtendedSlotCount(Minecraft.getInstance());
    }

    @Override
    public void renderGuiItemDecorations(PoseStack poseStack, Font font, ItemStack stack, int x, int y, @Nullable String text) {
        // ----- Copied and adjusted from super -----
        if (!stack.isEmpty()) {
            poseStack.pushPose();
            if (stack.getCount() != 1 || text != null) {
                String s = text == null ? GuiHelpers.quantityToScaledString(stack.getCount()) : text; // This part was changed
                poseStack.translate(0.0F, 0.0F, 200.0F);
                float scale = 0.5f; // This part was added
                poseStack.scale(scale, scale, 1.0f); // This part was added
                MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                font.drawInBatch(s, (float)((x + 19 - 2) / scale - font.width(s)), (float)(y + 6 + 3) / scale, 16777215,
                        true, poseStack.last().pose(), multibuffersource$buffersource, Font.DisplayMode.NORMAL, 0, 15728880); // Scale was added here
                multibuffersource$buffersource.endBatch();
            }

            if (stack.isBarVisible()) {
                RenderSystem.disableDepthTest();
                int k = stack.getBarWidth();
                int l = stack.getBarColor();
                int i = x + 2;
                int j = y + 13;
                GuiComponent.fill(poseStack, i, j, i + 13, j + 2, -16777216);
                GuiComponent.fill(poseStack, i, j, i + k, j + 1, l | -16777216);
                RenderSystem.enableDepthTest();
            }

            LocalPlayer localplayer = Minecraft.getInstance().player;
            float f = localplayer == null ? 0.0F : localplayer.getCooldowns().getCooldownPercent(stack.getItem(), Minecraft.getInstance().getFrameTime());
            if (f > 0.0F) {
                RenderSystem.disableDepthTest();
                int i1 = y + Mth.floor(16.0F * (1.0F - f));
                int j1 = i1 + Mth.ceil(16.0F * f);
                GuiComponent.fill(poseStack, x, i1, x + 16, j1, Integer.MAX_VALUE);
                RenderSystem.enableDepthTest();
            }

            poseStack.popPose();
            net.minecraftforge.client.ItemDecoratorHandler.of(stack).render(font, stack, x, y, 0);
        }
    }

    // Hacks to refer to correct RenderItem's itemModelMesher instance

    @Override
    public ItemModelShaper getItemModelShaper() {
        return renderItemInner.getItemModelShaper();
    }

    @Override
    public BakedModel getModel(ItemStack stack, @Nullable Level worldIn, @Nullable LivingEntity entitylivingbaseIn, int entityId) {
        return renderItemInner.getModel(stack, worldIn, entitylivingbaseIn, entityId);
    }
}
