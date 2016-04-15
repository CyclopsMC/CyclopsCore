package org.cyclops.cyclopscore.proxy;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.event.ConfigChangedEventHook;
import org.cyclops.cyclopscore.event.PlayerRingOfFire;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.item.IBucketRegistry;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.cyclopscore.network.packet.SoundPacket;
import org.cyclops.cyclopscore.world.gen.IRetroGenRegistry;

/**
 * Base proxy for server and client side.
 * @author rubensworks
 *
 */
public abstract class CommonProxyComponent implements ICommonProxy {
	
	protected static final String DEFAULT_RESOURCELOCATION_MOD = "minecraft";

    @Override
    public void registerRenderer(Class<? extends TileEntity> clazz, TileEntitySpecialRenderer renderer) {
        throw new IllegalArgumentException("Registration of renderers should not be called server side!");
    }

    @Override
    public void registerRenderers() {
        // Nothing here as the server doesn't render graphics!
    }

    @Override
    public void registerKeyBindings(IKeyRegistry keyRegistry) {
    }

    @Override
    public void registerPacketHandlers(PacketHandler packetHandler) {

    }

    @Override
    public void registerTickHandlers() {
        
    }

    @Override
    public void registerEventHooks() {
        IRetroGenRegistry retroGenRegistry = getMod().getRegistryManager().getRegistry(IRetroGenRegistry.class);
        if(retroGenRegistry != null) {
            MinecraftForge.EVENT_BUS.register(retroGenRegistry);
        }
        IBucketRegistry bucketRegistry = getMod().getRegistryManager().getRegistry(IBucketRegistry.class);
        if(bucketRegistry != null) {
            MinecraftForge.EVENT_BUS.register(bucketRegistry);
        }

        MinecraftForge.EVENT_BUS.register(new PlayerRingOfFire());
        MinecraftForge.EVENT_BUS.register(new ConfigChangedEventHook(getMod()));
    }

    @Override
    public void playSoundMinecraft(BlockPos pos, String sound, SoundCategory category, float volume, float frequency) {
    	playSoundMinecraft(pos.getX(), pos.getY(), pos.getZ(), sound, category, volume, frequency);
    }

    @Override
    public void playSoundMinecraft(double x, double y, double z, String sound, SoundCategory category, float volume, float frequency) {
    	playSound(x, y, z, sound, category, volume, frequency, DEFAULT_RESOURCELOCATION_MOD);
    }

    @Override
    public void playSound(double x, double y, double z, String sound, SoundCategory category, float volume, float frequency,
    		String mod) {
    	// No implementation server-side.
    }

    @Override
    public void playSound(double x, double y, double z, String sound, SoundCategory category, float volume, float frequency) {
    	playSound(x, y, z, sound, category, volume, frequency, getMod().getModId());
    }

    @Override
    public void sendSoundMinecraft(BlockPos pos, String sound, SoundCategory category, float volume, float frequency) {
		sendSound(pos.getX(), pos.getY(), pos.getZ(), sound, category, volume, frequency, DEFAULT_RESOURCELOCATION_MOD);
    }

    @Override
    public void sendSoundMinecraft(double x, double y, double z, String sound, SoundCategory category, float volume, float frequency) {
		sendSound(x, y, z, sound, category, volume, frequency, DEFAULT_RESOURCELOCATION_MOD);
    }

    @Override
    public void sendSound(double x, double y, double z, String sound, SoundCategory soundCategory, float volume, float frequency,
    		String mod) {
    	SoundPacket packet = new SoundPacket(x, y, z, sound, soundCategory, volume, frequency, mod);
        if(!MinecraftHelpers.isClientSide()) {
            CyclopsCore._instance.getPacketHandler().sendToAll(packet); // Yes, all sounds go through cyclops.
        } else {
            CyclopsCore._instance.getPacketHandler().sendToServer(packet); // Yes, all sounds go through cyclops.
        }
    }

    @Override
    public void sendSound(double x, double y, double z, String sound, SoundCategory category, float volume, float frequency) {
    	sendSound(x, y, z, sound, category, volume, frequency, getMod().getModId());
    }
}
