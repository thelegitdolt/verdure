package com.teamtaiga.verdure.Stuff.World;

import com.mojang.serialization.Codec;
import com.teamtaiga.verdure.Stuff.Registry.VerdureBiomeModifier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

import java.util.Optional;

// todo: make original code that's not just copy
public record AddFeaturesByFilterBiomeModifier(
        HolderSet<Biome> allowedBiomes,
        Optional<HolderSet<Biome>> deniedBiomes,
        Optional<Float> minimumTemperature,
        Optional<Float> maximumTemperature,
        HolderSet<PlacedFeature> features,
        GenerationStep.Decoration step) implements BiomeModifier
{

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && this.allowedBiomes.contains(biome)) {
            if (deniedBiomes.isPresent() && this.deniedBiomes.get().contains(biome)) {
                return;
            }
            if (minimumTemperature.isPresent() && biome.get().getBaseTemperature() < minimumTemperature.get()) {
                return;
            }
            if (maximumTemperature.isPresent() && biome.get().getBaseTemperature() > maximumTemperature.get()) {
                return;
            }
            BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
            this.features.forEach(holder -> generationSettings.addFeature(this.step, holder));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return VerdureBiomeModifier.ADD_FEATURES_BY_FILTER.get();
    }
}
