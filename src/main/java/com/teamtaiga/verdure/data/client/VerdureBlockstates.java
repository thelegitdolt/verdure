package com.teamtaiga.verdure.data.client;

import com.teamtaiga.verdure.util.Verdure;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Supplier;

import static com.teamtaiga.verdure.core.registry.VerdureBlocks.*;

public class VerdureBlockstates extends BlockStateProvider {

    public VerdureBlockstates(GatherDataEvent e) {
        super(e.getGenerator(), Verdure.MOD_ID, e.getExistingFileHelper());
    }

    @Override
    protected void registerStatesAndModels() {
        this.daisy(WHITE_DAISIES, "white");
        this.daisy(BLUE_DAISIES, "blue");
        this.daisy(PINK_DAISIES, "pink");
    }

    private void daisy(RegistryObject<? extends Block> daisyBlock, String daisyCol) {
        ModelFile wallModel = this.models().withExistingParent(daisyCol + "_daisies_wall", modLoc("daisies_wall"))
                .texture("daisy", this.modLoc("block/" + daisyCol + "_daisies_wall")).renderType("cutout");
        ModelFile groundModel = this.models().withExistingParent(daisyCol + "_daisies_ground", modLoc("daisies_ground"))
                .texture("side", modLoc("block/" + daisyCol + "_daisies_side"))
                .texture("side1", modLoc("block/" + daisyCol + "_daisies_side_1"))
                .texture("top1", modLoc("block/" + daisyCol + "_daisies_top_1"))
                .texture("top2", modLoc("block/" + daisyCol + "_daisies_top_2"));
        this.itemModels().withExistingParent(this.getName(daisyBlock), "item/generated").texture("layer0", this.modLoc("block/" + daisyCol + "_daisies_wall"));
        this.getMultipartBuilder(daisyBlock.get())
                .part().modelFile(wallModel).uvLock(true).rotationX(270).addModel().condition(BlockStateProperties.UP, true).end()
                .part().modelFile(wallModel).addModel().condition(BlockStateProperties.NORTH, true).end()
                .part().modelFile(wallModel).uvLock(true).rotationY(180).addModel().condition(BlockStateProperties.SOUTH, true).end()
                .part().modelFile(wallModel).uvLock(true).rotationY(270).addModel().condition(BlockStateProperties.WEST, true).end()
                .part().modelFile(wallModel).uvLock(true).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end()
                .part().modelFile(groundModel).uvLock(true).addModel().condition(BlockStateProperties.DOWN, true).end();
    }

    private String getName(Supplier<? extends ItemLike> object) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(object.get().asItem())).getPath();
    }

}
