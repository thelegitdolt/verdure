package com.teamtaiga.verdure.Stuff.Blocks;

import com.teamtaiga.verdure.Data.tags.VerdureTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;

public class DaisyBlock extends MultifaceBlock {
    public DaisyBlock(Properties props) {
        super(props);
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return null;
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return !context.getItemInHand().is(this.asItem()) || super.canBeReplaced(state, context);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        boolean flag = false;

        for (Direction direction : DIRECTIONS) {
            if (hasFace(state, direction)) {
                BlockPos blockpos = pos.relative(direction);
                if (!canAttachTo(level, direction, blockpos, level.getBlockState(blockpos))
                        && !level.getBlockState(pos).is(VerdureTags.DAISIES_PLACEABLE_ON)) {
                    return false;
                }

                flag = true;
            }
        }

        return flag;
    }


}
