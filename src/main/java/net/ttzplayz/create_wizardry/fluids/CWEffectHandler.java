package net.ttzplayz.create_wizardry.fluids;

import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;

public class CWEffectHandler {

    public class ManaEffectHandler implements OpenPipeEffectHandler {
        public void apply(Level level, AABB area, FluidStack fluid) {
            if (level.getGameTime() % 5L == 0L) {
                for(Entity entity : level.getEntities((Entity)null, area, (entityx) -> !entityx.fireImmune())) {
                    entity.igniteForSeconds(3.0F);
                }

            }
        }
    }
}
