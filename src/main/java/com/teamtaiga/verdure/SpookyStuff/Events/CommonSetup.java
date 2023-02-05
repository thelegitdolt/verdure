package com.teamtaiga.verdure.SpookyStuff.Events;

import com.teamtaiga.verdure.Stuff.World.VerdureGeneration;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonSetup {
    public static void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            VerdureGeneration.registerGeneration();
        });

    }

}
