package com.teamtaiga.verdure.Data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.teamtaiga.verdure.Stuff.Blocks.RockBlock;
import com.teamtaiga.verdure.Verdure;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.teamtaiga.verdure.Stuff.Registry.VerdureBlocks.*;

public class LootTableManager extends LootTableProvider {
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> tables = ImmutableList.of(Pair.of(VerdureBlockLoot::new, LootContextParamSets.BLOCK));

    public LootTableManager(DataGenerator gen) {
        super(gen);
    }

    @Override
    public List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return tables;
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext context) {}

    private static class VerdureBlockLoot extends BlockLoot {
        private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
        private static final LootItemCondition.Builder HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.SHEARS));
        private static final LootItemCondition.Builder HAS_SHEARS_OR_SILK_TOUCH = HAS_SHEARS.or(HAS_SILK_TOUCH);
        private static final LootItemCondition.Builder HAS_NO_SHEARS_OR_SILK_TOUCH = HAS_SHEARS_OR_SILK_TOUCH.invert();

        @Override
        public void addTables() {
            this.add(WHITE_DAISIES.get(), (block) -> createMultifaceBlockDrops(block, MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.SHEARS))));
            this.add(BLUE_DAISIES.get(), (block) -> createMultifaceBlockDrops(block, MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.SHEARS))));
            this.add(PINK_DAISIES.get(), (block) -> createMultifaceBlockDrops(block, MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.SHEARS))));

            this.add(ROCK.get(), this::createRockDrops);

            this.add(BUTTERFLY_ORCHID.get(), (block) -> createSinglePropConditionTable(block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));

            this.add(CLOVER.get(), BlockLoot::createGrassDrops);
            this.add(ELEPHANT_LEAF.get(), BlockLoot::createGrassDrops);

            this.dropSelf(BLUE_MORNING_GLORY.get());
            this.dropSelf(WHITE_MORNING_GLORY.get());
            this.dropSelf(PURPLE_MORNING_GLORY.get());

            this.dropSelf(PASTURE_PERENNIALS.get());
            this.dropSelf(FOREST_PERENNIALS.get());
            this.dropSelf(FROSTY_PERENNIALS.get());
            this.dropSelf(UNDERGROWTH_PERENNIALS.get());
        }

        @Override
        public Iterable<Block> getKnownBlocks() {
            return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> ForgeRegistries.BLOCKS.getKey(block).getNamespace().equals(Verdure.MOD_ID)).collect(Collectors.toSet());
        }

        protected LootTable.Builder createRockDrops(Block block) {
            return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(applyExplosionDecay(block, LootItem.lootTableItem(block).apply(List.of(2, 3),
                    (Blocked) -> { return SetItemCountFunction.setCount(ConstantValue.exactly((float)Blocked.intValue())).
                            when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().
                                    hasProperty(RockBlock.LEVEL, Blocked)));
            }))));
        }
    }
}
