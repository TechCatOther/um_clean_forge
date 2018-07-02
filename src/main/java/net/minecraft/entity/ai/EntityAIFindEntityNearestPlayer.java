package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.Collections;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearestPlayer extends EntityAIBase
{
	private static final Logger field_179436_a = LogManager.getLogger();
	private EntityLiving field_179434_b;
	private final Predicate field_179435_c;
	private final EntityAINearestAttackableTarget.Sorter field_179432_d;
	private EntityLivingBase field_179433_e;
	private static final String __OBFID = "CL_00002248";

	public EntityAIFindEntityNearestPlayer(EntityLiving p_i45882_1_)
	{
		this.field_179434_b = p_i45882_1_;

		if (p_i45882_1_ instanceof EntityCreature)
		{
			field_179436_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
		}

		this.field_179435_c = new Predicate()
		{
			private static final String __OBFID = "CL_00002247";
			public boolean func_179880_a(Entity p_179880_1_)
			{
				if (!(p_179880_1_ instanceof EntityPlayer))
				{
					return false;
				}
				else
				{
					double d0 = EntityAIFindEntityNearestPlayer.this.func_179431_f();

					if (p_179880_1_.isSneaking())
					{
						d0 *= 0.800000011920929D;
					}

					if (p_179880_1_.isInvisible())
					{
						float f = ((EntityPlayer)p_179880_1_).getArmorVisibility();

						if (f < 0.1F)
						{
							f = 0.1F;
						}

						d0 *= (double)(0.7F * f);
					}

					return (double)p_179880_1_.getDistanceToEntity(EntityAIFindEntityNearestPlayer.this.field_179434_b) > d0 ? false : EntityAITarget.func_179445_a(EntityAIFindEntityNearestPlayer.this.field_179434_b, (EntityLivingBase)p_179880_1_, false, true);
				}
			}
			public boolean apply(Object p_apply_1_)
			{
				return this.func_179880_a((Entity)p_apply_1_);
			}
		};
		this.field_179432_d = new EntityAINearestAttackableTarget.Sorter(p_i45882_1_);
	}

	public boolean shouldExecute()
	{
		double d0 = this.func_179431_f();
		List list = this.field_179434_b.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.field_179434_b.getEntityBoundingBox().expand(d0, 4.0D, d0), this.field_179435_c);
		Collections.sort(list, this.field_179432_d);

		if (list.isEmpty())
		{
			return false;
		}
		else
		{
			this.field_179433_e = (EntityLivingBase)list.get(0);
			return true;
		}
	}

	public boolean continueExecuting()
	{
		EntityLivingBase entitylivingbase = this.field_179434_b.getAttackTarget();

		if (entitylivingbase == null)
		{
			return false;
		}
		else if (!entitylivingbase.isEntityAlive())
		{
			return false;
		}
		else
		{
			Team team = this.field_179434_b.getTeam();
			Team team1 = entitylivingbase.getTeam();

			if (team != null && team1 == team)
			{
				return false;
			}
			else
			{
				double d0 = this.func_179431_f();
				return this.field_179434_b.getDistanceSqToEntity(entitylivingbase) > d0 * d0 ? false : !(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP)entitylivingbase).theItemInWorldManager.isCreative();
			}
		}
	}

	public void startExecuting()
	{
		this.field_179434_b.setAttackTarget(this.field_179433_e);
		super.startExecuting();
	}

	public void resetTask()
	{
		this.field_179434_b.setAttackTarget((EntityLivingBase)null);
		super.startExecuting();
	}

	protected double func_179431_f()
	{
		IAttributeInstance iattributeinstance = this.field_179434_b.getEntityAttribute(SharedMonsterAttributes.followRange);
		return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
	}
}