package dev.crossvas.ic2rei;

import dev.architectury.fluid.FluidStack;
import dev.crossvas.ic2rei.categories.*;
import dev.crossvas.ic2rei.categories.brewing.BeerBrewCategory;
import dev.crossvas.ic2rei.categories.brewing.PotionBrewCategory;
import dev.crossvas.ic2rei.categories.brewing.RumBrewCategory;
import dev.crossvas.ic2rei.displays.*;
import dev.crossvas.ic2rei.displays.brewing.BeerBrewDisplay;
import dev.crossvas.ic2rei.displays.brewing.PotionBrewDisplay;
import dev.crossvas.ic2rei.displays.brewing.RumBrewDisplay;
import dev.crossvas.ic2rei.handlers.*;
import dev.crossvas.ic2rei.recipes.*;
import dev.crossvas.ic2rei.recipes.brewing.BeerBrewRecipe;
import dev.crossvas.ic2rei.recipes.brewing.PotionBrewRecipe;
import dev.crossvas.ic2rei.recipes.brewing.RumBrewRecipe;
import dev.crossvas.ic2rei.utils.CategoryIDs;
import dev.crossvas.ic2rei.utils.GuiHelper;
import ic2.api.recipes.ingridients.inputs.IInput;
import ic2.api.recipes.ingridients.inputs.IngredientInput;
import ic2.api.recipes.registries.IMachineRecipeList;
import ic2.api.recipes.registries.IScrapBoxRegistry;
import ic2.core.IC2;
import ic2.core.block.cables.CableBlock;
import ic2.core.block.machines.recipes.misc.ScrapOutput;
import ic2.core.inventory.gui.ComponentContainerScreen;
import ic2.core.inventory.gui.IC2Screen;
import ic2.core.item.food_and_drink.GlassItem;
import ic2.core.item.food_and_drink.MugItem;
import ic2.core.item.misc.CropSeedItem;
import ic2.core.item.misc.FluidDisplay;
import ic2.core.platform.recipes.helpers.ItemStackCache;
import ic2.core.platform.recipes.misc.GlobalRecipes;
import ic2.core.platform.registries.IC2Blocks;
import ic2.core.platform.registries.IC2Fluids;
import ic2.core.platform.registries.IC2Items;
import ic2.core.utils.collection.CollectionUtils;
import it.unimi.dsi.fastutil.objects.ObjectList;
import me.shedaniel.rei.api.client.config.ConfigObject;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@REIPluginClient
public class IC2REIPlugin implements REIClientPlugin {

