package dev.crossvas.ic2rei.recipes.brewing;

import ic2.core.item.food_and_drink.IC2Drink;
import ic2.core.item.food_and_drink.IC2FoodsAndDrinks;
import ic2.core.item.food_and_drink.drinks.Beer;
import ic2.core.platform.registries.IC2Items;
import ic2.core.utils.collection.CollectionUtils;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *  Copy-paste of {@link ic2.jeiplugin.core.recipes.categories.brew.BeerBrewCategory.Brew}
 *  © IC2Classic Dev
 * */

public class BeerBrewRecipe implements Comparable<BeerBrewRecipe> {

    private final int WHEAT;
    private final int HOPS;
    private final int WATER;
    private final int TIME;

    private final List<ItemStack> INPUTS;
    private final List<ItemStack> OUTPUTS;

    public BeerBrewRecipe(int wheat, int hops, int water, int time, List<ItemStack> inputs, List<ItemStack> outputs) {
        this.WHEAT = wheat;
        this.HOPS = hops;
        this.WATER = water;
        this.TIME = time;
        this.INPUTS = inputs;
        this.OUTPUTS = outputs;
    }

    public ItemStack getHops() {
        return new ItemStack(IC2Items.HOPS, this.HOPS);
    }

    public ItemStack getWheat() {
        return new ItemStack(Items.WHEAT, this.WHEAT);
    }

    public ItemStack getWater() {
        return new ItemStack(IC2Items.CELL_WATER, this.WATER);
    }

    public int getTime() {
        return this.TIME;
    }

    public List<ItemStack> getInputs() {
        return this.INPUTS;
    }

    public List<ItemStack> getOutputs() {
        return this.OUTPUTS;
    }

    @Override
    public int compareTo(@NotNull BeerBrewRecipe recipe) {
        int result = Integer.compare(this.WHEAT, recipe.WHEAT);
        if (result != 0) {
            return result;
        } else {
            result = Integer.compare(this.HOPS, recipe.HOPS);
            if (result != 0) {
                return result;
            } else {
                result = Integer.compare(this.WATER, recipe.WATER);
                return result != 0 ? result : Long.compare((long)this.TIME, (long)recipe.TIME);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<BeerBrewRecipe> getBeerBrewRecipes() {
        IntSet duplicationCheck = new IntOpenHashSet();
        List<BeerBrewRecipe> result = new ObjectArrayList<>();

        for(int time = 0; time < 6; ++time) {
            for(int water = 1; water <= 32; ++water) {
                for(int hops = 0; hops < 64; ++hops) {
                    for(int wheat = 0; wheat < 64; ++wheat) {
                        MutableInt quality = new MutableInt(time);
                        MutableInt alc = new MutableInt();
                        MutableInt solid = new MutableInt();
                        createAlcoholLevel(hops, wheat, alc, quality);
                        createSolidLevel(hops, wheat, water * 1000, solid, quality);
                        if (duplicationCheck.add(Beer.createData(quality.getValue(), solid.getValue(), alc.getValue()))) {
                            List<ItemStack> input = CollectionUtils.createList();
                            List<ItemStack> output = CollectionUtils.createList();

                            for (IC2Drink drink : IC2FoodsAndDrinks.CONTAINERS) {
                                ItemStack item = drink.fillWith(IC2FoodsAndDrinks.BEER, 1);
                                if (!item.isEmpty()) {
                                    output.add(Beer.createData(item, quality.getValue(), solid.getValue(), alc.getValue()));
                                    input.add(new ItemStack(drink));
                                }
                            }
                            result.add(new BeerBrewRecipe(wheat, hops, water, quality.getValue(), input, output));
                        }
                    }
                }
            }
        }

        result.sort(null);
        return result;
    }

    private static void createAlcoholLevel(int hopsAmount, int wheatAmount, MutableInt alc, MutableInt quality) {
        float hopsToWheat = wheatAmount > 0 ? (float)hopsAmount / (float)wheatAmount : 10.0F;
        alc.setValue(0);
        if (hopsAmount <= 0 && wheatAmount <= 0) {
            hopsToWheat = 0.0F;
        }

        if (hopsToWheat >= 5.0F) {
            quality.setValue(5);
        } else if (hopsToWheat >= 4.0F) {
            alc.setValue(6);
        } else if (hopsToWheat >= 3.0F) {
            alc.setValue(5);
        } else if (hopsToWheat >= 2.0F) {
            alc.setValue(4);
        } else if (hopsToWheat > 0.5F) {
            alc.setValue(3);
        } else if (hopsToWheat > 0.33333334F) {
            alc.setValue(2);
        } else if (hopsToWheat > 0.25F) {
            alc.setValue(1);
        }
    }

    private static void createSolidLevel(int hopsAmount, int wheatAmount, int fluidAmount, MutableInt solidRatio, MutableInt quality) {
        float solid = fluidAmount > 0 ? (float)(hopsAmount + wheatAmount) / ((float)fluidAmount / 1000.0F) : 10.0F;
        solidRatio.setValue(0);
        if (solid >= 4.0F) {
            quality.setValue(5);
        } else if (solid >= 2.4F) {
            solidRatio.setValue(6);
        } else if (solid >= 2.0F) {
            solidRatio.setValue(5);
        } else if (solid > 1.0F) {
            solidRatio.setValue(4);
        } else if (solid == 1.0F) {
            solidRatio.setValue(3);
        } else if (solid > 0.5F) {
            solidRatio.setValue(2);
        } else if (solid > 0.4166667F) {
            solidRatio.setValue(1);
        }

    }
}
