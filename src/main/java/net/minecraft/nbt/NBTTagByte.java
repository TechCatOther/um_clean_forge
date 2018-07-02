package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTBase.NBTPrimitive
{
	private byte data;
	private static final String __OBFID = "CL_00001214";

	NBTTagByte() {}

	public NBTTagByte(byte data)
	{
		this.data = data;
	}

	void write(DataOutput output) throws IOException
	{
		output.writeByte(this.data);
	}

	void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
	{
		sizeTracker.read(8L);
		this.data = input.readByte();
	}

	public byte getId()
	{
		return (byte)1;
	}

	public String toString()
	{
		return "" + this.data + "b";
	}

	public NBTBase copy()
	{
		return new NBTTagByte(this.data);
	}

	public boolean equals(Object p_equals_1_)
	{
		if (super.equals(p_equals_1_))
		{
			NBTTagByte nbttagbyte = (NBTTagByte)p_equals_1_;
			return this.data == nbttagbyte.data;
		}
		else
		{
			return false;
		}
	}

	public int hashCode()
	{
		return super.hashCode() ^ this.data;
	}

	public long getLong()
	{
		return (long)this.data;
	}

	public int getInt()
	{
		return this.data;
	}

	public short getShort()
	{
		return (short)this.data;
	}

	public byte getByte()
	{
		return this.data;
	}

	public double getDouble()
	{
		return (double)this.data;
	}

	public float getFloat()
	{
		return (float)this.data;
	}
}