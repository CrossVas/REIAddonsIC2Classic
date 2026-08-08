package dev.crossvas.ic2rei.handlers;

import dev.architectury.event.EventResult;
import ic2.core.IC2;
import ic2.core.platform.recipes.crafting.RecipeIC2Base;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.display.visibility.DisplayVisibilityPredicate;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeHandler implements DisplayVisibilityPredicate {

    public static final RecipeHandler INSTANCE = new RecipeHandler();

    @Override
    public EventResult handleDisplay(DisplayCategory<?> category, Display display) {
        if (!IC2.CONFIG.recipeHiding.get()) {
            return EventResult.pass();
        }

        if (display.getCategoryIdentifier() == BuiltinPlugin.CRAFTING) {
            if (shouldHide(display)) {
                return EventResult.interruptFalse();
            }
        }
        return EventResult.pass();
    }

    private boolean shouldHide(Display display) {
        Object origin = getDisplay(display);
        if (origin instanceof Recipe<?> recipe) {
            if (recipe instanceof RecipeIC2Base ic2) {
                return ic2.isHidden();
            }
        }

        return false;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <A extends Display> Object getDisplay(A display) {
        Object origin = DisplayRegistry.getInstance().getDisplayOrigin(display);
        if (origin instanceof DefaultCraftingDisplay)
            origin = ((DefaultCraftingDisplay)origin).getOptionalRecipe().orElse(origin);
        return origin;
    }
}
