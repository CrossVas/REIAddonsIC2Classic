package dev.crossvas.ic2rei.categories.brewing;

import dev.crossvas.ic2rei.IC2REIPlugin;
import dev.crossvas.ic2rei.displays.brewing.RumBrewDisplay;
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
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class RumBrewCategory implements DisplayCategory<RumBrewDisplay>, IGuiHelper {

    String TIME_FORMAT = "HH:mm:ss";

    public static final RumBrewCategory INSTANCE = new RumBrewCategory();

    @Override
    public CategoryIdentifier<? extends RumBrewDisplay> getCategoryIdentifier() {
        return CategoryIDs.RUM_BREWING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("rei.cat.brewing.rum");
    }

    @Override
    public Renderer getIcon() {
        ItemStack stack = new ItemStack(IC2Blocks.BARREL);
        stack.getOrCreateTag().putInt("type", 2);
        return EntryStacks.of(stack);
    }

    @Override
    public List<Widget> setupDisplay(RumBrewDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        GuiHelper.createRecipeBase(widgets, bounds);
        Point inputPoint = adjustedInputPoint(bounds);
        Point secondInputPoint = point(inputPoint.getX() + slotSize + 2 * innerOffset + 15 + 2, inputPoint.getY());
        widgets.add(Widgets.createTexturedWidget(IC2REIPlugin.getTexture("gui"), inputPoint.getX() + slotSize + innerOffset, inputPoint.getY(), 215, 0, 15, 15));
        widgets.add(Widgets.createTexturedWidget(IC2REIPlugin.getTexture("gui"), secondInputPoint.getX() + slotSize, secondInputPoint.getY() - 1, 215, 16, 31, 18));
        String time = DurationFormatUtils.formatDuration(Duration.ofSeconds((long)(display.getRecipe().getTime() / 20)).toMillis(), TIME_FORMAT);
        GuiHelper.addLabelLeft(widgets, point(bounds.getMinX() + offset, bounds.getMaxY() - offset - lineHeight), format("rei.cat.brewing.rum.time", time));
        GuiHelper.addInputSlot(widgets, inputPoint, EntryIngredients.of(display.getRecipe().getInput()));
        GuiHelper.addInputSlot(widgets, secondInputPoint, EntryIngredients.of(IC2Blocks.BARREL.asItem()));
        GuiHelper.addLargeOutputSlot(widgets, adjustedOutputPoint(bounds), EntryIngredients.of(display.getRecipe().getOutput()));
        return widgets;
    }

    @Override
    public int getDisplayWidth(RumBrewDisplay display) {
        return 140;
    }

    @Override
    public int getDisplayHeight() {
        return 56;
    }
}
