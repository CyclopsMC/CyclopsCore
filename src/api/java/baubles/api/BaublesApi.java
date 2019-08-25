package baubles.api;

import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.PlayerEntity;

/**
 * @author Azanor
 */
public class BaublesApi 
{	
	
	/**
	 * Retrieves the baubles inventory capability handler for the supplied player
	 */
	public static IBaublesItemHandler getBaublesHandler(PlayerEntity player)
	{
		IBaublesItemHandler handler = player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null).orElse(null);
		handler.setPlayer(player);
		return handler;
	}
	
}
