package org.lt.conquer.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.lt.conquer.registy.ModEntities;
import org.lt.conquer.utils.RandomNum;

import java.util.Objects;

public class ModSpawn
{
    private static Vec3 targetPos;

    public Vec3 getTargetPos()
    {
        return targetPos;
    }

    public static void onLogging(final PlayerEvent.PlayerLoggedInEvent event)
    {
        Player player = (Player) event.getPlayer();
        Level l = player.level;
        targetPos =  new Vec3(player.getX(), player.getY(), player.getZ());
        int rangeX = 20;
        int rangeY = 32;

        int randomX = RandomNum.random(rangeX,rangeY);
        int randomY = RandomNum.random(rangeX,rangeY);
        int randomChance = RandomNum.random(0,100);

        if (randomChance < 25)
        {
            // x+ y+
            targetPos = new Vec3(player.getX() + randomX , player.getY(), player.getZ() + randomY);
            System.out.println(targetPos.x+" "+ targetPos.y+" "+ targetPos.z+" ");
        }
        else if (randomChance < 50)
        {
            // x+ y-
            targetPos = new Vec3(player.getX() + randomX , player.getY(), player.getZ() - randomY);
            System.out.println(targetPos.x+" "+ targetPos.y+" "+ targetPos.z+" ");
        }
        else if (randomChance < 75)
        {
            // x- y+
            targetPos = new Vec3(player.getX() - randomX , player.getY(), player.getZ() + randomY);
            System.out.println(targetPos.x+" "+ targetPos.y+" "+ targetPos.z+" ");
        }
        else
        {
            // x- y-
            targetPos = new Vec3(player.getX() - randomX , player.getY(), player.getZ() - randomY);
            System.out.println(targetPos.x+" "+ targetPos.y+" "+ targetPos.z+" ");
        }

        BlockPos targetBlock = new BlockPos(targetPos);
        BlockState block = l.getBlockState(targetBlock);
        Material blockMaterial = block.getMaterial();

        if (blockMaterial != Material.AIR)
        {
            for (int y = (int) targetPos.y; blockMaterial != Material.AIR; y++)
            {
                targetPos = new Vec3(targetPos.x, y, targetPos.z);
                BlockPos newBlockPos = new BlockPos(targetPos);
                BlockState newBlock = l.getBlockState(newBlockPos);
                blockMaterial = newBlock.getMaterial();
                System.out.println(targetPos.x+" "+ targetPos.y+" "+ targetPos.z+" ");
            }
        }

        Entity hunterEntity = ModEntities.BLUE_HUNTER.get().create(l);
        assert hunterEntity != null;
        hunterEntity.setPos(targetPos);
        System.out.println(targetPos.x+" "+ targetPos.y+" "+ targetPos.z+" ");
        System.out.println(targetPos.x );
        l.addFreshEntity(Objects.requireNonNull(hunterEntity));
    }
}
