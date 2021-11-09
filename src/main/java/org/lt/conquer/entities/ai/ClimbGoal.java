package org.lt.conquer.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ClimbGoal extends Goal
{
    private final Mob mob;

    public ClimbGoal(Mob m)
    {
        this.mob = m;
    }

    @Override
    public boolean canUse()
    {
        return this.mob.isOnGround();
    }
    // Create a function to get the distance between player and mob
    private double getDistanceToClimbable(BlockPos pos)
    {
        BlockPos blockpos = this.mob.blockPosition();
        return (double)(blockpos.getX() - pos.getX()) + (double)(blockpos.getZ() - pos.getZ());
    }
}
