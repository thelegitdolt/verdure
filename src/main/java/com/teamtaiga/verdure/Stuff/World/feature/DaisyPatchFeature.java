package com.teamtaiga.verdure.Stuff.World.feature;

import com.mojang.serialization.Codec;
import com.teamtaiga.verdure.Util.VerdureUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static com.teamtaiga.verdure.Util.VerdureUtil.DIRECTIONS_WITH_DIAGONALS;

public class DaisyPatchFeature extends Feature<NoneFeatureConfiguration> {
    private final DoublePlantBlock Tall;
    private final Block Short;
    private final MultifaceBlock Daisy;
    private final Block Flower;
    private final int Spread;
    public DaisyPatchFeature(Codec<NoneFeatureConfiguration> config, DoublePlantBlock tall, Block aShort, MultifaceBlock daisy, Block flower, int spread) {
        super(config);
        Tall = tall;
        Short = aShort;
        Daisy = daisy;
        Flower = flower;
        Spread = spread;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos pos = context.origin();
        int minecraft = 0;
        for (int[] wassup : GenerateDaisyOffset(random, Spread)) {
            if (wassup != null) {
                // todo: make it check so it's like actually the highest block instead of overwriting blocks.
                BlockPos daisyPos = new BlockPos(pos.getX() + wassup[0], pos.getY(), pos.getZ() + wassup[1]);
                level.setBlock(daisyPos, Daisy.defaultBlockState().setValue(PipeBlock.DOWN, true), 2);
                minecraft += SpreadDaisies(level, daisyPos, random, pos, 90);
            }
        }
        for (int i = -Spread; i <= Spread; i++) {
            for (int j = -Spread; j <= Spread; j++) {
                int chance = 90 - (int) (Math.sqrt(i*i + j*j) * 20);
                // todo: the thing
                BlockPos toPos = new BlockPos(pos.getX() +i, pos.getY(), pos.getZ() + j);
                if (random.nextInt(100) < chance && !level.getBlockState(toPos).is(Daisy) && !level.getBlockState(toPos).is(Flower)) {
                    if (random.nextInt(7) < 5) {
                        level.setBlock(toPos, Short.defaultBlockState(), 2);
                    }
                    else {
                        VerdureUtil.setDoubleBlock(level, toPos, Tall);
                    }
                }
            }
        }
        return minecraft == 0;
    }

    private static int[] generateCoords(RandomSource rand, int spread) {
        int i = VerdureUtil.RandomlyNegative(rand, rand.nextInt(2) + rand.nextInt(2) + spread - 2);
        int j = VerdureUtil.RandomlyNegative(rand, rand.nextInt(2) + rand.nextInt(2) + spread - 2);
        return new int[]{i, j};
    }

    private static int[][] GenerateDaisyOffset(RandomSource rand, int spread) {
        int[][] cords = new int[2][2];
        if (rand.nextBoolean()) {
            int[] generated = generateCoords(rand, spread);
            cords[0] = generated;
            for (int i = 0; i < 2; i++) {
                generated[i] = -generated[i];
            }
            cords[1] = generated;
        }
        else {
            cords[0] = new int[]{rand.nextInt(3)-1,  rand.nextInt(2) -1};
        }
        return cords;
    }

    private int SpreadDaisies(WorldGenLevel level, BlockPos pos, RandomSource rand, BlockPos origin, int chance) {
        if (chance > 0) {
            boolean notPlaced = true;
            if (rand.nextInt(100) < chance) {
                boolean daisyDoor = false;
                for (int[] transformers : VerdureUtil.Randomize(DIRECTIONS_WITH_DIAGONALS)) {
                    BlockPos relativePos = new BlockPos(pos.getX() + transformers[0], pos.getY(), pos.getZ() + transformers[1]);
                    // todo: make it check so it's like actually the highest block instead of overwriting blocks.
                    if ((!level.getBlockState(relativePos).is(Daisy) && !level.getBlockState(relativePos).is(Flower) && notPlaced && rand.nextInt(6) == 0) || daisyDoor) {
                        if (!isInsideBoundingBox(pos, relativePos)) {
                            daisyDoor = true;
                        }
                        else {
                            level.setBlock(relativePos, Daisy.defaultBlockState().setValue(PipeBlock.DOWN, true), 2);
                            boolean backdoor = false;
                            for (int[] kernel : VerdureUtil.Randomize(DIRECTIONS_WITH_DIAGONALS)) {
                                BlockPos SuperRelativePos = new BlockPos(relativePos.getX() + kernel[0], pos.getY(), relativePos.getZ() + kernel[1]);
                                if (rand.nextInt(13) == 1 || backdoor) {
                                    if (!isInsideBoundingBox(origin, SuperRelativePos)) {
                                        backdoor = true;
                                    } else {
                                        level.setBlock(SuperRelativePos, Flower.defaultBlockState(), 2);
                                        notPlaced = false;
                                        backdoor = false;
                                    }
                                }
                            }
                            daisyDoor = false;
                        }
                        SpreadDaisies(level, relativePos, rand, origin,chance - 20);
                    }
                }
            }
        }
        return 1;
    }

    private boolean isInsideBoundingBox(BlockPos origin, BlockPos isItIdunno) {
        return Math.abs(isItIdunno.getX() - origin.getX()) < Spread + 1|| Math.abs(isItIdunno.getZ() - origin.getX()) < Spread + 1;
    }


}