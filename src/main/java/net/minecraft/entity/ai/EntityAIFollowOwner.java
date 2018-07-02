package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowOwner extends EntityAIBase
{
	private EntityTameable thePet;
	private EntityLivingBase theOwner;
	World theWorld;
	private double field_75336_f;
	private PathNavigate petPathfinder;
	private int field_75343_h;
	float maxDist;
	float minDist;
	private boolean field_75344_i;
	private static final String __OBFID = "CL_00001585";

	public EntityAIFollowOwner(EntityTameable p_i1625_1_, double p_i1625_2_, float p_i1625_4_, float p_i1625_5_)
	{
		this.thePet = p_i1625_1_;
		this.theWorld = p_i1625_1_.worldObj;
		this.field_75336_f = p_i1625_2_;
		this.petPathfinder = p_i1625_1_.getNavigator();
		this.minDist = p_i1625_4_;
		this.maxDist = p_i1625_5_;
		this.setMutexBits(3);

		if (!(p_i1625_1_.getNavigator() instanceof PathNavigateGround))
		{
			throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
		}
	}

	public boolean shouldExecute()
	{
		EntityLivingBase entitylivingbase = this.thePet.getOwnerEntity();

		if (entitylivingbase == null)
		{
			return false;
		}
		else if (this.thePet.isSitting())
		{
			return false;
		}
		else if (this.thePet.getDistanceSqToEntity(entitylivingbase) < (double)(this.minDist * this.minDist))
		{
			return false;
		}
		else
		{
			this.theOwner = entitylivingbase;
			return true;
		}
	}

	public boolean continueExecuting()
	{
		return !this.petPathfinder.noPath() && this.thePet.getDistanceSqToEntity(this.theOwner) > (double)(this.maxDist * this.maxDist) && !this.thePet.isSitting();
	}

	public void startExecuting()
	{
		this.field_75343_h = 0;
		this.field_75344_i = ((PathNavigateGround)this.thePet.getNavigator()).func_179689_e();
		((PathNavigateGround)this.thePet.getNavigator()).func_179690_a(false);
	}

	public void resetTask()
	{
		this.theOwner = null;
		this.petPathfinder.clearPathEntity();
		((PathNavigateGround)this.thePet.getNavigator()).func_179690_a(true);
	}

	public void updateTask()
	{
		this.thePet.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float)this.thePet.getVerticalFaceSpeed());

		if (!this.thePet.isSitting())
		{
			if (--this.field_75343_h <= 0)
			{
				this.field_75343_h = 10;

				if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.field_75336_f))
				{
					if (!this.thePet.getLeashed())
					{
						if (this.thePet.getDistanceSqToEntity(this.theOwner) >= 144.0D)
						{
							int i = MathHelper.floor_double(this.theOwner.posX) - 2;
							int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
							int k = MathHelper.floor_double(this.theOwner.getEntityBoundingBox().minY);

							for (int l = 0; l <= 4; ++l)
							{
								for (int i1 = 0; i1 <= 4; ++i1)
								{
									if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && World.doesBlockHaveSolidTopSurface(this.theWorld, new BlockPos(i + l, k - 1, j + i1)) && !this.theWorld.getBlockState(new BlockPos(i + l, k, j + i1)).getBlock().isFullCube() && !this.theWorld.getBlockState(new BlockPos(i + l, k + 1, j + i1)).getBlock().isFullCube())
									{
										this.thePet.setLocationAndAngles((double)((float)(i + l) + 0.5F), (double)k, (double)((float)(j + i1) + 0.5F), this.thePet.rotationYaw, this.thePet.rotationPitch);
										this.petPathfinder.clearPathEntity();
										return;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}