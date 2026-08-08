package dev.crossvas.ic2rei.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.crossvas.ic2rei.IC2REIPlugin;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.impl.client.gui.widget.EntryWidget;
import net.minecraft.network.chat.Component;

import java.util.List;

public class GuiHelper {

    public static void createRecipeBase(List<Widget> list, Rectangle bounds) {
        list.add(Widgets.createRecipeBase(bounds));
    }

    public static void addProgressBar(List<Widget> list, Point point, double animationTime, ProgressType type) {
        list.add(createProgressBar(point.getX(), point.getY(), animationTime, type));
    }

    private static Widget createProgressBar(int x, int y, double animationTime, ProgressType type) {
        return Widgets.createDrawableWidget((helper, matrices, mouseX, mouseY, delta) -> {
            RenderSystem.setShaderTexture(0, IC2REIPlugin.getTexture("gui"));
            helper.blit(matrices, x, y, type.x, type.y, type.width, type.height);
            int i = (int) ((System.currentTimeMillis() / animationTime) % 1.0 * type.width);
            if (i < 0) {
                i = 0;
            }
            helper.blit(matrices, x, y, type.xActive, type.yActive, i, type.height);
        });
    }

    public static void addInputSlot(List<Widget> list, Point point, EntryIngredient entry, boolean background) {
        Slot slot = Widgets.createSlot(point).entries(entry).markInput();
        slot.setBackgroundEnabled(background);
        list.add(slot);
    }

    public static void addInputSlot(List<Widget> list, Point point, EntryIngredient entry) {
        list.add(Widgets.createSlot(point).entries(entry).markInput());
    }

    public static void addBaseOutputSlot(List<Widget> list, Point point, EntryIngredient entry) {
        list.add(Widgets.createSlot(point).entries(entry).markOutput());
    }

    public static void addLargeOutputSlot(List<Widget> list, Point point, EntryIngredient entry) {
        list.add(Widgets.createResultSlotBackground(new Point(point.getX(), point.getY())));
        list.add(Widgets.createSlot(point).entries(entry).markOutput().disableBackground());
    }

    public static void addTank(List<Widget> list, int x, int y, EntryIngredient entry) {
        // tank base
        list.add(Widgets.createTexturedWidget(IC2REIPlugin.getTexture("gui"), x, y, ProgressType.REFINERY.x, ProgressType.REFINERY.y, ProgressType.REFINERY.width, ProgressType.REFINERY.height));
        list.add(new EntryWidget(new Rectangle(x, y, ProgressType.REFINERY.width, ProgressType.REFINERY.height)).entries(entry).disableBackground().disableHighlight());
        // tank overlay
        list.add(Widgets.createTexturedWidget(IC2REIPlugin.getTexture("gui"), x + 1, y + 1, 19, 170, 16, 58));
    }

    public static void addLabel(List<Widget> widgetList, Point point, Component text) {
        widgetList.add(Widgets.createLabel(point, text).color(-12566464, -4473925).noShadow());
    }

    public static void addLabelRight(List<Widget> widgetList, Point point, Component text) {
        widgetList.add(Widgets.createLabel(point, text).color(-12566464, -4473925).noShadow().rightAligned());
    }

    public static void addLabelLeft(List<Widget> widgetList, Point point, Component text) {
        widgetList.add(Widgets.createLabel(point, text).color(-12566464, -4473925).noShadow().leftAligned());
    }

    public enum ProgressType {
        MACERATOR(25, 0, 25, 18, 24, 17),
        EXTRACTOR(75, 0, 75, 18, 24, 17),
        COMPRESSOR(50, 0, 50, 18, 24, 17),
        SAWMILL(150, 0, 150, 18, 24, 17),
        RECYCLER(125, 0, 125, 18, 24, 17),
        EARTH_EXTRACTOR(100, 0, 100, 18, 24, 17),
        SMELTER(0, 0, 0, 18, 24, 17),
        ELECTROLYZER(175, 0, 175, 18, 24, 17),
        CANNER(0, 36, 0, 50, 34, 13),
        REFINERY(0, 170, 0, 0, 18, 60),
        ENRICHER(0, 64, 0, 99, 52, 34),
        GENERATOR(175, 0, 175, 18, 24, 17);

        public final int x;
        public final int y;
        public final int xActive;
        public final int yActive;
        public final int width;
        public final int height;

        ProgressType(int x, int y, int xActive, int yActive, int width, int height) {
            this.x = x;
            this.y = y;
            this.xActive = xActive;
            this.yActive = yActive;
            this.width = width;
            this.height = height;

        }
    }
}
