package com.teamtaiga.verdure.data.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamtaiga.verdure.util.Verdure;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class VerdureTags {
    public static final TagKey<Block> POND_FOILIAGE = blockTag("pond_foiliage");
    public static final TagKey<Block> DAISIES_PLACEABLE_ON = blockTag("daisies_placeable_on");

    private static TagKey<Block> blockTag(String tagName) {
        return TagUtil.blockTag(Verdure.MOD_ID, tagName);
    }
}
