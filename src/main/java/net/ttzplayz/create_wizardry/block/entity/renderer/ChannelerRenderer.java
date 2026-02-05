package net.ttzplayz.create_wizardry.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import net.createmod.catnip.platform.ForgeCatnipServices;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
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

    private void renderFluid(ChannelerBlockEntity channeler, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        SmartFluidTankBehaviour tank = channeler.internalTank;
        if (tank == null)
            return;

        SmartFluidTankBehaviour.TankSegment primaryTank = tank.getPrimaryTank();
        FluidStack fluidStack = primaryTank.getRenderedFluid();
        float level = primaryTank.getFluidLevel().getValue(partialTicks);

        if (!fluidStack.isEmpty() && level != 0) {
            float min = 1f / 16f;
            float max = min + (15 / 16f);
            float minY = 1f / 16f;
            level *= (3 / 16f);
            ForgeCatnipServices.FLUID_RENDERER.renderFluidBox(
                    fluidStack,
                    min, minY, min,
                    max, minY + level, max,
                    buffer, poseStack, light,
                    false, false);
        }
    }
}
