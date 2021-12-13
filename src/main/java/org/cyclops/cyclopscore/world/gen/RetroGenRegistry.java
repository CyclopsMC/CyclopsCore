package org.cyclops.cyclopscore.world.gen;

import com.google.common.collect.Sets;
import lombok.Getter;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
	@Getter
	private final ModBase<?> mod;
	
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
			CompoundNBT tag = event.getData().getCompound(getNBTTag());
			if(tag == null) {
				tag = new CompoundNBT();
			}
			setChunkSeed(event.getWorld(), event.getChunk());
			
			boolean atLeastOneModified = false;
			for(IRetroGen retroGen : retroGeneratables) {
				if(retroGen.shouldRetroGen(tag, event.getWorld().dimensionType())) {
					retroGen.retroGenerateChunk(tag, event.getChunk(), random);
					getMod().log(Level.INFO, "Retrogenerating chunk at "
                            + event.getChunk().getPos().x + ":" + event.getChunk().getPos().z);
					atLeastOneModified = true;
				}
			}
			
			if(atLeastOneModified) {
				event.getChunk().setUnsaved(true);
			}
		}
	}
	
	private void setChunkSeed(IWorld world, IChunk chunk) {
		// Based on RWTema's DenseOres retrogen
		// TODO: Restore retrogen
        /*random.setSeed(world.getSeed());
        long xSeed = random.nextLong() >> 2 + 1L;
        long zSeed = random.nextLong() >> 2 + 1L;
        long chunkSeed = (xSeed * chunk.getPos().x + zSeed * chunk.getPos().z) ^ world.getSeed();
        random.setSeed(chunkSeed);*/
	}

	@Override
	@SubscribeEvent
    public void retroGenSave(ChunkDataEvent.Save event) {
		if(getMod().getReferenceValue(ModBase.REFKEY_RETROGEN) && event.getData() != null) {
			CompoundNBT tag = event.getData().getCompound(getNBTTag());
			if(tag == null) {
				tag = new CompoundNBT();
			}
			for(IRetroGen retroGen : retroGeneratables) {
				retroGen.afterRetroGen(tag);
			}
			event.getData().put(getNBTTag(), tag);
		}
	}
	
}
