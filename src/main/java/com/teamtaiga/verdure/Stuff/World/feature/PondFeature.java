package com.teamtaiga.verdure.Stuff.World.feature;

import com.mojang.serialization.Codec;
import com.teamtaiga.verdure.Stuff.VerdureUtil;
import com.teamtaiga.verdure.Stuff.World.TetrisCarver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.*;

public class PondFeature extends Feature<NoneFeatureConfiguration> {
    public PondFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();

        TetrisCarver carver = new TetrisCarver(4);
        carver.Carve(5);
        List<BlockPos> InitialHole = carver.ConvertToBlockPos(origin, origin.below(2).getY());
        for (BlockPos pos : InitialHole) {
            level.setBlock(pos, Blocks.WATER.defaultBlockState(), 2);
        }
        for (BlockPos pos : ExpandHole(FindBorderOffset(carver.getPositions()), origin, level)) {
            // stuff
        }
        return true;
    }

    // Find the borders of the previously carved pond
    // and then determine which the
    // 0 - +x
    // 1 - -x
    // 2 - +z
    // 3 - z
    public HashMap<int[], boolean[]> FindBorderOffset(List<int[]> Pos) {
        HashMap<int[], boolean[]> borders = new HashMap<>();
        boolean[] directions = new boolean[4];
        for (int[] cord : Pos) {
            for (int i = 0; i < 4; i++) {
                if (!VerdureUtil.ArrayInList(Pos, VerdureUtil.transformCords(cord, VerdureUtil.DIRECTION_NO_DIAGONALS[i]))) {
                    directions[i] = true;
                }
            }
            if (!Arrays.equals(directions, new boolean[]{false, false, false, false})) {
                borders.put(cord, directions);
            }
        }
        return borders;
    }

    // expands the hole created by carver
    // then returns a list of blockpos at the border of the pond to cover with sugarcane, dsies and other stuff
    public List<BlockPos> ExpandHole(HashMap<int[], boolean[]> border, BlockPos origin, WorldGenLevel level) {
        List<BlockPos> Possies = new ArrayList<>();
        for (Map.Entry<int[], boolean[]> entry : border.entrySet()) {
            int[] key = entry.getKey();
            boolean[] val = entry.getValue();
            BlockPos pos = new BlockPos(origin.getX() + key[0], origin.below().getY(), origin.getZ() + key [1]);

            for (int i = 0; i < 4; i++) {
                if (val[i]) {
                    level.setBlock(pos.relative(convert(i)), Blocks.WATER.defaultBlockState(), 2);
                    level.setBlock(pos.relative(convert(i), 2), Blocks.WATER.defaultBlockState(), 2);
                    Possies.add(pos.relative(convert(i), 3).above());
                }
            }
        }
        return Possies;
    }

    private static Direction convert(int i) {
        return switch (i) {
            case 0 -> Direction.EAST;
            case 1 -> Direction.WEST;
            case 2 -> Direction.SOUTH;
            case 3 -> Direction.NORTH;
            default -> null;
        };
    }

}
