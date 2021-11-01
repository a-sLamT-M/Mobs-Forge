package org.lt.conquer.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class BreakBlockGoal extends Goal
{
    private final Mob mob;
    private Entity targetPlayer;
    private final double reachDistance;
    private final List<BlockPos> targetBlocks = new ArrayList<>();
    private int tickToBreak = 0;
    private int breakingTick = 0;
    private BlockState blockState = null;
    private int prevBreakProgress = 0;
    private final boolean toolOnly;
    private final boolean properToolOnly;

    private int ticksWithNoPath = 0;

    public BreakBlockGoal(Mob m, boolean toolOnly, boolean properToolOnly)
    {
        this.mob = m;
        this.reachDistance = 4;
        this.toolOnly = toolOnly;
        this.properToolOnly = properToolOnly;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
    }

    public boolean canUse()
    {
        LivingEntity target = mob.getTarget();
        if (this.mob.getNavigation().isDone() || this.mob.getNavigation().isStuck())
            this.ticksWithNoPath++;
        else if (this.ticksWithNoPath > 0)
            this.ticksWithNoPath--;
        if (target != null)
        {
            return ticksWithNoPath >= 30
                    && this.mob.distanceToSqr(target) > 1.5d;
        }
        else
        {
            return ticksWithNoPath >= 30;
        }

    }

    public boolean canContinueToUse()
    {
        if (this.properToolOnly && this.blockState != null && !this.canHarvestBlock())
            return false;
        return !this.targetBlocks.isEmpty()
                && this.targetPlayer != null
                && this.targetPlayer.isAlive()
                && this.targetBlocks.get(0).distSqr(this.mob.blockPosition()) < this.reachDistance * this.reachDistance
                && this.mob.getNavigation().isDone()
                && !this.mob.level.getBlockState(this.targetBlocks.get(0)).isAir();
    }

    public void start()
    {
        this.targetPlayer = this.mob.getTarget();
        if (targetPlayer == null)
            return;
        fillTargetBlocks();
        if (!this.targetBlocks.isEmpty())
            initBlockBreak();
    }

    public void stop()
    {
        this.targetPlayer = null;
        if (!this.targetBlocks.isEmpty())
        {
            this.mob.level.destroyBlockProgress(this.mob.getId(), targetBlocks.get(0), -1);
            this.targetBlocks.clear();
        }
        this.tickToBreak = 0;
        this.breakingTick = 0;
        this.blockState = null;
        this.prevBreakProgress = 0;
        this.ticksWithNoPath = 0;
    }

    public void tick()
    {
        if (this.targetBlocks.isEmpty())
        {
            return;
        }
        if (this.properToolOnly && this.blockState != null && !this.canHarvestBlock())
        {
            return;
        }
        this.breakingTick++;
        this.mob.getLookControl().setLookAt(this.targetBlocks.get(0).getX() + 0.5d, this.targetBlocks.get(0).getY() + 0.5d, this.targetBlocks.get(0).getZ() + 0.5d);
        if (this.prevBreakProgress != (int) ((this.breakingTick / (float) this.tickToBreak) * 10))
        {
            this.mob.level.destroyBlockProgress(this.mob.getId(), targetBlocks.get(0), this.prevBreakProgress);
            this.prevBreakProgress = (int) ((this.breakingTick / (float) this.tickToBreak) * 10);
        }
        if (this.breakingTick % 6 == 0)
        {
            this.mob.swing(InteractionHand.MAIN_HAND);
        }
        if (this.breakingTick % 4 == 0)
        {
            SoundType soundType = this.blockState.getSoundType(this.mob.level, this.targetBlocks.get(0), this.mob);
            this.mob.level.playSound(null, this.targetBlocks.get(0), soundType.getHitSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
        }
        if (this.breakingTick >= this.tickToBreak)
        {
            this.mob.level.destroyBlock(targetBlocks.get(0), true, this.mob);
            this.mob.level.destroyBlockProgress(this.mob.getId(), targetBlocks.get(0), -1);
            this.targetBlocks.remove(0);
            if (!this.targetBlocks.isEmpty())
                initBlockBreak();
        }
    }

    private void initBlockBreak()
    {
        this.blockState = this.mob.level.getBlockState(this.targetBlocks.get(0));
        this.tickToBreak = computeTickToBreak();
        this.breakingTick = 0;
    }

    private int computeTickToBreak()
    {
        int canHarvestBlock = this.canHarvestBlock() ? 30 : 100;
        double diggingSpeed = this.getDigSpeed() / this.blockState.getDestroySpeed(this.mob.level, this.targetBlocks.get(0)) / canHarvestBlock;
        return Mth.ceil((1f / diggingSpeed) * 1.2f);
    }

    private float getDigSpeed()
    {
        float digSpeed = this.mob.getOffhandItem().getDestroySpeed(this.blockState);
        if (digSpeed > 1.0F)
        {
            int efficiencyLevel = EnchantmentHelper.getBlockEfficiency(this.mob);
            ItemStack itemstack = this.mob.getOffhandItem();
            if (efficiencyLevel > 0 && !itemstack.isEmpty())
            {
                digSpeed += (float)(efficiencyLevel * efficiencyLevel + 1);
            }
        }

        if (MobEffectUtil.hasDigSpeed(this.mob))
        {
            digSpeed *=
                    1.0F + (float)(MobEffectUtil.getDigSpeedAmplification(this.mob) + 1) * 0.2F;
        }

        if (this.mob.hasEffect(MobEffects.DIG_SLOWDOWN))
        {
            float miningFatigueAmplifier;
            switch (this.mob.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier())
            {
                case 0:
                    miningFatigueAmplifier = 0.3F;
                    break;
                case 1:
                    miningFatigueAmplifier = 0.09F;
                    break;
                case 2:
                    miningFatigueAmplifier = 0.0027F;
                    break;
                case 3:
                default:
                    miningFatigueAmplifier = 8.1E-4F;
            }
            digSpeed *= miningFatigueAmplifier;
        }

        if (this.mob.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this.mob))
        {
            digSpeed /= 5.0F;
        }

        if (!this.mob.isOnGround())
        {
            digSpeed /= 5.0F;
        }
        return digSpeed;
    }

    private boolean canHarvestBlock()
    {
        return true;
        // return blockState.canHarvestBlock(mob.level, targetBlocks.get(0), (Player) targetPlayer);
    }

    private void fillTargetBlocks()
    {
        int mobHeight = Mth.ceil(this.mob.getBbHeight());
        for (int i = 0; i < mobHeight; i++) {
            BlockHitResult rayTraceResult = this.mob.level.clip(new ClipContext(this.mob.position().add(0, i, 0), this.targetPlayer.getEyePosition(1f).add(0, i, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob));
            if (rayTraceResult.getType() == BlockHitResult.Type.MISS)
                continue;
            if (this.targetBlocks.contains(rayTraceResult.getBlockPos()))
                continue;
            double distance = this.mob.distanceToSqr(rayTraceResult.getLocation());
            if (distance > this.reachDistance * this.reachDistance)
                continue;

            BlockState state = this.mob.level.getBlockState(rayTraceResult.getBlockPos());

            if (state.hasBlockEntity())
                continue;

            boolean isInWhitelist = false;
            boolean isInBlacklist = false;

            this.targetBlocks.add(rayTraceResult.getBlockPos());
        }
        Collections.reverse(this.targetBlocks);
    }
}