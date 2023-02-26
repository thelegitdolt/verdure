package com.teamtaiga.verdure.Stuff.World.feature;

import com.mojang.serialization.Codec;
import com.teamtaiga.verdure.Stuff.World.TetrisCarver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.List;

public class PondFeature extends Feature<NoneFeatureConfiguration> {
    public PondFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();

        TetrisCarver carver = new TetrisCarver(4);
        carver.Carve(4);
        for (int[] hello : carver.getPositions()) {
            BlockPos block = new BlockPos(origin.getX() + hello[0], origin.getY(), origin.getZ() + hello[1]);
            level.setBlock(block, Blocks.WATER.defaultBlockState(), 2);
        }

        return true;
    }

    // expands the hole created by carver by either 2 blocks or 1 blocks
    // then returns a list of blockpos at the border of the pond to cover with sugarcane, dsies and other stuff
    public List<BlockPos> ExpandHole() {
        return null;
    }




}
