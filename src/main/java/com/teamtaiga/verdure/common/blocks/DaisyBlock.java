package com.teamtaiga.verdure.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class DaisyBlock extends MultifaceBlock implements BonemealableBlock {
    private final MultifaceSpreader spreader = new MultifaceSpreader(this);
    public DaisyBlock(Properties props) {
        super(props);
    }

    public @NotNull MultifaceSpreader getSpreader() {
        return this.spreader;
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext context) {
        return !context.getItemInHand().is(this.asItem()) || super.canBeReplaced(state, context);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull BlockState state, boolean p_153292_) {
        return Direction.stream().anyMatch((dir) -> this.spreader.canSpreadInAnyDirection(state, getter, pos, dir.getOpposite()));
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        this.spreader.spreadFromRandomFaceTowardRandomDirection(state, level, pos, random);
    }


}
