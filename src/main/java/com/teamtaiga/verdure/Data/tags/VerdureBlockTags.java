package com.teamtaiga.verdure.Data.tags;

import com.teamtaiga.verdure.Data.VerdureTags;
import com.teamtaiga.verdure.Stuff.Registry.VerdureBlocks;
import com.teamtaiga.verdure.Verdure;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class VerdureBlockTags extends BlockTagsProvider {
    public VerdureBlockTags(DataGenerator p_126511_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_126511_, Verdure.MOD_ID, existingFileHelper);
    }

    @Override
    public void addTags() {
        this.tag(VerdureTags.POND_FOILIAGE).add(Blocks.GRASS, VerdureBlocks.WHITE_DAISIES.get(), Blocks.SUGAR_CANE)
                .addOptional(new ResourceLocation("farmersdelight", "wild_carrots"));
    }

}
