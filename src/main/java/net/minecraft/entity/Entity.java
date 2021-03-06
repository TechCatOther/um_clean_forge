package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Entity implements ICommandSender
{
	private static final AxisAlignedBB ZERO_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	private static int nextEntityID;
	private int entityId;
	public double renderDistanceWeight;
	public boolean preventEntitySpawning;
	public Entity riddenByEntity;
	public Entity ridingEntity;
	public boolean forceSpawn;
	public World worldObj;
	public double prevPosX;
	public double prevPosY;
	public double prevPosZ;
	public double posX;
	public double posY;
	public double posZ;
	public double motionX;
	public double motionY;
	public double motionZ;
	public float rotationYaw;
	public float rotationPitch;
	public float prevRotationYaw;
	public float prevRotationPitch;
	private AxisAlignedBB boundingBox;
	public boolean onGround;
	public boolean isCollidedHorizontally;
	public boolean isCollidedVertically;
	public boolean isCollided;
	public boolean velocityChanged;
	protected boolean isInWeb;
	private boolean isOutsideBorder;
	public boolean isDead;
	public float width;
	public float height;
	public float prevDistanceWalkedModified;
	public float distanceWalkedModified;
	public float distanceWalkedOnStepModified;
	public float fallDistance;
	private int nextStepDistance;
	public double lastTickPosX;
	public double lastTickPosY;
	public double lastTickPosZ;
	public float stepHeight;
	public boolean noClip;
	public float entityCollisionReduction;
	protected Random rand;
	public int ticksExisted;
	public int fireResistance;
	private int fire;
	protected boolean inWater;
	public int hurtResistantTime;
	protected boolean firstUpdate;
	protected boolean isImmuneToFire;
	protected DataWatcher dataWatcher;
	private double entityRiderPitchDelta;
	private double entityRiderYawDelta;
	public boolean addedToChunk;
	public int chunkCoordX;
	public int chunkCoordY;
	public int chunkCoordZ;
	@SideOnly(Side.CLIENT)
	public int serverPosX;
	@SideOnly(Side.CLIENT)
	public int serverPosY;
	@SideOnly(Side.CLIENT)
	public int serverPosZ;
	public boolean ignoreFrustumCheck;
	public boolean isAirBorne;
	public int timeUntilPortal;
	protected boolean inPortal;
	protected int portalCounter;
	public int dimension;
	protected int teleportDirection;
	private boolean invulnerable;
	protected UUID entityUniqueID;
	private final CommandResultStats cmdResultStats;
	private static final String __OBFID = "CL_00001533";

	/** Forge: Used to store custom data for each entity. */
	private NBTTagCompound customEntityData;
	public boolean captureDrops = false;
	public java.util.ArrayList<EntityItem> capturedDrops = new java.util.ArrayList<EntityItem>();
	private UUID persistentID;

	protected java.util.HashMap<String, net.minecraftforge.common.IExtendedEntityProperties> extendedProperties = new java.util.HashMap<String, net.minecraftforge.common.IExtendedEntityProperties>();

	public int getEntityId()
	{
		return this.entityId;
	}

	public void setEntityId(int id)
	{
		this.entityId = id;
	}

	public void onKillCommand()
	{
		this.setDead();
	}

	public Entity(World worldIn)
	{
		this.entityId = nextEntityID++;
		this.renderDistanceWeight = 1.0D;
		this.boundingBox = ZERO_AABB;
		this.width = 0.6F;
		this.height = 1.8F;
		this.nextStepDistance = 1;
		this.rand = new Random();
		this.fireResistance = 1;
		this.firstUpdate = true;
		this.entityUniqueID = MathHelper.getRandomUuid(this.rand);
		this.cmdResultStats = new CommandResultStats();
		this.worldObj = worldIn;
		this.setPosition(0.0D, 0.0D, 0.0D);

		if (worldIn != null)
		{
			this.dimension = worldIn.provider.getDimensionId();
		}

		this.dataWatcher = new DataWatcher(this);
		this.dataWatcher.addObject(0, Byte.valueOf((byte)0));
		this.dataWatcher.addObject(1, Short.valueOf((short)300));
		this.dataWatcher.addObject(3, Byte.valueOf((byte)0));
		this.dataWatcher.addObject(2, "");
		this.dataWatcher.addObject(4, Byte.valueOf((byte)0));
		this.entityInit();
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.EntityEvent.EntityConstructing(this));
		for (net.minecraftforge.common.IExtendedEntityProperties props : extendedProperties.values()) props.init(this, worldIn);
	}

	protected abstract void entityInit();

	public DataWatcher getDataWatcher()
	{
		return this.dataWatcher;
	}

	public boolean equals(Object p_equals_1_)
	{
		return p_equals_1_ instanceof Entity ? ((Entity)p_equals_1_).entityId == this.entityId : false;
	}

	public int hashCode()
	{
		return this.entityId;
	}

	@SideOnly(Side.CLIENT)
	protected void preparePlayerToSpawn()
	{
		if (this.worldObj != null)
		{
			while (this.posY > 0.0D && this.posY < 256.0D)
			{
				this.setPosition(this.posX, this.posY, this.posZ);

				if (this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty())
				{
					break;
				}

				++this.posY;
			}

			this.motionX = this.motionY = this.motionZ = 0.0D;
			this.rotationPitch = 0.0F;
		}
	}

	public void setDead()
	{
		this.isDead = true;
	}

	protected void setSize(float width, float height)
	{
		if (width != this.width || height != this.height)
		{
			float f2 = this.width;
			this.width = width;
			this.height = height;
			this.setEntityBoundingBox(new AxisAlignedBB(this.getEntityBoundingBox().minX, this.getEntityBoundingBox().minY, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().minX + (double)this.width, this.getEntityBoundingBox().minY + (double)this.height, this.getEntityBoundingBox().minZ + (double)this.width));

			if (this.width > f2 && !this.firstUpdate && !this.worldObj.isRemote)
			{
				this.moveEntity((double)(f2 - this.width), 0.0D, (double)(f2 - this.width));
			}
		}
	}

	protected void setRotation(float yaw, float pitch)
	{
		this.rotationYaw = yaw % 360.0F;
		this.rotationPitch = pitch % 360.0F;
	}

	public void setPosition(double x, double y, double z)
	{
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		float f = this.width / 2.0F;
		float f1 = this.height;
		this.setEntityBoundingBox(new AxisAlignedBB(x - (double)f, y, z - (double)f, x + (double)f, y + (double)f1, z + (double)f));
	}

	@SideOnly(Side.CLIENT)
	public void setAngles(float yaw, float pitch)
	{
		float f2 = this.rotationPitch;
		float f3 = this.rotationYaw;
		this.rotationYaw = (float)((double)this.rotationYaw + (double)yaw * 0.15D);
		this.rotationPitch = (float)((double)this.rotationPitch - (double)pitch * 0.15D);
		this.rotationPitch = MathHelper.clamp_float(this.rotationPitch, -90.0F, 90.0F);
		this.prevRotationPitch += this.rotationPitch - f2;
		this.prevRotationYaw += this.rotationYaw - f3;
	}

	public void onUpdate()
	{
		this.onEntityUpdate();
	}

	public void onEntityUpdate()
	{
		this.worldObj.theProfiler.startSection("entityBaseTick");

		if (this.ridingEntity != null && this.ridingEntity.isDead)
		{
			this.ridingEntity = null;
		}

		this.prevDistanceWalkedModified = this.distanceWalkedModified;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.prevRotationPitch = this.rotationPitch;
		this.prevRotationYaw = this.rotationYaw;

		if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer)
		{
			this.worldObj.theProfiler.startSection("portal");
			MinecraftServer minecraftserver = ((WorldServer)this.worldObj).getMinecraftServer();
			int i = this.getMaxInPortalTime();

			if (this.inPortal)
			{
				if (minecraftserver.getAllowNether())
				{
					if (this.ridingEntity == null && this.portalCounter++ >= i)
					{
						this.portalCounter = i;
						this.timeUntilPortal = this.getPortalCooldown();
						byte b0;

						if (this.worldObj.provider.getDimensionId() == -1)
						{
							b0 = 0;
						}
						else
						{
							b0 = -1;
						}

						this.travelToDimension(b0);
					}

					this.inPortal = false;
				}
			}
			else
			{
				if (this.portalCounter > 0)
				{
					this.portalCounter -= 4;
				}

				if (this.portalCounter < 0)
				{
					this.portalCounter = 0;
				}
			}

			if (this.timeUntilPortal > 0)
			{
				--this.timeUntilPortal;
			}

			this.worldObj.theProfiler.endSection();
		}

		this.spawnRunningParticles();
		this.handleWaterMovement();

		if (this.worldObj.isRemote)
		{
			this.fire = 0;
		}
		else if (this.fire > 0)
		{
			if (this.isImmuneToFire)
			{
				this.fire -= 4;

				if (this.fire < 0)
				{
					this.fire = 0;
				}
			}
			else
			{
				if (this.fire % 20 == 0)
				{
					this.attackEntityFrom(DamageSource.onFire, 1.0F);
				}

				--this.fire;
			}
		}

		if (this.isInLava())
		{
			this.setOnFireFromLava();
			this.fallDistance *= 0.5F;
		}

		if (this.posY < -64.0D)
		{
			this.kill();
		}

		if (!this.worldObj.isRemote)
		{
			this.setFlag(0, this.fire > 0);
		}

		this.firstUpdate = false;
		this.worldObj.theProfiler.endSection();
	}

	public int getMaxInPortalTime()
	{
		return 0;
	}

	protected void setOnFireFromLava()
	{
		if (!this.isImmuneToFire)
		{
			this.attackEntityFrom(DamageSource.lava, 4.0F);
			this.setFire(15);
		}
	}

	public void setFire(int seconds)
	{
		int j = seconds * 20;
		j = EnchantmentProtection.getFireTimeForEntity(this, j);

		if (this.fire < j)
		{
			this.fire = j;
		}
	}

	public void extinguish()
	{
		this.fire = 0;
	}

	protected void kill()
	{
		this.setDead();
	}

	public boolean isOffsetPositionInLiquid(double x, double y, double z)
	{
		AxisAlignedBB axisalignedbb = this.getEntityBoundingBox().offset(x, y, z);
		return this.isLiquidPresentInAABB(axisalignedbb);
	}

	private boolean isLiquidPresentInAABB(AxisAlignedBB p_174809_1_)
	{
		return this.worldObj.getCollidingBoundingBoxes(this, p_174809_1_).isEmpty() && !this.worldObj.isAnyLiquid(p_174809_1_);
	}

	public void moveEntity(double x, double y, double z)
	{
		if (this.noClip)
		{
			this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, y, z));
			this.resetPositionToBB();
		}
		else
		{
			this.worldObj.theProfiler.startSection("move");
			double d3 = this.posX;
			double d4 = this.posY;
			double d5 = this.posZ;

			if (this.isInWeb)
			{
				this.isInWeb = false;
				x *= 0.25D;
				y *= 0.05000000074505806D;
				z *= 0.25D;
				this.motionX = 0.0D;
				this.motionY = 0.0D;
				this.motionZ = 0.0D;
			}

			double d6 = x;
			double d7 = y;
			double d8 = z;
			boolean flag = this.onGround && this.isSneaking() && this instanceof EntityPlayer;

			if (flag)
			{
				double d9;

				for (d9 = 0.05D; x != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0D, 0.0D)).isEmpty(); d6 = x)
				{
					if (x < d9 && x >= -d9)
					{
						x = 0.0D;
					}
					else if (x > 0.0D)
					{
						x -= d9;
					}
					else
					{
						x += d9;
					}
				}

				for (; z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(0.0D, -1.0D, z)).isEmpty(); d8 = z)
				{
					if (z < d9 && z >= -d9)
					{
						z = 0.0D;
					}
					else if (z > 0.0D)
					{
						z -= d9;
					}
					else
					{
						z += d9;
					}
				}

				for (; x != 0.0D && z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().offset(x, -1.0D, z)).isEmpty(); d8 = z)
				{
					if (x < d9 && x >= -d9)
					{
						x = 0.0D;
					}
					else if (x > 0.0D)
					{
						x -= d9;
					}
					else
					{
						x += d9;
					}

					d6 = x;

					if (z < d9 && z >= -d9)
					{
						z = 0.0D;
					}
					else if (z > 0.0D)
					{
						z -= d9;
					}
					else
					{
						z += d9;
					}
				}
			}

			List list1 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(x, y, z));
			AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
			AxisAlignedBB axisalignedbb1;

			for (Iterator iterator = list1.iterator(); iterator.hasNext(); y = axisalignedbb1.calculateYOffset(this.getEntityBoundingBox(), y))
			{
				axisalignedbb1 = (AxisAlignedBB)iterator.next();
			}

			this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));
			boolean flag1 = this.onGround || d7 != y && d7 < 0.0D;
			AxisAlignedBB axisalignedbb2;
			Iterator iterator8;

			for (iterator8 = list1.iterator(); iterator8.hasNext(); x = axisalignedbb2.calculateXOffset(this.getEntityBoundingBox(), x))
			{
				axisalignedbb2 = (AxisAlignedBB)iterator8.next();
			}

			this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0D, 0.0D));

			for (iterator8 = list1.iterator(); iterator8.hasNext(); z = axisalignedbb2.calculateZOffset(this.getEntityBoundingBox(), z))
			{
				axisalignedbb2 = (AxisAlignedBB)iterator8.next();
			}

			this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, 0.0D, z));

			if (this.stepHeight > 0.0F && flag1 && (d6 != x || d8 != z))
			{
				double d14 = x;
				double d10 = y;
				double d11 = z;
				AxisAlignedBB axisalignedbb3 = this.getEntityBoundingBox();
				this.setEntityBoundingBox(axisalignedbb);
				y = (double)this.stepHeight;
				List list = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(d6, y, d8));
				AxisAlignedBB axisalignedbb4 = this.getEntityBoundingBox();
				AxisAlignedBB axisalignedbb5 = axisalignedbb4.addCoord(d6, 0.0D, d8);
				double d12 = y;
				AxisAlignedBB axisalignedbb6;

				for (Iterator iterator1 = list.iterator(); iterator1.hasNext(); d12 = axisalignedbb6.calculateYOffset(axisalignedbb5, d12))
				{
					axisalignedbb6 = (AxisAlignedBB)iterator1.next();
				}

				axisalignedbb4 = axisalignedbb4.offset(0.0D, d12, 0.0D);
				double d18 = d6;
				AxisAlignedBB axisalignedbb7;

				for (Iterator iterator2 = list.iterator(); iterator2.hasNext(); d18 = axisalignedbb7.calculateXOffset(axisalignedbb4, d18))
				{
					axisalignedbb7 = (AxisAlignedBB)iterator2.next();
				}

				axisalignedbb4 = axisalignedbb4.offset(d18, 0.0D, 0.0D);
				double d19 = d8;
				AxisAlignedBB axisalignedbb8;

				for (Iterator iterator3 = list.iterator(); iterator3.hasNext(); d19 = axisalignedbb8.calculateZOffset(axisalignedbb4, d19))
				{
					axisalignedbb8 = (AxisAlignedBB)iterator3.next();
				}

				axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d19);
				AxisAlignedBB axisalignedbb13 = this.getEntityBoundingBox();
				double d20 = y;
				AxisAlignedBB axisalignedbb9;

				for (Iterator iterator4 = list.iterator(); iterator4.hasNext(); d20 = axisalignedbb9.calculateYOffset(axisalignedbb13, d20))
				{
					axisalignedbb9 = (AxisAlignedBB)iterator4.next();
				}

				axisalignedbb13 = axisalignedbb13.offset(0.0D, d20, 0.0D);
				double d21 = d6;
				AxisAlignedBB axisalignedbb10;

				for (Iterator iterator5 = list.iterator(); iterator5.hasNext(); d21 = axisalignedbb10.calculateXOffset(axisalignedbb13, d21))
				{
					axisalignedbb10 = (AxisAlignedBB)iterator5.next();
				}

				axisalignedbb13 = axisalignedbb13.offset(d21, 0.0D, 0.0D);
				double d22 = d8;
				AxisAlignedBB axisalignedbb11;

				for (Iterator iterator6 = list.iterator(); iterator6.hasNext(); d22 = axisalignedbb11.calculateZOffset(axisalignedbb13, d22))
				{
					axisalignedbb11 = (AxisAlignedBB)iterator6.next();
				}

				axisalignedbb13 = axisalignedbb13.offset(0.0D, 0.0D, d22);
				double d23 = d18 * d18 + d19 * d19;
				double d13 = d21 * d21 + d22 * d22;

				if (d23 > d13)
				{
					x = d18;
					z = d19;
					this.setEntityBoundingBox(axisalignedbb4);
				}
				else
				{
					x = d21;
					z = d22;
					this.setEntityBoundingBox(axisalignedbb13);
				}

				y = (double)(-this.stepHeight);
				AxisAlignedBB axisalignedbb12;

				for (Iterator iterator7 = list.iterator(); iterator7.hasNext(); y = axisalignedbb12.calculateYOffset(this.getEntityBoundingBox(), y))
				{
					axisalignedbb12 = (AxisAlignedBB)iterator7.next();
				}

				this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0D, y, 0.0D));

				if (d14 * d14 + d11 * d11 >= x * x + z * z)
				{
					x = d14;
					y = d10;
					z = d11;
					this.setEntityBoundingBox(axisalignedbb3);
				}
			}

			this.worldObj.theProfiler.endSection();
			this.worldObj.theProfiler.startSection("rest");
			this.resetPositionToBB();
			this.isCollidedHorizontally = d6 != x || d8 != z;
			this.isCollidedVertically = d7 != y;
			this.onGround = this.isCollidedVertically && d7 < 0.0D;
			this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
			int i = MathHelper.floor_double(this.posX);
			int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
			int k = MathHelper.floor_double(this.posZ);
			BlockPos blockpos = new BlockPos(i, j, k);
			Block block1 = this.worldObj.getBlockState(blockpos).getBlock();

			if (block1.getMaterial() == Material.air)
			{
				Block block = this.worldObj.getBlockState(blockpos.down()).getBlock();

				if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate)
				{
					block1 = block;
					blockpos = blockpos.down();
				}
			}

			this.func_180433_a(y, this.onGround, block1, blockpos);

			if (d6 != x)
			{
				this.motionX = 0.0D;
			}

			if (d8 != z)
			{
				this.motionZ = 0.0D;
			}

			if (d7 != y)
			{
				block1.onLanded(this.worldObj, this);
			}

			if (this.canTriggerWalking() && !flag && this.ridingEntity == null)
			{
				double d15 = this.posX - d3;
				double d16 = this.posY - d4;
				double d17 = this.posZ - d5;

				if (block1 != Blocks.ladder)
				{
					d16 = 0.0D;
				}

				if (block1 != null && this.onGround)
				{
					block1.onEntityCollidedWithBlock(this.worldObj, blockpos, this);
				}

				this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(d15 * d15 + d17 * d17) * 0.6D);
				this.distanceWalkedOnStepModified = (float)((double)this.distanceWalkedOnStepModified + (double)MathHelper.sqrt_double(d15 * d15 + d16 * d16 + d17 * d17) * 0.6D);

				if (this.distanceWalkedOnStepModified > (float)this.nextStepDistance && block1.getMaterial() != Material.air)
				{
					this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;

					if (this.isInWater())
					{
						float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.35F;

						if (f > 1.0F)
						{
							f = 1.0F;
						}

						this.playSound(this.getSwimSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
					}

					this.playStepSound(blockpos, block1);
				}
			}

			try
			{
				this.doBlockCollisions();
			}
			catch (Throwable throwable)
			{
				CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
				this.addEntityCrashInfo(crashreportcategory);
				throw new ReportedException(crashreport);
			}

			boolean flag2 = this.isWet();

			if (this.worldObj.func_147470_e(this.getEntityBoundingBox().contract(0.001D, 0.001D, 0.001D)))
			{
				this.dealFireDamage(1);

				if (!flag2)
				{
					++this.fire;

					if (this.fire == 0)
					{
						this.setFire(8);
					}
				}
			}
			else if (this.fire <= 0)
			{
				this.fire = -this.fireResistance;
			}

			if (flag2 && this.fire > 0)
			{
				this.playSound("random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
				this.fire = -this.fireResistance;
			}

			this.worldObj.theProfiler.endSection();
		}
	}

	private void resetPositionToBB()
	{
		this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0D;
		this.posY = this.getEntityBoundingBox().minY;
		this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0D;
	}

	protected String getSwimSound()
	{
		return "game.neutral.swim";
	}

	protected void doBlockCollisions()
	{
		BlockPos blockpos = new BlockPos(this.getEntityBoundingBox().minX + 0.001D, this.getEntityBoundingBox().minY + 0.001D, this.getEntityBoundingBox().minZ + 0.001D);
		BlockPos blockpos1 = new BlockPos(this.getEntityBoundingBox().maxX - 0.001D, this.getEntityBoundingBox().maxY - 0.001D, this.getEntityBoundingBox().maxZ - 0.001D);

		if (this.worldObj.isAreaLoaded(blockpos, blockpos1))
		{
			for (int i = blockpos.getX(); i <= blockpos1.getX(); ++i)
			{
				for (int j = blockpos.getY(); j <= blockpos1.getY(); ++j)
				{
					for (int k = blockpos.getZ(); k <= blockpos1.getZ(); ++k)
					{
						BlockPos blockpos2 = new BlockPos(i, j, k);
						IBlockState iblockstate = this.worldObj.getBlockState(blockpos2);

						try
						{
							iblockstate.getBlock().onEntityCollidedWithBlock(this.worldObj, blockpos2, iblockstate, this);
						}
						catch (Throwable throwable)
						{
							CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
							CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
							CrashReportCategory.addBlockInfo(crashreportcategory, blockpos2, iblockstate);
							throw new ReportedException(crashreport);
						}
					}
				}
			}
		}
	}

	protected void playStepSound(BlockPos p_180429_1_, Block p_180429_2_)
	{
		Block.SoundType soundtype = p_180429_2_.stepSound;

		if (this.worldObj.getBlockState(p_180429_1_.up()).getBlock() == Blocks.snow_layer)
		{
			soundtype = Blocks.snow_layer.stepSound;
			this.playSound(soundtype.getStepSound(), soundtype.getVolume() * 0.15F, soundtype.getFrequency());
		}
		else if (!p_180429_2_.getMaterial().isLiquid())
		{
			this.playSound(soundtype.getStepSound(), soundtype.getVolume() * 0.15F, soundtype.getFrequency());
		}
	}

	public void playSound(String name, float volume, float pitch)
	{
		if (!this.isSilent())
		{
			this.worldObj.playSoundAtEntity(this, name, volume, pitch);
		}
	}

	public boolean isSilent()
	{
		return this.dataWatcher.getWatchableObjectByte(4) == 1;
	}

	public void setSilent(boolean isSilent)
	{
		this.dataWatcher.updateObject(4, Byte.valueOf((byte)(isSilent ? 1 : 0)));
	}

	protected boolean canTriggerWalking()
	{
		return true;
	}

	protected void func_180433_a(double p_180433_1_, boolean p_180433_3_, Block p_180433_4_, BlockPos p_180433_5_)
	{
		if (p_180433_3_)
		{
			if (this.fallDistance > 0.0F)
			{
				if (p_180433_4_ != null)
				{
					p_180433_4_.onFallenUpon(this.worldObj, p_180433_5_, this, this.fallDistance);
				}
				else
				{
					this.fall(this.fallDistance, 1.0F);
				}

				this.fallDistance = 0.0F;
			}
		}
		else if (p_180433_1_ < 0.0D)
		{
			this.fallDistance = (float)((double)this.fallDistance - p_180433_1_);
		}
	}

	public AxisAlignedBB getBoundingBox()
	{
		return null;
	}

	protected void dealFireDamage(int amount)
	{
		if (!this.isImmuneToFire)
		{
			this.attackEntityFrom(DamageSource.inFire, (float)amount);
		}
	}

	public final boolean isImmuneToFire()
	{
		return this.isImmuneToFire;
	}

	public void fall(float distance, float damageMultiplier)
	{
		if (this.riddenByEntity != null)
		{
			this.riddenByEntity.fall(distance, damageMultiplier);
		}
	}

	public boolean isWet()
	{
		return this.inWater || this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY, this.posZ)) || this.worldObj.canLightningStrike(new BlockPos(this.posX, this.posY + (double)this.height, this.posZ));
	}

	public boolean isInWater()
	{
		return this.inWater;
	}

	public boolean handleWaterMovement()
	{
		if (this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this))
		{
			if (!this.inWater && !this.firstUpdate)
			{
				this.resetHeight();
			}

			this.fallDistance = 0.0F;
			this.inWater = true;
			this.fire = 0;
		}
		else
		{
			this.inWater = false;
		}

		return this.inWater;
	}

	protected void resetHeight()
	{
		float f = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.2F;

		if (f > 1.0F)
		{
			f = 1.0F;
		}

		this.playSound(this.getSplashSound(), f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
		float f1 = (float)MathHelper.floor_double(this.getEntityBoundingBox().minY);
		int i;
		float f2;
		float f3;

		for (i = 0; (float)i < 1.0F + this.width * 20.0F; ++i)
		{
			f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
			f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
			this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (double)f2, (double)(f1 + 1.0F), this.posZ + (double)f3, this.motionX, this.motionY - (double)(this.rand.nextFloat() * 0.2F), this.motionZ, new int[0]);
		}

		for (i = 0; (float)i < 1.0F + this.width * 20.0F; ++i)
		{
			f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
			f3 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
			this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (double)f2, (double)(f1 + 1.0F), this.posZ + (double)f3, this.motionX, this.motionY, this.motionZ, new int[0]);
		}
	}

	public void spawnRunningParticles()
	{
		if (this.isSprinting() && !this.isInWater())
		{
			this.createRunningParticles();
		}
	}

	protected void createRunningParticles()
	{
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
		int k = MathHelper.floor_double(this.posZ);
		BlockPos blockpos = new BlockPos(i, j, k);
		IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
		Block block = iblockstate.getBlock();

		if (block.getRenderType() != -1)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D, new int[] {Block.getStateId(iblockstate)});
		}
	}

	protected String getSplashSound()
	{
		return "game.neutral.swim.splash";
	}

	public boolean isInsideOfMaterial(Material materialIn)
	{
		double d0 = this.posY + (double)this.getEyeHeight();
		BlockPos blockpos = new BlockPos(this.posX, d0, this.posZ);
		IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
		Block block = iblockstate.getBlock();

		if (block.getMaterial() == materialIn)
		{
			return net.minecraftforge.common.ForgeHooks.isInsideOfMaterial(materialIn, this, blockpos);
		}
		else
		{
			return false;
		}
	}

	public boolean isInLava()
	{
		return this.worldObj.isMaterialInBB(this.getEntityBoundingBox().expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
	}

	public void moveFlying(float strafe, float forward, float friction)
	{
		float f3 = strafe * strafe + forward * forward;

		if (f3 >= 1.0E-4F)
		{
			f3 = MathHelper.sqrt_float(f3);

			if (f3 < 1.0F)
			{
				f3 = 1.0F;
			}

			f3 = friction / f3;
			strafe *= f3;
			forward *= f3;
			float f4 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
			float f5 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
			this.motionX += (double)(strafe * f5 - forward * f4);
			this.motionZ += (double)(forward * f5 + strafe * f4);
		}
	}

	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_)
	{
		BlockPos blockpos = new BlockPos(this.posX, 0.0D, this.posZ);

		if (this.worldObj.isBlockLoaded(blockpos))
		{
			double d0 = (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * 0.66D;
			int i = MathHelper.floor_double(this.posY + d0);
			return this.worldObj.getCombinedLight(blockpos.up(i), 0);
		}
		else
		{
			return 0;
		}
	}

	public float getBrightness(float p_70013_1_)
	{
		BlockPos blockpos = new BlockPos(this.posX, 0.0D, this.posZ);

		if (this.worldObj.isBlockLoaded(blockpos))
		{
			double d0 = (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * 0.66D;
			int i = MathHelper.floor_double(this.posY + d0);
			return this.worldObj.getLightBrightness(blockpos.up(i));
		}
		else
		{
			return 0.0F;
		}
	}

	public void setWorld(World worldIn)
	{
		this.worldObj = worldIn;
	}

	public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch)
	{
		this.prevPosX = this.posX = x;
		this.prevPosY = this.posY = y;
		this.prevPosZ = this.posZ = z;
		this.prevRotationYaw = this.rotationYaw = yaw;
		this.prevRotationPitch = this.rotationPitch = pitch;
		double d3 = (double)(this.prevRotationYaw - yaw);

		if (d3 < -180.0D)
		{
			this.prevRotationYaw += 360.0F;
		}

		if (d3 >= 180.0D)
		{
			this.prevRotationYaw -= 360.0F;
		}

		this.setPosition(this.posX, this.posY, this.posZ);
		this.setRotation(yaw, pitch);
	}

	public void moveToBlockPosAndAngles(BlockPos p_174828_1_, float p_174828_2_, float p_174828_3_)
	{
		this.setLocationAndAngles((double)p_174828_1_.getX() + 0.5D, (double)p_174828_1_.getY(), (double)p_174828_1_.getZ() + 0.5D, p_174828_2_, p_174828_3_);
	}

	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
	{
		this.lastTickPosX = this.prevPosX = this.posX = x;
		this.lastTickPosY = this.prevPosY = this.posY = y;
		this.lastTickPosZ = this.prevPosZ = this.posZ = z;
		this.rotationYaw = yaw;
		this.rotationPitch = pitch;
		this.setPosition(this.posX, this.posY, this.posZ);
	}

	public float getDistanceToEntity(Entity entityIn)
	{
		float f = (float)(this.posX - entityIn.posX);
		float f1 = (float)(this.posY - entityIn.posY);
		float f2 = (float)(this.posZ - entityIn.posZ);
		return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
	}

	public double getDistanceSq(double x, double y, double z)
	{
		double d3 = this.posX - x;
		double d4 = this.posY - y;
		double d5 = this.posZ - z;
		return d3 * d3 + d4 * d4 + d5 * d5;
	}

	public double getDistanceSq(BlockPos p_174818_1_)
	{
		return p_174818_1_.distanceSq(this.posX, this.posY, this.posZ);
	}

	public double getDistanceSqToCenter(BlockPos p_174831_1_)
	{
		return p_174831_1_.distanceSqToCenter(this.posX, this.posY, this.posZ);
	}

	public double getDistance(double x, double y, double z)
	{
		double d3 = this.posX - x;
		double d4 = this.posY - y;
		double d5 = this.posZ - z;
		return (double)MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
	}

	public double getDistanceSqToEntity(Entity entityIn)
	{
		double d0 = this.posX - entityIn.posX;
		double d1 = this.posY - entityIn.posY;
		double d2 = this.posZ - entityIn.posZ;
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	public void onCollideWithPlayer(EntityPlayer entityIn) {}

	public void applyEntityCollision(Entity entityIn)
	{
		if (entityIn.riddenByEntity != this && entityIn.ridingEntity != this)
		{
			if (!entityIn.noClip && !this.noClip)
			{
				double d0 = entityIn.posX - this.posX;
				double d1 = entityIn.posZ - this.posZ;
				double d2 = MathHelper.abs_max(d0, d1);

				if (d2 >= 0.009999999776482582D)
				{
					d2 = (double)MathHelper.sqrt_double(d2);
					d0 /= d2;
					d1 /= d2;
					double d3 = 1.0D / d2;

					if (d3 > 1.0D)
					{
						d3 = 1.0D;
					}

					d0 *= d3;
					d1 *= d3;
					d0 *= 0.05000000074505806D;
					d1 *= 0.05000000074505806D;
					d0 *= (double)(1.0F - this.entityCollisionReduction);
					d1 *= (double)(1.0F - this.entityCollisionReduction);

					if (this.riddenByEntity == null)
					{
						this.addVelocity(-d0, 0.0D, -d1);
					}

					if (entityIn.riddenByEntity == null)
					{
						entityIn.addVelocity(d0, 0.0D, d1);
					}
				}
			}
		}
	}

	public void addVelocity(double x, double y, double z)
	{
		this.motionX += x;
		this.motionY += y;
		this.motionZ += z;
		this.isAirBorne = true;
	}

	protected void setBeenAttacked()
	{
		this.velocityChanged = true;
	}

	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isEntityInvulnerable(source))
		{
			return false;
		}
		else
		{
			this.setBeenAttacked();
			return false;
		}
	}

	public Vec3 getLook(float p_70676_1_)
	{
		if (p_70676_1_ == 1.0F)
		{
			return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
		}
		else
		{
			float f1 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * p_70676_1_;
			float f2 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * p_70676_1_;
			return this.getVectorForRotation(f1, f2);
		}
	}

	protected final Vec3 getVectorForRotation(float pitch, float yaw)
	{
		float f2 = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
		float f3 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
		float f4 = -MathHelper.cos(-pitch * 0.017453292F);
		float f5 = MathHelper.sin(-pitch * 0.017453292F);
		return new Vec3((double)(f3 * f4), (double)f5, (double)(f2 * f4));
	}

	@SideOnly(Side.CLIENT)
	public Vec3 getPositionEyes(float p_174824_1_)
	{
		if (p_174824_1_ == 1.0F)
		{
			return new Vec3(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
		}
		else
		{
			double d0 = this.prevPosX + (this.posX - this.prevPosX) * (double)p_174824_1_;
			double d1 = this.prevPosY + (this.posY - this.prevPosY) * (double)p_174824_1_ + (double)this.getEyeHeight();
			double d2 = this.prevPosZ + (this.posZ - this.prevPosZ) * (double)p_174824_1_;
			return new Vec3(d0, d1, d2);
		}
	}

	@SideOnly(Side.CLIENT)
	public MovingObjectPosition rayTrace(double p_174822_1_, float p_174822_3_)
	{
		Vec3 vec3 = this.getPositionEyes(p_174822_3_);
		Vec3 vec31 = this.getLook(p_174822_3_);
		Vec3 vec32 = vec3.addVector(vec31.xCoord * p_174822_1_, vec31.yCoord * p_174822_1_, vec31.zCoord * p_174822_1_);
		return this.worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
	}

	public boolean canBeCollidedWith()
	{
		return false;
	}

	public boolean canBePushed()
	{
		return false;
	}

	public void addToPlayerScore(Entity entityIn, int amount) {}

	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRender3d(double x, double y, double z)
	{
		double d3 = this.posX - x;
		double d4 = this.posY - y;
		double d5 = this.posZ - z;
		double d6 = d3 * d3 + d4 * d4 + d5 * d5;
		return this.isInRangeToRenderDist(d6);
	}

	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance)
	{
		double d1 = this.getEntityBoundingBox().getAverageEdgeLength();
		d1 *= 64.0D * this.renderDistanceWeight;
		return distance < d1 * d1;
	}

	public boolean writeMountToNBT(NBTTagCompound tagCompund)
	{
		String s = this.getEntityString();

		if (!this.isDead && s != null)
		{
			tagCompund.setString("id", s);
			this.writeToNBT(tagCompund);
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean writeToNBTOptional(NBTTagCompound tagCompund)
	{
		String s = this.getEntityString();

		if (!this.isDead && s != null && this.riddenByEntity == null)
		{
			tagCompund.setString("id", s);
			this.writeToNBT(tagCompund);
			return true;
		}
		else
		{
			return false;
		}
	}

	public void writeToNBT(NBTTagCompound tagCompund)
	{
		try
		{
			tagCompund.setTag("Pos", this.newDoubleNBTList(new double[] {this.posX, this.posY, this.posZ}));
			tagCompund.setTag("Motion", this.newDoubleNBTList(new double[] {this.motionX, this.motionY, this.motionZ}));
			tagCompund.setTag("Rotation", this.newFloatNBTList(new float[] {this.rotationYaw, this.rotationPitch}));
			tagCompund.setFloat("FallDistance", this.fallDistance);
			tagCompund.setShort("Fire", (short)this.fire);
			tagCompund.setShort("Air", (short)this.getAir());
			tagCompund.setBoolean("OnGround", this.onGround);
			tagCompund.setInteger("Dimension", this.dimension);
			tagCompund.setBoolean("Invulnerable", this.invulnerable);
			tagCompund.setInteger("PortalCooldown", this.timeUntilPortal);
			tagCompund.setLong("UUIDMost", this.getUniqueID().getMostSignificantBits());
			tagCompund.setLong("UUIDLeast", this.getUniqueID().getLeastSignificantBits());

			if (this.getCustomNameTag() != null && this.getCustomNameTag().length() > 0)
			{
				tagCompund.setString("CustomName", this.getCustomNameTag());
				tagCompund.setBoolean("CustomNameVisible", this.getAlwaysRenderNameTag());
			}

			this.cmdResultStats.func_179670_b(tagCompund);

			if (this.isSilent())
			{
				tagCompund.setBoolean("Silent", this.isSilent());
			}

			if (customEntityData != null) tagCompund.setTag("ForgeData", customEntityData);
			for (String identifier : this.extendedProperties.keySet())
			{
				try
				{
					net.minecraftforge.common.IExtendedEntityProperties props = this.extendedProperties.get(identifier);
					props.saveNBTData(tagCompund);
				}
				catch (Throwable t)
				{
					net.minecraftforge.fml.common.FMLLog.severe("Failed to save extended properties for %s.  This is a mod issue.", identifier);
					t.printStackTrace();
				}
			}

			this.writeEntityToNBT(tagCompund);

			if (this.ridingEntity != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();

				if (this.ridingEntity.writeMountToNBT(nbttagcompound1))
				{
					tagCompund.setTag("Riding", nbttagcompound1);
				}
			}
		}
		catch (Throwable throwable)
		{
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Saving entity NBT");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being saved");
			this.addEntityCrashInfo(crashreportcategory);
			throw new ReportedException(crashreport);
		}
	}

	public void readFromNBT(NBTTagCompound tagCompund)
	{
		try
		{
			NBTTagList nbttaglist = tagCompund.getTagList("Pos", 6);
			NBTTagList nbttaglist1 = tagCompund.getTagList("Motion", 6);
			NBTTagList nbttaglist2 = tagCompund.getTagList("Rotation", 5);
			this.motionX = nbttaglist1.getDouble(0);
			this.motionY = nbttaglist1.getDouble(1);
			this.motionZ = nbttaglist1.getDouble(2);

			if (Math.abs(this.motionX) > 10.0D)
			{
				this.motionX = 0.0D;
			}

			if (Math.abs(this.motionY) > 10.0D)
			{
				this.motionY = 0.0D;
			}

			if (Math.abs(this.motionZ) > 10.0D)
			{
				this.motionZ = 0.0D;
			}

			this.prevPosX = this.lastTickPosX = this.posX = nbttaglist.getDouble(0);
			this.prevPosY = this.lastTickPosY = this.posY = nbttaglist.getDouble(1);
			this.prevPosZ = this.lastTickPosZ = this.posZ = nbttaglist.getDouble(2);
			this.prevRotationYaw = this.rotationYaw = nbttaglist2.getFloat(0);
			this.prevRotationPitch = this.rotationPitch = nbttaglist2.getFloat(1);
			this.fallDistance = tagCompund.getFloat("FallDistance");
			this.fire = tagCompund.getShort("Fire");
			this.setAir(tagCompund.getShort("Air"));
			this.onGround = tagCompund.getBoolean("OnGround");
			this.dimension = tagCompund.getInteger("Dimension");
			this.invulnerable = tagCompund.getBoolean("Invulnerable");
			this.timeUntilPortal = tagCompund.getInteger("PortalCooldown");

			if (tagCompund.hasKey("UUIDMost", 4) && tagCompund.hasKey("UUIDLeast", 4))
			{
				this.entityUniqueID = new UUID(tagCompund.getLong("UUIDMost"), tagCompund.getLong("UUIDLeast"));
			}
			else if (tagCompund.hasKey("UUID", 8))
			{
				this.entityUniqueID = UUID.fromString(tagCompund.getString("UUID"));
			}

			this.setPosition(this.posX, this.posY, this.posZ);
			this.setRotation(this.rotationYaw, this.rotationPitch);

			if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0)
			{
				this.setCustomNameTag(tagCompund.getString("CustomName"));
			}

			this.setAlwaysRenderNameTag(tagCompund.getBoolean("CustomNameVisible"));
			this.cmdResultStats.func_179668_a(tagCompund);
			this.setSilent(tagCompund.getBoolean("Silent"));

			if (tagCompund.hasKey("ForgeData")) customEntityData = tagCompund.getCompoundTag("ForgeData");
			for (String identifier : this.extendedProperties.keySet())
			{
				try
				{
					net.minecraftforge.common.IExtendedEntityProperties props = this.extendedProperties.get(identifier);
					props.loadNBTData(tagCompund);
				}
				catch (Throwable t)
				{
					net.minecraftforge.fml.common.FMLLog.severe("Failed to load extended properties for %s.  This is a mod issue.", identifier);
					t.printStackTrace();
				}
			}

			//Rawr, legacy code, Vanilla added a UUID, keep this so older maps will convert properly
			if (tagCompund.hasKey("PersistentIDMSB") && tagCompund.hasKey("PersistentIDLSB"))
			{
				this.entityUniqueID = new UUID(tagCompund.getLong("PersistentIDMSB"), tagCompund.getLong("PersistentIDLSB"));
			}

			this.readEntityFromNBT(tagCompund);

			if (this.shouldSetPosAfterLoading())
			{
				this.setPosition(this.posX, this.posY, this.posZ);
			}
		}
		catch (Throwable throwable)
		{
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Loading entity NBT");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being loaded");
			this.addEntityCrashInfo(crashreportcategory);
			throw new ReportedException(crashreport);
		}
	}

	protected boolean shouldSetPosAfterLoading()
	{
		return true;
	}

	protected final String getEntityString()
	{
		return EntityList.getEntityString(this);
	}

	protected abstract void readEntityFromNBT(NBTTagCompound tagCompund);

	protected abstract void writeEntityToNBT(NBTTagCompound tagCompound);

	public void onChunkLoad() {}

	protected NBTTagList newDoubleNBTList(double ... numbers)
	{
		NBTTagList nbttaglist = new NBTTagList();
		double[] adouble = numbers;
		int i = numbers.length;

		for (int j = 0; j < i; ++j)
		{
			double d1 = adouble[j];
			nbttaglist.appendTag(new NBTTagDouble(d1));
		}

		return nbttaglist;
	}

	protected NBTTagList newFloatNBTList(float ... numbers)
	{
		NBTTagList nbttaglist = new NBTTagList();
		float[] afloat = numbers;
		int i = numbers.length;

		for (int j = 0; j < i; ++j)
		{
			float f1 = afloat[j];
			nbttaglist.appendTag(new NBTTagFloat(f1));
		}

		return nbttaglist;
	}

	public EntityItem dropItem(Item itemIn, int size)
	{
		return this.dropItemWithOffset(itemIn, size, 0.0F);
	}

	public EntityItem dropItemWithOffset(Item itemIn, int size, float p_145778_3_)
	{
		return this.entityDropItem(new ItemStack(itemIn, size, 0), p_145778_3_);
	}

	public EntityItem entityDropItem(ItemStack itemStackIn, float offsetY)
	{
		if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null)
		{
			EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY + (double)offsetY, this.posZ, itemStackIn);
			entityitem.setDefaultPickupDelay();
			if (captureDrops)
				this.capturedDrops.add(entityitem);
			else
				this.worldObj.spawnEntityInWorld(entityitem);
			return entityitem;
		}
		else
		{
			return null;
		}
	}

	public boolean isEntityAlive()
	{
		return !this.isDead;
	}

	public boolean isEntityInsideOpaqueBlock()
	{
		if (this.noClip)
		{
			return false;
		}
		else
		{
			for (int i = 0; i < 8; ++i)
			{
				double d0 = this.posX + (double)(((float)((i >> 0) % 2) - 0.5F) * this.width * 0.8F);
				double d1 = this.posY + (double)(((float)((i >> 1) % 2) - 0.5F) * 0.1F);
				double d2 = this.posZ + (double)(((float)((i >> 2) % 2) - 0.5F) * this.width * 0.8F);

				if (this.worldObj.getBlockState(new BlockPos(d0, d1 + (double)this.getEyeHeight(), d2)).getBlock().isVisuallyOpaque())
				{
					return true;
				}
			}

			return false;
		}
	}

	public boolean interactFirst(EntityPlayer playerIn)
	{
		return false;
	}

	public AxisAlignedBB getCollisionBox(Entity entityIn)
	{
		return null;
	}

	public void updateRidden()
	{
		if (this.ridingEntity.isDead)
		{
			this.ridingEntity = null;
		}
		else
		{
			this.motionX = 0.0D;
			this.motionY = 0.0D;
			this.motionZ = 0.0D;
			this.onUpdate();

			if (this.ridingEntity != null)
			{
				this.ridingEntity.updateRiderPosition();
				this.entityRiderYawDelta += (double)(this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw);

				for (this.entityRiderPitchDelta += (double)(this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch); this.entityRiderYawDelta >= 180.0D; this.entityRiderYawDelta -= 360.0D)
				{
					;
				}

				while (this.entityRiderYawDelta < -180.0D)
				{
					this.entityRiderYawDelta += 360.0D;
				}

				while (this.entityRiderPitchDelta >= 180.0D)
				{
					this.entityRiderPitchDelta -= 360.0D;
				}

				while (this.entityRiderPitchDelta < -180.0D)
				{
					this.entityRiderPitchDelta += 360.0D;
				}

				double d0 = this.entityRiderYawDelta * 0.5D;
				double d1 = this.entityRiderPitchDelta * 0.5D;
				float f = 10.0F;

				if (d0 > (double)f)
				{
					d0 = (double)f;
				}

				if (d0 < (double)(-f))
				{
					d0 = (double)(-f);
				}

				if (d1 > (double)f)
				{
					d1 = (double)f;
				}

				if (d1 < (double)(-f))
				{
					d1 = (double)(-f);
				}

				this.entityRiderYawDelta -= d0;
				this.entityRiderPitchDelta -= d1;
			}
		}
	}

	public void updateRiderPosition()
	{
		if (this.riddenByEntity != null)
		{
			this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
		}
	}

	public double getYOffset()
	{
		return 0.0D;
	}

	public double getMountedYOffset()
	{
		return (double)this.height * 0.75D;
	}

	public void mountEntity(Entity entityIn)
	{
		if(!(this instanceof EntityLivingBase) && !net.minecraftforge.event.ForgeEventFactory.canMountEntity(this, entityIn, true)){ return; }  
		this.entityRiderPitchDelta = 0.0D;
		this.entityRiderYawDelta = 0.0D;

		if (entityIn == null)
		{
			if (this.ridingEntity != null)
			{
				this.setLocationAndAngles(this.ridingEntity.posX, this.ridingEntity.getEntityBoundingBox().minY + (double)this.ridingEntity.height, this.ridingEntity.posZ, this.rotationYaw, this.rotationPitch);
				this.ridingEntity.riddenByEntity = null;
			}

			this.ridingEntity = null;
		}
		else
		{
			if (this.ridingEntity != null)
			{
				this.ridingEntity.riddenByEntity = null;
			}

			if (entityIn != null)
			{
				for (Entity entity1 = entityIn.ridingEntity; entity1 != null; entity1 = entity1.ridingEntity)
				{
					if (entity1 == this)
					{
						return;
					}
				}
			}

			this.ridingEntity = entityIn;
			entityIn.riddenByEntity = this;
		}
	}

	@SideOnly(Side.CLIENT)
	public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_)
	{
		this.setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
		this.setRotation(p_180426_7_, p_180426_8_);
		List list = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().contract(0.03125D, 0.0D, 0.03125D));

		if (!list.isEmpty())
		{
			double d3 = 0.0D;
			Iterator iterator = list.iterator();

			while (iterator.hasNext())
			{
				AxisAlignedBB axisalignedbb = (AxisAlignedBB)iterator.next();

				if (axisalignedbb.maxY > d3)
				{
					d3 = axisalignedbb.maxY;
				}
			}

			p_180426_3_ += d3 - this.getEntityBoundingBox().minY;
			this.setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
		}
	}

	public float getCollisionBorderSize()
	{
		return 0.1F;
	}

	public Vec3 getLookVec()
	{
		return null;
	}

	public void setInPortal()
	{
		if (this.timeUntilPortal > 0)
		{
			this.timeUntilPortal = this.getPortalCooldown();
		}
		else
		{
			double d0 = this.prevPosX - this.posX;
			double d1 = this.prevPosZ - this.posZ;

			if (!this.worldObj.isRemote && !this.inPortal)
			{
				int i;

				if (MathHelper.abs((float)d0) > MathHelper.abs((float)d1))
				{
					i = d0 > 0.0D ? EnumFacing.WEST.getHorizontalIndex() : EnumFacing.EAST.getHorizontalIndex();
				}
				else
				{
					i = d1 > 0.0D ? EnumFacing.NORTH.getHorizontalIndex() : EnumFacing.SOUTH.getHorizontalIndex();
				}

				this.teleportDirection = i;
			}

			this.inPortal = true;
		}
	}

	public int getPortalCooldown()
	{
		return 300;
	}

	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z)
	{
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
	}

	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_) {}

	@SideOnly(Side.CLIENT)
	public void performHurtAnimation() {}

	public ItemStack[] getInventory()
	{
		return null;
	}

	public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {}

	public boolean isBurning()
	{
		boolean flag = this.worldObj != null && this.worldObj.isRemote;
		return !this.isImmuneToFire && (this.fire > 0 || flag && this.getFlag(0));
	}

	public boolean isRiding()
	{
		return this.ridingEntity != null && ridingEntity.shouldRiderSit();
	}

	public boolean isSneaking()
	{
		return this.getFlag(1);
	}

	public void setSneaking(boolean sneaking)
	{
		this.setFlag(1, sneaking);
	}

	public boolean isSprinting()
	{
		return this.getFlag(3);
	}

	public void setSprinting(boolean sprinting)
	{
		this.setFlag(3, sprinting);
	}

	public boolean isInvisible()
	{
		return this.getFlag(5);
	}

	@SideOnly(Side.CLIENT)
	public boolean isInvisibleToPlayer(EntityPlayer player)
	{
		return player.isSpectator() ? false : this.isInvisible();
	}

	public void setInvisible(boolean invisible)
	{
		this.setFlag(5, invisible);
	}

	@SideOnly(Side.CLIENT)
	public boolean isEating()
	{
		return this.getFlag(4);
	}

	public void setEating(boolean eating)
	{
		this.setFlag(4, eating);
	}

	protected boolean getFlag(int flag)
	{
		return (this.dataWatcher.getWatchableObjectByte(0) & 1 << flag) != 0;
	}

	protected void setFlag(int flag, boolean set)
	{
		byte b0 = this.dataWatcher.getWatchableObjectByte(0);

		if (set)
		{
			this.dataWatcher.updateObject(0, Byte.valueOf((byte)(b0 | 1 << flag)));
		}
		else
		{
			this.dataWatcher.updateObject(0, Byte.valueOf((byte)(b0 & ~(1 << flag))));
		}
	}

	public int getAir()
	{
		return this.dataWatcher.getWatchableObjectShort(1);
	}

	public void setAir(int air)
	{
		this.dataWatcher.updateObject(1, Short.valueOf((short)air));
	}

	public void onStruckByLightning(EntityLightningBolt lightningBolt)
	{
		this.attackEntityFrom(DamageSource.lightningBolt, 5.0F);
		++this.fire;

		if (this.fire == 0)
		{
			this.setFire(8);
		}
	}

	public void onKillEntity(EntityLivingBase entityLivingIn) {}

	protected boolean pushOutOfBlocks(double x, double y, double z)
	{
		BlockPos blockpos = new BlockPos(x, y, z);
		double d3 = x - (double)blockpos.getX();
		double d4 = y - (double)blockpos.getY();
		double d5 = z - (double)blockpos.getZ();
		List list = this.worldObj.func_147461_a(this.getEntityBoundingBox());

		if (list.isEmpty() && !this.worldObj.func_175665_u(blockpos))
		{
			return false;
		}
		else
		{
			byte b0 = 3;
			double d6 = 9999.0D;

			if (!this.worldObj.func_175665_u(blockpos.west()) && d3 < d6)
			{
				d6 = d3;
				b0 = 0;
			}

			if (!this.worldObj.func_175665_u(blockpos.east()) && 1.0D - d3 < d6)
			{
				d6 = 1.0D - d3;
				b0 = 1;
			}

			if (!this.worldObj.func_175665_u(blockpos.up()) && 1.0D - d4 < d6)
			{
				d6 = 1.0D - d4;
				b0 = 3;
			}

			if (!this.worldObj.func_175665_u(blockpos.north()) && d5 < d6)
			{
				d6 = d5;
				b0 = 4;
			}

			if (!this.worldObj.func_175665_u(blockpos.south()) && 1.0D - d5 < d6)
			{
				d6 = 1.0D - d5;
				b0 = 5;
			}

			float f = this.rand.nextFloat() * 0.2F + 0.1F;

			if (b0 == 0)
			{
				this.motionX = (double)(-f);
			}

			if (b0 == 1)
			{
				this.motionX = (double)f;
			}

			if (b0 == 3)
			{
				this.motionY = (double)f;
			}

			if (b0 == 4)
			{
				this.motionZ = (double)(-f);
			}

			if (b0 == 5)
			{
				this.motionZ = (double)f;
			}

			return true;
		}
	}

	public void setInWeb()
	{
		this.isInWeb = true;
		this.fallDistance = 0.0F;
	}

	public String getName()
	{
		if (this.hasCustomName())
		{
			return this.getCustomNameTag();
		}
		else
		{
			String s = EntityList.getEntityString(this);

			if (s == null)
			{
				s = "generic";
			}

			return StatCollector.translateToLocal("entity." + s + ".name");
		}
	}

	public Entity[] getParts()
	{
		return null;
	}

	public boolean isEntityEqual(Entity entityIn)
	{
		return this == entityIn;
	}

	public float getRotationYawHead()
	{
		return 0.0F;
	}

	public void setRotationYawHead(float rotation) {}

	public boolean canAttackWithItem()
	{
		return true;
	}

	public boolean hitByEntity(Entity entityIn)
	{
		return false;
	}

	public String toString()
	{
		return String.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]", new Object[] {this.getClass().getSimpleName(), this.getName(), Integer.valueOf(this.entityId), this.worldObj == null ? "~NULL~" : this.worldObj.getWorldInfo().getWorldName(), Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ)});
	}

	public boolean isEntityInvulnerable(DamageSource p_180431_1_)
	{
		return this.invulnerable && p_180431_1_ != DamageSource.outOfWorld && !p_180431_1_.isCreativePlayer();
	}

	public void copyLocationAndAnglesFrom(Entity entityIn)
	{
		this.setLocationAndAngles(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
	}

	public void copyDataFromOld(Entity p_180432_1_)
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		p_180432_1_.writeToNBT(nbttagcompound);
		this.readFromNBT(nbttagcompound);
		this.timeUntilPortal = p_180432_1_.timeUntilPortal;
		this.teleportDirection = p_180432_1_.teleportDirection;
	}

	public void travelToDimension(int dimensionId)
	{
		if (!this.worldObj.isRemote && !this.isDead)
		{
			this.worldObj.theProfiler.startSection("changeDimension");
			MinecraftServer minecraftserver = MinecraftServer.getServer();
			int j = this.dimension;
			WorldServer worldserver = minecraftserver.worldServerForDimension(j);
			WorldServer worldserver1 = minecraftserver.worldServerForDimension(dimensionId);
			this.dimension = dimensionId;

			if (j == 1 && dimensionId == 1)
			{
				worldserver1 = minecraftserver.worldServerForDimension(0);
				this.dimension = 0;
			}

			this.worldObj.removeEntity(this);
			this.isDead = false;
			this.worldObj.theProfiler.startSection("reposition");
			minecraftserver.getConfigurationManager().transferEntityToWorld(this, j, worldserver, worldserver1);
			this.worldObj.theProfiler.endStartSection("reloading");
			Entity entity = EntityList.createEntityByName(EntityList.getEntityString(this), worldserver1);

			if (entity != null)
			{
				entity.copyDataFromOld(this);

				if (j == 1 && dimensionId == 1)
				{
					BlockPos blockpos = this.worldObj.getTopSolidOrLiquidBlock(worldserver1.getSpawnPoint());
					entity.moveToBlockPosAndAngles(blockpos, entity.rotationYaw, entity.rotationPitch);
				}

				worldserver1.spawnEntityInWorld(entity);
			}

			this.isDead = true;
			this.worldObj.theProfiler.endSection();
			worldserver.resetUpdateEntityTick();
			worldserver1.resetUpdateEntityTick();
			this.worldObj.theProfiler.endSection();
		}
	}

	public float getExplosionResistance(Explosion p_180428_1_, World worldIn, BlockPos p_180428_3_, IBlockState p_180428_4_)
	{
		return p_180428_4_.getBlock().getExplosionResistance(worldIn, p_180428_3_.add(0, getEyeHeight(), 0), this, p_180428_1_);
	}

	public boolean func_174816_a(Explosion p_174816_1_, World worldIn, BlockPos p_174816_3_, IBlockState p_174816_4_, float p_174816_5_)
	{
		return true;
	}

	public int getMaxFallHeight()
	{
		return 3;
	}

	public int getTeleportDirection()
	{
		return this.teleportDirection;
	}

	public boolean doesEntityNotTriggerPressurePlate()
	{
		return false;
	}

	public void addEntityCrashInfo(CrashReportCategory category)
	{
		category.addCrashSectionCallable("Entity Type", new Callable()
		{
			private static final String __OBFID = "CL_00001534";
			public String call()
			{
				return EntityList.getEntityString(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
			}
		});
		category.addCrashSection("Entity ID", Integer.valueOf(this.entityId));
		category.addCrashSectionCallable("Entity Name", new Callable()
		{
			private static final String __OBFID = "CL_00001535";
			public String call()
			{
				return Entity.this.getName();
			}
		});
		category.addCrashSection("Entity\'s Exact location", String.format("%.2f, %.2f, %.2f", new Object[] {Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ)}));
		category.addCrashSection("Entity\'s Block location", CrashReportCategory.getCoordinateInfo((double)MathHelper.floor_double(this.posX), (double)MathHelper.floor_double(this.posY), (double)MathHelper.floor_double(this.posZ)));
		category.addCrashSection("Entity\'s Momentum", String.format("%.2f, %.2f, %.2f", new Object[] {Double.valueOf(this.motionX), Double.valueOf(this.motionY), Double.valueOf(this.motionZ)}));
		category.addCrashSectionCallable("Entity\'s Rider", new Callable()
		{
			private static final String __OBFID = "CL_00002259";
			public String call()
			{
				return Entity.this.riddenByEntity.toString();
			}
		});
		category.addCrashSectionCallable("Entity\'s Vehicle", new Callable()
		{
			private static final String __OBFID = "CL_00002258";
			public String call()
			{
				return Entity.this.ridingEntity.toString();
			}
		});
	}

	@SideOnly(Side.CLIENT)
	public boolean canRenderOnFire()
	{
		return this.isBurning();
	}

	public UUID getUniqueID()
	{
		return this.entityUniqueID;
	}

	public boolean isPushedByWater()
	{
		return true;
	}

	public IChatComponent getDisplayName()
	{
		ChatComponentText chatcomponenttext = new ChatComponentText(this.getName());
		chatcomponenttext.getChatStyle().setChatHoverEvent(this.func_174823_aP());
		chatcomponenttext.getChatStyle().setInsertion(this.getUniqueID().toString());
		return chatcomponenttext;
	}

	public void setCustomNameTag(String p_96094_1_)
	{
		this.dataWatcher.updateObject(2, p_96094_1_);
	}

	public String getCustomNameTag()
	{
		return this.dataWatcher.getWatchableObjectString(2);
	}

	public boolean hasCustomName()
	{
		return this.dataWatcher.getWatchableObjectString(2).length() > 0;
	}

	public void setAlwaysRenderNameTag(boolean p_174805_1_)
	{
		this.dataWatcher.updateObject(3, Byte.valueOf((byte)(p_174805_1_ ? 1 : 0)));
	}

	public boolean getAlwaysRenderNameTag()
	{
		return this.dataWatcher.getWatchableObjectByte(3) == 1;
	}

	public void setPositionAndUpdate(double x, double y, double z)
	{
		this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
	}

	@SideOnly(Side.CLIENT)
	public boolean getAlwaysRenderNameTagForRender()
	{
		return this.getAlwaysRenderNameTag();
	}

	public void func_145781_i(int p_145781_1_) {}

	public EnumFacing getHorizontalFacing()
	{
		return EnumFacing.getHorizontal(MathHelper.floor_double((double)(this.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
	}

	protected HoverEvent func_174823_aP()
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		String s = EntityList.getEntityString(this);
		nbttagcompound.setString("id", this.getUniqueID().toString());

		if (s != null)
		{
			nbttagcompound.setString("type", s);
		}

		nbttagcompound.setString("name", this.getName());
		return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new ChatComponentText(nbttagcompound.toString()));
	}

	public boolean func_174827_a(EntityPlayerMP p_174827_1_)
	{
		return true;
	}

	public AxisAlignedBB getEntityBoundingBox()
	{
		return this.boundingBox;
	}

	public void setEntityBoundingBox(AxisAlignedBB p_174826_1_)
	{
		this.boundingBox = p_174826_1_;
	}

	public float getEyeHeight()
	{
		return this.height * 0.85F;
	}

	public boolean isOutsideBorder()
	{
		return this.isOutsideBorder;
	}

	public void setOutsideBorder(boolean p_174821_1_)
	{
		this.isOutsideBorder = p_174821_1_;
	}

	public boolean replaceItemInInventory(int p_174820_1_, ItemStack p_174820_2_)
	{
		return false;
	}

	public void addChatMessage(IChatComponent message) {}

	public boolean canUseCommand(int permLevel, String commandName)
	{
		return true;
	}

	public BlockPos getPosition()
	{
		return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
	}

	public Vec3 getPositionVector()
	{
		return new Vec3(this.posX, this.posY, this.posZ);
	}

	public World getEntityWorld()
	{
		return this.worldObj;
	}

	public Entity getCommandSenderEntity()
	{
		return this;
	}

	public boolean sendCommandFeedback()
	{
		return false;
	}

	public void setCommandStat(CommandResultStats.Type type, int amount)
	{
		this.cmdResultStats.func_179672_a(this, type, amount);
	}

	public CommandResultStats func_174807_aT()
	{
		return this.cmdResultStats;
	}

	public void func_174817_o(Entity p_174817_1_)
	{
		this.cmdResultStats.func_179671_a(p_174817_1_.func_174807_aT());
	}

	public NBTTagCompound func_174819_aU()
	{
		return null;
	}

	@SideOnly(Side.CLIENT)
	public void func_174834_g(NBTTagCompound p_174834_1_) {}

	public boolean func_174825_a(EntityPlayer p_174825_1_, Vec3 p_174825_2_)
	{
		return false;
	}

	public boolean func_180427_aV()
	{
		return false;
	}

	protected void func_174815_a(EntityLivingBase p_174815_1_, Entity p_174815_2_)
	{
		if (p_174815_2_ instanceof EntityLivingBase)
		{
			EnchantmentHelper.func_151384_a((EntityLivingBase)p_174815_2_, p_174815_1_);
		}

		EnchantmentHelper.func_151385_b(p_174815_1_, p_174815_2_);
	}

	/* ================================== Forge Start =====================================*/
	/**
	 * Returns a NBTTagCompound that can be used to store custom data for this entity.
	 * It will be written, and read from disc, so it persists over world saves.
	 * @return A NBTTagCompound
	 */
	public NBTTagCompound getEntityData()
	{
		if (customEntityData == null)
		{
			customEntityData = new NBTTagCompound();
		}
		return customEntityData;
	}

	/**
	 * Used in model rendering to determine if the entity riding this entity should be in the 'sitting' position.
	 * @return false to prevent an entity that is mounted to this entity from displaying the 'sitting' animation.
	 */
	public boolean shouldRiderSit()
	{
		return true;
	}

	/**
	 * Called when a user uses the creative pick block button on this entity.
	 *
	 * @param target The full target the player is looking at
	 * @return A ItemStack to add to the player's inventory, Null if nothing should be added.
	 */
	public ItemStack getPickedResult(MovingObjectPosition target)
	{
		if (this instanceof net.minecraft.entity.item.EntityPainting)
		{
			return new ItemStack(net.minecraft.init.Items.painting);
		}
		else if (this instanceof EntityLeashKnot)
		{
			return new ItemStack(net.minecraft.init.Items.lead);
		}
		else if (this instanceof net.minecraft.entity.item.EntityItemFrame)
		{
			ItemStack held = ((net.minecraft.entity.item.EntityItemFrame)this).getDisplayedItem();
			if (held == null)
			{
				return new ItemStack(net.minecraft.init.Items.item_frame);
			}
			else
			{
				return held.copy();
			}
		}
		else if (this instanceof net.minecraft.entity.item.EntityMinecart)
		{
			return ((net.minecraft.entity.item.EntityMinecart)this).getCartItem();
		}
		else if (this instanceof net.minecraft.entity.item.EntityBoat)
		{
			return new ItemStack(net.minecraft.init.Items.boat);
		}
		else if (this instanceof net.minecraft.entity.item.EntityArmorStand)
		{
			return new ItemStack(net.minecraft.init.Items.armor_stand);
		}
		else
		{
			int id = EntityList.getEntityID(this);
			if (id > 0 && EntityList.entityEggs.containsKey(id))
			{
				return new ItemStack(net.minecraft.init.Items.spawn_egg, 1, id);
			}
			String name = EntityList.getEntityString(this);
			if (name != null && net.minecraftforge.fml.common.registry.EntityRegistry.getEggs().containsKey(name))
			{
				ItemStack stack = new ItemStack(net.minecraft.init.Items.spawn_egg);
				stack.setTagInfo("entity_name", new net.minecraft.nbt.NBTTagString(name));
				return stack;
			}
		}
		return null;
	}

	public UUID getPersistentID()
	{
		return entityUniqueID;
	}

	/**
	 * Reset the entity ID to a new value. Not to be used from Mod code
	 */
	public final void resetEntityId()
	{
		this.entityId = nextEntityID++;
	}

	public boolean shouldRenderInPass(int pass)
	{
		return pass == 0;
	}

	/**
	 * Returns true if the entity is of the @link{EnumCreatureType} provided
	 * @param type The EnumCreatureType type this entity is evaluating
	 * @param forSpawnCount If this is being invoked to check spawn count caps.
	 * @return If the creature is of the type provided
	 */
	public boolean isCreatureType(EnumCreatureType type, boolean forSpawnCount)
	{
		return type.getCreatureClass().isAssignableFrom(this.getClass());
	}

	/**
	 * Register the instance of IExtendedProperties into the entity's collection.
	 * @param identifier The identifier which you can use to retrieve these properties for the entity.
	 * @param properties The instanceof IExtendedProperties to register
	 * @return The identifier that was used to register the extended properties.  Empty String indicates an error.  If your requested key already existed, this will return a modified one that is unique.
	 */
	public String registerExtendedProperties(String identifier, net.minecraftforge.common.IExtendedEntityProperties properties)
	{
		if (identifier == null)
		{
			net.minecraftforge.fml.common.FMLLog.warning("Someone is attempting to register extended properties using a null identifier.  This is not allowed.  Aborting.  This may have caused instability.");
			return "";
		}
		if (properties == null)
		{
			net.minecraftforge.fml.common.FMLLog.warning("Someone is attempting to register null extended properties.  This is not allowed.  Aborting.  This may have caused instability.");
			return "";
		}

		String baseIdentifier = identifier;
		int identifierModCount = 1;
		while (this.extendedProperties.containsKey(identifier))
		{
			identifier = String.format("%s%d", baseIdentifier, identifierModCount++);
		}

		if (baseIdentifier != identifier)
		{
			net.minecraftforge.fml.common.FMLLog.info("An attempt was made to register exended properties using an existing key.  The duplicate identifier (%s) has been remapped to %s.", baseIdentifier, identifier);
		}

		this.extendedProperties.put(identifier, properties);
		return identifier;
	}

	/**
	 * Gets the extended properties identified by the passed in key
	 * @param identifier The key that identifies the extended properties.
	 * @return The instance of IExtendedProperties that was found, or null.
	 */
	public net.minecraftforge.common.IExtendedEntityProperties getExtendedProperties(String identifier)
	{
		return this.extendedProperties.get(identifier);
	}

	/**
	 * If a rider of this entity can interact with this entity. Should return true on the
	 * ridden entity if so.
	 *
	 * @return if the entity can be interacted with from a rider
	 */
	public boolean canRiderInteract()
	{
		return false;
	}

	/**
	 * If the rider should be dismounted from the entity when the entity goes under water
	 *
	 * @param rider The entity that is riding
	 * @return if the entity should be dismounted when under water
	 */
	public boolean shouldDismountInWater(Entity rider)
	{
		return this instanceof EntityLivingBase;
	}
	/* ================================== Forge End =====================================*/
}