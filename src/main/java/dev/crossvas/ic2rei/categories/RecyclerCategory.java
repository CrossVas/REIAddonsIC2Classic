package dev.crossvas.ic2rei.categories;

import com.google.common.collect.Lists;
import dev.crossvas.ic2rei.displays.RecyclerDisplay;
import dev.crossvas.ic2rei.utils.CategoryIDs;
import dev.crossvas.ic2rei.utils.GuiHelper;
import dev.crossvas.ic2rei.utils.IGuiHelper;
import ic2.core.platform.registries.IC2Blocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;

import java.util.List;

public class RecyclerCategory implements DisplayCategory<RecyclerDisplay>, IGuiHelper {

    public static final RecyclerCategory INSTANCE = new RecyclerCategory();

    @Override
    public CategoryIdentifier<? extends RecyclerDisplay> getCategoryIdentifier() {
        return CategoryIDs.RECYCLER;
    }

    @Override
    public Component getTitle() {
        return IC2Blocks.RECYCLER.getName();
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(IC2Blocks.RECYCLER);
    }

    @Override
    public List<Widget> setupDisplay(RecyclerDisplay display, Rectangle bounds) {
        List<Widget> widgets = Lists.newArrayList();
        GuiHelper.createRecipeBase(widgets, bounds);
        GuiHelper.addProgressBar(widgets, point(getProgressBarX(bounds), getProgressBarY(bounds)), 3000, GuiHelper.ProgressType.RECYCLER);
        // input
        EntryIngredient input = EntryIngredients.ofIngredient(display.getRecipe().getInput());
        GuiHelper.addInputSlot(widgets, adjustedInputPoint(bounds), input);
        // output
        EntryIngredient output = EntryIngredients.ofItemStacks(display.getRecipe().getOutputs());
        GuiHelper.addLargeOutputSlot(widgets, adjustedOutputPoint(bounds), output);
        float chance = display.getRecipe().getChance();
        Component chanceLabel = Component.literal(display.getRecipe().getChance() + "%");
        Point chancePoint = point(bounds.getMaxX() - offset, bounds.getMaxY() - lineHeight - offset);
        if (chance > 0) {
            GuiHelper.addLabelRight(widgets, chancePoint, chanceLabel);
            widgets.add(Widgets.createTooltip(
                    new Rectangle(bounds.getMaxX() - font.width(chanceLabel.getString()) - offset, bounds.getMaxY() - offset - lineHeight, font.width(chanceLabel.getString()), lineHeight),
                    Component.translatable("jei.ic2.recycler.chance")));
        }
        return widgets;
    }

    @Override
    public int getDisplayWidth(RecyclerDisplay display) {
        return getWidth();
    }

    @Override
    public int getDisplayHeight() {
        return 64;
    }
}
