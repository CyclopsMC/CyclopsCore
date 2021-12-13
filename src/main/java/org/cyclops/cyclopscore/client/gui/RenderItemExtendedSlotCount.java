package org.cyclops.cyclopscore.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
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
        this(mc.getTextureManager(), mc.getItemRenderer().getItemModelShaper().getModelManager(), mc.getItemColors(), mc.getItemRenderer());
    }

    protected RenderItemExtendedSlotCount(TextureManager textureManager, ModelManager modelManager,
                                          ItemColors itemColors, ItemRenderer renderItemInner) {
        super(Objects.requireNonNull(textureManager),
                Objects.requireNonNull(modelManager),
                Objects.requireNonNull(itemColors));
        this.renderItemInner = renderItemInner;
    }

    public static RenderItemExtendedSlotCount getInstance() {
        return instance;
    }

    public static void initialize() {
        instance = new RenderItemExtendedSlotCount(Minecraft.getInstance());
    }

    public void drawSlotText(FontRenderer fontRenderer, MatrixStack matrixstack, String string, int x, int y) {
        matrixstack.translate(0.0D, 0.0D, (double)(this.blitOffset + 200.0F));
        float scale = 0.5f;
        matrixstack.scale(scale, scale, 1.0f);
        IRenderTypeBuffer.Impl renderTypeBuffer = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        int width = fontRenderer.width(string);
        fontRenderer.drawInBatch(string, (x + 16) / scale - width, (y + 12) / scale, 16777215, true,
                matrixstack.last().pose(), renderTypeBuffer, false, 0, 15728880);
        renderTypeBuffer.endBatch();
    }

    @Override
    public void renderGuiItemDecorations(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text) {
        // ----- Copied and adjusted from super -----
        if (!stack.isEmpty()) {
            MatrixStack matrixstack = new MatrixStack();
            if (stack.getCount() != 1 || text != null) {
                drawSlotText(fr, matrixstack, text == null ? GuiHelpers.quantityToScaledString(stack.getCount()) : text, xPosition, yPosition);
            }

            if (stack.getItem().showDurabilityBar(stack)) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.disableAlphaTest();
                RenderSystem.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuilder();
                double health = stack.getItem().getDurabilityForDisplay(stack);
                int i = Math.round(13.0F - (float)health * 13.0F);
                int j = stack.getItem().getRGBDurabilityForDisplay(stack);
                this.fillRect(bufferbuilder, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
                this.fillRect(bufferbuilder, xPosition + 2, yPosition + 13, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);
                RenderSystem.enableBlend();
                RenderSystem.enableAlphaTest();
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }

            ClientPlayerEntity clientplayerentity = Minecraft.getInstance().player;
            float f3 = clientplayerentity == null ? 0.0F : clientplayerentity.getCooldowns().getCooldownPercent(stack.getItem(), Minecraft.getInstance().getFrameTime());
            if (f3 > 0.0F) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                Tessellator tessellator1 = Tessellator.getInstance();
                BufferBuilder bufferbuilder1 = tessellator1.getBuilder();
                this.fillRect(bufferbuilder1, xPosition, yPosition + MathHelper.floor(16.0F * (1.0F - f3)), 16, MathHelper.ceil(16.0F * f3), 255, 255, 255, 127);
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }

        }
    }

    // Hacks to refer to correct RenderItem's itemModelMesher instance

    @Override
    public ItemModelMesher getItemModelShaper() {
        return renderItemInner.getItemModelShaper();
    }

    @Override
    public IBakedModel getModel(ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entitylivingbaseIn) {
        return renderItemInner.getModel(stack, worldIn, entitylivingbaseIn);
    }
}
