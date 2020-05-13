package com.terraforged.mod.chunk.component;

import com.terraforged.mod.chunk.TerraChunkGenerator;
import com.terraforged.mod.chunk.fix.ChunkCarverFix;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.ConfiguredCarver;

import java.util.BitSet;
import java.util.List;
import java.util.ListIterator;

public class TerrainCarver {

    private final TerraChunkGenerator generator;

    public TerrainCarver(TerraChunkGenerator generator) {
        this.generator = generator;
    }

    public void carveTerrain(BiomeManager biomes, IChunk chunk, GenerationStage.Carving type) {
        // World carvers have hardcoded 'carvable' blocks which can be problematic with modded blocks
        // AirCarverFix shims the actual blockstates to an equivalent carvable type
        chunk = new ChunkCarverFix(chunk, generator.getContext().materials);

        SharedSeedRandom random = new SharedSeedRandom();

        ChunkPos chunkpos = chunk.getPos();
        int chunkX = chunkpos.x;
        int chunkZ = chunkpos.z;

        BitSet mask = chunk.getCarvingMask(type);
        Biome biome = generator.getBiome(biomes, chunkpos.asBlockPos());

        List<ConfiguredCarver<?>> carvers = biome.getCarvers(type);
        ListIterator<ConfiguredCarver<?>> iterator = carvers.listIterator();

        for(int cx = chunkX - 8; cx <= chunkX + 8; ++cx) {
            for(int cz = chunkZ - 8; cz <= chunkZ + 8; ++cz) {

                while(iterator.hasNext()) {
                    int index = iterator.nextIndex();
                    ConfiguredCarver<?> carver = iterator.next();
                    random.setLargeFeatureSeed(generator.getSeed() + index, cx, cz);
                    if (carver.shouldCarve(random, cx, cz)) {
                        carver.func_227207_a_(chunk, pos -> generator.getBiome(biomes, pos), random, generator.getSeaLevel(), cx, cz, chunkX, chunkZ, mask);
                    }
                }
            }
        }

    }
}