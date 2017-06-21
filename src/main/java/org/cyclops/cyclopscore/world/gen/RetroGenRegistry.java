package org.cyclops.cyclopscore.world.gen;

import com.google.common.collect.Sets;
import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.Random;
import java.util.Set;

/**
 * Registry for {@link IRetroGen} instances.
 * @author rubensworks
 */
public class RetroGenRegistry implements IRetroGenRegistry {
	
	private static RetroGenRegistry _instance = null;

	private final Set<IRetroGen> retroGeneratables = Sets.newHashSet();
	private final Random random = new Random();
	@Getter private final ModBase mod;
	
	public RetroGenRegistry(ModBase mod) {
		this.mod = mod;
	}

    protected String getNBTTag() {
        return getMod().getModId() + "-RetroGen";
    }
	
	@Override
	public void registerRetroGen(IRetroGen retroGen) {
		retroGeneratables.add(retroGen);
	}

	@Override
	@SubscribeEvent
    public void retroGenLoad(ChunkDataEvent.Load event) {
		if(getMod().getReferenceValue(ModBase.REFKEY_RETROGEN) && event.getData() != null) {
			NBTTagCompound tag = event.getData().getCompoundTag(getNBTTag());
			if(tag == null) {
				tag = new NBTTagCompound();
			}
			setChunkSeed(event.getWorld(), event.getChunk());
			
			boolean atLeastOneModified = false;
			for(IRetroGen retroGen : retroGeneratables) {
				if(retroGen.shouldRetroGen(tag, event.getWorld().provider.getDimension())) {
					retroGen.retroGenerateChunk(tag, event.getChunk(), random);
					getMod().log(Level.INFO, "Retrogenerating chunk at "
                            + event.getChunk().x + ":" + event.getChunk().z);
					atLeastOneModified = true;
				}
			}
			
			if(atLeastOneModified) {
				event.getChunk().markDirty();
			}
		}
	}
	
	private void setChunkSeed(World world, Chunk chunk) {
		// Based on RWTema's DenseOres retrogen
        random.setSeed(world.getSeed());
        long xSeed = random.nextLong() >> 2 + 1L;
        long zSeed = random.nextLong() >> 2 + 1L;
        long chunkSeed = (xSeed * chunk.x + zSeed * chunk.z) ^ world.getSeed();
        random.setSeed(chunkSeed);
	}

	@Override
	@SubscribeEvent
    public void retroGenSave(ChunkDataEvent.Save event) {
		if(getMod().getReferenceValue(ModBase.REFKEY_RETROGEN) && event.getData() != null) {
			NBTTagCompound tag = event.getData().getCompoundTag(getNBTTag());
			if(tag == null) {
				tag = new NBTTagCompound();
			}
			for(IRetroGen retroGen : retroGeneratables) {
				retroGen.afterRetroGen(tag);
			}
			event.getData().setTag(getNBTTag(), tag);
		}
	}
	
}
