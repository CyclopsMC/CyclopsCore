package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author rubensworks
 */
public class GuiHelpersCommon implements IGuiHelpers {

    protected final IModHelpers modHelpers;

    public GuiHelpersCommon(IModHelpers modHelpers) {
        this.modHelpers = modHelpers;
    }

    @Override
    public int getSlotSize() {
        return 18;
    }

    @Override
    public int getSlotSizeInner() {
        return 16;
    }

    @Override
    public void renderProgressBar(GuiGraphics gui, ResourceLocation texture, int x, int y, int width, int height, int textureX, int textureY,
                                  ProgressDirection direction, int progress, int progressMax) {
        if (progressMax > 0 && progress > 0) {
            int scaledWidth = width;
            int scaledHeight = height;

            // Scale the width and/or height
            if (direction.getIncrementX() != 0) {
                scaledWidth = (int) (scaledWidth * (((double) progress) / progressMax));
            }
            if (direction.getIncrementY() != 0) {
                scaledHeight = (int) (scaledHeight * (((double) progress) / progressMax));
            }

            // If increments happen inversely, make sure we start incrementing from the other end of the progress bar
            if (direction.getIncrementX() < 0) {
                int offset = width - scaledWidth;
                x += offset;
                textureX += offset;
            }
            if (direction.getIncrementY() < 0) {
                int offset = height - scaledHeight;
                y += offset;
                textureY += offset;
            }

            gui.blit(texture, x, y, textureX, textureY, scaledWidth, scaledHeight);
        }
    }

    @Override
    public void drawTooltip(AbstractContainerScreen gui, PoseStack poseStack, List<Component> lines, int x, int y) {
        int guiLeft = gui.leftPos;
        int guiTop = gui.topPos;
        int width = gui.width;
        int height = gui.height;
        Minecraft mc = Minecraft.getInstance();

        GL11.glDisable(GL11.GL_DEPTH_TEST);

        int tooltipWidth = 0;
        int tempWidth;
        int xStart;
        int yStart;

        for(Component line : lines) {
            tempWidth = mc.font.width(line.getString());

            if(tempWidth > tooltipWidth) {
                tooltipWidth = tempWidth;
            }
        }

        xStart = x + 12;
        yStart = y - 12;
        int tooltipHeight = 8;

        if(lines.size() > 1) {
            tooltipHeight += 2 + (lines.size() - 1) * 10;
        }

        if(guiLeft + xStart + tooltipWidth + 6 > width) {
            xStart = width - tooltipWidth - guiLeft - 6;
        }

        if(guiTop + yStart + tooltipHeight + 6 > height) {
            yStart = height - tooltipHeight - guiTop - 6;
        }

        drawTooltipBackground(poseStack, xStart, yStart, tooltipWidth, tooltipHeight);

        PoseStack matrixstack = new PoseStack();
        MultiBufferSource.BufferSource irendertypebuffer$impl = MultiBufferSource.immediate(new ByteBufferBuilder(1536));
        matrixstack.translate(0.0D, 0.0D, 300F);
        Matrix4f matrix4f = matrixstack.last().pose();

        for(int stringIndex = 0; stringIndex < lines.size(); ++stringIndex) {
            Component line = lines.get(stringIndex);

            if(stringIndex == 0) {
                line = Component.literal("\u00a7" + Integer.toHexString(15)).append(line);
            } else {
                line = Component.literal("\u00a77").append(line);
            }

            mc.font.drawInBatch(line.getVisualOrderText(), xStart + guiLeft, yStart + guiTop, -1, true, matrix4f,
                    irendertypebuffer$impl, Font.DisplayMode.NORMAL, 0, 15728880);

            if(stringIndex == 0) {
                yStart += 2;
            }

            yStart += 10;
        }

        irendertypebuffer$impl.endBatch();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    @Override
    public void drawTooltipBackground(PoseStack poseStack, int xStart, int yStart, int tooltipWidth, int tooltipHeight) {
        float zLevel = 300.0F;
        int color1 = -267386864;
        fillGradient(poseStack, xStart - 3, yStart - 4, xStart + tooltipWidth + 3, yStart - 3, color1, color1, zLevel);
        fillGradient(poseStack, xStart - 3, yStart + tooltipHeight + 3, xStart + tooltipWidth + 3, yStart + tooltipHeight + 4, color1, color1, zLevel);
        fillGradient(poseStack, xStart - 3, yStart - 3, xStart + tooltipWidth + 3, yStart + tooltipHeight + 3, color1, color1, zLevel);
        fillGradient(poseStack, xStart - 4, yStart - 3, xStart - 3, yStart + tooltipHeight + 3, color1, color1, zLevel);
        fillGradient(poseStack, xStart + tooltipWidth + 3, yStart - 3, xStart + tooltipWidth + 4, yStart + tooltipHeight + 3, color1, color1, zLevel);
        int color2 = 1347420415;
        int color3 = (color2 & 16711422) >> 1 | color2 & -16777216;
        fillGradient(poseStack, xStart - 3, yStart - 3 + 1, xStart - 3 + 1, yStart + tooltipHeight + 3 - 1, color2, color3, zLevel);
        fillGradient(poseStack, xStart + tooltipWidth + 2, yStart - 3 + 1, xStart + tooltipWidth + 3, yStart + tooltipHeight + 3 - 1, color2, color3, zLevel);
        fillGradient(poseStack, xStart - 3, yStart - 3, xStart + tooltipWidth + 3, yStart - 3 + 1, color2, color2, zLevel);
        fillGradient(poseStack, xStart - 3, yStart + tooltipHeight + 2, xStart + tooltipWidth + 3, yStart + tooltipHeight + 3, color3, color3, zLevel);
    }

    @Override
    public void fillGradient(PoseStack poseStack, int left, int top, int right, int bottom, int startColor, int endColor, float zLevel) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        Matrix4f matrix = poseStack.last().pose();
        bufferbuilder.addVertex(matrix, (float)right, (float)top, zLevel).setColor(f1, f2, f3, f);
        bufferbuilder.addVertex(matrix, (float)left, (float)top, zLevel).setColor(f1, f2, f3, f);
        bufferbuilder.addVertex(matrix, (float)left, (float)bottom, zLevel).setColor(f5, f6, f7, f4);
        bufferbuilder.addVertex(matrix, (float)right, (float)bottom, zLevel).setColor(f5, f6, f7, f4);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
        RenderSystem.disableBlend();
    }

