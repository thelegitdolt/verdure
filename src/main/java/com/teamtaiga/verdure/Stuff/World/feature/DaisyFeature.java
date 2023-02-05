package com.teamtaiga.verdure.Stuff.World.feature;

import com.mojang.serialization.Codec;
import com.teamtaiga.verdure.Stuff.World.DaisyPatchConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Random;

public class DaisyFeature extends Feature<DaisyPatchConfig> {
    public DaisyFeature(Codec<DaisyPatchConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<DaisyPatchConfig> context) {
        DaisyPatchConfig config = context.config();
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();
        RandomSource random = context.level().getRandom();

        int i = 0;
        int tries = config.tries();
        int xzSpread = config.xzSpread() + 1;
        int ySpread = config.ySpread() + 1;
        BlockPos.MutableBlockPos poss = new BlockPos.MutableBlockPos();

        for (int j = 0; j < tries * 4; j ++) {
            poss.setWithOffset(origin, random.nextInt(xzSpread) - random.nextInt(xzSpread), random.nextInt(ySpread) - random.nextInt(ySpread), random.nextInt(xzSpread) - random.nextInt(xzSpread));
            int hello = random.nextInt(64);
            if (hello < 24) {
                if (config.daisyType().value().place(level, context.chunkGenerator(), random, poss)) {
                    ++i;
                }
            } else if (hello < 36) {
                if (config.smallFlowerType().value().place(level, context.chunkGenerator(), random, poss)) {
                    ++i;
                }
            } else if (hello < 52) {
                if (config.shortFeature().value().place(level, context.chunkGenerator(), random, poss)) {
                    ++i;
                }
            } else if (hello <= 64) {
                if (config.tallFeature().value().place(level, context.chunkGenerator(), random, poss)) {
                    ++i;
                }
            }
        }
        return i > 0;
    }
}