    /**
     * @param path path to the texture starting from <b>gui</b> folder, the dark theme is handled here, so no file extension needed
     */
    public static ResourceLocation getTexture(String path) {
        boolean isDarkTheme = ConfigObject.getInstance().isUsingDarkTheme();
        return new ResourceLocation(IC2REI.ID, "textures/gui/" + path + (isDarkTheme ? "_dark" : "") + ".png");
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registerExistingStations(registry);

        addCat(registry, CategoryIDs.ALLOY_SMELTER, AlloySmelterCategory.INSTANCE,
                IC2Blocks.ALLOY_SMELTER,
                IC2Blocks.PRESSURE_ALLOY_FURNACE
        );
        addCat(registry, CategoryIDs.CANNER_REFILL, CannerCategory.REFILL,
                IC2Blocks.STONE_CANNER,
                IC2Blocks.CANNER,
                IC2Blocks.VACUUM_CANNER
        );
        addCat(registry, CategoryIDs.CANNER_REFUEL, CannerCategory.REFUEL,
                IC2Blocks.STONE_CANNER,
                IC2Blocks.CANNER,
                IC2Blocks.VACUUM_CANNER
        );
        addCat(registry, CategoryIDs.CANNER_REPAIR, CannerCategory.REPAIR,
                IC2Blocks.STONE_CANNER,
                IC2Blocks.CANNER,
                IC2Blocks.VACUUM_CANNER
        );
        addCat(registry, CategoryIDs.COMPRESSOR, new BaseMachineCategory(IC2Blocks.COMPRESSOR, CategoryIDs.COMPRESSOR, GuiHelper.ProgressType.COMPRESSOR),
                IC2Blocks.COMPRESSOR,
                IC2Blocks.SINGULARITY_COMPRESSOR,
                IC2Blocks.COLOSSAL_COMPRESSOR
        );
        addCat(registry, CategoryIDs.EARTH_EXTRACTOR, EarthExtractorCategory.INSTANCE,
                IC2Blocks.RARE_EARTH_EXTRACTOR,
                IC2Blocks.CENTRIFUGAL_RARE_EARTH_EXTRACTOR
        );
        addCat(registry, CategoryIDs.ELECTROLYZER, ElectrolyzerCategory.INSTANCE,
                IC2Blocks.ELECTROLYZER,
                IC2Blocks.CHARGED_ELECTROLYZER
        );
        addCat(registry, CategoryIDs.EXTRACTOR, new BaseMachineCategory(IC2Blocks.EXTRACTOR, CategoryIDs.EXTRACTOR, GuiHelper.ProgressType.EXTRACTOR),
                IC2Blocks.EXTRACTOR,
                IC2Blocks.CENTRIFUGAL_EXTRACTOR,
                IC2Blocks.COLOSSAL_EXTRACTOR
        );
        addCat(registry, CategoryIDs.GENERATOR, GeneratorCategory.INSTANCE, IC2Blocks.GENERATOR);
        addCat(registry, CategoryIDs.GEOTHERMAL_GENERATOR, FluidGeneratorsCategory.GEOTHERMAL, IC2Blocks.GEOTHERMAL_GENERATOR);
        addCat(registry, CategoryIDs.FLUID_GENERATOR, FluidGeneratorsCategory.FLUID_HENERATOR, IC2Blocks.LIQUID_FUEL_GENERATOR);
        addCat(registry, CategoryIDs.MACERATOR, new BaseMachineCategory(IC2Blocks.MACERATOR, CategoryIDs.MACERATOR, GuiHelper.ProgressType.MACERATOR),
                IC2Blocks.STONE_MACERATOR,
                IC2Blocks.MACERATOR,
                IC2Blocks.ROTARY_MACERATOR,
                IC2Blocks.COLOSSAL_MACERATOR
        );
        addCat(registry, CategoryIDs.MASS_FABRICATOR, MassFabricatorCategory.INSTANCE, IC2Blocks.MASS_FABRICATOR);
        addCat(registry, CategoryIDs.NUCLEAR_REACTOR, NuclearReactorCategory.INSTANCE,
                IC2Blocks.NUCLEAR_REACTOR,
                IC2Blocks.STEAM_REACTOR,
                IC2Blocks.REACTOR_PLANNER
        );
        addCat(registry, CategoryIDs.PLASMAFIER, PlasmafierCategory.INSTANCE, IC2Blocks.PLASMAFIER);
        addCat(registry, CategoryIDs.RECYCLER, RecyclerCategory.INSTANCE,
                IC2Blocks.RECYCLER,
                IC2Blocks.COMPACTING_RECYCLER,
                IC2Blocks.SLOW_GRINDER,
                IC2Blocks.COLOSSAL_RECYCLER
        );
        addCat(registry, CategoryIDs.REFINERY, RefineryCategory.INSTANCE, IC2Blocks.REFINERY);
        addCat(registry, CategoryIDs.SAWMILL, new BaseMachineCategory(IC2Blocks.SAWMILL, CategoryIDs.SAWMILL, GuiHelper.ProgressType.SAWMILL), IC2Blocks.SAWMILL);
        addCat(registry, CategoryIDs.ENRICHER, EnricherCategory.INSTANCE, IC2Blocks.URANIUM_ENRICHER);
        addCat(registry, CategoryIDs.WOOD_GASSIFIER, WoodGassifierCategory.INSTANCE,
                IC2Blocks.WOOD_GASSIFIER,
                IC2Blocks.ELECTRIC_WOOD_GASSIFIER
        );
        addCat(registry, CategoryIDs.RUM_BREWING, RumBrewCategory.INSTANCE, IC2Blocks.BARREL);
        addCat(registry, CategoryIDs.BEER_BREWING, BeerBrewCategory.INSTANCE, IC2Blocks.BARREL);
        addCat(registry, CategoryIDs.POTION_BREWING, PotionBrewCategory.INSTANCE, IC2Blocks.BARREL);
        addCat(registry, CategoryIDs.SCRAPBOX, ScrapboxCategory.INSTANCE, IC2Items.SCRAPBOX);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        GlobalRecipes RECIPES = IC2.RECIPES.get(false);
        RECIPES.macerator.getAllEntries().forEach(entry -> registry.add(new BaseMachineDisplay(new BaseMachineRecipe(entry), CategoryIDs.MACERATOR)));
        RECIPES.extractor.getAllEntries().forEach(entry -> registry.add(new BaseMachineDisplay(new BaseMachineRecipe(entry), CategoryIDs.EXTRACTOR)));
        RECIPES.compressor.getAllEntries().forEach(entry -> registry.add(new BaseMachineDisplay(new BaseMachineRecipe(entry), CategoryIDs.COMPRESSOR)));
        createRecyclerRecipes(RECIPES).forEach(entry -> registry.add(new RecyclerDisplay(new RecyclerRecipe(entry))));
        RECIPES.sawmill.getAllEntries().forEach(entry -> registry.add(new BaseMachineDisplay(new BaseMachineRecipe(entry), CategoryIDs.SAWMILL)));
        RECIPES.mixingFurnace.getAllEntries().forEach(entry -> registry.add(new DoubleInputMachineDisplay(new DoubleInputMachineRecipe(entry), CategoryIDs.ALLOY_SMELTER)));
        RECIPES.fluid_fuel.getFuels().forEach(fuelEntry -> registry.add(new FluidGeneratorsDisplay.FluidGeneratorDisplay(new FluidGeneratorRecipe(fuelEntry), false)));
        RECIPES.rare_earth.getAllRecipes().forEach(entry -> registry.add(new EarthExtractorDisplay(new EarthExtractorRecipe(entry))));
        RECIPES.electrolyzer.getRecipes().forEach(recipe -> registry.add(new ElectrolyzerDisplay(recipe)));
        RECIPES.refining.getAllRecipes().forEach(recipe -> registry.add(new RefineryDisplay(new RefineryRecipe(recipe))));
        RECIPES.enricher.getRecipes().forEach(recipe -> registry.add(new EnricherDisplay(recipe)));
        registry.add(new WoodGassifierDisplay(new WoodGassifierRecipe(WoodGassifierCategory.getLogList(), new ItemStack(Items.CHARCOAL), FluidStack.create(IC2Fluids.WOOD_GAS, 810))));
        List<IScrapBoxRegistry.IDrop> sortedScrapboxList = IC2.RECIPES.get(false).scrapBoxes.getAllDrops().stream().sorted(Comparator.comparing(IScrapBoxRegistry.IDrop::getChance).reversed()).toList();
        sortedScrapboxList.forEach(drop -> registry.add(new ScrapboxDisplay(new ScrapboxRecipe(drop))));
        registerCannerRecipe(registry, RECIPES);
        GeneratorRecipe.getGeneratorRecipes().forEach(recipe -> registry.add(new GeneratorDisplay(recipe)));
        FluidGeneratorRecipe.getGeothermalRecipes().forEach(recipe -> registry.add(new FluidGeneratorsDisplay.FluidGeneratorDisplay(recipe, true)));
        MassFabricatorRecipe.getMassFabRecipes().forEach(recipe -> registry.add(new MassFabricatorDisplay(recipe)));
        PlasmafierRecipe.getPlasmafierRecipes().forEach(recipe -> registry.add(new PlasmafierDisplay(recipe)));
        RumBrewRecipe.getRumRecipes().forEach(recipe -> registry.add(new RumBrewDisplay(recipe)));
        BeerBrewRecipe.getBeerBrewRecipes().forEach(recipe -> registry.add(new BeerBrewDisplay(recipe)));
        PotionBrewRecipe.getPotionRecipeList(RECIPES).forEach(recipe -> registry.add(new PotionBrewDisplay(recipe)));
        NuclearReactorScheme.getReactorSchemes().forEach(scheme -> registry.add(new NuclearReactorDisplay(scheme)));
        registry.registerVisibilityPredicate(new RecipeHandler());
    }

