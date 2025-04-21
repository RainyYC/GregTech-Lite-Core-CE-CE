package magicbook.gtlitecore.mixin.gregtech;

import gregtech.api.GTValues;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.capability.impl.PrimitiveRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SteamMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.RecipeMapSteamMultiblockController;
import gregtech.api.unification.material.Materials;
import gregtech.api.util.GTUtility;
import gregtech.api.util.TextFormattingUtil;
import gregtech.common.metatileentities.multi.MetaTileEntityLargeBoiler;
import gregtech.integration.theoneprobe.provider.CapabilityInfoProvider;
import gregtech.integration.theoneprobe.provider.RecipeLogicInfoProvider;
import magicbook.gtlitecore.api.capability.impl.OverMaxRecipeLogic;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RecipeLogicInfoProvider.class, remap = false)
public abstract class MixinRecipeLogicInfoProvider extends CapabilityInfoProvider<AbstractRecipeLogic> {

    @Inject(method = "addProbeInfo(Lgregtech/api/capability/impl/AbstractRecipeLogic;Lmcjty/theoneprobe/api/IProbeInfo;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/tileentity/TileEntity;Lmcjty/theoneprobe/api/IProbeHitData;)V", at = @At("HEAD"), cancellable = true)
    protected void onAddProbeInfo(AbstractRecipeLogic capability, IProbeInfo probeInfo,
                                EntityPlayer player, TileEntity tileEntity,
                                IProbeHitData data, CallbackInfo ci) {
        if (capability.isWorking()) {
            if (capability instanceof PrimitiveRecipeLogic) {
                ci.cancel();
                return; // do not show info for primitive machines, as they are supposed to appear powerless
            }
            long EUt = getEUt(capability);
            long absEUt = Math.abs(EUt);
            String text = null;

            if (tileEntity instanceof IGregTechTileEntity) {
                IGregTechTileEntity gtTileEntity = (IGregTechTileEntity) tileEntity;
                MetaTileEntity mte = gtTileEntity.getMetaTileEntity();
                if (mte instanceof SteamMetaTileEntity || mte instanceof MetaTileEntityLargeBoiler ||
                        mte instanceof RecipeMapSteamMultiblockController) {
                    text = TextFormatting.AQUA.toString() + TextFormattingUtil.formatNumbers(absEUt) +
                            TextStyleClass.INFO + " L/t {*" +
                            Materials.Steam.getUnlocalizedName() + "*}";
                }
            }
            if (text == null) {
                // Default behavior, if this TE is not a steam machine (or somehow not instanceof
                // IGregTechTileEntity...)
                text = TextFormatting.RED.toString() + TextFormattingUtil.formatNumbers(absEUt) + TextStyleClass.INFO +
                        " EU/t" + TextFormatting.GREEN +
                        " (" + GTValues.VNF[GTUtility.getTierByVoltage(absEUt)];
                if(absEUt > GTValues.V[GTValues.MAX]) text = text + TextFormatting.BOLD + "+" + TextFormatting.RESET;
                text = text + TextFormatting.GREEN + ")";
            }

            if (EUt == 0) {
                ci.cancel();
                return; // idk what to do for 0 eut
            }

            if (capability.consumesEnergy()) {
                probeInfo.text(TextStyleClass.INFO + "{*gregtech.top.energy_consumption*} " + text);
            } else {
                probeInfo.text(TextStyleClass.INFO + "{*gregtech.top.energy_production*} " + text);
            }
        }
        ci.cancel();
    }

    private static long getEUt(AbstractRecipeLogic logic) {
        if(logic instanceof OverMaxRecipeLogic) return ((OverMaxRecipeLogic) logic).getLInfoProviderEUt();
        return logic.getInfoProviderEUt();
    }
}