    @Override
    public void renderTooltipOptional(AbstractContainerScreen gui, PoseStack poseStack, int x, int y, int width, int height,
                                             int mouseX, int mouseY, Supplier<Optional<List<Component>>> linesSupplier) {
        if (modHelpers.getRenderHelpers().isPointInRegion(x, y, width, height, mouseX - gui.leftPos, mouseY - gui.topPos)) {
            linesSupplier.get().ifPresent(
                    lines -> drawTooltip(gui, poseStack, lines, mouseX - gui.leftPos, mouseY - gui.topPos));
        }
    }

    @Override
    public void renderTooltip(AbstractContainerScreen gui, PoseStack poseStack, int x, int y, int width, int height,
                                     int mouseX, int mouseY, Supplier<List<Component>> linesSupplier) {
        renderTooltipOptional(gui, poseStack, x, y, width, height, mouseX, mouseY, () -> Optional.of(linesSupplier.get()));
    }

    private static final List<Pair<Long, String>> COUNT_SCALES = Lists.newArrayList(
            Pair.of(1000000000000000000L, "E"),
            Pair.of(1000000000000000L, "P"),
            Pair.of(1000000000000L, "T"),
            Pair.of(1000000000L, "G"),
            Pair.of(1000000L, "M"),
            Pair.of(1000L, "K")
    );

    @Override
    public String quantityToScaledString(long quantity) {
        for (Pair<Long, String> countScale : COUNT_SCALES) {
            long scale = countScale.getLeft();
            if (quantity >= scale) {
                long division = quantity / scale;
                String divisionString = String.valueOf(division);

                // Add digits if string is short
                if (division < 10) {
                    long mod = quantity % scale;
                    if (mod > 0) {
                        long digits = mod * 100 / scale;
                        divisionString += "." + (digits < 10 ? "0" : "") + String.valueOf(digits);
                    }
                } else if (division < 100) {
                    long mod = quantity % scale;
                    if (mod > 0) {
                        long digits = mod * 10 / scale;
                        divisionString += "." + String.valueOf(digits);
                    }
                }

                return divisionString + countScale.getRight();
            }
        }
        return String.valueOf(quantity);
    }
}
