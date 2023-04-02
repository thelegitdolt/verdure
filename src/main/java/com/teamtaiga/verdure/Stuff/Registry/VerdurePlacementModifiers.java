package com.teamtaiga.verdure.Stuff.Registry;

import com.mojang.serialization.Codec;
import com.teamtaiga.verdure.Stuff.World.BiomeTagFilter;
import com.teamtaiga.verdure.Util.Verdure;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class VerdurePlacementModifiers {
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS = DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, Verdure.MOD_ID);

    public static final RegistryObject<PlacementModifierType<BiomeTagFilter>> BIOME_TAG = PLACEMENT_MODIFIERS.register("biome_tag", () -> typeConvert(BiomeTagFilter.CODEC));

    private static <P extends PlacementModifier> PlacementModifierType<P> typeConvert(Codec<P> codec) {
        return () -> codec;
    }
}
