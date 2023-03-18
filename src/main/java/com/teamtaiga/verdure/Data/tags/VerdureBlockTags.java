package com.teamtaiga.verdure.Data.tags;

import com.teamtaiga.verdure.Verdure;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import static com.teamtaiga.verdure.Stuff.Registry.VerdureBlocks.*;

public class VerdureBlockTags extends BlockTagsProvider {
    public VerdureBlockTags(DataGenerator p_126511_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_126511_, Verdure.MOD_ID, existingFileHelper);
    }

    @Override
    public void addTags() {
        this.tag(VerdureTags.POND_FOILIAGE).add(Blocks.GRASS, WHITE_DAISIES.get(), Blocks.SUGAR_CANE)
                .addOptional(new ResourceLocation("farmersdelight", "wild_carrots"));
        this.tag(VerdureTags.DAISIES_PLACEABLE_ON).addTag(BlockTags.OVERWORLD_NATURAL_LOGS).addTag(BlockTags.DIRT);


        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ROCK.get());
    }

}
