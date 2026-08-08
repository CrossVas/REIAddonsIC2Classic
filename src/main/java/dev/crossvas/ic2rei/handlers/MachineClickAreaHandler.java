package dev.crossvas.ic2rei.handlers;

import dev.crossvas.ic2rei.utils.CategoryIDs;
import ic2.core.block.base.tiles.impls.BaseGeneratorTileEntity;
import ic2.core.block.base.tiles.impls.machine.multi.BaseColossalMachineTileEntity;
import ic2.core.block.generators.tiles.FuelGenTileEntity;
import ic2.core.block.generators.tiles.GeoGenTileEntity;
import ic2.core.block.generators.tiles.LiquidFuelGenTileEntity;
import ic2.core.block.machines.components.ev.ColossalMachineComponent;
import ic2.core.block.machines.tiles.ev.*;
import ic2.core.block.machines.tiles.hv.MassFabricatorTileEntity;
import ic2.core.block.machines.tiles.hv.PressureAlloyFurnaceTileEntity;
import ic2.core.block.machines.tiles.hv.UraniumEnricherTileEntity;
import ic2.core.block.machines.tiles.lv.*;
import ic2.core.block.machines.tiles.mv.*;
import ic2.core.block.machines.tiles.nv.*;
import ic2.core.inventory.container.ContainerComponent;
import ic2.core.inventory.gui.ComponentContainerScreen;
import ic2.core.inventory.gui.components.simple.ChargeBarComponent;
import ic2.core.inventory.gui.components.simple.FuelComponent;
import ic2.core.inventory.gui.components.simple.ProgressComponent;
import ic2.core.inventory.gui.components.simple.PumpComponent;
import ic2.core.utils.collection.CollectionUtils;
import ic2.core.utils.math.geometry.Box2i;
import ic2.core.utils.math.geometry.Vec2i;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.registry.screen.ClickArea;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Map;

public class MachineClickAreaHandler implements ClickArea<ComponentContainerScreen> {

    // mapped tiles/ids
    public static Map<Class<? extends BlockEntity>, List<CategoryIdentifier<?>>> MAPPED = CollectionUtils.createLinkedMap();

