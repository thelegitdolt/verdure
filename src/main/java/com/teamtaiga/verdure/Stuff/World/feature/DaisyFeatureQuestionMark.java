package com.teamtaiga.verdure.Stuff.World.feature;

import com.mojang.serialization.Codec;
import com.teamtaiga.verdure.Stuff.Registry.VerdureBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class DaisyFeatureQuestionMark extends Feature<NoneFeatureConfiguration> {
    private DoublePlantBlock Tall;
    private Block Short;
    private MultifaceBlock Daisy;
    private Block Flower;
    private int Spread;

    public DaisyFeatureQuestionMark(Codec<NoneFeatureConfiguration> config, DoublePlantBlock tall, Block aShort, MultifaceBlock daisy, Block flower, int spread) {
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
        for (Integer[] wassup : GenerateDaisyCords(random, Spread)) {
            if (wassup != null) {
                // todo: make it check so it's like actually the highest block instead of overwriting blocks.
                BlockPos daisyPos = new BlockPos(pos.getX() + wassup[0], pos.getY(), pos.getZ() + wassup[1]);
                level.setBlock(daisyPos, Daisy.defaultBlockState().setValue(PipeBlock.DOWN, true), 2);
                minecraft += SpreadDaisies(level, daisyPos, random, 65);
            }
        }
        for (int i = -Spread; i <= Spread; i++) {
            for (int j = -Spread; j <= Spread; j++) {
                int chance = 100 - ((int) (Math.sqrt(i*i + j*j)) * 20);
                BlockPos toPos = new BlockPos(pos.getX() +i, pos.getY(), pos.getZ() + j);
                if (random.nextInt(100) > chance && !level.getBlockState(toPos).is(Daisy)) {
                    if (random.nextInt(5) < 3) {
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



    public static Integer[][] GenerateDaisyCords(RandomSource rand, int spread) {
        Integer[][] cords = new Integer[2][2];
        if (rand.nextBoolean()) {
            Integer[] generated = generateCoords(rand, spread);
            cords[0] = generated;
            for (int i = 0; i < 2; i++) {
                generated[i] = -generated[i];
            }
            cords[1] = generated;
        }
        else {
            cords[0] = new Integer[]{RandomlyNegative(rand, Math.round(rand.nextFloat())), RandomlyNegative(rand, Math.round(rand.nextFloat()))};
        }
        return cords;
    }

    public int SpreadDaisies(WorldGenLevel level, BlockPos pos, RandomSource rand,  int chance) {
        if (chance > 2) {
            boolean hasPlaced = false;
            if (rand.nextInt(100) < chance) {
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    BlockPos relativePos = pos.relative(dir);
                    // todo: make it check so it's like actually the highest block instead of overwriting blocks.
                    if (!level.getBlockState(relativePos).is(Daisy) && rand.nextInt(3) > 1 && hasPlaced) {
                        level.setBlock(relativePos, Daisy.defaultBlockState().setValue(PipeBlock.DOWN, true), 2);
                        hasPlaced = true;
                        SpreadDaisies(level, relativePos, rand, chance - 30);
                    }
                }
            }
        }
        else {
            if (level.getBlockState(pos).is(Daisy)) {
                for (Direction ect : Direction.Plane.HORIZONTAL) {
                    if (!level.getBlockState(pos.relative(ect)).is(Daisy)) {
                        level.setBlock(pos.relative(ect), Flower.defaultBlockState(), 2);
                    }
                }
            }
            else {
                level.setBlock(pos, Flower.defaultBlockState(), 2);
            }
        }
        return 1;
    }

    public static Integer[] generateCoords(RandomSource rand, int spread) {
        Integer i = (int) Math.round(rand.nextDouble()) + (int) Math.round(rand.nextDouble()) + spread - 2;
        Integer j = (int) Math.round(rand.nextDouble()) + (int) Math.round(rand.nextDouble()) + spread - 2;
        i = RandomlyNegative(rand, i);
        j = RandomlyNegative(rand, j);
        return new Integer[]{i, j};
    }
    public static int RandomlyNegative(RandomSource rand, int hi) {
        return rand.nextBoolean() ? hi : -hi;
    }


    public static void setDoubleBlock(WorldGenLevel level, BlockPos pos, DoublePlantBlock block) {
        level.setBlock(pos, block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 2);
        level.setBlock(pos.above(), block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 2);
    }

}
