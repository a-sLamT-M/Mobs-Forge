package org.lt.conquer.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

public abstract class BlockInteractGoal extends Goal
{
    protected Mob mob;
    protected BlockPos blockPos = BlockPos.ZERO;
    private boolean passed;
    private float blockDirX;
    private float blockDirZ;

    public BlockInteractGoal(Mob pMob)
    {
        this.mob = pMob;
        if (!GoalUtils.hasGroundPathNavigation(pMob))
        {
            throw new IllegalArgumentException("Unsupported mob type for BlockInteractGoal");
        }
    }

    public boolean canUse()
    {
        if (!GoalUtils.hasGroundPathNavigation(this.mob))
        {
            return false;
        }
        else if (!this.mob.horizontalCollision)
        {
            return false;
        }
        else
        {
            GroundPathNavigation groundpathnavigation = (GroundPathNavigation)this.mob.getNavigation();
            Path path = groundpathnavigation.getPath();
            if (path != null && !path.isDone())
            {
                for(int i = 0; i < Math.min(path.getNextNodeIndex() + 2, path.getNodeCount()); ++i)
                {
                    Node node = path.getNode(i);
                    this.blockPos = new BlockPos(node.x, node.y + 1, node.z);
                    if (!(this.mob.distanceToSqr((double)this.blockPos.getX(), this.mob.getY(), (double)this.blockPos.getZ()) > 2.25D))
                    {
                        return true;
                    }
                }

                this.blockPos = this.mob.blockPosition().above();
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    public boolean canContinueToUse() {
        return !this.passed;
    }
    public void start()
    {
        this.passed = false;
        this.blockDirX= (float)((double)this.blockPos.getX() + 0.5D - this.mob.getX());
        this.blockDirZ = (float)((double)this.blockPos.getZ() + 0.5D - this.mob.getZ());
    }
    public void tick()
    {
        float f = (float) ((double) this.blockPos.getX() + 0.5D - this.mob.getX());
        float f1 = (float) ((double) this.blockPos.getZ() + 0.5D - this.mob.getZ());
        float f2 = this.blockDirX * f + this.blockDirZ * f1;
        if (f2 < 0.0F)
        {
            this.passed = true;
        }
    }
}
