package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class EntitySilverfish extends EntityMob
{
	private EntitySilverfish.AISummonSilverfish field_175460_b;
	private static final String __OBFID = "CL_00001696";

	public EntitySilverfish(World worldIn)
	{
		super(worldIn);
		this.setSize(0.4F, 0.3F);
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(3, this.field_175460_b = new EntitySilverfish.AISummonSilverfish());
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		this.tasks.addTask(5, new EntitySilverfish.AIHideInStone());
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}

	public float getEyeHeight()
	{
		return 0.1F;
	}

	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
	}

	protected boolean canTriggerWalking()
	{
		return false;
	}

	protected String getLivingSound()
	{
		return "mob.silverfish.say";
	}

	protected String getHurtSound()
	{
		return "mob.silverfish.hit";
	}

	protected String getDeathSound()
	{
		return "mob.silverfish.kill";
	}

	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isEntityInvulnerable(source))
		{
			return false;
		}
		else
		{
			if (source instanceof EntityDamageSource || source == DamageSource.magic)
			{
				this.field_175460_b.func_179462_f();
			}

			return super.attackEntityFrom(source, amount);
		}
	}

	protected void playStepSound(BlockPos p_180429_1_, Block p_180429_2_)
	{
		this.playSound("mob.silverfish.step", 0.15F, 1.0F);
	}

	protected Item getDropItem()
	{
		return null;
	}

	public void onUpdate()
	{
		this.renderYawOffset = this.rotationYaw;
		super.onUpdate();
	}

	public float func_180484_a(BlockPos p_180484_1_)
	{
		return this.worldObj.getBlockState(p_180484_1_.down()).getBlock() == Blocks.stone ? 10.0F : super.func_180484_a(p_180484_1_);
	}

	protected boolean isValidLightLevel()
	{
		return true;
	}

	public boolean getCanSpawnHere()
	{
		if (super.getCanSpawnHere())
		{
			EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 5.0D);
			return entityplayer == null;
		}
		else
		{
			return false;
		}
	}

	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.ARTHROPOD;
	}

	class AIHideInStone extends EntityAIWander
	{
		private EnumFacing field_179483_b;
		private boolean field_179484_c;
		private static final String __OBFID = "CL_00002205";

		public AIHideInStone()
		{
			super(EntitySilverfish.this, 1.0D, 10);
			this.setMutexBits(1);
		}

		public boolean shouldExecute()
		{
			if (EntitySilverfish.this.getAttackTarget() != null)
			{
				return false;
			}
			else if (!EntitySilverfish.this.getNavigator().noPath())
			{
				return false;
			}
			else
			{
				Random random = EntitySilverfish.this.getRNG();

				if (random.nextInt(10) == 0)
				{
					this.field_179483_b = EnumFacing.random(random);
					BlockPos blockpos = (new BlockPos(EntitySilverfish.this.posX, EntitySilverfish.this.posY + 0.5D, EntitySilverfish.this.posZ)).offset(this.field_179483_b);
					IBlockState iblockstate = EntitySilverfish.this.worldObj.getBlockState(blockpos);

					if (BlockSilverfish.canContainSilverfish(iblockstate))
					{
						this.field_179484_c = true;
						return true;
					}
				}

				this.field_179484_c = false;
				return super.shouldExecute();
			}
		}

		public boolean continueExecuting()
		{
			return this.field_179484_c ? false : super.continueExecuting();
		}

		public void startExecuting()
		{
			if (!this.field_179484_c)
			{
				super.startExecuting();
			}
			else
			{
				World world = EntitySilverfish.this.worldObj;
				BlockPos blockpos = (new BlockPos(EntitySilverfish.this.posX, EntitySilverfish.this.posY + 0.5D, EntitySilverfish.this.posZ)).offset(this.field_179483_b);
				IBlockState iblockstate = world.getBlockState(blockpos);

				if (BlockSilverfish.canContainSilverfish(iblockstate))
				{
					world.setBlockState(blockpos, Blocks.monster_egg.getDefaultState().withProperty(BlockSilverfish.VARIANT, BlockSilverfish.EnumType.forModelBlock(iblockstate)), 3);
					EntitySilverfish.this.spawnExplosionParticle();
					EntitySilverfish.this.setDead();
				}
			}
		}
	}

	class AISummonSilverfish extends EntityAIBase
	{
		private EntitySilverfish field_179464_a = EntitySilverfish.this;
		private int field_179463_b;
		private static final String __OBFID = "CL_00002204";

		public void func_179462_f()
		{
			if (this.field_179463_b == 0)
			{
				this.field_179463_b = 20;
			}
		}

		public boolean shouldExecute()
		{
			return this.field_179463_b > 0;
		}

		public void updateTask()
		{
			--this.field_179463_b;

			if (this.field_179463_b <= 0)
			{
				World world = this.field_179464_a.worldObj;
				Random random = this.field_179464_a.getRNG();
				BlockPos blockpos = new BlockPos(this.field_179464_a);

				for (int i = 0; i <= 5 && i >= -5; i = i <= 0 ? 1 - i : 0 - i)
				{
					for (int j = 0; j <= 10 && j >= -10; j = j <= 0 ? 1 - j : 0 - j)
					{
						for (int k = 0; k <= 10 && k >= -10; k = k <= 0 ? 1 - k : 0 - k)
						{
							BlockPos blockpos1 = blockpos.add(j, i, k);
							IBlockState iblockstate = world.getBlockState(blockpos1);

							if (iblockstate.getBlock() == Blocks.monster_egg)
							{
								if (world.getGameRules().getGameRuleBooleanValue("mobGriefing"))
								{
									world.destroyBlock(blockpos1, true);
								}
								else
								{
									world.setBlockState(blockpos1, ((BlockSilverfish.EnumType)iblockstate.getValue(BlockSilverfish.VARIANT)).getModelBlock(), 3);
								}

								if (random.nextBoolean())
								{
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