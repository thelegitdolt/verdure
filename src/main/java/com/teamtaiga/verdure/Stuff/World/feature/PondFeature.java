package com.teamtaiga.verdure.Stuff.World.feature;

import com.mojang.serialization.Codec;
import com.teamtaiga.verdure.Stuff.Blocks.RockBlock;
import com.teamtaiga.verdure.Stuff.Registry.VerdureBlocks;
import com.teamtaiga.verdure.Stuff.World.TetrisCarver;
import com.teamtaiga.verdure.Util.VerdureUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
        carver.Carve(5, origin, origin.below(2).getY());
        List<BlockPos> InitialHole = carver.getPosses();
        assert InitialHole != null;
        for (BlockPos pos : InitialHole) {
            level.setBlock(pos, Blocks.WATER.defaultBlockState(), 2);
            level.setBlock(pos.above(), Blocks.WATER.defaultBlockState(), 2);
        }
        DecorateFoliage(ExpandHole(FindBorderOffset(InitialHole), level), level, rand);

        BoneMealItem.growWaterPlant(ItemStack.EMPTY, (Level) level, origin.below(2), null);
        for (BlockPos pos : InitialHole) {
            if (rand.nextInt(12) == 0) {
                BoneMealItem.growWaterPlant(ItemStack.EMPTY, (Level) level, pos, null);
            }
        }

        return true;
    }


    private HashMap<BlockPos, List<Direction>> FindBorderOffset(List<BlockPos> Pos) {
        HashMap<BlockPos, List<Direction>> borders = new HashMap<>();
        for (BlockPos possy : Pos) {
            List<Direction> directions = new ArrayList<>();
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                if (!Pos.contains(possy.relative(dir))) {
                    directions.add(dir);
                }
            }
            if (directions.size() > 0) {
                borders.put(possy.above(), directions);
            }
        }
        return borders;
    }

    // expands the hole created by carver
    // then returns a list of blockpos at the border of the pond to cover with sugarcane, dsies and other stuff
    private List<BlockPos> ExpandHole(HashMap<BlockPos, List<Direction>> border, WorldGenLevel level) {
        List<BlockPos> ToAddFoliage = new ArrayList<>();
        List<BlockPos> ToFillWater = new ArrayList<>();
        for (Map.Entry<BlockPos, List<Direction>> entry : border.entrySet()) {
            BlockPos pos = entry.getKey();
            List<Direction> val = entry.getValue();

            for (Direction shun : val) {
                ToFillWater.add(pos.relative(shun));
                ToFillWater.add(pos.relative(shun, 2));
                ToAddFoliage.add(pos.relative(shun, 3).above());
            }

            if (val.size() == 2) {
                BlockPos corner = pos;
                for (Direction dirac : val) {
                    corner = corner.relative(dirac);
                }

                ToFillWater.add(corner);
                for (Direction Dirac : val) {
                    ToAddFoliage.add(corner.relative(Dirac).above());
                }
            }
            if (val.size() == 3) {
                HashMap<BlockPos, Direction> corners = new HashMap<>();
                Direction theOne = GetTOrigin(val);
                assert theOne != null;
                for (Direction Shunned : val) {
                    if (!Shunned.equals(theOne)) {
                        corners.put(pos.relative(theOne).relative(Shunned), Shunned);
                    }
                }
                for (Map.Entry<BlockPos, Direction> entries : corners.entrySet()) {
                    BlockPos original = entries.getKey();
                    ToFillWater.add(original);
                    ToAddFoliage.add(original.relative(theOne).above());
                    ToAddFoliage.add(original.relative(entries.getValue()).above());
                }
            }
            for (BlockPos fill : ToFillWater) level.setBlock(fill, Blocks.WATER.defaultBlockState(), 2);
        }
        return ToAddFoliage;
    }

    private static Direction GetTOrigin(List<Direction> DumDums) {
        for (Direction dum : DumDums) {
            if (!DumDums.contains(dum.getOpposite())) {
                return dum;
            }
        }
        return null;
    }

    private void DecorateFoliage(List<BlockPos> possies, WorldGenLevel level, RandomSource rand) {
        HashMap<BlockPos, BlockState> placing = new HashMap<>();
        int feature = 0;
        while (feature < 4) {
            BlockPos toPos = possies.get(rand.nextInt(possies.size()));
            List<Object> parameters = List.of(
                    level, rand, toPos, possies, placing, 5
            );
            int num = rand.nextInt(4);
            if (rand.nextInt(4) < 3) {
                FOLIAGE_PLACER_TYPES.get(num).accept(parameters);
            }
            else PlaceSugarcane(rand, toPos, placing);
            if (rand.nextInt(5) < 4) feature++;
        }
        for (Map.Entry<BlockPos, BlockState> entries : placing.entrySet()) {
            level.setBlock(entries.getKey(), entries.getValue(), 2);
        }
    }

    private void PlaceSugarcane(RandomSource rand, BlockPos pos, HashMap<BlockPos, BlockState> adding) {
        for (int i = rand.nextInt(3, 4); i == 0; i--) {
            for (int j = 0; j < rand.nextInt(2, 4); i++) {
                adding.put(pos.above(i), Blocks.SUGAR_CANE.defaultBlockState());
            }
        }
    }

    private final List<Consumer<List<Object>>> FOLIAGE_PLACER_TYPES = List.of(
        NewFeatureConsumer((rand, state) -> state.add(0, rand.nextInt(4) == 0 ? Blocks.GRASS.defaultBlockState() : Blocks.TALL_GRASS.defaultBlockState())),
        NewFeatureConsumer((rand, state) -> state.add(0, VerdureBlocks.ROCK.get().defaultBlockState().setValue(RockBlock.LEVEL, rand.nextInt(3)))),
        NewFeatureConsumer((rand, state) -> state.add(0, VerdureBlocks.WHITE_DAISIES.get().defaultBlockState()))
    );

    private Consumer<List<Object>> NewFeatureConsumer(BiConsumer<RandomSource, List<BlockState>> Stater) {
        return (List<Object> a) -> {
            WorldGenLevel level = (WorldGenLevel) a.get(0);
            RandomSource rand = (RandomSource) a.get(1);
            BlockPos pos = (BlockPos) a.get(2);
            List<BlockPos> canHave = (List<BlockPos>) a.get(3);
            HashMap<BlockPos, BlockState> adding = (HashMap<BlockPos, BlockState>) a.get(4);
            Integer times = (Integer) a.get(5);
            List<BlockState> bob = new ArrayList<>();
            Stater.accept(rand, bob);
            BlockState state = bob.get(0);
            if (times < 0) {
                if (state.getBlock() instanceof DoublePlantBlock DP) {
                    VerdureUtil.putDoubleInMap(adding, pos, DP);
                }
                else adding.put(pos, state);
                if (rand.nextInt(4) == 0) adding.put(pos.below(), Blocks.COARSE_DIRT.defaultBlockState());

                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (!level.getBlockState(pos.below().relative(direction)).is(Blocks.WATER) && rand.nextInt(4) == 0) {
                        adding.put(pos.below().relative(direction), Blocks.COARSE_DIRT.defaultBlockState());
                    }
                }
            }
            for (Direction direction : Direction.Plane.HORIZONTAL.shuffledCopy(rand)) {
                BlockPos newerPos = pos.relative(direction);
                if (canHave.contains(newerPos) && !adding.containsKey(newerPos) && state.canSurvive(level, newerPos)) {
                    a.set(5, times - 1);
                    NewFeatureConsumer(Stater).accept(a);
                }
            }
        };
    }
}
