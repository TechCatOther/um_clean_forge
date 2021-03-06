package net.minecraft.util;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPos extends Vec3i
{
	public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);
	private static final int NUM_X_BITS = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
	private static final int NUM_Z_BITS = NUM_X_BITS;
	private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
	private static final int Y_SHIFT = 0 + NUM_Z_BITS;
	private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
	private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
	private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
	private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;
	private static final String __OBFID = "CL_00002334";

	public BlockPos(int x, int y, int z)
	{
		super(x, y, z);
	}

	public BlockPos(double x, double y, double z)
	{
		super(x, y, z);
	}

	public BlockPos(Entity source)
	{
		this(source.posX, source.posY, source.posZ);
	}

	public BlockPos(Vec3 source)
	{
		this(source.xCoord, source.yCoord, source.zCoord);
	}

	public BlockPos(Vec3i source)
	{
		this(source.getX(), source.getY(), source.getZ());
	}

	public BlockPos add(double x, double y, double z)
	{
		return new BlockPos((double)this.getX() + x, (double)this.getY() + y, (double)this.getZ() + z);
	}

	public BlockPos add(int x, int y, int z)
	{
		return new BlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
	}

	public BlockPos add(Vec3i vec)
	{
		return new BlockPos(this.getX() + vec.getX(), this.getY() + vec.getY(), this.getZ() + vec.getZ());
	}

	public BlockPos multiply(int factor)
	{
		return new BlockPos(this.getX() * factor, this.getY() * factor, this.getZ() * factor);
	}

	public BlockPos up()
	{
		return this.up(1);
	}

	@SideOnly(Side.CLIENT)
	public BlockPos subtract(Vec3i vec)
	{
		return new BlockPos(this.getX() - vec.getX(), this.getY() - vec.getY(), this.getZ() - vec.getZ());
	}

	public BlockPos up(int n)
	{
		return this.offset(EnumFacing.UP, n);
	}

	public BlockPos down()
	{
		return this.down(1);
	}

	public BlockPos down(int n)
	{
		return this.offset(EnumFacing.DOWN, n);
	}

	public BlockPos north()
	{
		return this.north(1);
	}

	public BlockPos north(int n)
	{
		return this.offset(EnumFacing.NORTH, n);
	}

	public BlockPos south()
	{
		return this.south(1);
	}

	public BlockPos south(int n)
	{
		return this.offset(EnumFacing.SOUTH, n);
	}

	public BlockPos west()
	{
		return this.west(1);
	}

	public BlockPos west(int n)
	{
		return this.offset(EnumFacing.WEST, n);
	}

	public BlockPos east()
	{
		return this.east(1);
	}

	public BlockPos east(int n)
	{
		return this.offset(EnumFacing.EAST, n);
	}

	public BlockPos offset(EnumFacing facing)
	{
		return this.offset(facing, 1);
	}

	public BlockPos offset(EnumFacing facing, int n)
	{
		return new BlockPos(this.getX() + facing.getFrontOffsetX() * n, this.getY() + facing.getFrontOffsetY() * n, this.getZ() + facing.getFrontOffsetZ() * n);
	}

	public BlockPos crossProductBP(Vec3i vec)
	{
		return new BlockPos(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
	}

	public long toLong()
	{
		return ((long)this.getX() & X_MASK) << X_SHIFT | ((long)this.getY() & Y_MASK) << Y_SHIFT | ((long)this.getZ() & Z_MASK) << 0;
	}

	public static BlockPos fromLong(long serialized)
	{
		int j = (int)(serialized << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
		int k = (int)(serialized << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
		int l = (int)(serialized << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
		return new BlockPos(j, k, l);
	}

	public static Iterable getAllInBox(BlockPos from, BlockPos to)
	{
		final BlockPos blockpos2 = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
		final BlockPos blockpos3 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
		return new Iterable()
		{
			private static final String __OBFID = "CL_00002333";
			public Iterator iterator()
			{
				return new AbstractIterator()
				{
					private BlockPos lastReturned = null;
					private static final String __OBFID = "CL_00002332";
					protected BlockPos computeNext0()
					{
						if (this.lastReturned == null)
						{
							this.lastReturned = blockpos2;
							return this.lastReturned;
						}
						else if (this.lastReturned.equals(blockpos3))
						{
							return (BlockPos)this.endOfData();
						}
						else
						{
							int i = this.lastReturned.getX();
							int j = this.lastReturned.getY();
							int k = this.lastReturned.getZ();

							if (i < blockpos3.getX())
							{
								++i;
							}
							else if (j < blockpos3.getY())
							{
								i = blockpos2.getX();
								++j;
							}
							else if (k < blockpos3.getZ())
							{
								i = blockpos2.getX();
								j = blockpos2.getY();
								++k;
							}

							this.lastReturned = new BlockPos(i, j, k);
							return this.lastReturned;
						}
					}
					protected Object computeNext()
					{
						return this.computeNext0();
					}
				};
			}
		};
	}

	public static Iterable getAllInBoxMutable(BlockPos from, BlockPos to)
	{
		final BlockPos blockpos2 = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
		final BlockPos blockpos3 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
		return new Iterable()
		{
			private static final String __OBFID = "CL_00002331";
			public Iterator iterator()
			{
				return new AbstractIterator()
				{
					private BlockPos.MutableBlockPos theBlockPos = null;
					private static final String __OBFID = "CL_00002330";
					protected BlockPos.MutableBlockPos computeNext0()
					{
						if (this.theBlockPos == null)
						{
							this.theBlockPos = new BlockPos.MutableBlockPos(blockpos2.getX(), blockpos2.getY(), blockpos2.getZ(), null);
							return this.theBlockPos;
						}
						else if (this.theBlockPos.equals(blockpos3))
						{
							return (BlockPos.MutableBlockPos)this.endOfData();
						}
						else
						{
							int i = this.theBlockPos.getX();
							int k = this.theBlockPos.getY();
							int j = this.theBlockPos.getZ();

							if (i < blockpos3.getX())
							{
								++i;
							}
							else if (k < blockpos3.getY())
							{
								i = blockpos2.getX();
								++k;
							}
							else if (j < blockpos3.getZ())
							{
								i = blockpos2.getX();
								k = blockpos2.getY();
								++j;
							}

							this.theBlockPos.x = i;
							this.theBlockPos.y = k;
							this.theBlockPos.z = j;
							return this.theBlockPos;
						}
					}
					protected Object computeNext()
					{
						return this.computeNext0();
					}
				};
			}
		};
	}

	public Vec3i crossProduct(Vec3i vec)
	{
		return this.crossProductBP(vec);
	}

	public static final class MutableBlockPos extends BlockPos
		{
			public int x;
			public int y;
			public int z;
			private static final String __OBFID = "CL_00002329";

			private MutableBlockPos(int x_, int y_, int z_)
			{
				super(0, 0, 0);
				this.x = x_;
				this.y = y_;
				this.z = z_;
			}

			public int getX()
			{
				return this.x;
			}

			public int getY()
			{
				return this.y;
			}

			public int getZ()
			{
				return this.z;
			}

			public Vec3i crossProduct(Vec3i vec)
			{
				return super.crossProductBP(vec);
			}

			MutableBlockPos(int p_i46025_1_, int p_i46025_2_, int p_i46025_3_, Object p_i46025_4_)
			{
				this(p_i46025_1_, p_i46025_2_, p_i46025_3_);
			}
		}
}