    public static void init() {
        MAPPED.put(MaceratorTileEntity.class, List.of(CategoryIDs.MACERATOR));
        MAPPED.put(RotaryMaceratorTileEntity.class, List.of(CategoryIDs.MACERATOR));
        MAPPED.put(StoneMaceratorTileEntity.class, List.of(CategoryIDs.MACERATOR));
        MAPPED.put(ColossalMacerator.class, List.of(CategoryIDs.MACERATOR));

        MAPPED.put(ExtractorTileEntity.class, List.of(CategoryIDs.EXTRACTOR));
        MAPPED.put(CentrifugalExtractorTileEntity.class, List.of(CategoryIDs.EXTRACTOR));
        MAPPED.put(ColossalExtractor.class, List.of(CategoryIDs.EXTRACTOR));

        MAPPED.put(CompressorTileEntity.class, List.of(CategoryIDs.COMPRESSOR));
        MAPPED.put(SingularityCompressorTileEntity.class, List.of(CategoryIDs.COMPRESSOR));
        MAPPED.put(ColossalCompressor.class, List.of(CategoryIDs.COMPRESSOR));

        MAPPED.put(ElectrolyzerTileEntity.class, List.of(CategoryIDs.ELECTROLYZER));
        MAPPED.put(ChargedElectrolyzerTileEntity.class, List.of(CategoryIDs.ELECTROLYZER));

        MAPPED.put(RecyclerTileEntity.class, List.of(CategoryIDs.RECYCLER));
        MAPPED.put(CompactingRecyclerTileEntity.class, List.of(CategoryIDs.RECYCLER));
        MAPPED.put(ColossalRecycler.class, List.of(CategoryIDs.RECYCLER));

        MAPPED.put(AlloySmelterTileEntity.class, List.of(CategoryIDs.ALLOY_SMELTER));
        MAPPED.put(PressureAlloyFurnaceTileEntity.class, List.of(CategoryIDs.ALLOY_SMELTER));

        MAPPED.put(StoneCannerTileEntity.class, List.of(CategoryIDs.CANNER_REPAIR, CategoryIDs.CANNER_REFUEL, CategoryIDs.CANNER_REFILL));
        MAPPED.put(CannerTileEntity.class, List.of(CategoryIDs.CANNER_REPAIR, CategoryIDs.CANNER_REFUEL, CategoryIDs.CANNER_REFILL));
        MAPPED.put(VacuumCannerTileEntity.class, List.of(CategoryIDs.CANNER_REPAIR, CategoryIDs.CANNER_REFUEL, CategoryIDs.CANNER_REFILL));

        MAPPED.put(RareEarthExtractorTileEntity.class, List.of(CategoryIDs.EARTH_EXTRACTOR));
        MAPPED.put(RareEarthCentrifugeTileEntity.class, List.of(CategoryIDs.EARTH_EXTRACTOR));

        MAPPED.put(WoodGassifierTileEntity.class, List.of(CategoryIDs.WOOD_GASSIFIER));
        MAPPED.put(StoneWoodGassifierTileEntity.class, List.of(CategoryIDs.WOOD_GASSIFIER));

        MAPPED.put(SawmillTileEntity.class, List.of(CategoryIDs.SAWMILL));
        MAPPED.put(RefineryTileEntity.class, List.of(CategoryIDs.REFINERY));
        MAPPED.put(UraniumEnricherTileEntity.class, List.of(CategoryIDs.ENRICHER));

        MAPPED.put(FuelGenTileEntity.class, List.of(CategoryIDs.GENERATOR));
        MAPPED.put(LiquidFuelGenTileEntity.class, List.of(CategoryIDs.FLUID_GENERATOR));
        MAPPED.put(GeoGenTileEntity.class, List.of(CategoryIDs.GEOTHERMAL_GENERATOR));

        MAPPED.put(PlasmafierTileEntity.class, List.of(CategoryIDs.PLASMAFIER));
        MAPPED.put(MassFabricatorTileEntity.class, List.of(CategoryIDs.MASS_FABRICATOR));

        MAPPED.put(IronFurnaceTileEntity.class, List.of(BuiltinPlugin.SMELTING));
        MAPPED.put(ElectricFurnaceTileEntity.class, List.of(BuiltinPlugin.SMELTING));
        MAPPED.put(InductionFurnaceTileEntity.class, List.of(BuiltinPlugin.SMELTING));
        MAPPED.put(ColossalFurnace.class, List.of(BuiltinPlugin.SMELTING));

        MAPPED.put(ElectricSmokerTileEntity.class, List.of(BuiltinPlugin.SMOKING));
        MAPPED.put(SmokerInductionFurnaceTileEntity.class, List.of(BuiltinPlugin.SMOKING));

        MAPPED.put(ElectricBlastFurnaceTileEntity.class, List.of(BuiltinPlugin.BLASTING));
        MAPPED.put(BlastInductionFurnaceTileEntity.class, List.of(BuiltinPlugin.BLASTING));

        MAPPED.put(IndustrialWorkbenchTileEntity.class, List.of(BuiltinPlugin.CRAFTING));
    }

