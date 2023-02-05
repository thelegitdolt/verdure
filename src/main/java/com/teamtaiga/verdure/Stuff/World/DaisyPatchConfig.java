package com.teamtaiga.verdure.Stuff.World;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.Nullable;
import java.util.Optional;

public record DaisyPatchConfig(int tries, int xzSpread, int ySpread, Holder<PlacedFeature> daisyType,
 Holder<PlacedFeature> smallFlowerType, Holder<PlacedFeature> tallFeature,  Holder<PlacedFeature> shortFeature, @Nullable Holder<PlacedFeature> floorFeature
) implements FeatureConfiguration {
    public static final Codec<DaisyPatchConfig> CODEC = RecordCodecBuilder.create((config) -> config.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(16).forGetter(DaisyPatchConfig::tries),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xz_spread").orElse(4).forGetter(DaisyPatchConfig::xzSpread),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter(DaisyPatchConfig::ySpread),
            PlacedFeature.CODEC.fieldOf("daisy_type").forGetter(DaisyPatchConfig::daisyType),
            PlacedFeature.CODEC.fieldOf("small_flower_type").forGetter(DaisyPatchConfig::smallFlowerType),
            PlacedFeature.CODEC.fieldOf("tall_feature").forGetter(DaisyPatchConfig::tallFeature),
            PlacedFeature.CODEC.fieldOf("short_feature").forGetter(DaisyPatchConfig::shortFeature),
            PlacedFeature.CODEC.optionalFieldOf("floor_feature").forGetter(floorConfig -> Optional.ofNullable(floorConfig.floorFeature))
    ).apply(config, (tries, xzSpread, yspread, daisy, flower, tall, shortType, floor) -> floor.map(placedFeatureHolder ->
            new DaisyPatchConfig(tries, xzSpread, yspread, daisy, flower, tall, shortType, placedFeatureHolder)).orElseGet(() ->
            new DaisyPatchConfig(tries, xzSpread, yspread, daisy, flower, tall, shortType, null))));

    public DaisyPatchConfig(int tries, int xzSpread, int ySpread, Holder<PlacedFeature> daisyType,
                            Holder<PlacedFeature> smallFlowerType, Holder<PlacedFeature> tallFeature,  Holder<PlacedFeature> shortFeature,
                            @Nullable Holder<PlacedFeature> floorFeature) {
        this.tries = tries;
        this.xzSpread = xzSpread;
        this.ySpread = ySpread;
        this.daisyType = daisyType;
        this.smallFlowerType = smallFlowerType;
        this.tallFeature = tallFeature;
        this.shortFeature = shortFeature;
        this.floorFeature = floorFeature;
    }

    public int tries() {
        return this.tries;
    }

    public int xzSpread() {
        return this.xzSpread;
    }

    public int ySpread() {
        return this.ySpread;
    }

    public Holder<PlacedFeature> getDaisyType() {
        return this.daisyType;
    }

    public Holder<PlacedFeature> getSmallFlower() {
        return this.smallFlowerType;
    }

    public Holder<PlacedFeature> getTallFeature() {
        return this.tallFeature;
    }

    public Holder<PlacedFeature> getShortFeature() {
        return this.shortFeature;
    }

    public Holder<PlacedFeature> floorFeature() {
        return this.floorFeature;
    }
}
