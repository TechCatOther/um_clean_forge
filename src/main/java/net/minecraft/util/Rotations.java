package net.minecraft.util;

import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;

public class Rotations
{
	protected final float x;
	protected final float y;
	protected final float z;
	private static final String __OBFID = "CL_00002316";

	public Rotations(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Rotations(NBTTagList nbt)
	{
		this.x = nbt.getFloat(0);
		this.y = nbt.getFloat(1);
		this.z = nbt.getFloat(2);
	}

	public NBTTagList writeToNBT()
	{
		NBTTagList nbttaglist = new NBTTagList();
		nbttaglist.appendTag(new NBTTagFloat(this.x));
		nbttaglist.appendTag(new NBTTagFloat(this.y));
		nbttaglist.appendTag(new NBTTagFloat(this.z));
		return nbttaglist;
	}

	public boolean equals(Object p_equals_1_)
	{
		if (!(p_equals_1_ instanceof Rotations))
		{
			return false;
		}
		else
		{
			Rotations rotations = (Rotations)p_equals_1_;
			return this.x == rotations.x && this.y == rotations.y && this.z == rotations.z;
		}
	}

	public float getX()
	{
		return this.x;
	}

	public float getY()
	{
		return this.y;
	}

	public float getZ()
	{
		return this.z;
	}
}