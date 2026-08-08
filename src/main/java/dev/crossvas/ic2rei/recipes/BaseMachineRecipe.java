package dev.crossvas.ic2rei.recipes;

import ic2.api.recipes.ingridients.recipes.IRecipeOutput;
import ic2.api.recipes.ingridients.recipes.IRecipeOutputChance;
import ic2.api.recipes.registries.IMachineRecipeList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class BaseMachineRecipe {

    private final Ingredient INPUT;
    private final List<ItemStack> OUTPUTS;
    float CHANCE;

    public BaseMachineRecipe(IMachineRecipeList.RecipeEntry recipeEntry) {
        this.INPUT = recipeEntry.getInputs()[0].asIngredient();
        this.OUTPUTS = recipeEntry.getOutput().getAllOutputs();
        IRecipeOutput output = recipeEntry.getOutput();
        if (output instanceof IRecipeOutputChance chance) {
            CHANCE = chance.getChance();
        }
    }

    public Ingredient getInput() {
        return this.INPUT;
    }

    public List<ItemStack> getOutputs() {
        return this.OUTPUTS;
    }

    public float getChance() {
        return this.CHANCE;
    }
}
