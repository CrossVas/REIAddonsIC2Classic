package dev.crossvas.ic2rei.handlers;

import com.google.common.base.MoreObjects;
import dev.architectury.event.EventResult;
import ic2.core.IC2;
import ic2.core.platform.recipes.crafting.RecipeIC2Base;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.display.visibility.DisplayVisibilityPredicate;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.*;

public class RecipeHandler implements DisplayVisibilityPredicate {

    public static final RecipeHandler INSTANCE = new RecipeHandler();

    public List<CraftingRecipe> HIDDEN_RECIPES;

    static Map<CategoryIdentifier<?>, Set<Object>> hiddenRecipes;
    public Map<ResourceLocation, CategoryIdentifier<?>> CATEGORY_ID_MAP = new HashMap<>();

    public RecipeHandler() {
        CATEGORY_ID_MAP.put(new ResourceLocation("minecraft", "crafting"), BuiltinPlugin.CRAFTING);
        CATEGORY_ID_MAP.put(new ResourceLocation("minecraft", "stonecutting"), BuiltinPlugin.STONE_CUTTING);
        CATEGORY_ID_MAP.put(new ResourceLocation("minecraft", "furnace"), BuiltinPlugin.SMELTING);
        CATEGORY_ID_MAP.put(new ResourceLocation("minecraft", "smoking"), BuiltinPlugin.SMOKING);
        CATEGORY_ID_MAP.put(new ResourceLocation("minecraft", "blasting"), BuiltinPlugin.BLASTING);
        CATEGORY_ID_MAP.put(new ResourceLocation("minecraft", "campfire"), BuiltinPlugin.CAMPFIRE);
        CATEGORY_ID_MAP.put(new ResourceLocation("minecraft", "brewing"), BuiltinPlugin.BREWING);
        CATEGORY_ID_MAP.put(new ResourceLocation("minecraft", "anvil"), BuiltinPlugin.ANVIL);
        CATEGORY_ID_MAP.put(new ResourceLocation("minecraft", "smithing"), BuiltinPlugin.SMITHING);
        CATEGORY_ID_MAP.put(new ResourceLocation("minecraft", "compostable"), BuiltinPlugin.COMPOSTING);
        CATEGORY_ID_MAP.put(new ResourceLocation("minecraft", "fuel"), BuiltinPlugin.FUEL);
        CATEGORY_ID_MAP.put(new ResourceLocation("minecraft", "information"), BuiltinPlugin.INFO);
    }

    public void init() {
        if (IC2.CONFIG.recipeHiding.get()) {
            HIDDEN_RECIPES = new ObjectArrayList<>();
            hiddenRecipes = new HashMap<>();
            List<CraftingRecipe> recipes = Minecraft.getInstance().player.connection.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
            for (CraftingRecipe recipe : recipes) {
                if (recipe instanceof RecipeIC2Base ic2Recipe) {
                    if (ic2Recipe.isHidden()) {
                        HIDDEN_RECIPES.add(ic2Recipe);
                    }
                }
            }

            hideRecipes(BuiltinPlugin.CRAFTING, HIDDEN_RECIPES);
        } else {
            hiddenRecipes = null;
            HIDDEN_RECIPES = null;
        }
    }

    @Override
    public EventResult handleDisplay(DisplayCategory<?> category, Display display) {
        if (IC2.CONFIG.recipeHiding.get()) {
            if (display.getCategoryIdentifier() == BuiltinPlugin.CRAFTING) {
                Set<Object> hidden = hiddenRecipes.get(category.getCategoryIdentifier());
                if (hidden != null && hidden.contains(MoreObjects.firstNonNull(getDisplay(display), display)))
                    return EventResult.interruptFalse();
            }
        }
        return EventResult.pass();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <A extends Display> Object getDisplay(A display) {
        Object origin = DisplayRegistry.getInstance().getDisplayOrigin(display);
        if (origin instanceof DefaultCraftingDisplay)
            origin = ((DefaultCraftingDisplay)origin).getOptionalRecipe().orElse(origin);
        return origin;
    }

    public <T extends Recipe<?>> void hideRecipes(CategoryIdentifier<?> id, Collection<T> recipes) {
        (hiddenRecipes.computeIfAbsent(categoryId(id), identifier -> new HashSet<>())).addAll(recipes);
    }

    public <T extends Display> CategoryIdentifier<T> categoryId(CategoryIdentifier<?> id) {
        return categoryId(id.getIdentifier());
    }

    public <T extends Display> CategoryIdentifier<T> categoryId(ResourceLocation id) {
        CategoryIdentifier<?> existingId = CATEGORY_ID_MAP.get(id);
        if (existingId != null)
            return existingId.cast();
        CategoryIdentifier<T> identifier = CategoryIdentifier.of(id);
        CATEGORY_ID_MAP.putIfAbsent(id, identifier);
        return identifier;
    }
}
