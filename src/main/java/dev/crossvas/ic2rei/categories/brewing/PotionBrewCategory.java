package dev.crossvas.ic2rei.categories.brewing;

import dev.crossvas.ic2rei.IC2REIPlugin;
import dev.crossvas.ic2rei.displays.brewing.PotionBrewDisplay;
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

import java.util.List;

public class PotionBrewCategory implements DisplayCategory<PotionBrewDisplay>, IGuiHelper {

    public static final PotionBrewCategory INSTANCE = new PotionBrewCategory();

    @Override
    public CategoryIdentifier<? extends PotionBrewDisplay> getCategoryIdentifier() {
        return CategoryIDs.POTION_BREWING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("rei.cat.brewing.potion");
    }

    @Override
    public Renderer getIcon() {
        ItemStack stack = new ItemStack(IC2Blocks.BARREL);
        stack.getOrCreateTag().putInt("type", 10);
        return EntryStacks.of(stack);
    }

    @Override
    public List<Widget> setupDisplay(PotionBrewDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ObjectArrayList<>();
        ResourceLocation texture = IC2REIPlugin.getTexture("misc/gui_brewing_potion");
        ItemStack CLOCK = new ItemStack(Items.CLOCK, display.getRecipe().getDuration() + 1);
        StackUtil.addTooltip(CLOCK, Component.translatable("rei.cat.brewing.beer.clock"));

        widgets.add(Widgets.createTexturedWidget(texture, bounds.getMinX(), bounds.getMinY(), 0, 0, 102, 115));

        GuiHelper.addInputSlot(widgets, point(bounds.getX() + slotSize - 1, bounds.getMinY() + 2 * slotSize - innerOffset), EntryIngredients.of(CLOCK), false);

        GuiHelper.addInputSlot(widgets, point(bounds.getMinX() + 2 * slotSize + innerOffset + 4, bounds.getMinY() + slotSize - offset), EntryIngredients.of(display.getRecipe().getBaseInput()), false);
        GuiHelper.addInputSlot(widgets, point(bounds.getMinX() + 2 * slotSize + innerOffset + 4, bounds.getMinY() + 2 * slotSize + innerOffset), EntryIngredients.of(display.getRecipe().getContainer()), false);
        GuiHelper.addInputSlot(widgets, point(bounds.getMinX() + 3 * slotSize + innerOffset + 4 + 8, bounds.getMinY() + 2 * slotSize - innerOffset), EntryIngredients.of(display.getRecipe().getIngredient()), false);

        GuiHelper.addInputSlot(widgets, point(bounds.getMinX() + slotSize + innerOffset + offset + 1, bounds.getMinY() + 4 * slotSize - offset - 2), EntryIngredients.of(display.getRecipe().getGlowstoneStack()), false);
        GuiHelper.addInputSlot(widgets, point(bounds.getMinX() + 3 * slotSize + offset, bounds.getMinY() + 4 * slotSize - offset - 2), EntryIngredients.of(display.getRecipe().getRedstoneStack()), false);

        GuiHelper.addInputSlot(widgets, point(bounds.getMaxX() - slotSize - offset, bounds.getMaxY() - slotSize - offset), EntryIngredients.of(display.getRecipe().getOutput()), false);

        long time = 0L;
        for(int i = 0; i <= display.getRecipe().getDuration(); ++i) {
            time = (long)((double)time + 5000.0 * Math.pow(3.0, i) * 2.5);
        }
        String timeString = DurationFormatUtils.formatDuration(time, "HH:mm:ss");
        GuiHelper.addLabel(widgets, point(bounds.getMinX() + 2 * offset + slotSize + innerOffset, bounds.getMaxY() - offset - lineHeight), Component.literal(" " + timeString + " ").withStyle(ChatFormatting.WHITE));

        return widgets;
    }

    @Override
    public int getDisplayWidth(PotionBrewDisplay display) {
        return 102;
    }

    @Override
    public int getDisplayHeight() {
        return 115;
    }
}