    @Override
    public void registerExclusionZones(ExclusionZones zones) {
        zones.register(IC2Screen.class, new ExclusionZoneHandler());
    }

    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        registry.register(new IndustrialWorktableTransferHandler());
        registry.register(new CraftingUpgradeTransferHandler());
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        MachineClickAreaHandler.init();
        registry.registerClickArea(ComponentContainerScreen.class, new MachineClickAreaHandler());
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void registerCollapsibleEntries(CollapsibleEntryRegistry registry) {
        registry.group(new ResourceLocation("ic2", "crops_group"), Component.translatable("rei.group.crop_seeds"), stack ->
                stack.getType() == VanillaEntryTypes.ITEM && stack.<ItemStack>castValue().getItem() instanceof CropSeedItem);
        registry.group(new ResourceLocation("ic2", "mugs_group"), Component.translatable("rei.group.mugs"), stack ->
                stack.getType() == VanillaEntryTypes.ITEM && stack.<ItemStack>castValue().getItem() instanceof MugItem);
        registry.group(new ResourceLocation("ic2", "glasses_group"), Component.translatable("rei.group.glasses"), stack ->
                stack.getType() == VanillaEntryTypes.ITEM && stack.<ItemStack>castValue().getItem() instanceof GlassItem);
    }

    @Override
    public void registerEntries(EntryRegistry registry) {
        // remove cable blocks
        ForgeRegistries.BLOCKS.forEach(block -> {
            if (block instanceof CableBlock cable) {
                registry.removeEntry(EntryStacks.of(cable));
            }
        });
        // remove debug items
        registry.removeEntry(EntryStacks.of(IC2Items.ICON_DISPLAY));
        registry.removeEntry(EntryStacks.of(IC2Items.TAG_ITEM));
        registry.removeEntry(EntryStacks.of(IC2Items.TAG_BLOCK));
        // replace IC2 FluidDisplay stacks with FluidStacks
        ForgeRegistries.FLUIDS.forEach(fluid -> {
            if (fluid.isSource(fluid.defaultFluidState())) {
                registry.removeEntry(EntryStacks.of(FluidDisplay.createDisplay(fluid)));
                registry.addEntry(EntryStack.of(VanillaEntryTypes.FLUID, FluidStack.create(fluid, 1000)));
            }
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void addCat(CategoryRegistry r, CategoryIdentifier id, DisplayCategory displayCat, Block... stations) {
        r.add(displayCat);
        Arrays.stream(stations).forEach(station -> r.addWorkstations(id, EntryStacks.of(station)));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void addCat(CategoryRegistry r, CategoryIdentifier id, DisplayCategory displayCat, Item... stations) {
        r.add(displayCat);
        Arrays.stream(stations).forEach(station -> r.addWorkstations(id, EntryStacks.of(station)));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void addWorkstations(CategoryRegistry r, CategoryIdentifier id, Block... stations) {
        Arrays.stream(stations).forEach(station -> r.addWorkstations(id, EntryStacks.of(station)));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void addItemWorkstations(CategoryRegistry r, CategoryIdentifier id, Item... stations) {
        Arrays.stream(stations).forEach(station -> r.addWorkstations(id, EntryStacks.of(station)));
    }

    private static List<IMachineRecipeList.RecipeEntry> createRecyclerRecipes(GlobalRecipes recipe) {
        List<IMachineRecipeList.RecipeEntry> result = CollectionUtils.createList();
        List<ItemStack> scrapItems = CollectionUtils.createList();
        List<ItemStack> scrapMetalItems = CollectionUtils.createList();
        ObjectList<ItemStack> lists = ItemStackCache.INSTANCE.getCache();
        int i = 0;

        for(int m = lists.size(); i < m; ++i) {
            IMachineRecipeList.RecipeEntry entry = recipe.recycler.getRecipe(lists.get(i), false);
            if (entry != null && entry.getOutput() instanceof ScrapOutput) {
                String path = entry.getLocation().getPath();
                if (path.equalsIgnoreCase("scrap")) {
                    scrapItems.add(lists.get(i));
                } else if (path.equalsIgnoreCase("scrap_metal")) {
                    scrapMetalItems.add(lists.get(i));
                }
            }
        }

        if (IC2.CONFIG.showDefaultRecyclerRecipe.get()) {
            result.add(new IMachineRecipeList.RecipeEntry(new ResourceLocation("ic2", "scrap"), new ScrapOutput(new ItemStack(IC2Items.SCRAP), 4), new IInput[]{new IngredientInput(Ingredient.of((ItemStack[])scrapItems.toArray(new ItemStack[scrapItems.size()])), 1)}));
        }

        result.add(new IMachineRecipeList.RecipeEntry(new ResourceLocation("ic2", "scrap_metal"), new ScrapOutput(new ItemStack(IC2Items.SCRAP_METAL), 8), new IInput[]{new IngredientInput(Ingredient.of((ItemStack[])scrapMetalItems.toArray(new ItemStack[scrapMetalItems.size()])), 1)}));
        result.add(ScrapOutput.createEntry(0));
        result.addAll(recipe.recycler.getAllEntries());
        return result;
    }

    public static void registerExistingStations(CategoryRegistry r) {
        addWorkstations(r, BuiltinPlugin.SMELTING,
                IC2Blocks.IRON_FURNACE,
                IC2Blocks.ELECTRIC_FURNACE,
                IC2Blocks.INDUCTION_FURNACE,
                IC2Blocks.COLOSSAL_FURNACE
        );

        addWorkstations(r, BuiltinPlugin.BLASTING,
                IC2Blocks.ELECTRIC_BLAST_FURNACE,
                IC2Blocks.INDUCTION_BLAST_FURNACE
        );

        addWorkstations(r, BuiltinPlugin.SMOKING,
                IC2Blocks.ELECTRIC_SMOKER,
                IC2Blocks.INDUCTION_SMOKER
        );

        addWorkstations(r, BuiltinPlugin.CRAFTING, IC2Blocks.INDUSTRIAL_WORKBENCH);
        addItemWorkstations(r, BuiltinPlugin.CRAFTING, IC2Items.CRAFTING_UPGRADE);
        addWorkstations(r, BuiltinPlugin.ANVIL, IC2Blocks.ELECTRIC_ENCHANTER);
    }

    public void registerCannerRecipe(DisplayRegistry registry, GlobalRecipes recipes) {
        CannerRecipe.getRepairRecipes(recipes).forEach(recipe -> registry.add(new CannerDisplay.CannerRepairDisplay(recipe)));
        CannerRecipe.getFuelingRecipes(recipes).forEach(recipe -> registry.add(new CannerDisplay.CannerRefuelDisplay(recipe)));
        CannerRecipe.getRefillRecipes(recipes).forEach(recipe -> registry.add(new CannerDisplay.CannerRefillDisplay(recipe)));
    }

    public static List<ItemStack> convertIngredientToItemStacks(final EntryIngredient ingredient) {
        return me.shedaniel.rei.api.common.util.CollectionUtils.<EntryStack<?>, ItemStack>filterAndMap(
                ingredient,
                stack -> stack.getType() == VanillaEntryTypes.ITEM,
                EntryStack::castValue
        );
    }
}
