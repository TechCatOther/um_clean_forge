package net.minecraft.entity.passive;

import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySquid extends EntityWaterMob
{
	public float squidPitch;
	public float prevSquidPitch;
	public float squidYaw;
	public float prevSquidYaw;
	public float squidRotation;
	public float prevSquidRotation;
	public float tentacleAngle;
	public float lastTentacleAngle;
	private float randomMotionSpeed;
	private float rotationVelocity;
	private float field_70871_bB;
	private float randomMotionVecX;
	private float randomMotionVecY;
	private float randomMotionVecZ;
	private static final String __OBFID = "CL_00001651";

	public EntitySquid(World worldIn)
	{
		super(worldIn);
		this.setSize(0.95F, 0.95F);
		this.rand.setSeed((long)(1 + this.getEntityId()));
		this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
		this.tasks.addTask(0, new EntitySquid.AIMoveRandom());
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
	}

	public float getEyeHeight()
	{
		return this.height * 0.5F;
	}

	protected String getLivingSound()
	{
		return null;
	}

	protected String getHurtSound()
	{
		return null;
	}

	protected String getDeathSound()
	{
		return null;
	}

	protected float getSoundVolume()
	{
		return 0.4F;
	}

	protected Item getDropItem()
	{
		return null;
	}

	protected boolean canTriggerWalking()
	{
		return false;
	}

	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		int j = this.rand.nextInt(3 + p_70628_2_) + 1;

		for (int k = 0; k < j; ++k)
		{
			this.entityDropItem(new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeDamage()), 0.0F);
		}
	}

	public boolean isInWater()
	{
		return this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.0D, -0.6000000238418579D, 0.0D), Material.water, this);
	}

	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		this.prevSquidPitch = this.squidPitch;
		this.prevSquidYaw = this.squidYaw;
		this.prevSquidRotation = this.squidRotation;
		this.lastTentacleAngle = this.tentacleAngle;
		this.squidRotation += this.rotationVelocity;

		if ((double)this.squidRotation > (Math.PI * 2D))
		{
			if (this.worldObj.isRemote)
			{
				this.squidRotation = ((float)Math.PI * 2F);
			}
			else
			{
				this.squidRotation = (float)((double)this.squidRotation - (Math.PI * 2D));

				if (this.rand.nextInt(10) == 0)
				{
					this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
				}

				this.worldObj.setEntityState(this, (byte)19);
			}
		}

		if (this.inWater)
		{
			float f;

			if (this.squidRotation < (float)Math.PI)
			{
				f = this.squidRotation / (float)Math.PI;
				this.tentacleAngle = MathHelper.sin(f * f * (float)Math.PI) * (float)Math.PI * 0.25F;

				if ((double)f > 0.75D)
				{
					this.randomMotionSpeed = 1.0F;
					this.field_70871_bB = 1.0F;
				}
				else
				{
					this.field_70871_bB *= 0.8F;
				}
			}
			else
			{
				this.tentacleAngle = 0.0F;
				this.randomMotionSpeed *= 0.9F;
				this.field_70871_bB *= 0.99F;
			}

			if (!this.worldObj.isRemote)
			{
				this.motionX = (double)(this.randomMotionVecX * this.randomMotionSpeed);
				this.motionY = (double)(this.randomMotionVecY * this.randomMotionSpeed);
				this.motionZ = (double)(this.randomMotionVecZ * this.randomMotionSpeed);
			}

			f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.renderYawOffset += (-((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / (float)Math.PI - this.renderYawOffset) * 0.1F;
			this.rotationYaw = this.renderYawOffset;
			this.squidYaw = (float)((double)this.squidYaw + Math.PI * (double)this.field_70871_bB * 1.5D);
			this.squidPitch += (-((float)Math.atan2((double)f, this.motionY)) * 180.0F / (float)Math.PI - this.squidPitch) * 0.1F;
		}
		else
		{
			this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * (float)Math.PI * 0.25F;

			if (!this.worldObj.isRemote)
			{
				this.motionX = 0.0D;
				this.motionY -= 0.08D;
				this.motionY *= 0.9800000190734863D;
				this.motionZ = 0.0D;
			}

			this.squidPitch = (float)((double)this.squidPitch + (double)(-90.0F - this.squidPitch) * 0.02D);
		}
	}

	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
	{
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}

	public boolean getCanSpawnHere()
	{
		return this.posY > 45.0D && this.posY < 63.0D && super.getCanSpawnHere();
	}

	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_)
	{
		if (p_70103_1_ == 19)
		{
			this.squidRotation = 0.0F;
		}
		else
		{
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	public void func_175568_b(float p_175568_1_, float p_175568_2_, float p_175568_3_)
	{
		this.randomMotionVecX = p_175568_1_;
		this.randomMotionVecY = p_175568_2_;
		this.randomMotionVecZ = p_175568_3_;
	}

	public boolean func_175567_n()
	{
		return this.randomMotionVecX != 0.0F || this.randomMotionVecY != 0.0F || this.randomMotionVecZ != 0.0F;
	}

	class AIMoveRandom extends EntityAIBase
	{
		private EntitySquid field_179476_a = EntitySquid.this;
		private static final String __OBFID = "CL_00002232";

		public boolean shouldExecute()
		{
			return true;
		}

		public void updateTask()
		{
			int i = this.field_179476_a.getAge();

			if (i > 100)
			{
				this.field_179476_a.func_175568_b(0.0F, 0.0F, 0.0F);
			}
			else if (this.field_179476_a.getRNG().nextInt(50) == 0 || !this.field_179476_a.inWater || !this.field_179476_a.func_175567_n())
			{
				float f = this.field_179476_a.getRNG().nextFloat() * (float)Math.PI * 2.0F;
				float f1 = MathHelper.cos(f) * 0.2F;
				float f2 = -0.1F + this.field_179476_a.getRNG().nextFloat() * 0.2F;
				float f3 = MathHelper.sin(f) * 0.2F;
				this.field_179476_a.func_175568_b(f1, f2, f3);
			}
		}
	}
}