package com.teamtaiga.verdure.Stuff.World;

import com.google.common.collect.ImmutableList;
import com.teamtaiga.verdure.Stuff.Registry.VerdureBlocks;
import com.teamtaiga.verdure.Stuff.World.feature.DaisyPatchFeature;
import com.teamtaiga.verdure.Verdure;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class VerdureGeneration {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Verdure.MOD_ID);
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> FEATURE_DAISY_PATCH_WHITE = FEATURES.register("daisy_patch_white", () -> new DaisyPatchFeature
            (NoneFeatureConfiguration.CODEC, (DoublePlantBlock) Blocks.TALL_GRASS, Blocks.GRASS, (MultifaceBlock) VerdureBlocks.WHITE_DAISIES.get(), Blocks.OXEYE_DAISY, 2));


    public static final class VerdureFeature {
        public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Verdure.MOD_ID);

        public static final RegistryObject<ConfiguredFeature<NoneFeatureConfiguration, ?>> DAISY_PATCH_WHITE = register
                ("daisy_patch_white", () -> new ConfiguredFeature<>(VerdureGeneration.FEATURE_DAISY_PATCH_WHITE.get(), FeatureConfiguration.NONE));


        private static <FC extends FeatureConfiguration, F extends Feature<FC>> RegistryObject<ConfiguredFeature<FC, ?>> register(String name, Supplier<ConfiguredFeature<FC, F>> feature) {
            return CONFIGURED_FEATURES.register(name, feature);
        }
    }

    public static final class VerdurePlacedFeatures {
        public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Verdure.MOD_ID);

        public static final RegistryObject<PlacedFeature> DAISY_PATCH_WHITE = register("daisy_patch_white", VerdureFeature.DAISY_PATCH_WHITE,
                RarityFilter.onAverageOnceEvery(8), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

        private static RegistryObject<PlacedFeature> register(String name, RegistryObject<? extends ConfiguredFeature<?, ?>> feature, PlacementModifier... placementModifiers) {
            return PLACED_FEATURES.register(name, () -> new PlacedFeature((Holder<ConfiguredFeature<?, ?>>) feature.getHolder().get(), ImmutableList.copyOf(placementModifiers)));
        }
    }
}
