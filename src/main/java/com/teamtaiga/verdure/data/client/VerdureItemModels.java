package com.teamtaiga.verdure.data.client;

import com.teamtaiga.verdure.core.Verdure;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Supplier;

import static com.teamtaiga.verdure.core.registry.VerdureBlocks.ROCK;

public class VerdureItemModels extends ItemModelProvider {
    public VerdureItemModels(GatherDataEvent e) {
        super(e.getGenerator(), Verdure.MOD_ID, e.getExistingFileHelper());
    }

    @Override
    protected void registerModels() {
        this.generated(ROCK);
    }


    private void generated(RegistryObject<? extends ItemLike> item) {
        this.withExistingParent(this.getName(item), "item/generated").texture("layer0", this.modLoc("item/" + this.getName(item)));
    }

    private String getName(Supplier<? extends ItemLike> object) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(object.get().asItem())).getPath();
    }
}