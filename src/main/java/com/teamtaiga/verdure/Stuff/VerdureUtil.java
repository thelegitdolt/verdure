package com.teamtaiga.verdure.Stuff;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VerdureUtil {
    public static final int[][] DIRECTION_NO_DIAGONALS = new int[][] {
            new int[]{0, 1},
            new int[]{0, -1},
            new int[]{1, 0},
            new int[]{-1, 0}
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
    public static void setDoubleBlock(WorldGenLevel level, BlockPos pos, DoublePlantBlock block) {
        level.setBlock(pos, block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 2);
        level.setBlock(pos.above(), block.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 2);
    }



    public static int[][] Randomize(int[][] toR) {
        List<int[]> intList =  Arrays.asList(toR);

        Collections.shuffle(intList);

        return intList.toArray(toR);
    }

    public static int RandomlyNegative(RandomSource rand, int hi) {
        return rand.nextBoolean() ? hi : -hi;
    }

    public static int[] transformCords(int[] base, int[] kernel) {
        for (int i = 0; i < base.length; i++) {
            base[i] += kernel[i];
        }
        return base;
    }

    public static int FindNextAvailableY() {
        return 0;
    }

}