    @Override
    public Result handle(ClickAreaContext<ComponentContainerScreen> context) {
        ComponentContainerScreen container = context.getScreen();
        Point mousePoint = context.getMousePosition();
        ContainerComponent<?> comp = (ContainerComponent<?>)container.getCastedContainer(ContainerComponent.class);
        if (comp != null) {
            // basic machines
            if (comp.getHolder() != null) {
                if (MAPPED.containsKey(comp.getHolder().getClass())) {
                    ProgressComponent progress = container.getComponentFromClass(ProgressComponent.class);
                    if (progress != null) {
                        Box2i box = progress.getBox();
                        Rectangle progressBox = new Rectangle(container.getGuiLeft() + box.getX(), container.getGuiTop() + box.getY(), box.getWidth(), box.getHeight());
                        if (progressBox.contains(mousePoint)) {
                            return ClickArea.Result.success().categories(MAPPED.get(comp.getHolder().getClass()).stream().toList());
                        }
                    }

                    // colossal
                    if (comp.getHolder() instanceof BaseColossalMachineTileEntity colossalMachineTile) {
                        ColossalMachineComponent component = container.getComponentFromClass(ColossalMachineComponent.class);
                        if (component != null) {
                            int hovered = getHoveredColossalProgress(colossalMachineTile, container.getGuiLeft(), container.getGuiTop(), mousePoint);
                            if (hovered != -1) {
                                return ClickArea.Result.success()
                                        .categories(MAPPED.get(comp.getHolder().getClass()).stream().toList());
                            }
                        }
                    }

                    // generators
                    if (comp.getHolder() instanceof BaseGeneratorTileEntity generator) {
                        FuelComponent fuel = container.getComponentFromClass(FuelComponent.class);
                        if (fuel != null) {
                            Box2i box = fuel.getBox();
                            Rectangle fuelBox = new Rectangle(container.getGuiLeft() + box.getX(), container.getGuiTop() + box.getY(), box.getWidth(), box.getHeight());
                            if (fuelBox.contains(mousePoint)) {
                                if (generator instanceof LiquidFuelGenTileEntity) {
                                    return ClickArea.Result.success().category(CategoryIDs.FLUID_GENERATOR);
                                } else if (generator instanceof GeoGenTileEntity) {
                                    return ClickArea.Result.success().category(CategoryIDs.GEOTHERMAL_GENERATOR);
                                } else {
                                    return ClickArea.Result.success().category(CategoryIDs.GENERATOR);
                                }
                            }
                        }
                    }
                    // special - no components
                    if (comp.getHolder() instanceof MassFabricatorTileEntity) {
                        Rectangle progressBox = new Rectangle(container.getGuiLeft() + 79, container.getGuiTop() + 40, 18, 15);
                        if (progressBox.contains(mousePoint)) {
                            return ClickArea.Result.success().category(CategoryIDs.MASS_FABRICATOR);
                        }
                    }
                    // special - pump component
                    if (comp.getHolder() instanceof PlasmafierTileEntity) {
                        PumpComponent pumpBar = container.getComponentFromClass(PumpComponent.class);
                        if (pumpBar != null) {
                            Box2i pumpBox = pumpBar.getBox();
                            Rectangle progressBox = new Rectangle(container.getGuiLeft() + pumpBox.getX(), container.getGuiTop() + pumpBox.getY(), pumpBox.getWidth(), pumpBox.getHeight());
                            if (progressBox.contains(mousePoint)) {
                                return ClickArea.Result.success().category(CategoryIDs.PLASMAFIER);
                            }
                        }
                    }
                    // special - chargeBar component
                    if (comp.getHolder() instanceof ElectrolyzerTileEntity || comp.getHolder() instanceof ChargedElectrolyzerTileEntity) {
                        ChargeBarComponent chargebarComponent = container.getComponentFromClass(ChargeBarComponent.class);
                        if (chargebarComponent != null) {
                            Box2i chargeBarBox = chargebarComponent.getBox();
                            Rectangle progressBox = new Rectangle(container.getGuiLeft() + chargeBarBox.getX(), container.getGuiTop() + chargeBarBox.getY(), chargeBarBox.getWidth(), chargeBarBox.getHeight());
                            if (progressBox.contains(mousePoint)) {
                                return ClickArea.Result.success().category(CategoryIDs.ELECTROLYZER);
                            }
                        }
                    }
                    if (comp.getHolder() instanceof IndustrialWorkbenchTileEntity) {

                        Rectangle arrowBox = new Rectangle(container.getGuiLeft() + 90, container.getGuiTop() + 35, 22, 15);
                        if (arrowBox.contains(mousePoint)) {

                            return ClickArea.Result.success().category(BuiltinPlugin.CRAFTING);
                        }
                    }
                }
            }
        }
        return ClickArea.Result.success();
    }

    private static int getHoveredColossalProgress(BaseColossalMachineTileEntity tile, int guiLeft, int guiTop, Point mousePoint) {
        int size = tile.getSlotsInUse();

        if (size <= 0 || size >= ColossalMachineComponent.PROGRESS.length) {
            return -1;
        }

        Vec2i[] positions = ColossalMachineComponent.PROGRESS[size];
        for (int i = 0; i < positions.length; i++) {
            Vec2i pos = positions[i];
            Rectangle box = new Rectangle(guiLeft + pos.getX(), guiTop + pos.getY(), 24, 16);
            if (box.contains(mousePoint)) {
                return i;
            }
        }
        return -1;
    }
}
