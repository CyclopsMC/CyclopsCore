package org.cyclops.cyclopscore.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
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
        this(mc.getTextureManager(), mc.getItemRenderer().getItemModelMesher().getModelManager(), mc.getItemColors(), mc.getItemRenderer());
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

    public static void drawSlotText(FontRenderer fontRenderer, String string, int x, int y) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepthTest();
        GlStateManager.disableBlend();

        GlStateManager.pushMatrix();
        float scale = 0.5f;
        GlStateManager.scalef(scale, scale, 1.0f);
        int width = fontRenderer.getStringWidth(string);
        fontRenderer.drawStringWithShadow(string, (x + 16) / scale - width, (y + 12) / scale, 16777215);
        GlStateManager.popMatrix();

        GlStateManager.enableLighting();
        GlStateManager.enableDepthTest();
        GlStateManager.enableBlend();
    }

    @Override
    public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text) {
        // ----- Copied and adjusted from super -----
        if (!stack.isEmpty()) {
            if (stack.getCount() != 1 || text != null) {
                drawSlotText(fr, text == null ? GuiHelpers.quantityToScaledString(stack.getCount()) : text, xPosition, yPosition);
            }

            if (stack.getItem().showDurabilityBar(stack)) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepthTest();
                GlStateManager.disableTexture();
                GlStateManager.disableAlphaTest();
                GlStateManager.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                double health = stack.getItem().getDurabilityForDisplay(stack);
                int i = Math.round(13.0F - (float)health * 13.0F);
                int j = stack.getItem().getRGBDurabilityForDisplay(stack);
                this.draw(bufferbuilder, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
                this.draw(bufferbuilder, xPosition + 2, yPosition + 13, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);
                GlStateManager.enableBlend();
                GlStateManager.enableAlphaTest();
                GlStateManager.enableTexture();
                GlStateManager.enableLighting();
                GlStateManager.enableDepthTest();
            }

            ClientPlayerEntity clientplayerentity = Minecraft.getInstance().player;
            float f3 = clientplayerentity == null ? 0.0F : clientplayerentity.getCooldownTracker().getCooldown(stack.getItem(), Minecraft.getInstance().getRenderPartialTicks());
            if (f3 > 0.0F) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepthTest();
                GlStateManager.disableTexture();
                Tessellator tessellator1 = Tessellator.getInstance();
                BufferBuilder bufferbuilder1 = tessellator1.getBuffer();
                this.draw(bufferbuilder1, xPosition, yPosition + MathHelper.floor(16.0F * (1.0F - f3)), 16, MathHelper.ceil(16.0F * f3), 255, 255, 255, 127);
                GlStateManager.enableTexture();
                GlStateManager.enableLighting();
                GlStateManager.enableDepthTest();
            }

        }
    }

    // Hacks to refer to correct RenderItem's itemModelMesher instance

    @Override
    public ItemModelMesher getItemModelMesher() {
        return renderItemInner.getItemModelMesher();
    }

    @Override
    public IBakedModel getItemModelWithOverrides(ItemStack stack, @Nullable World worldIn, @Nullable LivingEntity entitylivingbaseIn) {
        return renderItemInner.getItemModelWithOverrides(stack, worldIn, entitylivingbaseIn);
    }
}
