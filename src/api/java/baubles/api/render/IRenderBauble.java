/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 27, 2014, 8:55:00 PM (GMT)]
 */

package baubles.api.render;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * A Bauble Item that implements this will be have hooks to render something on
 * the player while its equipped.
 * This class doesn't extend IBauble to make the API not depend on the Baubles
 * API, but the item in question still needs to implement IBauble.
 */
public interface IRenderBauble {

	/**
	 * Called for the rendering of the bauble on the player. The player instance can be
	 * acquired through the event parameter. Transformations are already applied for
	 * the RenderType passed in. Make sure to check against the type parameter for
	 * rendering. 
	 */
	public void onPlayerBaubleRender(ItemStack stack, PlayerEntity player, RenderType type, float partialTicks);

	/**
	 * A few helper methods for the render.
	 */
	final class Helper {

		/**
		 * Rotates the render for a bauble correctly if the player is sneaking.
		 * Use for renders under {@link RenderType#BODY}.
		 */
		public static void rotateIfSneaking(PlayerEntity player) {
			if(player.isSneaking())
				applySneakingRotation();
		}

		/**
		 * Rotates the render for a bauble correctly for a sneaking player.
		 * Use for renders under {@link RenderType#BODY}.
		 */
		public static void applySneakingRotation() {
		}

		/**
		 * Shifts the render for a bauble correctly to the head, including sneaking rotation.
		 * Use for renders under {@link RenderType#HEAD}.
		 */
		public static void translateToHeadLevel(PlayerEntity player) {
		}

		/**
		 * Shifts the render for a bauble correctly to the face.
		 * Use for renders under {@link RenderType#HEAD}, and usually after calling {@link Helper#translateToHeadLevel(PlayerEntity)}.
		 */
		public static void translateToFace() {
		}

		/**
		 * Scales down the render to a correct size.
		 * Use for any render.
		 */
		public static void defaultTransforms() {

		}

		/**
		 * Shifts the render for a bauble correctly to the chest.
		 * Use for renders under {@link RenderType#BODY}, and usually after calling {@link Helper#rotateIfSneaking(PlayerEntity)}.
		 */
		public static void translateToChest() {

		}

	}

	public enum RenderType {
		/**
		 * Render Type for the player's body, translations apply on the player's rotation.
		 * Sneaking is not handled and should be done during the render.
		 * @see IRenderBauble.Helper
		 */
		BODY,

		/**
		 * Render Type for the player's body, translations apply on the player's head rotations.
		 * Sneaking is not handled and should be done during the render.
		 * @see IRenderBauble.Helper
		 */
		HEAD;
	}

}