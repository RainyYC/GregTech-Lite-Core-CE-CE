package magicbook.gtlitecore.api.capability.impl;

import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.multiblock.ParallelLogicType;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.recipes.FluidKey;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.ingredients.GTRecipeInput;
import gregtech.api.util.GTTransferUtils;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ItemStackHashStrategy;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class AdvancedRecipeLogic extends MultiblockRecipeLogic {
    public AdvancedRecipeLogic(RecipeMapMultiblockController tileEntity) {
        super(tileEntity);
    }

    public AdvancedRecipeLogic(RecipeMapMultiblockController tileEntity, boolean hasPerfectOC) {
        super(tileEntity, hasPerfectOC);
    }

    // for MAX+ OC

    // not getter no setter
    public class OCParameter {
        public double durationMultiplier, eutMultiplier;
        public int count;

        public OCParameter(double durationMultiplier, double eutMultiplier, int count) {
            this.durationMultiplier = durationMultiplier;
            this.eutMultiplier = eutMultiplier;
            this.count = count;
        }

        public OCParameter copy() {
            return new OCParameter(durationMultiplier, eutMultiplier, count);
        }
    }

    public class OCResult {
        public long eut, duration;
        public int parallel;

        public OCResult(long eut, long duration, int parallel) {
            this.eut = eut;
            this.duration = duration;
            this.parallel = parallel;
        }
    }

    @Override
    final public boolean isAllowOverclocking() {
        return false;
    }

    public long recipeEUt = 0;

    @Override
    public boolean prepareRecipe(Recipe recipe, IItemHandlerModifiable inputInventory,
                                 IMultipleTankHandler inputFluidInventory) {
        recipe = Recipe.trimRecipeOutputs(recipe, getRecipeMap(), metaTileEntity.getItemOutputLimit(),
                metaTileEntity.getFluidOutputLimit());

        double euDiscount = getEUtDiscount(), speedBonus = getSpeedBonus();
        // apply EU/speed discount (if any) before parallel
        if (euDiscount > 0 || speedBonus > 0) { // if-statement to avoid unnecessarily creating RecipeBuilder object
            RecipeBuilder<?> builder = new RecipeBuilder<>(recipe, getRecipeMap());
            if (euDiscount > 0) {
                int newEUt = (int) Math.round(recipe.getEUt() * euDiscount);
                if (newEUt <= 0) newEUt = 1;
                builder.EUt(newEUt);
            }
            if (speedBonus > 0) {
                int duration = recipe.getDuration();
                int newDuration = (int) Math.round(duration * speedBonus);
                if (newDuration <= 0) newDuration = 1;
                builder.duration(newDuration);
            }
            recipe = builder.build().getResult();
        }

        // Pass in the trimmed recipe to the parallel logic
        recipe = find1tocParallelRecipe(
                recipe,
                inputInventory,
                inputFluidInventory,
                getOutputInventory(),
                getOutputTank(),
                getMaxParallelVoltage(),
                getParallelLimit());

        if (recipe != null && setupAndConsumeRecipeInputs(recipe, inputInventory, inputFluidInventory)) {
            setupRecipe(recipe);
            return true;
        }
        return false;
    }

    private Recipe find1tocParallelRecipe(Recipe currentRecipe, IItemHandlerModifiable inputs,
                                      IMultipleTankHandler fluidInputs,
                                      IItemHandlerModifiable outputs,
                                      IMultipleTankHandler fluidOutputs, long maxVoltage, int parallelLimit) {
        OCResult oc = makeOC(currentRecipe);
        if ((parallelLimit > 1 || oc.parallel > 1) && getRecipeMap() != null) {
            RecipeBuilder<?> parallelBuilder = switch (getParallelLogicType()) {
                case MULTIPLY -> findMultipliedParallelRecipe(getRecipeMap(), currentRecipe, inputs, fluidInputs,
                        outputs, fluidOutputs, parallelLimit * oc.parallel, maxVoltage, getMetaTileEntity());
                case APPEND_ITEMS -> findAppendedParallelItemRecipe(getRecipeMap(), inputs, outputs,
                        parallelLimit * oc.parallel, maxVoltage, getMetaTileEntity());
            };

            // if the builder returned is null, no recipe was found.
            if (parallelBuilder == null) {
                invalidateInputs();
                return null;
            } else {
                // if the builder returned does not parallel, its outputs are full
                if (parallelBuilder.getParallel() == 0) {
                    invalidateOutputs();
                    return null;
                } else {
                    setParallelRecipesPerformed(parallelBuilder.getParallel());
                    // apply any parallel bonus
                    applyParallelBonus(parallelBuilder);
                    return parallelBuilder.build().getResult();
                }
            }
        }
        return currentRecipe;
    }

    private OCResult makeOC(Recipe recipe) {
        return makeOC(recipe.getEUt(), recipe.getDuration());
    }

    private OCResult makeOC(double eut, double duration) {
        long maxEU = getInputEUt();
        List<OCParameter> ocParameters = getOCList();
        double parallel = 1;
        boolean is1tick = duration <= 1.0D;
        for(OCParameter parameter : ocParameters) {
            parameter = parameter.copy();
            while(parameter.count --> 0) {
                if(eut * parameter.eutMultiplier > maxEU)
                    return new OCResult((long) eut, Math.round(duration), (int) Math.round(parallel));
                eut *= parameter.eutMultiplier;
                if(is1tick)
                    parallel = Math.min(Integer.MAX_VALUE, parallel * parameter.durationMultiplier);
                else {
                    duration = Math.max(duration / parameter.durationMultiplier, 1.0D);
                    if(Math.round(duration) == 1) {
                        duration = 1.0D;
                        is1tick = true;
                    }
                }
            }
        }
        return new OCResult((long) eut, Math.round(duration), (int) Math.round(parallel));
    }

    @Override
    protected void setupRecipe(Recipe recipe) {
        LSetupRecipe(recipe, overclockResults[0], overclockResults[1]);
    }

    protected boolean drawEnergy(long recipeEUt, boolean simulate) {
        long resultEnergy = getEnergyStored() - recipeEUt;
        if (resultEnergy >= 0L && resultEnergy <= getEnergyCapacity()) {
            if (!simulate) getEnergyContainer().changeEnergy(-recipeEUt);
            return true;
        } else return false;
    }

    @Override
    protected void updateRecipeProgress() {
        if (canRecipeProgress && drawEnergy(recipeEUt, true)) {
            drawEnergy(recipeEUt, false);
            // as recipe starts with progress on 1 this has to be > only not => to compensate for it
            if (++progressTime > maxProgressTime) {
                completeRecipe();
            }
            if (this.hasNotEnoughEnergy && getEnergyInputPerSecond() > 19L * recipeEUt) {
                this.hasNotEnoughEnergy = false;
            }
        } else if (recipeEUt > 0) {
            this.hasNotEnoughEnergy = true;
            decreaseProgress();
        }
    }

    public long getLInfoProviderEUt() {
        return recipeEUt;
    }

    public List<OCParameter> getOCList() {
        return Arrays.asList(new OCParameter(getOverclockingDurationDivisor(), getOverclockingVoltageMultiplier(), Integer.MAX_VALUE));
    }

    // for Recipe Async

    public boolean isAllowRecipeAsync() { return false; }

    // distinct

    @Override
    protected void trySearchNewRecipeDistinct() {
        if(!isAllowRecipeAsync()) {
            super.trySearchNewRecipeDistinct();
            return;
        }
        long maxEUt = Math.max(getEnergyContainer().getInputVoltage(), getEnergyContainer().getOutputVoltage());

        List<IItemHandlerModifiable> importInventory = getInputBuses();
        IMultipleTankHandler importFluids = getInputTank();

        Map<Recipe, IItemHandlerModifiable> recipesTodo = new HashMap<>();

        ArrayList<FluidStack> fakeFluids = new ArrayList<>();

        for(IFluidTank tank : importFluids)
            if(tank.getFluid() != null)
                fakeFluids.add(tank.getFluid().copy());

        long currentEUt = 0;
        int currentParallel = 0, maxParallel = getParallelLimit();

        // On a cache miss, our efficiency is much worse, as it will check
        // each bus individually instead of the combined inventory all at once.
        for (int i = 0; i < importInventory.size(); i++) {
            IItemHandlerModifiable bus = importInventory.get(i);
            // Skip this bus if no recipe was found last time
            if (invalidatedInputList.contains(bus)) {
                continue;
            }

            ArrayList<ItemStack> fakeItems = new ArrayList<>();
            for(int j = 0; j < bus.getSlots(); j++)
                if(bus.getStackInSlot(j) != ItemStack.EMPTY)
                    fakeItems.add(bus.getStackInSlot(j).copy());

            // Look for a new recipe after a cache miss
            while(true) {
                long finalCurrentEUt = currentEUt;
                Recipe nxt = getRecipeMap().find(fakeItems, fakeFluids, recipe ->
                        0 < getEUt(recipe)
                            && getEUt(recipe) < maxEUt - finalCurrentEUt
                            && checkRecipe(recipe)
                            && recipe.matches(false, fakeItems, fakeFluids));
                if(nxt == null) break;
                long realEUt = getEUt(nxt);
                int parallel = (int) Math.min(Integer.MAX_VALUE, (maxEUt - currentEUt) / realEUt);
                parallel = Math.min(getMaxRecipeMultiplier(nxt, fakeItems, fakeFluids), parallel);
                parallel = Math.min(parallel, maxParallel - currentParallel);
                nxt = getRecipeMap().recipeBuilder().append(nxt, parallel, false).EUt(1).build().getResult();
                if(!nxt.matches(true, fakeItems, fakeFluids)) return; // how?
                fakeItems.removeIf(Objects::isNull);
                fakeFluids.removeIf(Objects::isNull);
                currentEUt += realEUt * parallel;
                currentParallel += parallel;
                recipesTodo.put(nxt, bus);
                if(currentParallel == maxParallel) {
                    break;
                }
            }
        }

        if(!recipesTodo.isEmpty()) {
            prepareRecipeDistinct(recipesTodo, currentEUt);
        }
    }

    @Override
    protected void trySearchNewRecipeCombined() {
        if(!isAllowRecipeAsync()) {
            super.trySearchNewRecipeCombined();
            return;
        }
        long maxEUt = getInputEUt();

        IItemHandlerModifiable importInventory = getInputInventory();
        IMultipleTankHandler importFluids = getInputTank();

        RecipeBuilder<?> resultBuilder = getRecipeMap().recipeBuilder();

        ArrayList<ItemStack> fakeItems = new ArrayList<>();
        ArrayList<FluidStack> fakeFluids = new ArrayList<>();
        for(int i = 0; i < importInventory.getSlots(); i++)
            if(importInventory.getStackInSlot(i) != ItemStack.EMPTY)
                fakeItems.add(importInventory.getStackInSlot(i).copy());
        for(IFluidTank tank : importFluids)
            if(tank.getFluid() != null)
                fakeFluids.add(tank.getFluid().copy());

        long currentEUt = 0;
        int currentParallel = 0, maxParallel = getParallelLimit();

            // Look for a new recipe after a cache miss
        while(true) {
            long finalCurrentEUt = currentEUt;
            Recipe nxt = getRecipeMap().find(fakeItems, fakeFluids, recipe ->
                    0 < getEUt(recipe)
                            && getEUt(recipe) < maxEUt - finalCurrentEUt
                            && checkRecipe(recipe)
                            && recipe.matches(false, fakeItems, fakeFluids));
            if(nxt == null) break;
            long realEUt = getEUt(nxt);
            int parallel = (int) Math.min(Integer.MAX_VALUE, (maxEUt - currentEUt) / realEUt);
            parallel = Math.min(getMaxRecipeMultiplier(nxt, fakeItems, fakeFluids), parallel);
            parallel = Math.min(parallel, maxParallel - currentParallel);
            nxt = getRecipeMap().recipeBuilder().append(nxt, parallel, false).EUt(1).build().getResult();
            if(!nxt.matches(true, fakeItems, fakeFluids)) return; // how?
            fakeItems.removeIf(Objects::isNull);
            fakeFluids.removeIf(Objects::isNull);

            currentEUt += realEUt * parallel;
            currentParallel += parallel;
            resultBuilder.append(nxt, 1, false);
            if(currentParallel == maxParallel) {
                break;
            }
        }

        if(currentParallel > 0) {
            prepareRecipeCombined(resultBuilder.EUt(1).duration(64).build().getResult(), currentEUt);
        }
    }

    private long getEUt(Recipe recipe) {
        setMaxProgress(recipe.getDuration());
        double duration = maxProgressTime;
        if(duration <= 64) return recipe.getEUt();
        BigDecimal eut = BigDecimal.valueOf(recipe.getEUt());
        if(getOverclockingDurationDivisor() < getOverclockingVoltageMultiplier()) {
            // not perfect oc => continuum perfect oc
            eut = eut.multiply(BigDecimal.valueOf(duration / 64D));
        } else {
            // perfect oc => continuum 4/6 oc
            // 82.7188 = 64 / log_{6}4
            eut = eut.multiply(BigDecimal.valueOf(duration / 82.7188D));
        }
        return eut.min(BigDecimal.valueOf(16L * Integer.MAX_VALUE)).longValue();
    }

    private boolean prepareRecipeDistinct(Map<Recipe, IItemHandlerModifiable> recipesTodo, long recipeEUt) {
        if(isAllowRecipeAsync()) {
            RecipeBuilder<?> builder = getRecipeMap().recipeBuilder();
            recipesTodo.forEach((recipe, trash) -> builder.append(recipe, 1, false));
            Recipe recipe = builder.EUt(1).duration(64).build().getResult();

            // plz do not parallel more

            if (distinctSetupAndConsumeRecipeInputs(recipe, recipesTodo, getInputTank(), recipeEUt)) {
                LSetupRecipe(recipe, recipeEUt, 64);
                return true;
            }
        } else {
            // we assume recipesTodo here is size of one
            Recipe recipe = recipesTodo.keySet().stream().findAny().get();
            IItemHandlerModifiable itemInventory = recipesTodo.get(recipe);
            recipe = Recipe.trimRecipeOutputs(recipe, getRecipeMap(), metaTileEntity.getItemOutputLimit(),
                    metaTileEntity.getFluidOutputLimit());

            recipe = find1tocParallelRecipe(
                    recipe,
                    currentDistinctInputBus,
                    getInputTank(),
                    getOutputInventory(),
                    getOutputTank(),
                    getMaxParallelVoltage(),
                    getParallelLimit());

            if (recipe != null && setupAndConsumeRecipeInputs(recipe, itemInventory)) {
                setupRecipe(recipe);
                return true;
            }

            return false;
        }
        return false;
    }

    private boolean prepareRecipeCombined(Recipe recipe, long recipeEUt) {
        // plz do not parallel more

        if (combinedSetupAndConsumeRecipeInputs(recipe, recipeEUt)) {
            LSetupRecipe(recipe, recipeEUt, 64);
            return true;
        }

        return false;
    }

    private boolean combinedSetupAndConsumeRecipeInputs(Recipe recipe, long recipeEUt) {
        // ty, but I do oc myself

        if (getEnergyStored() < recipeEUt * 8) {
            return false;
        }

        return super.setupAndConsumeRecipeInputs(recipe, getInputInventory(), getInputTank());
    }

    protected boolean distinctSetupAndConsumeRecipeInputs(Recipe recipe, Map<Recipe, IItemHandlerModifiable> recipesTodo,
                                                          IMultipleTankHandler importFluids, long recipeEUt) {
        // ty, but I do oc myself

        if (getEnergyStored() < recipeEUt * 8) {
            return false;
        }

        IItemHandlerModifiable exportInventory = getOutputInventory();
        IMultipleTankHandler exportFluids = getOutputTank();

        // We have already trimmed outputs and chanced outputs at this time
        // Attempt to merge all outputs + chanced outputs into the output bus, to prevent voiding chanced outputs
        if (!metaTileEntity.canVoidRecipeItemOutputs() &&
                !GTTransferUtils.addItemsToItemHandler(exportInventory, true, recipe.getAllItemOutputs())) {
            this.isOutputsFull = true;
            return false;
        }

        // We have already trimmed fluid outputs at this time (really?
        if (!metaTileEntity.canVoidRecipeFluidOutputs() &&
                !GTTransferUtils.addFluidsToFluidHandler(exportFluids, true, recipe.getAllFluidOutputs())) {
            this.isOutputsFull = true;
            return false;
        }

        this.isOutputsFull = false;

        AtomicBoolean somethingbad = new AtomicBoolean(false);
        recipesTodo.forEach((single, handler) -> {
            if(!single.matches(true, handler, importFluids)) {
                somethingbad.set(true);
                metaTileEntity.addNotifiedInput(single);
            }
        });

        return !somethingbad.get();
    }


    protected void LSetupRecipe(Recipe recipe, long recipeEUt, long duration) {
        this.progressTime = 1;

        this.recipeEUt = recipeEUt;

        if(!isAllowRecipeAsync()) {
            OCResult result = makeOC(recipeEUt, (double) duration);
            setMaxProgress((int) Math.max(1, result.duration));
            this.recipeEUt = result.eut;
        } else {
            this.maxProgressTime = Math.max(1, (int) (double) duration);
        }


        int recipeTier = GTUtility.getTierByVoltage(recipe.getEUt());
        int machineTier = getOverclockForTier(getMaximumOverclockVoltage());
        this.fluidOutputs = GTUtility
                .copyFluidList(recipe.getResultFluidOutputs(recipeTier, machineTier, getRecipeMap()));
        this.itemOutputs = GTUtility
                .copyStackList(recipe.getResultItemOutputs(recipeTier, machineTier, getRecipeMap()));

        if (this.wasActiveAndNeedsUpdate) {
            this.wasActiveAndNeedsUpdate = false;
        } else {
            this.setActive(true);
        }
    }


    // utils

    private static int getMaxRecipeMultiplier(Recipe recipe, List<ItemStack> inputs, List<FluidStack> fluidInputs) {
        // Find all the items in the combined Item Input inventories and create oversized ItemStacks
        Object2IntMap<ItemStack> ingredientStacks = fromItemHandler(inputs);

        // Find all the fluids in the combined Fluid Input inventories and create oversized FluidStacks
        Map<FluidKey, Integer> fluidStacks = fromFluidHandler(fluidInputs);

        // Find the maximum number of recipes that can be performed from the items in the item input inventories
        int itemMultiplier = getMaxRatioItem(ingredientStacks, recipe);
        // Find the maximum number of recipes that can be performed from the fluids in the fluid input inventories
        int fluidMultiplier = getMaxRatioFluid(fluidStacks, recipe);

        if (itemMultiplier == Integer.MAX_VALUE && fluidMultiplier == Integer.MAX_VALUE) {
            return 0;
        }

        // Find the maximum number of recipes that can be performed from all available inputs
        return Math.min(itemMultiplier, fluidMultiplier);
    }

    private static Object2IntMap<ItemStack> fromItemHandler(List<ItemStack> inputs) {
        final Object2IntMap<ItemStack> map = new Object2IntOpenCustomHashMap<>(ItemStackHashStrategy.comparingAllButCount());

        for(ItemStack is : inputs)
            if(!is.isEmpty())
                map.put(is.copy(), map.getInt(is) + is.getCount());

        return map;
    }

    private static Map<FluidKey, Integer> fromFluidHandler(List<FluidStack> fluidInputs) {
        final Object2IntMap<FluidKey> map = new Object2IntLinkedOpenHashMap<>();

        for (FluidStack fluidStack : fluidInputs) {
            if (fluidStack != null && fluidStack.amount > 0) {
                FluidKey key = new FluidKey(fluidStack);
                map.put(key, map.getInt(key) + fluidStack.amount);
            }
        }

        return map;
    }

    protected static int getMaxRatioItem(Object2IntMap<ItemStack> countIngredients,Recipe recipe) {
        int minMultiplier = Integer.MAX_VALUE;
        // map the recipe ingredients to account for duplicated and notConsumable ingredients.
        // notConsumable ingredients are not counted towards the max ratio
        Object2IntOpenHashMap<GTRecipeInput> notConsumableMap = new Object2IntOpenHashMap<>();
        Object2IntOpenHashMap<GTRecipeInput> countableMap = new Object2IntOpenHashMap<>();
        for (GTRecipeInput recipeIngredient : recipe.getInputs()) {
            int ingredientCount = recipeIngredient.getAmount();
            if (recipeIngredient.isNonConsumable()) {
                notConsumableMap.computeIfPresent(recipeIngredient, (k, v) -> v + ingredientCount);
                notConsumableMap.putIfAbsent(recipeIngredient, ingredientCount);
            } else {
                countableMap.computeIfPresent(recipeIngredient, (k, v) -> v + ingredientCount);
                countableMap.putIfAbsent(recipeIngredient, ingredientCount);
            }
        }

        // Iterate through the recipe inputs, excluding the not consumable ingredients from the inventory map
        for (Object2IntMap.Entry<GTRecipeInput> recipeInputEntry : notConsumableMap.object2IntEntrySet()) {
            int needed = recipeInputEntry.getIntValue();
            int available = 0;
            // For every stack in the ingredients gathered from the input bus.
            for (Object2IntMap.Entry<ItemStack> inventoryEntry : countIngredients.object2IntEntrySet()) {
                if (recipeInputEntry.getKey().acceptsStack(inventoryEntry.getKey())) {
                    available = inventoryEntry.getIntValue();
                    if (available > needed) {
                        inventoryEntry.setValue(available - needed);
                        needed -= available;
                        break;
                    } else {
                        inventoryEntry.setValue(0);
                        recipeInputEntry.setValue(needed - available);
                        needed -= available;
                    }
                }
            }
            // We need to check >= available here because of Non-Consumable inputs with stack size. If there is a NC
            // input
            // with size 2, and only 1 in the input, needed will be equal to available, but this situation should still
            // fail
            // as not all inputs are present
            if (needed >= available) {
                return 0;
            }
        }

        // Return the maximum parallel limit here if there are only non-consumed inputs, which are all found in the
        // input bus
        // At this point, we would have already returned 0 if we were missing any non-consumable inputs, so we can omit
        // that check
        if (countableMap.isEmpty() && !notConsumableMap.isEmpty()) {
            return Integer.MAX_VALUE;
        }

        // Iterate through the recipe inputs
        for (Object2IntMap.Entry<GTRecipeInput> recipeInputEntry : countableMap.object2IntEntrySet()) {
            int needed = recipeInputEntry.getIntValue();
            int available = 0;
            // For every stack in the ingredients gathered from the input bus.
            for (Object2IntMap.Entry<ItemStack> inventoryEntry : countIngredients.object2IntEntrySet()) {
                if (recipeInputEntry.getKey().acceptsStack(inventoryEntry.getKey())) {
                    available += inventoryEntry.getIntValue();
                }
            }
            if (available >= needed) {
                int ratio = available / needed;
                if (ratio < minMultiplier) {
                    minMultiplier = ratio;
                }
            } else {
                return 0;
            }
        }
        return minMultiplier;
    }

    protected static int getMaxRatioFluid(Map<FluidKey, Integer> countFluid,Recipe recipe) {
        int minMultiplier = Integer.MAX_VALUE;
        // map the recipe input fluids to account for duplicated fluids,
        // so their sum is counted against the total of fluids available in the input
        Map<FluidKey, Integer> fluidCountMap = new HashMap<>();
        Map<FluidKey, Integer> notConsumableMap = new HashMap<>();
        for (GTRecipeInput fluidInput : recipe.getFluidInputs()) {
            int fluidAmount = fluidInput.getAmount();
            if (fluidInput.isNonConsumable()) {
                notConsumableMap.computeIfPresent(new FluidKey(fluidInput.getInputFluidStack()),
                        (k, v) -> v + fluidAmount);
                notConsumableMap.putIfAbsent(new FluidKey(fluidInput.getInputFluidStack()), fluidAmount);
            } else {
                fluidCountMap.computeIfPresent(new FluidKey(fluidInput.getInputFluidStack()),
                        (k, v) -> v + fluidAmount);
                fluidCountMap.putIfAbsent(new FluidKey(fluidInput.getInputFluidStack()), fluidAmount);
            }
        }

        // Iterate through the recipe inputs, excluding the not consumable fluids from the fluid inventory map
        for (Map.Entry<FluidKey, Integer> notConsumableFluid : notConsumableMap.entrySet()) {
            int needed = notConsumableFluid.getValue();
            int available = 0;
            // For every fluid gathered from the fluid inputs.
            for (Map.Entry<FluidKey, Integer> inputFluid : countFluid.entrySet()) {
                // Strip the Non-consumable tags here, as FluidKey compares the tags, which causes finding matching
                // fluids
                // in the input tanks to fail, because there is nothing in those hatches with a non-consumable tag
                if (notConsumableFluid.getKey().equals(inputFluid.getKey())) {
                    available = inputFluid.getValue();
                    if (available > needed) {
                        inputFluid.setValue(available - needed);
                        needed -= available;
                        break;
                    } else {
                        inputFluid.setValue(0);
                        notConsumableFluid.setValue(needed - available);
                        needed -= available;
                    }
                }
            }
            // We need to check >= available here because of Non-Consumable inputs with stack size. If there is a NC
            // input
            // with size 1000, and only 500 in the input, needed will be equal to available, but this situation should
            // still fail
            // as not all inputs are present
            if (needed >= available) {
                return 0;
            }
        }

        // Return the maximum parallel limit here if there are only non-consumed inputs, which are all found in the
        // input bus
        // At this point, we would have already returned 0 if we were missing any non-consumable inputs, so we can omit
        // that check
        if (fluidCountMap.isEmpty() && !notConsumableMap.isEmpty()) {
            return Integer.MAX_VALUE;
        }

        // Iterate through the fluid inputs in the recipe
        for (Map.Entry<FluidKey, Integer> fs : fluidCountMap.entrySet()) {
            int needed = fs.getValue();
            int available = 0;
            // For every fluid gathered from the fluid inputs.
            for (Map.Entry<FluidKey, Integer> inputFluid : countFluid.entrySet()) {
                if (fs.getKey().equals(inputFluid.getKey())) {
                    available += inputFluid.getValue();
                }
            }
            if (available >= needed) {
                int ratio = available / needed;
                if (ratio < minMultiplier) {
                    minMultiplier = ratio;
                }
            } else {
                return 0;
            }
        }
        return minMultiplier;
    }

    public static int getOverMAXV(long eut) {
        // 8 = ULV(0)
        long cur = 8;
        int res = 0;
        while(cur * 4 <= eut) {
            cur *= 4;
            ++res;
        }
        return res;
    }

    public long getInputEUt() {
        return Math.max(getEnergyContainer().getInputVoltage(), getEnergyContainer().getOutputVoltage());
    }
}
