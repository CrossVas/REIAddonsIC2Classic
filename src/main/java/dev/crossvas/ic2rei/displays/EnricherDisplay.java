package dev.crossvas.ic2rei.displays;

import dev.crossvas.ic2rei.utils.CategoryIDs;
import ic2.core.block.machines.recipes.misc.EnrichRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EnricherDisplay extends BasicDisplay {

    private List<ItemStack> INPUTS;
    private List<ItemStack> FUELS;
    private int COLOR;
    private int POINT_INGOT;
    private EnrichRecipe RECIPE;

    public EnricherDisplay(EnrichRecipe recipe) {
        this(Arrays.asList(
                EntryIngredients.ofItemStacks(recipe.getInputs().stream().map(ItemStack::new).toList()),
                        EntryIngredients.ofItemStacks(recipe.getFuels())),
                Collections.singletonList(EntryIngredients.of(recipe.getOutput()))
        );
        this.INPUTS = recipe.getInputs().stream().map(ItemStack::new).toList();
        this.FUELS = recipe.getFuels();
        this.COLOR = recipe.getColor();
        this.POINT_INGOT = recipe.getRequiredPoints();
        this.RECIPE = recipe;
    }

    public EnrichRecipe getRecipe() {
        return this.RECIPE;
    }

    public EnricherDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
        super(inputs, outputs);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CategoryIDs.ENRICHER;
    }

    public List<ItemStack> getInputs() {
        return this.INPUTS;
    }

    public List<ItemStack> getFuels() {
        return this.FUELS;
    }

    public int getColor() {
        return this.COLOR;
    }

    public int getPointsPerIngot() {
        return this.POINT_INGOT;
    }
}
