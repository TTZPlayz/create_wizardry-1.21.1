package net.ttzplayz.create_wizardry.fluids;

import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.particle.ZapParticleOption;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.SoundAction;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.resources.ResourceLocation;
import net.ttzplayz.create_wizardry.CreateWizardry;
import net.ttzplayz.create_wizardry.item.CWItems;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CWFluidRegistry {

    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Registries.FLUID, CreateWizardry.MOD_ID);
    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, CreateWizardry.MOD_ID);

    // TEXTURES
    public static final ResourceLocation LIGHTNING_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreateWizardry.MOD_ID, "block/lightning");
    public static final ResourceLocation LIGHTNING_TEXTURE_FLOWING = ResourceLocation.fromNamespaceAndPath(CreateWizardry.MOD_ID, "block/lightning_flow");
    public static final ResourceLocation MANA_TEXTURE = ResourceLocation.fromNamespaceAndPath(CreateWizardry.MOD_ID, "block/mana");
    public static final ResourceLocation MANA_TEXTURE_FLOWING = ResourceLocation.fromNamespaceAndPath(CreateWizardry.MOD_ID, "block/mana_flow");

    public static final DeferredHolder<FluidType, FluidType> MANA_TYPE =
            FLUID_TYPES.register("mana_type", () ->
                    new MagicFluidType(FluidType.Properties.create()
                            .viscosity(200)
                            .temperature(3000)
                            .lightLevel(15), MANA_TEXTURE, MANA_TEXTURE){
                        @Override
                        public void onVaporize(@Nullable Player player, Level level, BlockPos pos, FluidStack stack) {
                            level.playSound(player, pos, SoundRegistry.EVOCATION_CAST.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
                            if (level instanceof ServerLevel) {
                                AABB area = new AABB(pos).inflate(1.5, 1.5, 1.5);
                                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAffectedByPotions);
                                for(LivingEntity entity : entities) {
                                    if (entity instanceof AbstractSpellCastingMob) {
                                        ((AbstractSpellCastingMob) entity).getMagicData().addMana(50);
                                    } else {
                                        entity.addEffect(new MobEffectInstance(MobEffectRegistry.INSTANT_MANA, 1, 50, true, false));
                                    }
                                }
                            }
                        }
                    }); //TODO: MAKE TEXTURE
    public static final DeferredHolder<Fluid, FlowingFluid> MANA =
            FLUIDS.register("mana", () -> new BaseFlowingFluid.Source(CWFluidRegistry.MANA_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> MANA_FLOWING =
            FLUIDS.register("mana_flowing", () -> new BaseFlowingFluid.Flowing(CWFluidRegistry.MANA_PROPERTIES));

    private static final BaseFlowingFluid.Properties MANA_PROPERTIES =
            new BaseFlowingFluid.Properties(
                    MANA_TYPE,
                    MANA,
                    MANA_FLOWING)
                    .explosionResistance(100f)
                    .bucket(CWItems.MANA_BUCKET)
                    .levelDecreasePerBlock(1)
                    .tickRate(20);
    // LIGHTNING
    public static final DeferredHolder<FluidType, FluidType> LIGHTNING_TYPE =
            FLUID_TYPES.register("lightning_type", () ->
                    new MagicFluidType(FluidType.Properties.create()
                            .viscosity(200)
                            .temperature(3000)
                            .pathType(PathType.BLOCKED)
                            .canConvertToSource(false)
                            .density(-1000)
//                            .sound()
                            .lightLevel(15), LIGHTNING_TEXTURE, LIGHTNING_TEXTURE)
                    {
                        @Override
                        public void onVaporize(@Nullable Player player, Level level, BlockPos pos, FluidStack stack) {
                            level.playSound(player, pos, SoundRegistry.EVOCATION_CAST.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
                            if (level instanceof ServerLevel) {
                                AABB area = new AABB(pos).inflate(1.5, 1.5, 1.5);
                                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
                                List<Creeper> creepers = level.getEntitiesOfClass(Creeper.class, area, c -> c != null && c.isAlive() && !c.isPowered());
                                int creeperCount = creepers.size();
                                if (creeperCount > 0) {

                                }
                                for (int i = 0; i < 5; i++) {
                                    Vec3 start = pos.getBottomCenter();
                                    Vec3 dest = start.add(Utils.getRandomVec3(1).multiply(1.1, 2.5, 1.1).add(0, 2, 0));
                                    ((ServerLevel) level).sendParticles(new ZapParticleOption(dest), start.x, start.y, start.z, 1, 0, 0, 0, 0);
                                    MagicManager.spawnParticles(level, ParticleHelper.ELECTRICITY, area.minX, area.minY, area.minZ, 3, area.maxX, area.maxY, area.maxZ, 0.1, false);
                                }
                                for(LivingEntity entity : entities) {
                                    entity.hurt((level.damageSources().magic()), 6.0F);
                                    MagicManager.spawnParticles(level, ParticleHelper.ELECTRICITY, entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 3, entity.getBbWidth() / 3, entity.getBbHeight() / 3, entity.getBbWidth() / 3, 0.1, false);
                                }
                                for(Creeper creeper : creepers) {
                                    if (Math.random() <= 0.30) {
                                        var dummyLightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                                        dummyLightningBolt.setDamage(0);
                                        dummyLightningBolt.setVisualOnly(true);
                                        creeper.thunderHit((ServerLevel) level, dummyLightningBolt);
                                    }
                                }
                            }
                        }
                    });
    public static final DeferredHolder<Fluid, FlowingFluid> LIGHTNING =
            FLUIDS.register("lightning", () -> new BaseFlowingFluid.Source(CWFluidRegistry.LIGHTNING_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> LIGHTNING_FLOWING =
            FLUIDS.register("lightning_flowing", () -> new BaseFlowingFluid.Flowing(CWFluidRegistry.LIGHTNING_PROPERTIES));

    private static final BaseFlowingFluid.Properties LIGHTNING_PROPERTIES =
            new BaseFlowingFluid.Properties(
                    LIGHTNING_TYPE,
                    LIGHTNING,
                    LIGHTNING_FLOWING)
                    .explosionResistance(100f)
                    .bucket(CWItems.LIGHTNING_BUCKET)
                    .levelDecreasePerBlock(1)
                    .tickRate(20);

//    //SIMPLE FLUIDS
    public static final DeferredHolder<FluidType, FluidType> FIRE_ALE_TYPE = FLUID_TYPES.register("fire_ale_type", () ->
            new FluidType(FluidType.Properties.create()));
    public static final DeferredHolder<FluidType, FluidType> NETHERWARD_TINCTURE_TYPE = FLUID_TYPES.register("netherward_tincture_type", () ->
            new FluidType(FluidType.Properties.create()));

    public static final DeferredHolder<Fluid, FlowingFluid> FIRE_ALE_FLUID =
            FLUIDS.register("fire_ale", () -> new BaseFlowingFluid.Source(CWFluidRegistry.FIRE_ALE_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> FIRE_ALE_FLUID_FLOWING =
            FLUIDS.register("fire_ale_flowing", () -> new BaseFlowingFluid.Flowing(CWFluidRegistry.FIRE_ALE_PROPERTIES));
    private static final BaseFlowingFluid.Properties FIRE_ALE_PROPERTIES =
            new BaseFlowingFluid.Properties(
                    FIRE_ALE_TYPE,
                    FIRE_ALE_FLUID,
                    FIRE_ALE_FLUID_FLOWING);

    public static final DeferredHolder<Fluid, FlowingFluid> NETHERWARD_TINCTURE_FLUID =
            FLUIDS.register("netherward_tincture", () -> new BaseFlowingFluid.Source(CWFluidRegistry.NETHERWARD_TINCTURE_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> NETHERWARD_TINCTURE_FLUID_FLOWING =
            FLUIDS.register("netherward_tincture_flowing", () -> new BaseFlowingFluid.Flowing(CWFluidRegistry.NETHERWARD_TINCTURE_PROPERTIES));
    private static final BaseFlowingFluid.Properties NETHERWARD_TINCTURE_PROPERTIES =
            new BaseFlowingFluid.Properties(
                    NETHERWARD_TINCTURE_TYPE,
                    NETHERWARD_TINCTURE_FLUID,
                    NETHERWARD_TINCTURE_FLUID_FLOWING);



    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
    }
}
