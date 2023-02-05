package com.teamtaiga.verdure.Stuff.Blocks;

import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;

public class DaisyBlock extends MultifaceBlock {
    public DaisyBlock(Properties props) {
        super(props);
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return null;
    }

}
