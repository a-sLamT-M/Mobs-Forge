package org.lt.conquer.entities;

import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.lt.conquer.entities.ai.BreakBlockGoal;
import org.lt.conquer.registy.ModEntities;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumSet;
import java.util.function.Predicate;

public class HunterEntity extends AbstractIllager
{
    private static final String TAG_JOHNNY = "Johnny";
    static final Predicate<Difficulty> BREAKING_PREDICATE = (difficulty) ->
    {
        return difficulty == Difficulty.EASY || difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD ;
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
        this.goalSelector.addGoal(1, new HunterBreakBlockGoal(this));
        this.goalSelector.addGoal(2, new OpenDoorGoal(this, false));
        this.goalSelector.addGoal(3, new Raider.HoldGroundAttackGoal(this, 10.0F));
        this.goalSelector.addGoal(4, new HunterMeleeAttackGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Raider.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new HunterAttackGoal(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    protected void customServerAiStep()
    {
        if (!this.isNoAi() && GoalUtils.hasGroundPathNavigation(this))
        {
            boolean flag = ((ServerLevel)this.level).isRaided(this.blockPosition());
            ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(flag);
        }
        super.customServerAiStep();
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.377F)
                .add(Attributes.FOLLOW_RANGE, 43.0D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_KNOCKBACK)
                .add(Attributes.ATTACK_DAMAGE, 4D);
    }

    public Monster getBreedOffSpring(ServerLevel level, Monster parent)
    {
        return ModEntities.HUNTER.get().create(level);
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

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
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
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        this.populateDefaultEquipmentSlots(pDifficulty);
        this.populateDefaultEquipmentEnchantments(pDifficulty);
        return spawngroupdata;
    }

    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty)
    {
        if (this.getCurrentRaid() == null)
        {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        }

    }

    /**
     * Returns whether this Entity is on the same team as the given Entity.
     */
    @ParametersAreNonnullByDefault
    public boolean isAlliedTo(Entity pEntity) 
    {
        if (super.isAlliedTo(pEntity)) 
        {
            return true;
        } 
        else if (pEntity instanceof LivingEntity && ((LivingEntity)pEntity).getMobType() == MobType.ILLAGER) 
        {
            return this.getTeam() == null && pEntity.getTeam() == null;
        } 
        else 
        {
            return false;
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.VINDICATOR_HURT;
    }

    public void applyRaidBuffs(int p_34079_, boolean p_34080_)
    {
        // DO NOT RAID
    }

    static class HunterBreakBlockGoal extends BreakBlockGoal
    {
        public HunterBreakBlockGoal(Mob mob)
        {
            super(mob, false,false);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }
        public boolean canUse()
        {
            return true;
        }
    }

    static class HunterAttackGoal extends NearestAttackableTargetGoal<LivingEntity>
    {
        public HunterAttackGoal(HunterEntity h)
        {
            super(h, LivingEntity.class, 0, false, true, LivingEntity::attackable);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse()
        {
            return ((HunterEntity)this.mob).isJohnny && super.canUse();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
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
                return (double)(f * 2.0F * f * 2.0F + playerAttackTarget.getBbWidth());
            }
            else
            {
                return super.getAttackReachSqr(playerAttackTarget);
            }
        }
    }
}
