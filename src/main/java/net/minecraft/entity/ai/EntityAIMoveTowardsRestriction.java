package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class EntityAIMoveTowardsRestriction extends EntityAIBase
{
	private EntityCreature theEntity;
	private double movePosX;
	private double movePosY;
	private double movePosZ;
	private double movementSpeed;
	private static final String __OBFID = "CL_00001598";

	public EntityAIMoveTowardsRestriction(EntityCreature p_i2347_1_, double p_i2347_2_)
	{
		this.theEntity = p_i2347_1_;
		this.movementSpeed = p_i2347_2_;
		this.setMutexBits(1);
	}

	public boolean shouldExecute()
	{
		if (this.theEntity.isWithinHomeDistanceCurrentPosition())
		{
			return false;
		}
		else
		{
			BlockPos blockpos = this.theEntity.func_180486_cf();
			Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, new Vec3((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ()));

			if (vec3 == null)
			{
				return false;
			}
			else
			{
				this.movePosX = vec3.xCoord;
				this.movePosY = vec3.yCoord;
				this.movePosZ = vec3.zCoord;
				return true;
			}
		}
	}

	public boolean continueExecuting()
	{
		return !this.theEntity.getNavigator().noPath();
	}

	public void startExecuting()
	{
		this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
	}
}