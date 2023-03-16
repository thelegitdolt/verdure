package com.teamtaiga.verdure.Stuff.World.feature;

import com.mojang.serialization.Codec;
import com.teamtaiga.verdure.Data.VerdureTags;
import com.teamtaiga.verdure.Stuff.Blocks.DaisyBlock;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PondFeature extends Feature<NoneFeatureConfiguration> {
    public PondFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        for (int i = 0; i < 50; i++) {
            System.out.println("hello hi dolt here wassup");
        }
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
        List<BlockPos> posses = ExpandHole(FindBorderOffset(InitialHole), level, rand);

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
        List<Direction> directions = new ArrayList<>();
        for (BlockPos possy : Pos) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                if (Pos.contains(possy.relative(dir))) {
                    directions.add(dir);
                }
            }
            if (directions.size() > 0) {
                borders.put(possy, directions);
            }
        }
        return borders;
    }

    // expands the hole created by carver
    // then returns a list of blockpos at the border of the pond to cover with sugarcane, dsies and other stuff
    private List<BlockPos> ExpandHole(HashMap<BlockPos, List<Direction>> border, WorldGenLevel level, RandomSource rand) {
        List<BlockPos> ToAddFoliage = new ArrayList<>();
        List<BlockPos> ToFillWater = new ArrayList<>();
        for (Map.Entry<BlockPos, List<Direction>> entry : border.entrySet()) {
            BlockPos pos = entry.getKey();
            List<Direction> val = entry.getValue();

            for (Direction shun : val) {
                ToFillWater.add(pos.relative(shun).above());
                ToFillWater.add(pos.relative(shun, 2));
                ToAddFoliage.add(pos.relative(shun, 3).above(2));
            }

            if (val.size() == 2) {
                BlockPos corner = pos;
                for (Direction dirac : val) {
                    corner = corner.relative(dirac);
                }
                ToFillWater.add(corner);
                for (Direction erwin : val) {
                    ToAddFoliage.add(corner.relative(erwin).above());
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
            smooth(ToFillWater, ToAddFoliage);
            for (BlockPos fill : ToFillWater) level.setBlock(fill, Blocks.WATER.defaultBlockState(), 2);
        }
        return ToAddFoliage;
    }

    private void smooth(List<BlockPos> toWater, List<BlockPos> toBorder) {

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

    private void PlaceRocks(WorldGenLevel level, BlockPos pos, DaisyBlock daisy) {

    }

    private void PlaceMisc(WorldGenLevel level, BlockPos pos, DaisyBlock daisy) {
        List<Block> PossibleBlocks = ForgeRegistries.BLOCKS.tags().getTag(VerdureTags.POND_FOILIAGE).stream().toList();
    }

}
