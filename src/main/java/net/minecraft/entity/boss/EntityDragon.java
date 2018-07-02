package net.minecraft.entity.boss;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityDragon extends EntityLiving implements IBossDisplayData, IEntityMultiPart, IMob
{
	public double targetX;
	public double targetY;
	public double targetZ;
	public double[][] ringBuffer = new double[64][3];
	public int ringBufferIndex = -1;
	public EntityDragonPart[] dragonPartArray;
	public EntityDragonPart dragonPartHead;
	public EntityDragonPart dragonPartBody;
	public EntityDragonPart dragonPartTail1;
	public EntityDragonPart dragonPartTail2;
	public EntityDragonPart dragonPartTail3;
	public EntityDragonPart dragonPartWing1;
	public EntityDragonPart dragonPartWing2;
	public float prevAnimTime;
	public float animTime;
	public boolean forceNewTarget;
	public boolean slowed;
	private Entity target;
	public int deathTicks;
	public EntityEnderCrystal healingEnderCrystal;
	private static final String __OBFID = "CL_00001659";

	public EntityDragon(World worldIn)
	{
		super(worldIn);
		this.dragonPartArray = new EntityDragonPart[] {this.dragonPartHead = new EntityDragonPart(this, "head", 6.0F, 6.0F), this.dragonPartBody = new EntityDragonPart(this, "body", 8.0F, 8.0F), this.dragonPartTail1 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail2 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail3 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartWing1 = new EntityDragonPart(this, "wing", 4.0F, 4.0F), this.dragonPartWing2 = new EntityDragonPart(this, "wing", 4.0F, 4.0F)};
		this.setHealth(this.getMaxHealth());
		this.setSize(16.0F, 8.0F);
		this.noClip = true;
		this.isImmuneToFire = true;
		this.targetY = 100.0D;
		this.ignoreFrustumCheck = true;
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0D);
	}

	protected void entityInit()
	{
		super.entityInit();
	}

	public double[] getMovementOffsets(int p_70974_1_, float p_70974_2_)
	{
		if (this.getHealth() <= 0.0F)
		{
			p_70974_2_ = 0.0F;
		}

		p_70974_2_ = 1.0F - p_70974_2_;
		int j = this.ringBufferIndex - p_70974_1_ * 1 & 63;
		int k = this.ringBufferIndex - p_70974_1_ * 1 - 1 & 63;
		double[] adouble = new double[3];
		double d0 = this.ringBuffer[j][0];
		double d1 = MathHelper.wrapAngleTo180_double(this.ringBuffer[k][0] - d0);
		adouble[0] = d0 + d1 * (double)p_70974_2_;
		d0 = this.ringBuffer[j][1];
		d1 = this.ringBuffer[k][1] - d0;
		adouble[1] = d0 + d1 * (double)p_70974_2_;
		adouble[2] = this.ringBuffer[j][2] + (this.ringBuffer[k][2] - this.ringBuffer[j][2]) * (double)p_70974_2_;
		return adouble;
	}

	public void onLivingUpdate()
	{
		float f;
		float f1;

		if (this.worldObj.isRemote)
		{
			f = MathHelper.cos(this.animTime * (float)Math.PI * 2.0F);
			f1 = MathHelper.cos(this.prevAnimTime * (float)Math.PI * 2.0F);

			if (f1 <= -0.3F && f >= -0.3F && !this.isSilent())
			{
				this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.enderdragon.wings", 5.0F, 0.8F + this.rand.nextFloat() * 0.3F, false);
			}
		}

		this.prevAnimTime = this.animTime;
		float f2;

		if (this.getHealth() <= 0.0F)
		{
			f = (this.rand.nextFloat() - 0.5F) * 8.0F;
			f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
			f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
			this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + (double)f, this.posY + 2.0D + (double)f1, this.posZ + (double)f2, 0.0D, 0.0D, 0.0D, new int[0]);
		}
		else
		{
			this.updateDragonEnderCrystal();
			f = 0.2F / (MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
			f *= (float)Math.pow(2.0D, this.motionY);

			if (this.slowed)
			{
				this.animTime += f * 0.5F;
			}
			else
			{
				this.animTime += f;
			}

			this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);

			if (this.ringBufferIndex < 0)
			{
				for (int i = 0; i < this.ringBuffer.length; ++i)
				{
					this.ringBuffer[i][0] = (double)this.rotationYaw;
					this.ringBuffer[i][1] = this.posY;
				}
			}

			if (++this.ringBufferIndex == this.ringBuffer.length)
			{
				this.ringBufferIndex = 0;
			}

			this.ringBuffer[this.ringBufferIndex][0] = (double)this.rotationYaw;
			this.ringBuffer[this.ringBufferIndex][1] = this.posY;
			double d0;
			double d1;
			double d2;
			double d10;
			float f12;

			if (this.worldObj.isRemote)
			{
				if (this.newPosRotationIncrements > 0)
				{
					d10 = this.posX + (this.newPosX - this.posX) / (double)this.newPosRotationIncrements;
					d0 = this.posY + (this.newPosY - this.posY) / (double)this.newPosRotationIncrements;
					d1 = this.posZ + (this.newPosZ - this.posZ) / (double)this.newPosRotationIncrements;
					d2 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - (double)this.rotationYaw);
					this.rotationYaw = (float)((double)this.rotationYaw + d2 / (double)this.newPosRotationIncrements);
					this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
					--this.newPosRotationIncrements;
					this.setPosition(d10, d0, d1);
					this.setRotation(this.rotationYaw, this.rotationPitch);
				}
			}
			else
			{
				d10 = this.targetX - this.posX;
				d0 = this.targetY - this.posY;
				d1 = this.targetZ - this.posZ;
				d2 = d10 * d10 + d0 * d0 + d1 * d1;
				double d8;

				if (this.target != null)
				{
					this.targetX = this.target.posX;
					this.targetZ = this.target.posZ;
					double d3 = this.targetX - this.posX;
					double d5 = this.targetZ - this.posZ;
					double d7 = Math.sqrt(d3 * d3 + d5 * d5);
					d8 = 0.4000000059604645D + d7 / 80.0D - 1.0D;

					if (d8 > 10.0D)
					{
						d8 = 10.0D;
					}

					this.targetY = this.target.getEntityBoundingBox().minY + d8;
				}
				else
				{
					this.targetX += this.rand.nextGaussian() * 2.0D;
					this.targetZ += this.rand.nextGaussian() * 2.0D;
				}

				if (this.forceNewTarget || d2 < 100.0D || d2 > 22500.0D || this.isCollidedHorizontally || this.isCollidedVertically)
				{
					this.setNewTarget();
				}

				d0 /= (double)MathHelper.sqrt_double(d10 * d10 + d1 * d1);
				f12 = 0.6F;
				d0 = MathHelper.clamp_double(d0, (double)(-f12), (double)f12);
				this.motionY += d0 * 0.10000000149011612D;
				this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
				double d4 = 180.0D - Math.atan2(d10, d1) * 180.0D / Math.PI;
				double d6 = MathHelper.wrapAngleTo180_double(d4 - (double)this.rotationYaw);

				if (d6 > 50.0D)
				{
					d6 = 50.0D;
				}

				if (d6 < -50.0D)
				{
					d6 = -50.0D;
				}

				Vec3 vec3 = (new Vec3(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ)).normalize();
				d8 = (double)(-MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F));
				Vec3 vec31 = (new Vec3((double)MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F), this.motionY, d8)).normalize();
				float f5 = ((float)vec31.dotProduct(vec3) + 0.5F) / 1.5F;

				if (f5 < 0.0F)
				{
					f5 = 0.0F;
				}

				this.randomYawVelocity *= 0.8F;
				float f6 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0F + 1.0F;
				double d9 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0D + 1.0D;

				if (d9 > 40.0D)
				{
					d9 = 40.0D;
				}

				this.randomYawVelocity = (float)((double)this.randomYawVelocity + d6 * (0.699999988079071D / d9 / (double)f6));
				this.rotationYaw += this.randomYawVelocity * 0.1F;
				float f7 = (float)(2.0D / (d9 + 1.0D));
				float f8 = 0.06F;
				this.moveFlying(0.0F, -1.0F, f8 * (f5 * f7 + (1.0F - f7)));

				if (this.slowed)
				{
					this.moveEntity(this.motionX * 0.800000011920929D, this.motionY * 0.800000011920929D, this.motionZ * 0.800000011920929D);
				}
				else
				{
					this.moveEntity(this.motionX, this.motionY, this.motionZ);
				}

				Vec3 vec32 = (new Vec3(this.motionX, this.motionY, this.motionZ)).normalize();
				float f9 = ((float)vec32.dotProduct(vec31) + 1.0F) / 2.0F;
				f9 = 0.8F + 0.15F * f9;
				this.motionX *= (double)f9;
				this.motionZ *= (double)f9;
				this.motionY *= 0.9100000262260437D;
			}

			this.renderYawOffset = this.rotationYaw;
			this.dragonPartHead.width = this.dragonPartHead.height = 3.0F;
			this.dragonPartTail1.width = this.dragonPartTail1.height = 2.0F;
			this.dragonPartTail2.width = this.dragonPartTail2.height = 2.0F;
			this.dragonPartTail3.width = this.dragonPartTail3.height = 2.0F;
			this.dragonPartBody.height = 3.0F;
			this.dragonPartBody.width = 5.0F;
			this.dragonPartWing1.height = 2.0F;
			this.dragonPartWing1.width = 4.0F;
			this.dragonPartWing2.height = 3.0F;
			this.dragonPartWing2.width = 4.0F;
			f1 = (float)(this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F / 180.0F * (float)Math.PI;
			f2 = MathHelper.cos(f1);
			float f10 = -MathHelper.sin(f1);
			float f3 = this.rotationYaw * (float)Math.PI / 180.0F;
			float f11 = MathHelper.sin(f3);
			float f4 = MathHelper.cos(f3);
			this.dragonPartBody.onUpdate();
			this.dragonPartBody.setLocationAndAngles(this.posX + (double)(f11 * 0.5F), this.posY, this.posZ - (double)(f4 * 0.5F), 0.0F, 0.0F);
			this.dragonPartWing1.onUpdate();
			this.dragonPartWing1.setLocationAndAngles(this.posX + (double)(f4 * 4.5F), this.posY + 2.0D, this.posZ + (double)(f11 * 4.5F), 0.0F, 0.0F);
			this.dragonPartWing2.onUpdate();
			this.dragonPartWing2.setLocationAndAngles(this.posX - (double)(f4 * 4.5F), this.posY + 2.0D, this.posZ - (double)(f11 * 4.5F), 0.0F, 0.0F);

			if (!this.worldObj.isRemote && this.hurtTime == 0)
			{
				this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
				this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
				this.attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.getEntityBoundingBox().expand(1.0D, 1.0D, 1.0D)));
			}

			double[] adouble1 = this.getMovementOffsets(5, 1.0F);
			double[] adouble = this.getMovementOffsets(0, 1.0F);
			f12 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
			float f13 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
			this.dragonPartHead.onUpdate();
			this.dragonPartHead.setLocationAndAngles(this.posX + (double)(f12 * 5.5F * f2), this.posY + (adouble[1] - adouble1[1]) * 1.0D + (double)(f10 * 5.5F), this.posZ - (double)(f13 * 5.5F * f2), 0.0F, 0.0F);

			for (int j = 0; j < 3; ++j)
			{
				EntityDragonPart entitydragonpart = null;

				if (j == 0)
				{
					entitydragonpart = this.dragonPartTail1;
				}

				if (j == 1)
				{
					entitydragonpart = this.dragonPartTail2;
				}

				if (j == 2)
				{
					entitydragonpart = this.dragonPartTail3;
				}

				double[] adouble2 = this.getMovementOffsets(12 + j * 2, 1.0F);
				float f14 = this.rotationYaw * (float)Math.PI / 180.0F + this.simplifyAngle(adouble2[0] - adouble1[0]) * (float)Math.PI / 180.0F * 1.0F;
				float f15 = MathHelper.sin(f14);
				float f16 = MathHelper.cos(f14);
				float f17 = 1.5F;
				float f18 = (float)(j + 1) * 2.0F;
				entitydragonpart.onUpdate();
				entitydragonpart.setLocationAndAngles(this.posX - (double)((f11 * f17 + f15 * f18) * f2), this.posY + (adouble2[1] - adouble1[1]) * 1.0D - (double)((f18 + f17) * f10) + 1.5D, this.posZ + (double)((f4 * f17 + f16 * f18) * f2), 0.0F, 0.0F);
			}

			if (!this.worldObj.isRemote)
			{
				this.slowed = this.destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox());
			}
		}
	}

	private void updateDragonEnderCrystal()
	{
		if (this.healingEnderCrystal != null)
		{
			if (this.healingEnderCrystal.isDead)
			{
				if (!this.worldObj.isRemote)
				{
					this.attackEntityFromPart(this.dragonPartHead, DamageSource.setExplosionSource((Explosion)null), 10.0F);
				}

				this.healingEnderCrystal = null;
			}
			else if (this.ticksExisted % 10 == 0 && this.getHealth() < this.getMaxHealth())
			{
				this.setHealth(this.getHealth() + 1.0F);
			}
		}

		if (this.rand.nextInt(10) == 0)
		{
			float f = 32.0F;
			List list = this.worldObj.getEntitiesWithinAABB(EntityEnderCrystal.class, this.getEntityBoundingBox().expand((double)f, (double)f, (double)f));
			EntityEnderCrystal entityendercrystal = null;
			double d0 = Double.MAX_VALUE;
			Iterator iterator = list.iterator();

			while (iterator.hasNext())
			{
				EntityEnderCrystal entityendercrystal1 = (EntityEnderCrystal)iterator.next();
				double d1 = entityendercrystal1.getDistanceSqToEntity(this);

				if (d1 < d0)
				{
					d0 = d1;
					entityendercrystal = entityendercrystal1;
				}
			}

			this.healingEnderCrystal = entityendercrystal;
		}
	}

	private void collideWithEntities(List p_70970_1_)
	{
		double d0 = (this.dragonPartBody.getEntityBoundingBox().minX + this.dragonPartBody.getEntityBoundingBox().maxX) / 2.0D;
		double d1 = (this.dragonPartBody.getEntityBoundingBox().minZ + this.dragonPartBody.getEntityBoundingBox().maxZ) / 2.0D;
		Iterator iterator = p_70970_1_.iterator();

		while (iterator.hasNext())
		{
			Entity entity = (Entity)iterator.next();

			if (entity instanceof EntityLivingBase)
			{
				double d2 = entity.posX - d0;
				double d3 = entity.posZ - d1;
				double d4 = d2 * d2 + d3 * d3;
				entity.addVelocity(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
			}
		}
	}

	private void attackEntitiesInList(List p_70971_1_)
	{
		for (int i = 0; i < p_70971_1_.size(); ++i)
		{
			Entity entity = (Entity)p_70971_1_.get(i);

			if (entity instanceof EntityLivingBase)
			{
				entity.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
				this.func_174815_a(this, entity);
			}
		}
	}

	private void setNewTarget()
	{
		this.forceNewTarget = false;
		ArrayList arraylist = Lists.newArrayList(this.worldObj.playerEntities);
		Iterator iterator = arraylist.iterator();

		while (iterator.hasNext())
		{
			if (((EntityPlayer)iterator.next()).isSpectator())
			{
				iterator.remove();
			}
		}

		if (this.rand.nextInt(2) == 0 && !arraylist.isEmpty())
		{
			this.target = (Entity)arraylist.get(this.rand.nextInt(arraylist.size()));
		}
		else
		{
			boolean flag;

			do
			{
				this.targetX = 0.0D;
				this.targetY = (double)(70.0F + this.rand.nextFloat() * 50.0F);
				this.targetZ = 0.0D;
				this.targetX += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
				this.targetZ += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
				double d0 = this.posX - this.targetX;
				double d1 = this.posY - this.targetY;
				double d2 = this.posZ - this.targetZ;
				flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
			}
			while (!flag);

			this.target = null;
		}
	}

	private float simplifyAngle(double p_70973_1_)
	{
		return (float)MathHelper.wrapAngleTo180_double(p_70973_1_);
	}

	private boolean destroyBlocksInAABB(AxisAlignedBB p_70972_1_)
	{
		int i = MathHelper.floor_double(p_70972_1_.minX);
		int j = MathHelper.floor_double(p_70972_1_.minY);
		int k = MathHelper.floor_double(p_70972_1_.minZ);
		int l = MathHelper.floor_double(p_70972_1_.maxX);
		int i1 = MathHelper.floor_double(p_70972_1_.maxY);
		int j1 = MathHelper.floor_double(p_70972_1_.maxZ);
		boolean flag = false;
		boolean flag1 = false;

		for (int k1 = i; k1 <= l; ++k1)
		{
			for (int l1 = j; l1 <= i1; ++l1)
			{
				for (int i2 = k; i2 <= j1; ++i2)
				{
					Block block = this.worldObj.getBlockState(new BlockPos(k1, l1, i2)).getBlock();

					if (!block.isAir(worldObj, new BlockPos(k1, l1, i2)))
					{
						if (block.canEntityDestroy(worldObj, new BlockPos(k1, l1, i2), this) && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"))
						{
							flag1 = this.worldObj.setBlockToAir(new BlockPos(k1, l1, i2)) || flag1;
						}
						else
						{
							flag = true;
						}
					}
				}
			}
		}

		if (flag1)
		{
			double d1 = p_70972_1_.minX + (p_70972_1_.maxX - p_70972_1_.minX) * (double)this.rand.nextFloat();
			double d2 = p_70972_1_.minY + (p_70972_1_.maxY - p_70972_1_.minY) * (double)this.rand.nextFloat();
			double d0 = p_70972_1_.minZ + (p_70972_1_.maxZ - p_70972_1_.minZ) * (double)this.rand.nextFloat();
			this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d1, d2, d0, 0.0D, 0.0D, 0.0D, new int[0]);
		}

		return flag;
	}

	public boolean attackEntityFromPart(EntityDragonPart p_70965_1_, DamageSource p_70965_2_, float p_70965_3_)
	{
		if (p_70965_1_ != this.dragonPartHead)
		{
			p_70965_3_ = p_70965_3_ / 4.0F + 1.0F;
		}

		float f1 = this.rotationYaw * (float)Math.PI / 180.0F;
		float f2 = MathHelper.sin(f1);
		float f3 = MathHelper.cos(f1);
		this.targetX = this.posX + (double)(f2 * 5.0F) + (double)((this.rand.nextFloat() - 0.5F) * 2.0F);
		this.targetY = this.posY + (double)(this.rand.nextFloat() * 3.0F) + 1.0D;
		this.targetZ = this.posZ - (double)(f3 * 5.0F) + (double)((this.rand.nextFloat() - 0.5F) * 2.0F);
		this.target = null;

		if (p_70965_2_.getEntity() instanceof EntityPlayer || p_70965_2_.isExplosion())
		{
			this.func_82195_e(p_70965_2_, p_70965_3_);
		}

		return true;
	}

	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage())
		{
			this.func_82195_e(source, amount);
		}

		return false;
	}

	protected boolean func_82195_e(DamageSource p_82195_1_, float p_82195_2_)
	{
		return super.attackEntityFrom(p_82195_1_, p_82195_2_);
	}

	public void onKillCommand()
	{
		this.setDead();
	}

	protected void onDeathUpdate()
	{
		++this.deathTicks;

		if (this.deathTicks >= 180 && this.deathTicks <= 200)
		{
			float f = (this.rand.nextFloat() - 0.5F) * 8.0F;
			float f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
			float f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
			this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + (double)f, this.posY + 2.0D + (double)f1, this.posZ + (double)f2, 0.0D, 0.0D, 0.0D, new int[0]);
		}

		int i;
		int j;

		if (!this.worldObj.isRemote)
		{
			if (this.deathTicks > 150 && this.deathTicks % 5 == 0 && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot"))
			{
				i = 1000;

				while (i > 0)
				{
					j = EntityXPOrb.getXPSplit(i);
					i -= j;
					this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
				}
			}

			if (this.deathTicks == 1)
			{
				this.worldObj.playBroadcastSound(1018, new BlockPos(this), 0);
			}
		}

		this.moveEntity(0.0D, 0.10000000149011612D, 0.0D);
		this.renderYawOffset = this.rotationYaw += 20.0F;

		if (this.deathTicks == 200 && !this.worldObj.isRemote)
		{
			i = 2000;

			while (i > 0)
			{
				j = EntityXPOrb.getXPSplit(i);
				i -= j;
				this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
			}

			this.func_175499_a(new BlockPos(this.posX, 64.0D, this.posZ));
			this.setDead();
		}
	}

	private void func_175499_a(BlockPos p_175499_1_)
	{
		boolean flag = true;
		double d0 = 12.25D;
		double d1 = 6.25D;

		for (int i = -1; i <= 32; ++i)
		{
			for (int j = -4; j <= 4; ++j)
			{
				for (int k = -4; k <= 4; ++k)
				{
					double d2 = (double)(j * j + k * k);

					if (d2 <= 12.25D)
					{
						BlockPos blockpos1 = p_175499_1_.add(j, i, k);

						if (i < 0)
						{
							if (d2 <= 6.25D)
							{
								this.worldObj.setBlockState(blockpos1, Blocks.bedrock.getDefaultState());
							}
						}
						else if (i > 0)
						{
							this.worldObj.setBlockState(blockpos1, Blocks.air.getDefaultState());
						}
						else if (d2 > 6.25D)
						{
							this.worldObj.setBlockState(blockpos1, Blocks.bedrock.getDefaultState());
						}
						else
						{
							this.worldObj.setBlockState(blockpos1, Blocks.end_portal.getDefaultState());
						}
					}
				}
			}
		}

		this.worldObj.setBlockState(p_175499_1_, Blocks.bedrock.getDefaultState());
		this.worldObj.setBlockState(p_175499_1_.up(), Blocks.bedrock.getDefaultState());
		BlockPos blockpos2 = p_175499_1_.up(2);
		this.worldObj.setBlockState(blockpos2, Blocks.bedrock.getDefaultState());
		this.worldObj.setBlockState(blockpos2.west(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST));
		this.worldObj.setBlockState(blockpos2.east(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST));
		this.worldObj.setBlockState(blockpos2.north(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH));
		this.worldObj.setBlockState(blockpos2.south(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.NORTH));
		this.worldObj.setBlockState(p_175499_1_.up(3), Blocks.bedrock.getDefaultState());
		this.worldObj.setBlockState(p_175499_1_.up(4), Blocks.dragon_egg.getDefaultState());
	}

	protected void despawnEntity() {}

	public Entity[] getParts()
	{
		return this.dragonPartArray;
	}

	public boolean canBeCollidedWith()
	{
		return false;
	}

	public World getWorld()
	{
		return this.worldObj;
	}

	protected String getLivingSound()
	{
		return "mob.enderdragon.growl";
	}

	protected String getHurtSound()
	{
		return "mob.enderdragon.hit";
	}

	protected float getSoundVolume()
	{
		return 5.0F;
	}
}