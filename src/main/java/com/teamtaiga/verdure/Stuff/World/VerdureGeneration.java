package com.teamtaiga.verdure.Stuff.World;

import com.teamtaiga.verdure.Stuff.Registry.VerdureBiomeFeature;
import com.teamtaiga.verdure.Stuff.Registry.VerdureBlocks;
import com.teamtaiga.verdure.Verdure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class VerdureGeneration {
    public static Holder<ConfiguredFeature<DaisyPatchConfig, ?>> FEATURE_DAISY_PATCH_WHITE;
    public static Holder<ConfiguredFeature<DaisyPatchConfig, ?>> FEATURE_DAISY_PATCH_PINK;
    public static Holder<ConfiguredFeature<DaisyPatchConfig, ?>> FEATURE_DAISY_PATCH_BLUE;

    public static Holder<PlacedFeature> DAISY_PATCH_WHITE;
    public static Holder<PlacedFeature> DAISY_PATCH_PINK;
    public static Holder<PlacedFeature> DAISY_PATCH_BLUE;

    public static final BlockPos BLOCK_BELOW = new BlockPos(0, -1, 0);
    public static final BlockPos BLOCK_ABOVE = new BlockPos(0, 1, 0);

    public static final BiomeTagFilter TAGGED_IS_OVERWORLD = BiomeTagFilter.biomeIsInTag(BiomeTags.IS_OVERWORLD);


    public static void registerGeneration() {
        FEATURE_DAISY_PATCH_WHITE = register(new ResourceLocation(Verdure.MOD_ID, "daisy_patch_white"),
                VerdureBiomeFeature.DAISY_PATCH.get(),
                getDaisyConfig(VerdureBlocks.WHITE_DAISIES.get(),
                        Blocks.OXEYE_DAISY, Blocks.TALL_GRASS, Blocks.GRASS,
                        BlockPredicate.matchesTag(BLOCK_BELOW, BlockTags.DIRT)));


        DAISY_PATCH_WHITE = registerPlacement(new ResourceLocation(Verdure.MOD_ID, "daisy_patch_white"),
                FEATURE_DAISY_PATCH_WHITE, RarityFilter.onAverageOnceEvery(2),
                InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome(), TAGGED_IS_OVERWORLD);

    }


    static Holder<PlacedFeature> registerPlacement(ResourceLocation id,
               Holder<? extends ConfiguredFeature<?, ?>> feature,
               PlacementModifier... modifiers) {
        return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, id,
                new PlacedFeature(Holder.hackyErase(feature), List.of(modifiers)));
    }

    public static DaisyPatchConfig getDaisyConfig(Block DT, Block SFT, Block tall, Block Short,
                                                       BlockPredicate plantedOn) {
        return new DaisyPatchConfig(64, 6, 3,
                plantBlockConfig(DT, plantedOn),
                plantBlockConfig(SFT, plantedOn), plantBlockConfig(Short, plantedOn),
                plantBlockConfig(tall, plantedOn),null);
    }

    private static <V extends T, T> Holder<V> register(Registry<T> registry, ResourceLocation id, V value) {
        return (Holder<V>) BuiltinRegistries.<T>register(registry, id, value);
    }

    protected static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<FC, ?>> register(ResourceLocation id, F feature, FC featureConfig) {
        return register(BuiltinRegistries.CONFIGURED_FEATURE, id, new ConfiguredFeature<>(feature, featureConfig));
    }

    public static Holder<PlacedFeature> plantBlockConfig(Block block, BlockPredicate plantedOn) {
        return PlacementUtils.filtered(
                Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)),
                BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, plantedOn));
    }

}
