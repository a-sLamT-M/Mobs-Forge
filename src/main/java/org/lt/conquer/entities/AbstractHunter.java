package org.lt.conquer.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.lt.conquer.registy.ModEntities;
import org.lt.conquer.utils.RandomNum;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class AbstractHunter extends AbstractIllager
{
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID =
            SynchedEntityData.defineId(RedHunterEntity.class, EntityDataSerializers.BYTE);

    boolean isJohnny;

    protected AbstractHunter(EntityType<? extends AbstractIllager> p_32105_, Level p_32106_)
    {
        super(p_32105_, p_32106_);
    }

    protected void registerGoals()
    {
        super.registerGoals();
    }

    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    public boolean onClimbable()
    {
        return this.isClimbing();
    }

    public boolean isClimbing()
    {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    @Override
    public void applyRaidBuffs(int p_37844_, boolean p_37845_)
    {
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel)
    {
        return new WallClimberNavigation(this, pLevel);
    }

    protected void customServerAiStep()
    {
        if (!this.isNoAi() && GoalUtils.hasGroundPathNavigation(this))
        {
            boolean flag = ((ServerLevel) this.level).isRaided(this.blockPosition());
            ((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(flag);
        }
        super.customServerAiStep();
    }

    public void tick()
    {
        super.tick();
        if (!this.level.isClientSide)
        {
            this.setClimbing(this.horizontalCollision);
        }

    }


    public void setClimbing(boolean pClimbing)
    {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (pClimbing)
        {
            b0 = (byte)(b0 | 1);
        }
        else
        {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        double attackDamage = RandomNum.random(6, 9);
        double chanceDamage = 0;
        if (RandomNum.random(0.5f))
        {
            attackDamage = 6;
        }
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3823F)
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.MAX_HEALTH, 52.0D)
                .add(Attributes.ATTACK_SPEED, 0.9F)
                .add(Attributes.KNOCKBACK_RESISTANCE, RandomNum.random(0F, 0.8F))
                .add(Attributes.ATTACK_DAMAGE, attackDamage);
    }

    @Override
    protected abstract ResourceLocation getDefaultLootTable();

    public void addAdditionalSaveData(CompoundTag pCompound)
    {
        super.addAdditionalSaveData(pCompound);
        if (this.isJohnny)
        {
            pCompound.putBoolean("Johnny", true);
        }
    }

    public AbstractIllager.IllagerArmPose getArmPose()
    {
        if (this.isAggressive())
        {
            return AbstractIllager.IllagerArmPose.ATTACKING;
        }
        else
        {
            return this.isCelebrating() ? AbstractIllager.IllagerArmPose.CELEBRATING : AbstractIllager.IllagerArmPose.CROSSED;
        }
    }

    public void readAdditionalSaveData(CompoundTag pCompound)
    {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("Johnny", 99))
        {
            this.isJohnny = pCompound.getBoolean("Johnny");
        }
    }

    public SoundEvent getCelebrateSound()
    {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }

    @Nullable
    @ParametersAreNonnullByDefault
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag)
    {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        ((GroundPathNavigation) this.getNavigation()).setCanOpenDoors(true);
        this.populateDefaultEquipmentSlots(pDifficulty);
        this.populateDefaultEquipmentEnchantments(pDifficulty);
        return spawngroupdata;
    }

    @ParametersAreNonnullByDefault
    public boolean isAlliedTo(Entity pEntity)
    {
        if (super.isAlliedTo(pEntity))
        {
            return true;
        }
        else if (pEntity instanceof LivingEntity && ((LivingEntity) pEntity).getMobType() == MobType.ILLAGER)
        {
            return this.getTeam() == null && pEntity.getTeam() == null;
        }
        else
        {
            return false;
        }
    }

    protected SoundEvent getAmbientSound()
    {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.VINDICATOR_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource)
    {
        return SoundEvents.VINDICATOR_HURT;
    }

    protected static class HunterOpenDoorGoal extends OpenDoorGoal
    {
        public HunterOpenDoorGoal(Raider p_32128_)
        {
            super(p_32128_, false);
        }

        static class HunterAttackGoal extends NearestAttackableTargetGoal<LivingEntity>
        {
            public HunterAttackGoal(RedHunterEntity h)
            {
                super(h, LivingEntity.class, 0, false, true, LivingEntity::attackable);
            }

            public boolean canUse()
            {
                return ((RedHunterEntity) this.mob).isJohnny && super.canUse();
            }

            public void start()
            {
                super.start();
                this.mob.setNoActionTime(0);
            }
        }
        static class HunterMeleeAttackGoal extends MeleeAttackGoal
        {
            public HunterMeleeAttackGoal(RedHunterEntity redHunterEntity)
            {
                super(redHunterEntity, 1.0D, false);
            }

            @ParametersAreNonnullByDefault
            protected double getAttackReachSqr(LivingEntity playerAttackTarget)
            {
                if (this.mob.getVehicle() instanceof Ravager)
                {
                    float f = this.mob.getVehicle().getBbWidth() - 0.1F;
                    return f * 2.0F * f * 2.0F + playerAttackTarget.getBbWidth();
                }
                else
                {
                    return super.getAttackReachSqr(playerAttackTarget);
                }
            }
        }
    }
}
