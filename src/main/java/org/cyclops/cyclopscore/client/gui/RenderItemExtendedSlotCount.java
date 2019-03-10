package org.cyclops.cyclopscore.client.gui;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.GuiHelpers;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * An item renderer that can handle stack sizes larger than 64.
 * @author rubensworks
 */
@SideOnly(Side.CLIENT)
public class RenderItemExtendedSlotCount extends RenderItem {

    private static RenderItemExtendedSlotCount instance;

    private final RenderItem renderItemInner;

    protected RenderItemExtendedSlotCount(Minecraft mc) {
        this(mc.getTextureManager(), mc.getRenderItem().getItemModelMesher().getModelManager(), mc.getItemColors(), mc.getRenderItem());
    }

    protected RenderItemExtendedSlotCount(TextureManager textureManager, ModelManager modelManager,
                                          ItemColors itemColors, RenderItem renderItemInner) {
        super(Objects.requireNonNull(textureManager),
                Objects.requireNonNull(modelManager),
                Objects.requireNonNull(itemColors));
        this.renderItemInner = renderItemInner;
    }

    public static RenderItemExtendedSlotCount getInstance() {
        return instance;
    }

    public static void initialize() {
        instance = new RenderItemExtendedSlotCount(Minecraft.getMinecraft());
    }

    public static void drawSlotText(FontRenderer fontRenderer, String string, int x, int y) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();

        GlStateManager.pushMatrix();
        float scale = 0.5f;
        GlStateManager.scale(scale, scale, 1.0f);
        int width = fontRenderer.getStringWidth(string);
        fontRenderer.drawStringWithShadow(string, (x + 16) / scale - width, (y + 12) / scale, 16777215);
        GlStateManager.popMatrix();

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableBlend();
    }

    @Override
    public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, @Nullable String text) {
        // ----- Copied and adjusted from super -----
        if (!stack.isEmpty())
        {
            if (stack.getCount() != 1 || text != null)
            {
                drawSlotText(fr, text == null ? GuiHelpers.quantityToScaledString(stack.getCount()) : text, xPosition, yPosition);
            }

            if (stack.getItem().showDurabilityBar(stack))
            {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                double health = stack.getItem().getDurabilityForDisplay(stack);
                int rgbfordisplay = stack.getItem().getRGBDurabilityForDisplay(stack);
                int i = Math.round(13.0F - (float)health * 13.0F);
                int j = rgbfordisplay;
                this.draw(bufferbuilder, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
                this.draw(bufferbuilder, xPosition + 2, yPosition + 13, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }

            EntityPlayerSP entityplayersp = Minecraft.getMinecraft().player;
            float f3 = entityplayersp == null ? 0.0F : entityplayersp.getCooldownTracker().getCooldown(stack.getItem(), Minecraft.getMinecraft().getRenderPartialTicks());

            if (f3 > 0.0F)
            {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableTexture2D();
                Tessellator tessellator1 = Tessellator.getInstance();
                BufferBuilder bufferbuilder1 = tessellator1.getBuffer();
                this.draw(bufferbuilder1, xPosition, yPosition + MathHelper.floor(16.0F * (1.0F - f3)), 16, MathHelper.ceil(16.0F * f3), 255, 255, 255, 127);
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }
    }

    // Hacks to refer to correct RenderItem's itemModelMesher instance

    @Override
    public ItemModelMesher getItemModelMesher() {
        return renderItemInner.getItemModelMesher();
    }

    @Override
    public boolean shouldRenderItemIn3D(ItemStack stack) {
        return renderItemInner.shouldRenderItemIn3D(stack);
    }

    @Override
    public IBakedModel getItemModelWithOverrides(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entitylivingbaseIn) {
        return renderItemInner.getItemModelWithOverrides(stack, worldIn, entitylivingbaseIn);
    }

    @Override
    protected void registerItem(Item itm, int subType, String identifier) {
        // Avoid registering anything here
    }

    @Override
    protected void registerBlock(Block blk, int subType, String identifier) {
        // Avoid registering anything here
    }
}
