package org.cyclops.cyclopscore.client.render.tileentity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

/**
 * General renderer for {@link org.cyclops.cyclopscore.tileentity.CyclopsTileEntity} with models.
 * @author rubensworks
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public abstract class RenderTileEntityModel<T extends CyclopsTileEntity, M> extends TileEntityRenderer<T> {

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

    protected void preRotate(T tile) {
        GlStateManager.translatef(0.5F, 0.5F, 0.5F);
    }

    protected void postRotate(T tile) {
        GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
    }

    @Override
    public void render(T tile, double x, double y, double z, float partialTick, int destroyStage) {
        Direction direction = tile.getRotation();

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scalef(4.0F, 4.0F, 1.0F);
            GlStateManager.translatef(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else if(getTexture() != null) {
            this.bindTexture(getTexture());
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GlStateManager.scalef(1.0F, -1.0F, -1.0F);
        preRotate(tile);
        short rotation = 0;

        if (direction == Direction.SOUTH) {
            rotation = 180;
        }
        if (direction == Direction.NORTH) {
            rotation = 0;
        }
        if (direction == Direction.EAST) {
            rotation = 90;
        }
        if (direction == Direction.WEST) {
            rotation = -90;
        }

        GlStateManager.rotatef((float) rotation, 0.0F, 1.0F, 0.0F);
        postRotate(tile);

        renderModel(tile, getModel(), partialTick, destroyStage);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
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
