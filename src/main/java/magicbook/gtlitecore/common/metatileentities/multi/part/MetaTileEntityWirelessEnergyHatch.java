package magicbook.gtlitecore.common.metatileentities.multi.part;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregtech.api.GTValues;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.gui.ModularUI;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;
import magicbook.gtlitecore.api.capability.GTLiteDataCode;
import magicbook.gtlitecore.api.capability.impl.WirelessEnergyContainerHandler;
import magicbook.gtlitecore.api.misc.WirelessEnergyNetworkWorldSavedData;
import magicbook.gtlitecore.client.renderer.texture.GTLiteTextures;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Wireless Energy Hatch
 *
 * @author Colen, cloud (original authors)
 *
 * <p>
 *     This class creates wireless energy hatch by {@link WirelessEnergyContainerHandler},
 *     integration related energy hatch in {@code GT5u} and {@code TecTech}, and use world saved data
 *     at {@link WirelessEnergyNetworkWorldSavedData}.
 * </p>
 *
 * FIXME Sometime caused crash, but why?
 */
public class MetaTileEntityWirelessEnergyHatch extends MetaTileEntityMultiblockPart implements IMultiblockAbilityPart<IEnergyContainer> {

    private final int amperage;
    private final boolean isExport;
    private final WirelessEnergyContainerHandler energyContainer;
    private UUID uuid = null;
    private final String NBT_TAG = "WirelessEnergyNetworkUUID";
    public static final UUID UNIVERSAL_UUID = new UUID(0, 0);

    public MetaTileEntityWirelessEnergyHatch(ResourceLocation metaTileEntityId,
                                             int tier,
                                             int amperage,
                                             boolean isExport) {
        super(metaTileEntityId, tier);
        this.amperage = amperage;
        this.isExport = isExport;
        if (isExport) {
            this.energyContainer = WirelessEnergyContainerHandler.emitterContainer(this, GTValues.V[tier] * 64L * (long) amperage, GTValues.V[tier], amperage);
        } else {
            this.energyContainer = WirelessEnergyContainerHandler.receiverContainer(this, GTValues.V[tier] * 64L * (long) amperage, GTValues.V[tier], amperage);
        }
        setUUID(UNIVERSAL_UUID);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityWirelessEnergyHatch(metaTileEntityId, this.getTier(), amperage, isExport);
    }

    @Override
    public MultiblockAbility<IEnergyContainer> getAbility() {
        return isExport ? MultiblockAbility.OUTPUT_ENERGY : MultiblockAbility.INPUT_ENERGY;
    }

    @Override
    public void registerAbilities(List<IEnergyContainer> list) {
        list.add(energyContainer);
    }

