package org.cyclops.cyclopscore.world.gen;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.List;
import java.util.Random;

/**
 * A world generator in which {@link WorldGenMinableExtended} instances can be added.
 * Retrogen support is also implicity registered for these instances.
 * @author rubensworks
 *
 */
public class SimpleMinableWorldGenerator implements IWorldGenerator {

	private List<WorldGenMinableExtended> worldGenerators;
	
	/**
	 * Make a new instance.
     * @param mod The mod.
	 * @param worldGenerators The world generator to add.
	 */
	public SimpleMinableWorldGenerator(ModBase mod, List<WorldGenMinableExtended> worldGenerators) {
		this.worldGenerators = worldGenerators;
        IRetroGenRegistry registry = mod.getRegistryManager().getRegistry(IRetroGenRegistry.class);
        if(registry != null) {
            for (WorldGenMinableExtended gen : worldGenerators) {
                registry.registerRetroGen(gen);
            }
        }
	}
	
	@Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        for(WorldGenMinableExtended worldGen : worldGenerators) {
        	worldGen.loopGenerate(world, random, chunkX * 16, chunkZ * 16);
        }
    }

}
