package com.teamtaiga.verdure.common.world.feature;

import com.mojang.serialization.Codec;
import com.teamtaiga.verdure.common.blocks.RockBlock;
import com.teamtaiga.verdure.core.registry.VerdureBlocks;
import com.teamtaiga.verdure.util.VerdureUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.Nullable;
import java.util.ArrayList;
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
        carver.Carve(5, origin, origin.below(2).getY());
         List<BlockPos> InitialHole = carver.getPosses();
        assert InitialHole != null;
        for (BlockPos pos : InitialHole) {
            level.setBlock(pos, Blocks.WATER.defaultBlockState(), 2);
            level.setBlock(pos.above(), Blocks.WATER.defaultBlockState(), 2);
        }
        HashMap<BlockPos, BlockState> toPlace = decorateFoliage(expandHole(findBorderOffset(InitialHole), level), level, rand);


        for (Map.Entry<BlockPos, BlockState> entry : toPlace.entrySet()) {
            level.setBlock(entry.getKey(), entry.getValue(), 2);
        }


        BoneMealItem.growWaterPlant(ItemStack.EMPTY, (Level) level, origin.below(2), null);
        for (BlockPos pos : InitialHole) {
            if (rand.nextInt(12) == 0) {
                BoneMealItem.growWaterPlant(ItemStack.EMPTY, (Level) level, pos, null);
            }
        }

        return true;
    }


    private HashMap<BlockPos, List<Direction>> findBorderOffset(List<BlockPos> Pos) {
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
    // then returns a list of blockpos at the border of the pond to cover with sugarcane, daisies and other stuff
    private List<BlockPos> expandHole(HashMap<BlockPos, List<Direction>> border, WorldGenLevel level) {
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
                Direction theOne = getTOrigin(val);
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

    private static Direction getTOrigin(List<Direction> DumDums) {
        for (Direction dum : DumDums) {
            if (!DumDums.contains(dum.getOpposite())) {
                return dum;
            }
        }
        return null;
    }

    private HashMap<BlockPos, BlockState> decorateFoliage(List<BlockPos> possies, WorldGenLevel level, RandomSource rand) {
        HashMap<BlockPos, BlockState> placing = new HashMap<>();

        int daisyChance = 1;
        int caneChance = 1;
        int rockChance = 1;

        for (BlockPos pos : possies) {
            int hello = level.getRandom().nextInt( (int) (possies.size() / 2.75));
            if (hello == 0) {
                if (rand.nextInt(caneChance) == 0)  {
                    addSugarcane(level.getRandom(), pos, level, placing);
//                    caneChance = 3;
//                }
//                else {
//                    caneChance--;
                }
            }
            else if (hello == 1) {
                 if (rand.nextInt(daisyChance) == 0) {
                     addDaisies(level.getRandom(), pos, level, placing, level.getRandom().nextInt(2, 3));
//                     daisyChance = 3;
//                 }
//                 else {
//                     daisyChance--;
                 }
            }
            else if (hello == 2) {
                if (rand.nextInt(rockChance) == 0) {
                    addRocks(level.getRandom(), pos, level, placing, level.getRandom().nextInt(1, 2));
//                    rockChance = 3;
//                }
//                else {
//                    rockChance--;
                }
            }
        }

        touchUpWithGrass(rand, level, possies, placing);
        return placing;
    }

    private static void addSugarcane(RandomSource rand, BlockPos pos, WorldGenLevel level, HashMap<BlockPos, BlockState> adding) {
        if (Blocks.SUGAR_CANE.defaultBlockState().canSurvive(level, pos)) {
            for (int i = 0; i < rand.nextInt(2, 4); i++) {
                adding.put(pos.above(i), Blocks.SUGAR_CANE.defaultBlockState().setValue(BlockStateProperties.AGE_15, 0));
            }
            Direction dir = null;
            for (Direction dicks : Direction.Plane.HORIZONTAL.shuffledCopy(rand))
                if (!adding.containsKey(pos.relative(dicks))
                        && Blocks.SUGAR_CANE.defaultBlockState().canSurvive(level, pos.relative(dicks))
                        && inBoundingBox(pos.relative(dicks).below(), level, Blocks.WATER))
                    dir = dicks;
            for (int i = 0; i < rand.nextInt(1, 2); i++) {
                if (dir != null) {
                    adding.put(pos.relative(dir).above(i), Blocks.SUGAR_CANE.defaultBlockState().setValue(BlockStateProperties.AGE_15, 0));
                }
            }
        }
    }

    private static void addRocks(RandomSource rand, BlockPos pos, WorldGenLevel level, HashMap<BlockPos, BlockState> adding, int tries) {
        if (tries > 0) {
            int value = 0;
            if (tries == 2 ) value =  2;
            else if (tries == 1) value = 1;
            BlockState state = VerdureBlocks.ROCK.get().defaultBlockState().setValue(RockBlock.LEVEL, value);
            adding.put(pos, state);
            if (rand.nextInt(2) == 0) adding.put(pos.below(), Blocks.COARSE_DIRT.defaultBlockState());
            @Nullable Direction direction = null;
            for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(rand))
                if (!adding.containsKey(pos.relative(dir)) && state.canSurvive(level, pos.relative(dir)) && inBoundingBox(pos.relative(dir).below(), level, Blocks.WATER))  direction = dir;
            if (direction != null) addRocks(rand, pos.relative(direction), level, adding, tries - 1);
        }
    }

    private static void addDaisies(RandomSource rand, BlockPos pos, WorldGenLevel level, HashMap<BlockPos, BlockState> adding, int tries) {
        if (tries > 0) {
            BlockState state = (rand.nextInt(10) > 3 ? VerdureBlocks.WHITE_DAISIES : VerdureBlocks.PINK_DAISIES).get().defaultBlockState().setValue(PipeBlock.DOWN, true);
            adding.put(pos, state);
            if (rand.nextInt(2) == 0) adding.put(pos.below(), Blocks.COARSE_DIRT.defaultBlockState());
            @Nullable Direction direction = null;
            for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(rand))
                if (!adding.containsKey(pos.relative(dir)) && state.canSurvive(level, pos.relative(dir)) && inBoundingBox(pos.relative(dir).below(), level, Blocks.WATER))  direction = dir;
            if (direction != null) addDaisies(rand, pos.relative(direction), level, adding, tries - 1);
        }
    }

    private static void touchUpWithGrass(RandomSource rand, WorldGenLevel level, List<BlockPos> possies, HashMap<BlockPos, BlockState> adding) {
        for (BlockPos pos : possies.stream().filter((pos) -> !VerdureUtil.isAnyAvailableBlock(adding.get(pos), VerdureBlocks.PINK_DAISIES.get(), VerdureBlocks.WHITE_DAISIES.get(),
                VerdureBlocks.ROCK.get(), Blocks.SUGAR_CANE) && Blocks.GRASS.defaultBlockState().canSurvive(level, pos)).toList()) {
            int grassChance = 6;
            for (BlockPos possed : VerdureUtil.getOrthogonalPos(pos)) {
                if (level.getBlockState(possed.above()).is(Blocks.GRASS) || level.getBlockState(possed.above()).is(Blocks.TALL_GRASS)) {
                    grassChance = 4;
                }
            }
            if (rand.nextInt(grassChance) < 3) {
                if (rand.nextInt(3) == 0) {
                    adding.put(pos, Blocks.TALL_GRASS.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
                    adding.put(pos.above(), Blocks.TALL_GRASS.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER));
                    if (rand.nextInt(3) < 2) {
                        adding.put(pos.below(), Blocks.COARSE_DIRT.defaultBlockState());
                    }
                }
                else {
                    adding.put(pos, Blocks.GRASS.defaultBlockState());
                    if (rand.nextInt(3) == 0) {
                        adding.put(pos.below(), Blocks.COARSE_DIRT.defaultBlockState());
                    }
                }
            }
        }
    }

    private static boolean inBoundingBox(BlockPos pos, WorldGenLevel level, Block block) {
        for (BlockPos possy : VerdureUtil.getOrthogonalPos(pos)) {
            if (level.getBlockState(possy).is(block)) {
                return true;
            }
        }
        return false;
    }
}
