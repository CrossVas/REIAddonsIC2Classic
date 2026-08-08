package dev.crossvas.ic2rei.categories.brewing;

import dev.crossvas.ic2rei.IC2REIPlugin;
import dev.crossvas.ic2rei.displays.brewing.BeerBrewDisplay;
import dev.crossvas.ic2rei.utils.CategoryIDs;
import dev.crossvas.ic2rei.utils.GuiHelper;
import dev.crossvas.ic2rei.utils.IGuiHelper;
import ic2.core.platform.registries.IC2Blocks;
import ic2.core.utils.helpers.StackUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.util.List;

public class BeerBrewCategory implements DisplayCategory<BeerBrewDisplay>, IGuiHelper {

    public static final BeerBrewCategory INSTANCE = new BeerBrewCategory();

    @Override
    public CategoryIdentifier<? extends BeerBrewDisplay> getCategoryIdentifier() {
        return CategoryIDs.BEER_BREWING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("rei.cat.brewing.beer");
    }

    @Override
    public Renderer getIcon() {
        ItemStack stack = new ItemStack(IC2Blocks.BARREL);
        stack.getOrCreateTag().putInt("type", 1);
        return EntryStacks.of(stack);
    }

    @Override
    public List<Widget> setupDisplay(BeerBrewDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ObjectArrayList<>();
        ResourceLocation texture = IC2REIPlugin.getTexture("misc/gui_brewing_beer_alt");
        ItemStack CLOCK = new ItemStack(Items.CLOCK, display.getRecipe().getTime() + 1);
        StackUtil.addTooltip(CLOCK, Component.translatable("rei.cat.brewing.beer.clock"));

        widgets.add(Widgets.createTexturedWidget(texture, bounds.getMinX(), bounds.getMinY(), 0, 0, 102, 115));

        GuiHelper.addInputSlot(widgets, point(bounds.getMinX() + 2 * slotSize + innerOffset + 4, bounds.getMinY() + slotSize), EntryIngredients.of(CLOCK), false);

        GuiHelper.addInputSlot(widgets, point(bounds.getMinX() + slotSize + innerOffset, bounds.getMinY() + 2 * slotSize + 4), EntryIngredients.of(display.getRecipe().getWheat()), false);
        GuiHelper.addInputSlot(widgets, point(bounds.getMinX() + 2 * slotSize + innerOffset + 4, bounds.getMinY() + 2 * slotSize + 4), EntryIngredients.of(display.getRecipe().getHops()), false);
        GuiHelper.addInputSlot(widgets, point(bounds.getMinX() + 3 * slotSize + innerOffset + 8, bounds.getMinY() + 2 * slotSize + 4), EntryIngredients.of(display.getRecipe().getWater()), false);

        GuiHelper.addInputSlot(widgets, point(bounds.getMinX() + 2 * slotSize + innerOffset + 4, bounds.getMinY() + 4 * slotSize), EntryIngredients.ofItemStacks(display.getRecipe().getInputs()), false);
        GuiHelper.addInputSlot(widgets, point(bounds.getMaxX() - slotSize - offset, bounds.getMaxY() - slotSize - offset), EntryIngredients.ofItemStacks(display.getRecipe().getOutputs()), false);

        String TIME_FORMAT = "HH:mm:ss";
        long time = 0L;
        for(int i = display.getRecipe().getTime(); i >= 1; --i) {
            time = (long)((double)time + 24000.0 * Math.pow(3.0, i - 1));
        }
        String timeString = DurationFormatUtils.formatDuration(Duration.ofSeconds((long)(time / 20)).toMillis(), TIME_FORMAT);
        GuiHelper.addLabel(widgets, point(bounds.getMinX() + 2 * offset + slotSize + innerOffset, bounds.getMaxY() - offset - lineHeight), Component.literal(" " + timeString + " ").withStyle(ChatFormatting.WHITE));

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 115;
    }

    @Override
    public int getDisplayWidth(BeerBrewDisplay display) {
        return 102;
    }
}
