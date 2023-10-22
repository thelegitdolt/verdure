package com.teamtaiga.verdure.data.server.tags;

import com.teamtaiga.verdure.util.Verdure;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;

import static com.teamtaiga.verdure.core.registry.VerdureBlocks.*;

public class VerdureBlockTags extends BlockTagsProvider {
    public VerdureBlockTags(GatherDataEvent e) {
        super(e.getGenerator(), Verdure.MOD_ID, e.getExistingFileHelper());
    }

    @Override
    public void addTags() {
        this.tag(VerdureTags.POND_FOILIAGE).add(Blocks.GRASS, WHITE_DAISIES.get(), Blocks.SUGAR_CANE)
                .addOptional(new ResourceLocation("farmersdelight", "wild_carrots"));


        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ROCK.get());
    }

}
