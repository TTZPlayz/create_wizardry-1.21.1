package net.ttzplayz.create_wizardry.fluids;

import com.simibubi.create.api.effect.OpenPipeEffectHandler;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.particle.ZapParticleOption;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidStack;
import net.ttzplayz.create_wizardry.advancement.CWAdvancements;

import java.util.List;

public class CWEffectHandlers {

    public static class ManaEffectHandler implements OpenPipeEffectHandler {
        @Override
        public void apply(Level level, AABB area, FluidStack fluid) {
            if (level.getGameTime() % 5L == 0L) {
                MagicManager.spawnParticles(level, ParticleTypes.GLOW_SQUID_INK, area.getCenter().x, area.getCenter().y, area.getCenter().z, 10, 0.2, 0.2, 0.2, 0.3, false);
                level.playLocalSound(
                        area.getCenter().x, area.getCenter().y, area.getCenter().z,
                        SoundRegistry.EVOCATION_CAST.get(),
                        SoundSource.BLOCKS,
                        10000.0F,
                        0.8F,
                        false
                );
                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAffectedByPotions);
                for(LivingEntity entity : entities) {
                    MagicManager.spawnParticles(level, ParticleTypes.GLOW_SQUID_INK, entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 10, entity.getBbWidth() / 3, entity.getBbHeight() / 3, entity.getBbWidth() / 3, 0.1, false);
                    level.playSound(entity, entity.getOnPos(), SoundRegistry.EVOCATION_CAST.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
                    if (entity instanceof AbstractSpellCastingMob) {
                        ((AbstractSpellCastingMob) entity).getMagicData().addMana(5);
                    } else {
                        entity.addEffect(new MobEffectInstance(MobEffectRegistry.INSTANT_MANA.get(), 1, 3, true, false));
                    }
                }
            }
        }
    }
    public static class LightningEffectHandler implements OpenPipeEffectHandler {

        @Override
        public void apply(Level level, AABB area, FluidStack fluid) {
            if (level.getGameTime() % 5L == 0L) {

                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
                if (entities.isEmpty()) {
                    MagicManager.spawnParticles(level, ParticleHelper.ELECTRICITY, area.getCenter().x, area.getCenter().y, area.getCenter().z, 10, 0.2, 0.2, 0.2, 0.3, false);
                    level.playLocalSound(
                            area.getCenter().x, area.getCenter().y, area.getCenter().z,
                            SoundRegistry.LIGHTNING_CAST.get(),
                            SoundSource.BLOCKS,
                            10000.0F,
                            0.8F,
                            false
                    );
                } else {
                    LivingEntity entity = entities.get(level.random.nextInt(entities.size()));
                    entity.hurt((level.damageSources().magic()), 6.0F);
                    Vec3 start = entity.getBoundingBox().getCenter();
                    Vec3 dest = start.add(Utils.getRandomVec3(1).multiply(1.5, 1.5, 1.5).add(1, 1, 1));
                    ((ServerLevel) level).sendParticles(new ZapParticleOption(dest), start.x, start.y, start.z, 3, 0, 0, 0, 0);
                    MagicManager.spawnParticles(level, ParticleHelper.ELECTRICITY, entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 10, entity.getBbWidth() / 3, entity.getBbHeight() / 3, entity.getBbWidth() / 3, 0.1, false);
                    level.playSound(entity, entity.getOnPos(), SoundRegistry.LIGHTNING_CAST.get(), SoundSource.BLOCKS, 0.5F, 1.0F);

                    if (entity instanceof Creeper creeper) {
                        var dummyLightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                        dummyLightningBolt.setDamage(0);
                        dummyLightningBolt.setVisualOnly(true);
                        creeper.thunderHit((ServerLevel) level, dummyLightningBolt);
                    } else if (entity instanceof Player player && !(player instanceof FakePlayer)) {
                        CWAdvancements.SHOCKING.awardTo(player);
                    }
                }
            }
        }
    }
    public static class FireAleEffectHandler implements OpenPipeEffectHandler {
        @Override
        public void apply(Level level, AABB area, FluidStack fluid) {
            if (level.getGameTime() % 5L == 0L) {
                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAffectedByPotions);
                for(LivingEntity entity : entities) {
                    entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0, false, false));
                    entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0,  false, false));
                    entity.playSound(SoundEvents.HONEY_DRINK);
                }
            }
        }
    }
    public static class NetherwardEffectHandler implements OpenPipeEffectHandler {
        @Override
        public void apply(Level level, AABB area, FluidStack fluid) {
            if (level.getGameTime() % 5L == 0L) {
                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAffectedByPotions);
                for(LivingEntity entity : entities) {
                    if (entity instanceof AbstractPiglin piglin) {
                        piglin.setImmuneToZombification(true);
                        piglin.playSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
                    } else if (entity instanceof Hoglin hoglin) {
                        hoglin.setImmuneToZombification(true);
                        hoglin.playSound(SoundEvents.HOGLIN_CONVERTED_TO_ZOMBIFIED);
                    }
                    entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 10 * 20));
                    entity.playSound(SoundEvents.INK_SAC_USE);
                }

            }
        }
    }
    public static class IceVenomEffectHandler implements OpenPipeEffectHandler {
        @Override
        public void apply(Level level, AABB area, FluidStack fluid) {
            if (level.getGameTime() % 5L == 0L) {

                MagicManager.spawnParticles(level, ParticleHelper.ICY_FOG, area.getCenter().x, area.getCenter().y, area.getCenter().z, 3, 0.2, 0.2, 0.2, 0.3, false);
                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAffectedByPotions);
                for(LivingEntity entity : entities) {
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, false));
                    entity.setTicksFrozen(100);
                }
            }
        }
    }
    public static class BloodEffectHandler implements OpenPipeEffectHandler {
        @Override
        public void apply(Level level, AABB area, FluidStack fluidStack) {
            if (level.getGameTime() % 5L == 0L) {
                List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area, LivingEntity::isAffectedByPotions);
                for(LivingEntity entity : entities) {
                    if (entity instanceof Player player && !(player instanceof FakePlayer)) {
                        CWAdvancements.VAMPIRE_SHOWER.awardTo(player);
                    }
                }
            }
        }
    }
}
