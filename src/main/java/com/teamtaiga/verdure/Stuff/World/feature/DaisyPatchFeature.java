package com.teamtaiga.verdure.Stuff.World.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
                        setDoubleBlock(level, toPos, Tall);
                    }
                }
            }
        }
        return minecraft == 0;
    }

    public static int[] generateCoords(RandomSource rand, int spread) {
        int i = RandomlyNegative(rand, rand.nextInt(2) + rand.nextInt(2) + spread - 2);
        int j = RandomlyNegative(rand, rand.nextInt(2) + rand.nextInt(2) + spread - 2);
        return new int[]{i, j};
    }

    public static int[][] GenerateDaisyOffset(RandomSource rand, int spread) {
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

    public int SpreadDaisies(WorldGenLevel level, BlockPos pos, RandomSource rand, BlockPos origin, int chance) {
        if (chance > 0) {
            boolean notPlaced = true;
            if (rand.nextInt(100) < chance) {
                boolean daisyDoor = false;
                for (int[] transformers : Randomize(DIRECTIONS_WITH_DIAGONALS)) {
                    BlockPos relativePos = new BlockPos(pos.getX() + transformers[0], pos.getY(), pos.getZ() + transformers[1]);
                    // todo: make it check so it's like actually the highest block instead of overwriting blocks.
                    if ((!level.getBlockState(relativePos).is(Daisy) && !level.getBlockState(relativePos).is(Flower) && notPlaced && rand.nextInt(6) == 0) || daisyDoor) {
                        if (!isInsideBoundingBox(pos, relativePos)) {
                            daisyDoor = true;
                        }
                        else {
                            level.setBlock(relativePos, Daisy.defaultBlockState().setValue(PipeBlock.DOWN, true), 2);
                            boolean backdoor = false;
                            for (int[] kernel : Randomize(DIRECTIONS_WITH_DIAGONALS)) {
                                BlockPos SuperRelativePos = new BlockPos(relativePos.getX() + kernel[0], pos.getY(), relativePos.getZ() + kernel[1]);
                                if ((!level.getBlockState(SuperRelativePos).is(Daisy) && rand.nextInt(12) == 1) || backdoor) {
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
    public static int RandomlyNegative(RandomSource rand, int hi) {
        return rand.nextBoolean() ? hi : -hi;
    }

    public boolean isInsideBoundingBox(BlockPos origin, BlockPos isItIdunno) {
        return Math.abs(isItIdunno.getX() - origin.getX()) < Spread + 1|| Math.abs(isItIdunno.getZ() - origin.getX()) < Spread + 1;
    }


    public static void setDoubleBlock(WorldGenLevel level, BlockPos pos, DoublePlantBlock block) {
        level.setBlock(pos, block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 2);
        level.setBlock(pos.above(), block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 2);
    }

    public int[][] Randomize(int[][] toR) {
        List<int[]> intList =  Arrays.asList(toR);

        Collections.shuffle(intList);

        return intList.toArray(toR);
    }
}
