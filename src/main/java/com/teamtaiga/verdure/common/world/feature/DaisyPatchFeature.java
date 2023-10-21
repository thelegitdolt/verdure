package com.teamtaiga.verdure.common.world.feature;

import com.mojang.serialization.Codec;
import com.teamtaiga.verdure.util.VerdureUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.HashMap;
import java.util.Map;

import static com.teamtaiga.verdure.util.VerdureUtil.DIRECTIONS_WITH_DIAGONALS;

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
        Map<BlockPos, BlockState> blocksToPlace = new HashMap<>();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos originPos = context.origin();

        for (int[] wassup : generateDaisyOffset(random, Spread)) {
            if (wassup != null) {
                BlockPos daisyPos = new BlockPos(originPos.getX() + wassup[0], originPos.getY(), originPos.getZ() + wassup[1]);

                blocksToPlace.put(daisyPos, Daisy.defaultBlockState().setValue(PipeBlock.DOWN, true));
                spreadDaisies(blocksToPlace, daisyPos, random, originPos, 90);
            }
        }

        for (int i = -Spread; i <= Spread; i++) {
            for (int j = -Spread; j <= Spread; j++) {
                int chance = 90 - (int) (Math.sqrt(i*i + j*j) * 20);

                BlockPos toPos = new BlockPos(originPos.getX() +i, originPos.getY(), originPos.getZ() + j);
                if (random.nextInt(100) < chance && !blocksToPlace.get(toPos).is(Daisy) && !blocksToPlace.get(toPos).is(Daisy)) {
                    if (random.nextInt(7) < 5) {
                        blocksToPlace.put(toPos, Short.defaultBlockState());
                    }
                    else {
                        VerdureUtil.addDoubleBlockToMap(blocksToPlace, toPos, Tall);
                    }
                }
            }
        }
        return generateBlocksInMap(level, blocksToPlace);
    }


    /**
     * If all the blocks in blocksToPlace are valid to place, places them and returns true. If not, return a false.
     */
    private static boolean generateBlocksInMap(WorldGenLevel level, Map<BlockPos, BlockState> blocksToPlace) {
        Map<BlockPos, BlockState> actualMap = new HashMap<>();
        for (Map.Entry<BlockPos, BlockState> entry : blocksToPlace.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue();

            boolean canPlaceThisEntry = false;
            for (int i = 0; i < 4; i++) {
                if (level.getBlockState(pos.relative(Direction.UP, i)).is(Blocks.GRASS_BLOCK)) {
                    canPlaceThisEntry = true;
                    actualMap.put(pos.relative(Direction.UP, i + 1),state);
                    break;
                }
            }
            if (!canPlaceThisEntry)
                return false;
        }

        for (Map.Entry<BlockPos, BlockState> entry : actualMap.entrySet()) {
            level.setBlock(entry.getKey(), entry.getValue(), 2);
        }

        return true;
    }

    /**
     * I DON'T FUCKING KNOW
     */
    private static int[] generateCoords(RandomSource rand, int spread) {
        int i = VerdureUtil.randomlyNegative(rand, rand.nextInt(2) + rand.nextInt(2) + spread - 2);
        int j = VerdureUtil.randomlyNegative(rand, rand.nextInt(2) + rand.nextInt(2) + spread - 2);
        return new int[]{i, j};
    }

    /**
     * Okay so this gives you a 2d array of ????????????
     */
    private static int[][] generateDaisyOffset(RandomSource rand, int spread) {
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
            cords[0] = new int[]{rand.nextInt(3) - 1,  rand.nextInt(2) - 1};
        }
        return cords;
    }

    /**
     * Recursion
     */
    private void spreadDaisies(Map<BlockPos, BlockState> blocksToPlace, BlockPos pos, RandomSource rand, BlockPos origin, int chance) {
        if (chance > 0) {
            boolean notPlaced = true;
            if (rand.nextInt(100) < chance) {
                boolean daisyDoor = false;
                for (int[] transformers : VerdureUtil.randomize(DIRECTIONS_WITH_DIAGONALS)) {
                    BlockPos relativePos = new BlockPos(pos.getX() + transformers[0], pos.getY(), pos.getZ() + transformers[1]);
                    if ((!blocksToPlace.get(relativePos).is(Daisy) && !blocksToPlace.get(relativePos).is(Flower) && notPlaced && rand.nextInt(6) == 0) || daisyDoor) {
                        if (!isPosInsideBoundingBox(pos, relativePos)) {
                            daisyDoor = true;
                        }
                        else {
                            blocksToPlace.put(relativePos, Daisy.defaultBlockState().setValue(PipeBlock.DOWN, true));
                            boolean backdoor = false;
                            for (int[] kernel : VerdureUtil.randomize(DIRECTIONS_WITH_DIAGONALS)) {
                                BlockPos superRelativePos = new BlockPos(relativePos.getX() + kernel[0], pos.getY(), relativePos.getZ() + kernel[1]);
                                if (rand.nextInt(13) == 1 || backdoor) {
                                    if (!isPosInsideBoundingBox(origin, superRelativePos)) {
                                        backdoor = true;
                                    }
                                    else {
                                        blocksToPlace.put(superRelativePos, Flower.defaultBlockState());
                                        notPlaced = false;
                                        backdoor = false;
                                    }
                                }
                            }
                            daisyDoor = false;
                        }
                        spreadDaisies(blocksToPlace, relativePos, rand, origin,chance - 20);
                    }
                }
            }
        }
    }

    private boolean isPosInsideBoundingBox(BlockPos origin, BlockPos pos) {
        return Math.abs(pos.getX() - origin.getX()) < Spread + 1|| Math.abs(pos.getZ() - origin.getX()) < Spread + 1;
    }
}