    @Override
    public void receiveCustomData(int dataId, PacketBuffer buf) {
        super.receiveCustomData(dataId, buf);
        if (dataId == GTLiteDataCode.ChannelWirelessEnergyHatchUUID) {
            this.uuid = buf.readUniqueId();
            this.energyContainer.uuid = buf.readUniqueId();
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        if (this.uuid != null)
            data.setUniqueId(NBT_TAG, uuid);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        if (data.hasKey(NBT_TAG + "Most")) {
            this.uuid = data.getUniqueId(NBT_TAG);
            this.energyContainer.uuid = data.getUniqueId(NBT_TAG);
        }
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return null;
    }

    @Override
    protected boolean openGUIOnRightClick() {
        return false;
    }

    @Override
    public final boolean onScrewdriverClick(EntityPlayer player,
                                            EnumHand hand,
                                            EnumFacing facing,
                                            CuboidRayTraceResult hitResult) {
        if (player.isSneaking()) {
            // setUUID(player.getUniqueID());
            if (player.getEntityWorld().isRemote) {
                player.sendMessage( new TextComponentTranslation("gtlitecore.machine.wireless_energy_hatch.connect"));
            }
        }
        return true;
    }

    /**
     * Set player UUID to machine channel.
     *
     * <p>
     *     Used {@link MetaTileEntity#writeCustomData(int, Consumer)} to write unique id,
     *     and used {@link MetaTileEntity#receiveCustomData(int, PacketBuffer)} to read unique id.
     * </p>
     *
     * @param uuid  UUID of player.
     */
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
        this.energyContainer.uuid = uuid;
        this.writeCustomData(GTLiteDataCode.ChannelWirelessEnergyHatchUUID, (b) -> b.writeUniqueId(this.uuid));
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState,
                                     Matrix4 translation,
                                     IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        if (this.shouldRenderOverlay()) {
            switch (amperage) {
                case (2) -> GTLiteTextures.MULTIPART_WIRELESS_ENERGY.renderSided(this.getFrontFacing(), renderState, translation, pipeline);
                case (4) -> GTLiteTextures.MULTIPART_WIRELESS_ENERGY_4x.renderSided(this.getFrontFacing(), renderState, translation, pipeline);
                case (16) -> GTLiteTextures.MULTIPART_WIRELESS_ENERGY_16x.renderSided(this.getFrontFacing(), renderState, translation, pipeline);
                case (64) -> GTLiteTextures.MULTIPART_WIRELESS_ENERGY_64x.renderSided(this.getFrontFacing(), renderState, translation, pipeline);
                case (256) -> GTLiteTextures.MULTIPART_WIRELESS_ENERGY_256x.renderSided(this.getFrontFacing(), renderState, translation, pipeline);
                case (1024) -> GTLiteTextures.MULTIPART_WIRELESS_ENERGY_1024x.renderSided(this.getFrontFacing(), renderState, translation, pipeline);
                case (4096) -> GTLiteTextures.MULTIPART_WIRELESS_ENERGY_4096x.renderSided(this.getFrontFacing(), renderState, translation, pipeline);
                case (16384) -> GTLiteTextures.MULTIPART_WIRELESS_ENERGY_16384x.renderSided(this.getFrontFacing(), renderState, translation, pipeline);
                case (65536) -> GTLiteTextures.MULTIPART_WIRELESS_ENERGY_65536x.renderSided(this.getFrontFacing(), renderState, translation, pipeline);
                case (262144) -> GTLiteTextures.MULTIPART_WIRELESS_ENERGY_262144x.renderSided(this.getFrontFacing(), renderState, translation, pipeline);
                case (1048576) -> GTLiteTextures.MULTIPART_WIRELESS_ENERGY_1048576x.renderSided(this.getFrontFacing(), renderState, translation, pipeline);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack,
                               World player,
                               @NotNull List<String> tooltip,
                               boolean advanced) {
        String tierName = GTValues.VNF[this.getTier()];
        tooltip.add(I18n.format("gtlitecore.machine.wireless_energy_hatch.tooltip.1"));
        tooltip.add(I18n.format("gtlitecore.machine.wireless_energy_hatch.tooltip.2"));

        if (this.isExport) {
            tooltip.add(I18n.format("gregtech.universal.tooltip.voltage_out", this.energyContainer.getOutputVoltage(), tierName));
            tooltip.add(I18n.format("gregtech.universal.tooltip.amperage_out_till", this.energyContainer.getOutputAmperage()));
        } else {
            tooltip.add(I18n.format("gregtech.universal.tooltip.voltage_in", this.energyContainer.getInputVoltage(), tierName));
            tooltip.add(I18n.format("gregtech.universal.tooltip.amperage_in_till", this.energyContainer.getInputAmperage()));
        }

        tooltip.add(I18n.format("gregtech.universal.tooltip.energy_storage_capacity", this.energyContainer.getEnergyCapacity()));
        tooltip.add(I18n.format("gregtech.universal.enabled"));

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(I18n.format("gtlitecore.machine.wireless_energy_hatch.tooltip.shift"));
        } else {
            tooltip.add(I18n.format("gregtech.tooltip.hold_shift"));
        }
    }

    @Override
    public void addToolUsages(ItemStack stack,
                              @Nullable World world,
                              List<String> tooltip,
                              boolean advanced) {
        tooltip.add(I18n.format("gregtech.tool_action.screwdriver.access_covers"));
        tooltip.add(I18n.format("gregtech.tool_action.wrench.set_facing"));
        super.addToolUsages(stack, world, tooltip, advanced);
    }
}
