package net.minecraft.util;

public class AxisAlignedBB
{
	public final double minX;
	public final double minY;
	public final double minZ;
	public final double maxX;
	public final double maxY;
	public final double maxZ;
	private static final String __OBFID = "CL_00000607";

	public AxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		this.minX = Math.min(x1, x2);
		this.minY = Math.min(y1, y2);
		this.minZ = Math.min(z1, z2);
		this.maxX = Math.max(x1, x2);
		this.maxY = Math.max(y1, y2);
		this.maxZ = Math.max(z1, z2);
	}

	public AxisAlignedBB(BlockPos pos1, BlockPos pos2)
	{
		this.minX = (double)pos1.getX();
		this.minY = (double)pos1.getY();
		this.minZ = (double)pos1.getZ();
		this.maxX = (double)pos2.getX();
		this.maxY = (double)pos2.getY();
		this.maxZ = (double)pos2.getZ();
	}

	public AxisAlignedBB addCoord(double x, double y, double z)
	{
		double d3 = this.minX;
		double d4 = this.minY;
		double d5 = this.minZ;
		double d6 = this.maxX;
		double d7 = this.maxY;
		double d8 = this.maxZ;

		if (x < 0.0D)
		{
			d3 += x;
		}
		else if (x > 0.0D)
		{
			d6 += x;
		}

		if (y < 0.0D)
		{
			d4 += y;
		}
		else if (y > 0.0D)
		{
			d7 += y;
		}

		if (z < 0.0D)
		{
			d5 += z;
		}
		else if (z > 0.0D)
		{
			d8 += z;
		}

		return new AxisAlignedBB(d3, d4, d5, d6, d7, d8);
	}

	public AxisAlignedBB expand(double x, double y, double z)
	{
		double d3 = this.minX - x;
		double d4 = this.minY - y;
		double d5 = this.minZ - z;
		double d6 = this.maxX + x;
		double d7 = this.maxY + y;
		double d8 = this.maxZ + z;
		return new AxisAlignedBB(d3, d4, d5, d6, d7, d8);
	}

	public AxisAlignedBB union(AxisAlignedBB other)
	{
		double d0 = Math.min(this.minX, other.minX);
		double d1 = Math.min(this.minY, other.minY);
		double d2 = Math.min(this.minZ, other.minZ);
		double d3 = Math.max(this.maxX, other.maxX);
		double d4 = Math.max(this.maxY, other.maxY);
		double d5 = Math.max(this.maxZ, other.maxZ);
		return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
	}

	public static AxisAlignedBB fromBounds(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		double d6 = Math.min(x1, x2);
		double d7 = Math.min(y1, y2);
		double d8 = Math.min(z1, z2);
		double d9 = Math.max(x1, x2);
		double d10 = Math.max(y1, y2);
		double d11 = Math.max(z1, z2);
		return new AxisAlignedBB(d6, d7, d8, d9, d10, d11);
	}

	public AxisAlignedBB offset(double x, double y, double z)
	{
		return new AxisAlignedBB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
	}

	public double calculateXOffset(AxisAlignedBB other, double p_72316_2_)
	{
		if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ)
		{
			double d1;

			if (p_72316_2_ > 0.0D && other.maxX <= this.minX)
			{
				d1 = this.minX - other.maxX;

				if (d1 < p_72316_2_)
				{
					p_72316_2_ = d1;
				}
			}
			else if (p_72316_2_ < 0.0D && other.minX >= this.maxX)
			{
				d1 = this.maxX - other.minX;

				if (d1 > p_72316_2_)
				{
					p_72316_2_ = d1;
				}
			}

			return p_72316_2_;
		}
		else
		{
			return p_72316_2_;
		}
	}

	public double calculateYOffset(AxisAlignedBB other, double p_72323_2_)
	{
		if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ)
		{
			double d1;

			if (p_72323_2_ > 0.0D && other.maxY <= this.minY)
			{
				d1 = this.minY - other.maxY;

				if (d1 < p_72323_2_)
				{
					p_72323_2_ = d1;
				}
			}
			else if (p_72323_2_ < 0.0D && other.minY >= this.maxY)
			{
				d1 = this.maxY - other.minY;

				if (d1 > p_72323_2_)
				{
					p_72323_2_ = d1;
				}
			}

			return p_72323_2_;
		}
		else
		{
			return p_72323_2_;
		}
	}

	public double calculateZOffset(AxisAlignedBB other, double p_72322_2_)
	{
		if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY)
		{
			double d1;

			if (p_72322_2_ > 0.0D && other.maxZ <= this.minZ)
			{
				d1 = this.minZ - other.maxZ;

				if (d1 < p_72322_2_)
				{
					p_72322_2_ = d1;
				}
			}
			else if (p_72322_2_ < 0.0D && other.minZ >= this.maxZ)
			{
				d1 = this.maxZ - other.minZ;

				if (d1 > p_72322_2_)
				{
					p_72322_2_ = d1;
				}
			}

			return p_72322_2_;
		}
		else
		{
			return p_72322_2_;
		}
	}

	public boolean intersectsWith(AxisAlignedBB other)
	{
		return other.maxX > this.minX && other.minX < this.maxX ? (other.maxY > this.minY && other.minY < this.maxY ? other.maxZ > this.minZ && other.minZ < this.maxZ : false) : false;
	}

	public boolean isVecInside(Vec3 vec)
	{
		return vec.xCoord > this.minX && vec.xCoord < this.maxX ? (vec.yCoord > this.minY && vec.yCoord < this.maxY ? vec.zCoord > this.minZ && vec.zCoord < this.maxZ : false) : false;
	}

	public double getAverageEdgeLength()
	{
		double d0 = this.maxX - this.minX;
		double d1 = this.maxY - this.minY;
		double d2 = this.maxZ - this.minZ;
		return (d0 + d1 + d2) / 3.0D;
	}

	public AxisAlignedBB contract(double x, double y, double z)
	{
		double d3 = this.minX + x;
		double d4 = this.minY + y;
		double d5 = this.minZ + z;
		double d6 = this.maxX - x;
		double d7 = this.maxY - y;
		double d8 = this.maxZ - z;
		return new AxisAlignedBB(d3, d4, d5, d6, d7, d8);
	}

	public MovingObjectPosition calculateIntercept(Vec3 p_72327_1_, Vec3 p_72327_2_)
	{
		Vec3 vec32 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, this.minX);
		Vec3 vec33 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, this.maxX);
		Vec3 vec34 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, this.minY);
		Vec3 vec35 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, this.maxY);
		Vec3 vec36 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, this.minZ);
		Vec3 vec37 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, this.maxZ);

		if (!this.isVecInYZ(vec32))
		{
			vec32 = null;
		}

		if (!this.isVecInYZ(vec33))
		{
			vec33 = null;
		}

		if (!this.isVecInXZ(vec34))
		{
			vec34 = null;
		}

		if (!this.isVecInXZ(vec35))
		{
			vec35 = null;
		}

		if (!this.isVecInXY(vec36))
		{
			vec36 = null;
		}

		if (!this.isVecInXY(vec37))
		{
			vec37 = null;
		}

		Vec3 vec38 = null;

		if (vec32 != null)
		{
			vec38 = vec32;
		}

		if (vec33 != null && (vec38 == null || p_72327_1_.squareDistanceTo(vec33) < p_72327_1_.squareDistanceTo(vec38)))
		{
			vec38 = vec33;
		}

		if (vec34 != null && (vec38 == null || p_72327_1_.squareDistanceTo(vec34) < p_72327_1_.squareDistanceTo(vec38)))
		{
			vec38 = vec34;
		}

		if (vec35 != null && (vec38 == null || p_72327_1_.squareDistanceTo(vec35) < p_72327_1_.squareDistanceTo(vec38)))
		{
			vec38 = vec35;
		}

		if (vec36 != null && (vec38 == null || p_72327_1_.squareDistanceTo(vec36) < p_72327_1_.squareDistanceTo(vec38)))
		{
			vec38 = vec36;
		}

		if (vec37 != null && (vec38 == null || p_72327_1_.squareDistanceTo(vec37) < p_72327_1_.squareDistanceTo(vec38)))
		{
			vec38 = vec37;
		}

		if (vec38 == null)
		{
			return null;
		}
		else
		{
			EnumFacing enumfacing = null;

			if (vec38 == vec32)
			{
				enumfacing = EnumFacing.WEST;
			}
			else if (vec38 == vec33)
			{
				enumfacing = EnumFacing.EAST;
			}
			else if (vec38 == vec34)
			{
				enumfacing = EnumFacing.DOWN;
			}
			else if (vec38 == vec35)
			{
				enumfacing = EnumFacing.UP;
			}
			else if (vec38 == vec36)
			{
				enumfacing = EnumFacing.NORTH;
			}
			else
			{
				enumfacing = EnumFacing.SOUTH;
			}

			return new MovingObjectPosition(vec38, enumfacing);
		}
	}

	private boolean isVecInYZ(Vec3 vec)
	{
		return vec == null ? false : vec.yCoord >= this.minY && vec.yCoord <= this.maxY && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
	}

	private boolean isVecInXZ(Vec3 vec)
	{
		return vec == null ? false : vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ;
	}

	private boolean isVecInXY(Vec3 vec)
	{
		return vec == null ? false : vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.yCoord >= this.minY && vec.yCoord <= this.maxY;
	}

	public String toString()
	{
		return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
	}
}