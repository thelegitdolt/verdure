package com.teamtaiga.verdure.Data;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamtaiga.verdure.Verdure;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class VerdureTags {
    public static final TagKey<Block> POND_FOILIAGE = blockTag("pond_foiliage");

    private static TagKey<Block> blockTag(String tagName) {
        return TagUtil.blockTag(Verdure.MOD_ID, tagName);
    }
}
