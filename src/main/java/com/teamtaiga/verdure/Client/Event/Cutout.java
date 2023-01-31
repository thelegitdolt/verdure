package com.teamtaiga.verdure.Client.Event;

import com.teamtaiga.verdure.Stuff.Registry.VerdureBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class Cutout {
    public static void init(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(VerdureBlocks.BUTTERFLY_ORCHID.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(VerdureBlocks.CLOVER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(VerdureBlocks.BLUE_DAISIES.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(VerdureBlocks.PINK_DAISIES.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(VerdureBlocks.WHITE_DAISIES.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(VerdureBlocks.BLUE_MORNING_GLORY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(VerdureBlocks.WHITE_MORNING_GLORY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(VerdureBlocks.PURPLE_MORNING_GLORY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(VerdureBlocks.FOREST_PERENNIALS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(VerdureBlocks.PASTURE_PERENNIALS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(VerdureBlocks.FROSTY_PERENNIALS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(VerdureBlocks.UNDERGROWTH_PERENNIALS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(VerdureBlocks.ROCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(VerdureBlocks.ELEPHANT_LEAF.get(), RenderType.cutout());
    }
}
