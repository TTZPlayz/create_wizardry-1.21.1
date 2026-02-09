package net.ttzplayz.create_wizardry.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import net.createmod.catnip.platform.ForgeCatnipServices;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import net.ttzplayz.create_wizardry.block.entity.ChannelerBlockEntity;

public class ChannelerRenderer extends SmartBlockEntityRenderer<ChannelerBlockEntity> {

    public ChannelerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(ChannelerBlockEntity channeler, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        renderFluid(channeler, partialTicks, poseStack, buffer, light);
        super.renderSafe(channeler, partialTicks, poseStack, buffer, light, overlay);
    }

    protected void renderFluid(ChannelerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {
        SmartFluidTankBehaviour tank = be.internalTank;
        if (tank != null) {
            SmartFluidTankBehaviour.TankSegment primaryTank = tank.getPrimaryTank();
            FluidStack fluidStack = primaryTank.getRenderedFluid();
            float level = primaryTank.getFluidLevel().getValue(partialTicks);
            if (!fluidStack.isEmpty() && level != 0.0F) {
                float yMin = 0.3125F;
                float min = 0.125F;
                float max = min + 0.75F;
                float yOffset = 0.4375F * level;
                ms.pushPose();
                ms.translate(0.0F, yOffset, 0.0F);
                ForgeCatnipServices.FLUID_RENDERER.renderFluidBox(fluidStack, min, yMin - yOffset, min, max, yMin, max, buffer, ms, light, false, false);
                ms.popPose();
            }
        }
    }
}
