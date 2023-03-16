package com.teamtaiga.verdure.Stuff.World.feature;

import com.mojang.serialization.Codec;
import com.teamtaiga.verdure.Data.VerdureTags;
import com.teamtaiga.verdure.Stuff.Blocks.DaisyBlock;
import com.teamtaiga.verdure.Stuff.VerdureUtil;
import com.teamtaiga.verdure.Stuff.World.TetrisCarver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PondFeature extends Feature<NoneFeatureConfiguration> {
    public PondFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource rand = level.getRandom();

        TetrisCarver carver = new TetrisCarver(4);
        carver.Carve(5);
        List<BlockPos> InitialHole = carver.ConvertToBlockPos(origin, origin.below(2).getY());
        for (BlockPos pos : InitialHole) {
            level.setBlock(pos, Blocks.WATER.defaultBlockState(), 2);
        }
        List<BlockPos> posses = ExpandHole(FindBorderOffset(carver.getPositions()), origin, level, rand);

        BoneMealItem.growWaterPlant(ItemStack.EMPTY, (Level) level, origin.below(2), null);
        for (BlockPos pos : InitialHole) {
            if (rand.nextInt(12) == 0) {
                BoneMealItem.growWaterPlant(ItemStack.EMPTY, (Level) level, pos, null);
            }
        }

        return true;
    }

    // Find the borders of the previously carved pond
    // and then determine which the
    // 0 - +x
    // 1 - -x
    // 2 - +z
    // 3 - -z
    private HashMap<int[], boolean[]> FindBorderOffset(List<int[]> Pos) {
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
    private List<BlockPos> ExpandHole(HashMap<int[], boolean[]> border, BlockPos origin, WorldGenLevel level, RandomSource rand) {
        List<BlockPos> Possies = new ArrayList<>();
        for (Map.Entry<int[], boolean[]> entry : border.entrySet()) {
            int[] key = entry.getKey();
            boolean[] val = entry.getValue();
            BlockPos pos = new BlockPos(origin.getX() + key[0], origin.below().getY(), origin.getZ() + key[1]);

            List<Direction> dirs = rand.nextBoolean() ? List.of(Direction.EAST, Direction.WEST) :
                    List.of(Direction.SOUTH, Direction.NORTH);
            int numsOfCorners = 0;
            for (int i = 0; i < 4; i++) {
                if (val[i]) {
                    level.setBlock(pos.relative(convert(i)), Blocks.WATER.defaultBlockState(), 2);
                    level.setBlock(pos.relative(convert(i), 2), Blocks.WATER.defaultBlockState(), 2);
                    Possies.add(pos.relative(convert(i), 3).above());
                    numsOfCorners++;
                }
                if (numsOfCorners == 2) {
                    BlockPos minecraft = pos;
                    for (int j = 0; j < 4; j++) {
                        if (val[j]) {
                            minecraft = minecraft.relative(convert(j));
                        }
                    }
                    level.setBlock(minecraft, Blocks.WATER.defaultBlockState(), 2);
                }
                if (numsOfCorners == 3) {
                    List<BlockPos> toWaterize = new ArrayList<>();
                    for (Direction dir : ActuallyKillMe(val)) {
                        if (dir.equals(OhMyGodWhyDoesThisFeatureHaveSoManyFuckingMethods(val))) {
                            for (Direction dird : ActuallyKillMe(val)) {
                                 if (dird != dir) toWaterize.add(pos.relative(dir).relative(dird));
                            }
                        }
                    }
                    for (BlockPos waterization : toWaterize) {
                        level.setBlock(waterization, Blocks.WATER.defaultBlockState(), 2);
                    }
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

    private static Direction OhMyGodWhyDoesThisFeatureHaveSoManyFuckingMethods(boolean[] bools) {
        ArrayList<Direction> ThisIsProbablyReallyInefficientButIdontCare = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (bools[i]) {
                ThisIsProbablyReallyInefficientButIdontCare.add(convert(i));
            }
        }
        for (Direction KillMe : ThisIsProbablyReallyInefficientButIdontCare) {
            if (!ThisIsProbablyReallyInefficientButIdontCare.contains(KillMe.getOpposite())) {
                return KillMe;
            }
        }
        return null;
    }

    public static List<Direction> ActuallyKillMe(boolean[] a) {
        List<Direction> b = new ArrayList<>();
        for (int c = 0; c<4; c++) {
            if (a[c]) {
                b.add(convert(c));
            }
        }
        return b;
    }

    private void DecorateFoliage(List<BlockPos> possies, WorldGenLevel level, RandomSource rand) {
        int feature = 0;
        while (feature < 5) {
            BlockPos pos = possies.get(rand.nextInt(possies.size()));
            for (int i = 0; i < rand.nextInt(2, 5); i++) {
                for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(rand)) {
                }
            }
        }
    }

    private void PlaceDaisy(WorldGenLevel level, BlockPos pos, DaisyBlock daisy) {

    }

    private void PlaceSugarcane(WorldGenLevel level, RandomSource rand, BlockPos pos, DaisyBlock daisy) {
        for (int i = 0; i < rand.nextInt(2, 4); i++) {
            level.setBlock(pos.above(i), Blocks.SUGAR_CANE.defaultBlockState(), 2);
        }
    }

    private void PlaceGrass(WorldGenLevel level, BlockPos pos, DaisyBlock daisy) {

    }
// /place feature verdure:pond_big
    private void PlaceRocks(WorldGenLevel level, BlockPos pos, DaisyBlock daisy) {

    }

    private void PlaceMisc(WorldGenLevel level, BlockPos pos, DaisyBlock daisy) {
        List<Block> PossibleBlocks = ForgeRegistries.BLOCKS.tags().getTag(VerdureTags.POND_FOILIAGE).stream().toList();
    }

}
