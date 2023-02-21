package com.teamtaiga.verdure.Stuff.World.feature;

import com.mojang.serialization.Codec;
import com.teamtaiga.verdure.Stuff.World.VerdureGeneration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class DaisyPatchFeature extends Feature<NoneFeatureConfiguration> {
    public static final int[][] DIRECTIONS_WITH_DIAGONALS = new int[][] {
        new int[]{0, 1},
        new int[]{0, -1},
        new int[]{1, -1},
        new int[]{1, 0},
        new int[]{1, 1},
        new int[]{-1, -1},
        new int[]{-1, 1},
        new int[]{-1, 0}
    };
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
        for (int[] wassup : GenerateDaisyCords(random, Spread)) {
            if (wassup != null) {
                // todo: make it check so it's like actually the highest block instead of overwriting blocks.
                BlockPos daisyPos = new BlockPos(pos.getX() + wassup[0], pos.getY(), pos.getZ() + wassup[1]);
                level.setBlock(daisyPos, Daisy.defaultBlockState().setValue(PipeBlock.DOWN, true), 2);
                minecraft += SpreadDaisies(level, daisyPos, random, pos, 90);
            }
        }
        for (int i = -Spread; i <= Spread; i++) {
            for (int j = -Spread; j <= Spread; j++) {
                int chance = 90 - (int) (Math.sqrt(i*i + j*j) * 25);
                // todo: the thing
                BlockPos toPos = new BlockPos(pos.getX() +i, pos.getY(), pos.getZ() + j);
                if (random.nextInt(100) < chance && !level.getBlockState(toPos).is(Daisy)) {
                    if (random.nextInt(3) < 2) {
                        level.setBlock(toPos, Short.defaultBlockState(), 2);
                    }
                    else {
                        setDoubleBlock(level, toPos, Tall);
                    }
                }
            }
        }
        return minecraft == 0;
    }

    public static int[] generateCoords(RandomSource rand, int spread) {
        int i = RandomlyNegative(rand, rand.nextInt(2) + rand.nextInt(2) + spread - 1);
        int j = RandomlyNegative(rand, rand.nextInt(2) + rand.nextInt(2) + spread - 1);
        return new int[]{i, j};
    }

    public static int[][] GenerateDaisyCords(RandomSource rand, int spread) {
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
            cords[0] = new int[]{RandomlyNegative(rand,  rand.nextInt(2)),  RandomlyNegative(rand, rand.nextInt(2))};
        }
        return cords;
    }

    public int SpreadDaisies(WorldGenLevel level, BlockPos pos, RandomSource rand, BlockPos origin, int chance) {
        if (chance > 0) {
            if (rand.nextInt(100) < chance) {
                for (int[] transformers : DIRECTIONS_WITH_DIAGONALS) {
                    BlockPos relativePos = new BlockPos(pos.getX() + transformers[0], pos.getY(), pos.getZ() + transformers[1]);
                    // todo: make it check so it's like actually the highest block instead of overwriting blocks.
                    if (!level.getBlockState(relativePos).is(Daisy) && isInsideBoundingBox(origin, relativePos) && !level.getBlockState(relativePos).is(Flower) && rand.nextInt(4) == 0) {
                        level.setBlock(relativePos, Daisy.defaultBlockState().setValue(PipeBlock.DOWN, true), 2);
                        for (int[] kernel : DIRECTIONS_WITH_DIAGONALS) {
                            BlockPos SuperRelativePos = new BlockPos(relativePos.getX() + kernel[0], pos.getY(), relativePos.getZ() + kernel[1]);
                            if (!level.getBlockState(SuperRelativePos).is(Daisy) && rand.nextInt(8)  == 1) {
                                level.setBlock(SuperRelativePos, Flower.defaultBlockState(), 2);
                            }
                        }
                        SpreadDaisies(level, relativePos, rand, origin,chance - 20);
                        break;
                    }
                }
            }
        }
        return 1;
    }
    public static int RandomlyNegative(RandomSource rand, int hi) {
        return rand.nextBoolean() ? hi : -hi;
    }

    public boolean isInsideBoundingBox(BlockPos origin, BlockPos isItIdunno) {
        return !(Math.abs(isItIdunno.getX() - origin.getX()) > Spread + 1|| Math.abs(isItIdunno.getZ() - origin.getX()) > Spread + 1);
    }


    public static void setDoubleBlock(WorldGenLevel level, BlockPos pos, DoublePlantBlock block) {
        level.setBlock(pos, block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 2);
        level.setBlock(pos.above(), block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 2);
    }

}
