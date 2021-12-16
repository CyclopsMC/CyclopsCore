package org.cyclops.cyclopscore.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.Font;
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
        this(mc.getTextureManager(), mc.getItemRenderer().getItemModelShaper().getModelManager(), mc.getItemColors(), mc.getItemRenderer().getBlockEntityRenderer(), mc.getItemRenderer());
    }

    protected RenderItemExtendedSlotCount(TextureManager textureManager, ModelManager modelManager,
                                          ItemColors itemColors, BlockEntityWithoutLevelRenderer blockEntityRenderer,
                                          ItemRenderer renderItemInner) {
        super(Objects.requireNonNull(textureManager),
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

    public void drawSlotText(Font fontRenderer, PoseStack matrixstack, String string, int x, int y) {
        matrixstack.translate(0.0D, 0.0D, (double)(this.blitOffset + 200.0F));
        float scale = 0.5f;
        matrixstack.scale(scale, scale, 1.0f);
        MultiBufferSource.BufferSource renderTypeBuffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        int width = fontRenderer.width(string);
        fontRenderer.drawInBatch(string, (x + 16) / scale - width, (y + 12) / scale, 16777215, true,
                matrixstack.last().pose(), renderTypeBuffer, false, 0, 15728880);
        renderTypeBuffer.endBatch();
    }

    @Override
    public void renderGuiItemDecorations(Font font, ItemStack stack, int x, int y, @Nullable String text) {
        // ----- Copied and adjusted from super -----
        if (!stack.isEmpty()) {
            PoseStack posestack = new PoseStack();
            if (stack.getCount() != 1 || text != null) {
                drawSlotText(font, posestack, text == null ? GuiHelpers.quantityToScaledString(stack.getCount()) : text, x, y);
            }

            if (stack.isBarVisible()) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.disableBlend();
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder bufferbuilder = tesselator.getBuilder();
                int i = stack.getBarWidth();
                int j = stack.getBarColor();
                this.fillRect(bufferbuilder, x + 2, y + 13, 13, 2, 0, 0, 0, 255);
                this.fillRect(bufferbuilder, x + 2, y + 13, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);
                RenderSystem.enableBlend();
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }

            LocalPlayer localplayer = Minecraft.getInstance().player;
            float f = localplayer == null ? 0.0F : localplayer.getCooldowns().getCooldownPercent(stack.getItem(), Minecraft.getInstance().getFrameTime());
            if (f > 0.0F) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                Tesselator tesselator1 = Tesselator.getInstance();
                BufferBuilder bufferbuilder1 = tesselator1.getBuilder();
                this.fillRect(bufferbuilder1, x, y + Mth.floor(16.0F * (1.0F - f)), 16, Mth.ceil(16.0F * f), 255, 255, 255, 127);
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }

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
