package com.teamtaiga.verdure.data.client;

import com.teamtaiga.verdure.core.Verdure;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.codehaus.plexus.util.StringUtils;

import java.util.Objects;

import static com.teamtaiga.verdure.core.registry.VerdureBlocks.*;

public class VerdureLangs extends LanguageProvider {
    public VerdureLangs(GatherDataEvent e) {
        super(e.getGenerator(), Verdure.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.simpleBlock(ROCK);

        this.simpleBlock(BUTTERFLY_ORCHID);
        this.simpleBlock(ELEPHANT_LEAF);

        this.simpleBlock(BLUE_DAISIES);
        this.simpleBlock(PINK_DAISIES);
        this.simpleBlock(WHITE_DAISIES);

        this.simpleBlock(WHITE_MORNING_GLORY);
        this.simpleBlock(BLUE_MORNING_GLORY);
        this.simpleBlock(PURPLE_MORNING_GLORY);

        this.simpleBlock(FOREST_PERENNIALS);
        this.simpleBlock(FROSTY_PERENNIALS);
        this.simpleBlock(PASTURE_PERENNIALS);
        this.simpleBlock(UNDERGROWTH_PERENNIALS);
    }

    private void simpleBlock(RegistryObject<Block> block) {
        String name = StringUtils.capitaliseAllWords(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(block.get().asItem())).getPath().replace('_', ' '));

        this.add(block.get(), name);
    }

    private void simpleItem(RegistryObject<Item> item) {
        String name = StringUtils.capitaliseAllWords(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.get())).getPath().replace('_', ' '));

        this.add(item.get(), name);
    }
}
