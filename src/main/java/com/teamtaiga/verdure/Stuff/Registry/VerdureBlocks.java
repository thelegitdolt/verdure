package com.teamtaiga.verdure.Stuff.Registry;

import com.teamabnormals.blueprint.common.block.BlueprintFlowerBlock;
import com.teamabnormals.blueprint.common.block.BlueprintTallFlowerBlock;
import com.teamabnormals.blueprint.core.util.PropertyUtil;
import com.teamabnormals.blueprint.core.util.registry.BlockSubRegistryHelper;
import com.teamtaiga.verdure.Stuff.Blocks.CarpetFlowerBlock;
import com.teamtaiga.verdure.Stuff.Blocks.DaisyBlock;
import com.teamtaiga.verdure.Stuff.Blocks.RockBlock;
import com.teamtaiga.verdure.Verdure;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Verdure.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VerdureBlocks {
    public static final BlockSubRegistryHelper HELPER = Verdure.REGISTRY_HELPER.getBlockSubHelper();

    public static final RegistryObject<Block> WHITE_MORNING_GLORY = HELPER.createBlock("white_morning_glory", () -> new CarpetFlowerBlock(()-> MobEffects.DAMAGE_RESISTANCE, 16, Block.Properties.of(Material.PLANT).noCollission().strength(0.0F).sound(SoundType.GRASS)), CreativeModeTab.TAB_DECORATIONS);
    public static final RegistryObject<Block> PURPLE_MORNING_GLORY = HELPER.createBlock("purple_morning_glory", () -> new CarpetFlowerBlock(()-> MobEffects.DAMAGE_RESISTANCE, 16, Block.Properties.of(Material.PLANT).noCollission().strength(0.0F).sound(SoundType.GRASS)), CreativeModeTab.TAB_DECORATIONS);
    public static final RegistryObject<Block> BLUE_MORNING_GLORY = HELPER.createBlock("blue_morning_glory", () -> new CarpetFlowerBlock(()-> MobEffects.DAMAGE_RESISTANCE, 16, Block.Properties.of(Material.PLANT).noCollission().strength(0.0F).sound(SoundType.GRASS)), CreativeModeTab.TAB_DECORATIONS);

    public static final RegistryObject<Block> CLOVER = HELPER.createBlock("clover", () -> new BushBlock(Props.CLOVER), CreativeModeTab.TAB_DECORATIONS);

    public static final RegistryObject<Block> WHITE_DAISIES = HELPER.createBlock("white_daisies", () -> new DaisyBlock(Block.Properties.of(Material.PLANT).noCollission().strength(0.0F).sound(SoundType.GRASS)), CreativeModeTab.TAB_DECORATIONS);
    public static final RegistryObject<Block> BLUE_DAISIES = HELPER.createBlock("blue_daisies", () -> new DaisyBlock(Block.Properties.of(Material.PLANT).noCollission().strength(0.0F).sound(SoundType.GRASS)), CreativeModeTab.TAB_DECORATIONS);
    public static final RegistryObject<Block> PINK_DAISIES = HELPER.createBlock("pink_daisies", () -> new DaisyBlock(Block.Properties.of(Material.PLANT).noCollission().strength(0.0F).sound(SoundType.GRASS)), CreativeModeTab.TAB_DECORATIONS);

    public static final RegistryObject<Block> PASTURE_PERENNIALS = HELPER.createBlock("pasture_perennials", () -> new BushBlock(Props.CLOVER), CreativeModeTab.TAB_DECORATIONS);
    public static final RegistryObject<Block> FROSTY_PERENNIALS = HELPER.createBlock("frosty_perennials", () -> new BushBlock(Props.CLOVER), CreativeModeTab.TAB_DECORATIONS);
    public static final RegistryObject<Block> UNDERGROWTH_PERENNIALS = HELPER.createBlock("undergrowth_perennials", () -> new BushBlock(Props.CLOVER), CreativeModeTab.TAB_DECORATIONS);
    public static final RegistryObject<Block> FOREST_PERENNIALS = HELPER.createBlock("forest_perennials", () -> new BushBlock(Props.CLOVER), CreativeModeTab.TAB_DECORATIONS);


    public static final RegistryObject<Block> BUTTERFLY_ORCHID = HELPER.createBlock("butterfly_orchid", () -> new BlueprintTallFlowerBlock(PropertyUtil.FLOWER), CreativeModeTab.TAB_DECORATIONS);

    public static final RegistryObject<Block> ROCK = HELPER.createBlock("rock", () -> new RockBlock(Props.ROCK), CreativeModeTab.TAB_DECORATIONS);

    public static final RegistryObject<Block> ELEPHANT_LEAF = HELPER.createBlock("elephant_leaf", () -> new BushBlock(Props.CLOVER), CreativeModeTab.TAB_DECORATIONS);




    static class Props {
        public static final BlockBehaviour.Properties CLOVER = BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT, MaterialColor.COLOR_GREEN).noCollission().instabreak().sound(SoundType.GRASS);
        public static final BlockBehaviour.Properties ROCK = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.STONE).strength(1.5F, 6.0F);

    }
}
