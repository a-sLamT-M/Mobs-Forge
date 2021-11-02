package org.lt.conquer.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.lt.conquer.Conquer;
import org.lt.conquer.entities.ai.BreakBlockGoal;
import org.lt.conquer.registy.ModEntities;
import org.lt.conquer.utils.RandomNum;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;
import java.util.function.Predicate;

public class HunterEntity extends AbstractIllager
{
    private static final ResourceLocation LOOT_TABLE = new ResourceLocation(Conquer.MOD_ID, "entities/hunter_entity");

    private static final String TAG_JOHNNY = "Johnny";
    static final Predicate<Difficulty> BREAKING_PREDICATE = (difficulty) ->
    {
        return difficulty == Difficulty.EASY || difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD;
    };
    boolean isJohnny;

    public HunterEntity(EntityType<? extends AbstractIllager> entityType, Level level)
    {
        super(entityType, level);
    }

    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        // this.goalSelector.addGoal(1, new BreakBlockGoal(this, BREAKING_PREDICATE));
        this.goalSelector.addGoal(1, new BreakBlockGoal(this, false, false));
        this.goalSelector.addGoal(2, new HunterOpenDoorGoal(this));
        this.goalSelector.addGoal(3, new Raider.HoldGroundAttackGoal(this, 10.0F));
        this.goalSelector.addGoal(4, new HunterOpenDoorGoal.HunterMeleeAttackGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new HunterOpenDoorGoal.HunterAttackGoal(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    @Override
    public void applyRaidBuffs(int p_37844_, boolean p_37845_)
    {
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

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double) 0.382F)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.MAX_HEALTH, 42.0D)
                .add(Attributes.ATTACK_SPEED, 0.9F)
                .add(Attributes.KNOCKBACK_RESISTANCE, RandomNum.random(0F, 0.8F))
                .add(Attributes.ATTACK_DAMAGE, 3.8D);
    }


    @Override
    protected ResourceLocation getDefaultLootTable()
    {
        return LOOT_TABLE;
    }

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
            public HunterAttackGoal(HunterEntity h)
            {
                super(h, LivingEntity.class, 0, false, true, LivingEntity::attackable);
            }

            public boolean canUse()
            {
                return ((HunterEntity) this.mob).isJohnny && super.canUse();
            }

            public void start()
            {
                super.start();
                this.mob.setNoActionTime(0);
            }
        }

        static class HunterMeleeAttackGoal extends MeleeAttackGoal
        {

            public HunterMeleeAttackGoal(HunterEntity hunterEntity)
            {
                super(hunterEntity, 1.0D, false);
            }

            @ParametersAreNonnullByDefault
            protected double getAttackReachSqr(LivingEntity playerAttackTarget)
            {
                if (this.mob.getVehicle() instanceof Ravager)
                {
                    float f = this.mob.getVehicle().getBbWidth() - 0.1F;
                    return (double) (f * 2.0F * f * 2.0F + playerAttackTarget.getBbWidth());
                }
                else
                {
                    return super.getAttackReachSqr(playerAttackTarget);
                }
            }
        }
    }
}
