package com.teamtaiga.verdure.data.client;

import com.teamtaiga.verdure.core.Verdure;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Function;
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

        this.tallPlant(BUTTERFLY_ORCHID);

        this.morningGlory(BLUE_MORNING_GLORY, "blue");
        this.morningGlory(PURPLE_MORNING_GLORY, "purple");
        this.morningGlory(WHITE_MORNING_GLORY, "white");

        this.perennials(FOREST_PERENNIALS, "forest");
        this.perennials(FROSTY_PERENNIALS, "frosty");
        this.perennials(PASTURE_PERENNIALS, "pasture");
        this.perennials(UNDERGROWTH_PERENNIALS, "undergrowth");
    }

    private void perennials(RegistryObject<Block> perennials, String flowerType) {
        ModelFile model = this.models().withExistingParent(this.getName(perennials), modLoc("template_perennials"))
                .texture("side", this.modLoc("block/" + flowerType + "_perennials_side"))
                .texture("top", this.modLoc("block/" + flowerType + "_perennials_top")).renderType("cutout");
        this.itemModels().withExistingParent(this.getName(perennials), "item/generated").texture("layer0", this.modLoc("block/" + flowerType + "_perennials_top"));
        this.getVariantBuilder(perennials.get()).partialState()
                .setModels(new ConfiguredModel(model, 0, 0, false),
                        new ConfiguredModel(model,0,  90, false),
                        new ConfiguredModel(model, 0, 180, false),
                        new ConfiguredModel(model, 0, 270, false));
    }

    private void morningGlory(RegistryObject<? extends Block> mgBlock, String flowerCol) {
        ModelFile model = this.models().withExistingParent(flowerCol + "_morning_glory", modLoc("template_morning_glory"))
                .texture("morning_glory", this.modLoc("block/" + flowerCol + "_morning_glory")).renderType("cutout");
        this.getVariantBuilder(mgBlock.get()).partialState()
                .setModels(new ConfiguredModel(model, 0, 0, false),
                        new ConfiguredModel(model,0,  90, false),
                        new ConfiguredModel(model, 0, 180, false),
                        new ConfiguredModel(model, 0, 270, false));
    }

    private void daisy(RegistryObject<? extends Block> daisyBlock, String daisyCol) {
        ModelFile wallModel = this.models().withExistingParent(daisyCol + "_daisies_wall", modLoc("template_daisies_wall"))
                .texture("daisy", this.modLoc("block/" + daisyCol + "_daisies_wall")).renderType("cutout");
        ModelFile groundModel = this.models().withExistingParent(daisyCol + "_daisies_ground", modLoc("template_daisies_ground"))
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


    private void tallPlant(RegistryObject<Block> flower) {
        String name = getName(flower);
        Function<String, ModelFile> model = s -> this.models().cross(name + "_" + s, this.modLoc("block/" + name + "_" + s)).renderType("cutout");

        this.itemModels().withExistingParent(name, "item/generated").texture("layer0", this.modLoc("block/" + name + "_top"));
        this.getVariantBuilder(flower.get())
                .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER).addModels(new ConfiguredModel(model.apply("top")))
                .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).addModels(new ConfiguredModel(model.apply("bottom")));
    }

    private String getName(Supplier<? extends ItemLike> object) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(object.get().asItem())).getPath();
    }

}
