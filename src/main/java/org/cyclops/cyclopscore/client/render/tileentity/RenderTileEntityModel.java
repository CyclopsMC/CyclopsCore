package org.cyclops.cyclopscore.client.render.tileentity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

/**
 * General renderer for {@link org.cyclops.cyclopscore.tileentity.CyclopsTileEntity} with models.
 * @author rubensworks
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public abstract class RenderTileEntityModel<T extends CyclopsTileEntity, M> extends TileEntitySpecialRenderer {

    protected final M model;
	private final ResourceLocation texture;

    /**
     * Make a new instance.
     * @param model The model to render.
     * @param texture The texture to render the model with.
     */
    public RenderTileEntityModel(M model, ResourceLocation texture) {
        this.model = model;
        this.texture = texture;
    }

    /**
     * Get the texture.
     * @return The texture.
     */
	public ResourceLocation getTexture() {
		return texture;
	}
    
    protected void renderTileEntityAt(T tile, double x, double y, double z, float partialTick, int destroyStage) {
        EnumFacing direction = tile.getRotation();

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else if(getTexture() != null) {
            this.bindTexture(getTexture());
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        short rotation = 0;

        if (direction == EnumFacing.SOUTH) {
            rotation = 180;
        }
        if (direction == EnumFacing.NORTH) {
            rotation = 0;
        }
        if (direction == EnumFacing.EAST) {
            rotation = 90;
        }
        if (direction == EnumFacing.WEST) {
            rotation = -90;
        }

        GlStateManager.rotate((float) rotation, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);

        renderModel(tile, getModel(), partialTick, destroyStage);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
	
	@Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick, int destroyStage) {
        this.renderTileEntityAt((T) tile, x, y, z, partialTick, destroyStage);
    }
	
	/**
     * Render the actual model, override this to change the way the model should be rendered.
     * @param tile The tile entity.
     * @param model The base model.
     * @param partialTick The partial render tick.
     * @param destroyStage The destroy stage
     */
    protected abstract void renderModel(T tile, M model, float partialTick, int destroyStage);
}
