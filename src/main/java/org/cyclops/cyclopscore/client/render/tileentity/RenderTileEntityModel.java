package org.cyclops.cyclopscore.client.render.tileentity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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
        if(getTexture() != null) this.bindTexture(getTexture());

        GlStateManager.pushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
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

        renderModel(tile, getModel(), partialTick);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
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
     */
    protected abstract void renderModel(T tile, M model, float partialTick);
}
