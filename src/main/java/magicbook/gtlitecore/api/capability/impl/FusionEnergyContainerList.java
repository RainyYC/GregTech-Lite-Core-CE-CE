
package magicbook.gtlitecore.api.capability.impl;

import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.EnergyContainerList;

import net.minecraft.util.EnumFacing;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class FusionEnergyContainerList extends EnergyContainerList {

    private long limitPerHatch = 0;

    public FusionEnergyContainerList(@NotNull List<IEnergyContainer> energyContainerList, long limitPerHatch) {
        super(energyContainerList);
        this.limitPerHatch = limitPerHatch;
        long totalInputVoltage = 0;
        long totalOutputVoltage = 0;
        for (IEnergyContainer container : energyContainerList) {
            totalInputVoltage += Math.min(container.getInputVoltage() * container.getInputAmperage(), limitPerHatch);
            totalOutputVoltage += Math.min(container.getOutputVoltage() * container.getOutputAmperage(), limitPerHatch);
        }
        // Compact Fusion Reactor use individual OC logics, hence it ignores input amperage, 
        // and uses total input power as voltage.
        // We use reflect to override power parameters for fusion. 

        List<Pair<String, Long>> overrides = Arrays.asList(
            Pair.of("inputVoltage", totalInputVoltage), 
            Pair.of("inputAmperage", 1L), 
            Pair.of("outputVoltage", totalOutputVoltage),
            Pair.of("outputAmperage", 1L)
        );

        // Begin unsafe - use reflection to access parent class 
        overrides.forEach(
            overrideRule -> {
                Field targetField = null;
                try {
                    targetField = EnergyContainerList.class.getDeclaredField(overrideRule.getLeft());
                } catch (NoSuchFieldException ignored) {}
                try {
                    targetField.setAccessible(true);
                    targetField.set(this, (long) overrideRule.getRight());
                } catch (IllegalAccessException ignored) {}
            }
        );
        // End unsafe 
    }

    private List<IEnergyContainer> getEnergyContainerList() {
        // Begin unsafe - use reflection to access parent class 
        Field targetField = null;
        try {
            targetField = EnergyContainerList.class.getDeclaredField("energyContainerList");
        } catch (NoSuchFieldException ignored) {}
        try {
            targetField.setAccessible(true);
            return (List<IEnergyContainer>) targetField.get(this);
        } catch (IllegalAccessException ignored) {}
        // End unsafe 
        return null;
    }
    
    @Override
    public long getEnergyStored() {
        long energyStored = 0L;
        List<IEnergyContainer> energyContainerList = getEnergyContainerList();
        if (energyContainerList == null) return 0;

        for (IEnergyContainer iEnergyContainer : energyContainerList) {
            energyStored += Math.min(iEnergyContainer.getEnergyStored(), this.limitPerHatch);
        }
        return energyStored;
    }

    @Override
    public long getEnergyCapacity() {
        long energyCapacity = 0L;
        List<IEnergyContainer> energyContainerList = getEnergyContainerList();
        if (energyContainerList == null) return 0;

        for (IEnergyContainer iEnergyContainer : energyContainerList) {
            energyCapacity += Math.min(iEnergyContainer.getEnergyCapacity(), this.limitPerHatch);
        }
        return energyCapacity;
    }

    @Override
    public long changeEnergy(long energyToAdd) {
        long energyAdded = 0L;
        List<IEnergyContainer> energyContainerList = getEnergyContainerList();
        if (energyContainerList == null) return 0;

        for (IEnergyContainer iEnergyContainer : energyContainerList) {
            long flux = Math.max(Math.min(energyToAdd - energyAdded, this.limitPerHatch), -this.limitPerHatch);
            energyAdded += iEnergyContainer.changeEnergy(flux);
            if (energyAdded == energyToAdd) {
                return energyAdded;
            }
        }
        return energyAdded;
    }
    
}
