package com.teamtaiga.verdure.core;

import com.mojang.logging.LogUtils;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamtaiga.verdure.data.client.VerdureBlockstates;
import com.teamtaiga.verdure.data.client.VerdureItemModels;
import com.teamtaiga.verdure.data.client.VerdureLangs;
import com.teamtaiga.verdure.data.server.VerdureLootTables;
import com.teamtaiga.verdure.data.server.tags.VerdureBlockTags;
import com.teamtaiga.verdure.core.events.CommonSetup;
import com.teamtaiga.verdure.core.registry.VerdureBiomeModifier;
import com.teamtaiga.verdure.core.registry.VerdurePlacementModifiers;
import com.teamtaiga.verdure.common.world.VerdureGeneration;
import net.minecraft.client.Minecraft;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

@Mod(Verdure.MOD_ID)
public class Verdure {
    public static final String MOD_ID = "verdure";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);
    public Verdure() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext context = ModLoadingContext.get();
        MinecraftForge.EVENT_BUS.register(this);

        REGISTRY_HELPER.register(bus);
        VerdureGeneration.VerdureFeature.CONFIGURED_FEATURES.register(bus);
        VerdureGeneration.VerdurePlacedFeatures.PLACED_FEATURES.register(bus);
        VerdureGeneration.FEATURES.register(bus);
        VerdurePlacementModifiers.PLACEMENT_MODIFIERS.register(bus);
        VerdureBiomeModifier.BIOME_MODIFIER_SERIALIZERS.register(bus);
        bus.addListener(CommonSetup::init);
        bus.addListener(this::dataSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }

    private void dataSetup(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        boolean server = event.includeServer();
        boolean client = event.includeClient();

        generator.addProvider(server, new VerdureBlockTags(event));
        generator.addProvider(server, new VerdureLootTables(event));

        generator.addProvider(client, new VerdureBlockstates(event));
        generator.addProvider(client, new VerdureItemModels(event));
        generator.addProvider(client, new VerdureLangs(event));

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    public static ResourceLocation rlOf(String namespace) {
        return new ResourceLocation(MOD_ID, namespace);
    }
}
