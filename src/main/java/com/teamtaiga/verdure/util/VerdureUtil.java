package com.teamtaiga.verdure.util;

import net.minecraft.core.BlockPos;

import static net.minecraft.core.Direction.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.*;

public class VerdureUtil {
    public static final int[][] DIRECTION_NO_DIAGONALS = new int[][] {
            new int[]{1, 0},
            new int[]{-1, 0},
            new int[]{0, 1},
            new int[]{0, -1}
    };
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

    public static BlockPos[] getOrthogonalPos(BlockPos pos) {
        return new BlockPos[]{
                pos.relative(SOUTH), pos.relative(NORTH), pos.relative(WEST), pos.relative(EAST),
                pos.relative(NORTH).relative(EAST), pos.relative(SOUTH).relative(EAST),
                pos.relative(NORTH).relative(WEST), pos.relative(SOUTH).relative(WEST)
        };
    }



    public static void addDoubleBlockToMap(Map<BlockPos, BlockState> map, BlockPos pos, DoublePlantBlock block) {
        map.put(pos, block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
        map.put(pos, block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER));
    }

    public static void setDoubleBlock(WorldGenLevel level, BlockPos pos, DoublePlantBlock block) {
        level.setBlock(pos, block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 2);
        level.setBlock(pos.above(), block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 2);
    }

    public static void setMaybeDoubleOrSingleBlock(WorldGenLevel level, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof DoublePlantBlock DP) {
            setDoubleBlock(level, pos, DP);
        }
        else {
            level.setBlock(pos, state, 2);
        }
    }

    public static void putDoubleInMap(HashMap<BlockPos, BlockState> feature, BlockPos pos, DoublePlantBlock block) {
        feature.put(pos, block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
        feature.put(pos.above(), block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER));
    }

    public static int[][] randomize(int[][] toR) {
        List<int[]> intList =  Arrays.asList(toR);

        Collections.shuffle(intList);

        return intList.toArray(toR);
    }

    // Credit: Sean Patrick Floyd on StackOverflow
    public static boolean ArrayInList(List<int[]> list, int[] candidate) {
        return list.stream().anyMatch(a -> Arrays.equals(a, candidate));
    }

    public static boolean ArrayAllInList(List<int[]> list, List<int[]> candidate) {
        return AAILHelper(list, candidate, 0, candidate.size());
    }
    private static boolean AAILHelper(List<int[]> list, List<int[]> candidate, int chosen, int size) {
        if (chosen >= size) {
            return true;
        }
        else {
            if (ArrayInList(list, candidate.get(chosen))) {
                AAILHelper(list, candidate, chosen + 1, size);
            }
            else return false;
        }
        return false;
    }

    public static int randomlyNegative(RandomSource rand, int hi) {
        return rand.nextBoolean() ? hi : -hi;
    }

    public static int[] transformCords(int[] base, int[] kernel) {
        return new int[]{base[0]  + kernel[0], base[1] + kernel[1]};
    }

    public static boolean isAnyAvailableBlock(BlockState state, Block... blocks) {
        if (state == null) return false;
        for (Block block : blocks) {
            if (state.is(block)) {
               return true;
            }
        }
        return false;
    }

    public static int FindNextAvailableY() {
        return 0;
    }

}
