package com.teamtaiga.verdure.Stuff.Registry;

import com.teamtaiga.verdure.Stuff.World.DaisyPatchConfig;
import com.teamtaiga.verdure.Stuff.World.feature.DaisyFeature;
import com.teamtaiga.verdure.Verdure;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VerdureBiomeFeature {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Verdure.MOD_ID);

    public static final RegistryObject<Feature<DaisyPatchConfig>> DAISY_PATCH = FEATURES.register("daisy_patch", () -> new DaisyFeature(DaisyPatchConfig.CODEC));


}
