package com.teamtaiga.verdure.Stuff.Blocks;
import com.teamabnormals.blueprint.common.block.BlueprintFlowerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class CarpetFlowerBlock extends BlueprintFlowerBlock {
    private static final VoxelShape SHAPE = box(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D);

    public CarpetFlowerBlock(Supplier<MobEffect> mobEffect, int i, Properties properties) {
        super(mobEffect, i, properties);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return true;
    }
}
