package net.minecraft.nbt;

import com.google.common.collect.Lists;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagList extends NBTBase
{
	private static final Logger LOGGER = LogManager.getLogger();
	private List tagList = Lists.newArrayList();
	private byte tagType = 0;
	private static final String __OBFID = "CL_00001224";

	void write(DataOutput output) throws IOException
	{
		if (!this.tagList.isEmpty())
		{
			this.tagType = ((NBTBase)this.tagList.get(0)).getId();
		}
		else
		{
			this.tagType = 0;
		}

		output.writeByte(this.tagType);
		output.writeInt(this.tagList.size());

		for (int i = 0; i < this.tagList.size(); ++i)
		{
			((NBTBase)this.tagList.get(i)).write(output);
		}
	}

	void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
	{
		if (depth > 512)
		{
			throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
		}
		else
		{
			sizeTracker.read(8L);
			this.tagType = input.readByte();
			sizeTracker.read(32); //Forge: Count the length as well
			int j = input.readInt();
			this.tagList = Lists.newArrayList();

			for (int k = 0; k < j; ++k)
			{
				sizeTracker.read(32); //Forge: 4 extra bytes for the object allocation.
				NBTBase nbtbase = NBTBase.createNewByType(this.tagType);
				nbtbase.read(input, depth + 1, sizeTracker);
				this.tagList.add(nbtbase);
			}
		}
	}

	public byte getId()
	{
		return (byte)9;
	}

	public String toString()
	{
		String s = "[";
		int i = 0;

		for (Iterator iterator = this.tagList.iterator(); iterator.hasNext(); ++i)
		{
			NBTBase nbtbase = (NBTBase)iterator.next();
			s = s + "" + i + ':' + nbtbase + ',';
		}

		return s + "]";
	}

	public void appendTag(NBTBase nbt)
	{
		if (this.tagType == 0)
		{
			this.tagType = nbt.getId();
		}
		else if (this.tagType != nbt.getId())
		{
			LOGGER.warn("Adding mismatching tag types to tag list");
			return;
		}

		this.tagList.add(nbt);
	}

	public void set(int idx, NBTBase nbt)
	{
		if (idx >= 0 && idx < this.tagList.size())
		{
			if (this.tagType == 0)
			{
				this.tagType = nbt.getId();
			}
			else if (this.tagType != nbt.getId())
			{
				LOGGER.warn("Adding mismatching tag types to tag list");
				return;
			}

			this.tagList.set(idx, nbt);
		}
		else
		{
			LOGGER.warn("index out of bounds to set tag in tag list");
		}
	}

	public NBTBase removeTag(int i)
	{
		return (NBTBase)this.tagList.remove(i);
	}

	public boolean hasNoTags()
	{
		return this.tagList.isEmpty();
	}

	public NBTTagCompound getCompoundTagAt(int i)
	{
		if (i >= 0 && i < this.tagList.size())
		{
			NBTBase nbtbase = (NBTBase)this.tagList.get(i);
			return nbtbase.getId() == 10 ? (NBTTagCompound)nbtbase : new NBTTagCompound();
		}
		else
		{
			return new NBTTagCompound();
		}
	}

	public int[] getIntArray(int i)
	{
		if (i >= 0 && i < this.tagList.size())
		{
			NBTBase nbtbase = (NBTBase)this.tagList.get(i);
			return nbtbase.getId() == 11 ? ((NBTTagIntArray)nbtbase).getIntArray() : new int[0];
		}
		else
		{
			return new int[0];
		}
	}

	public double getDouble(int i)
	{
		if (i >= 0 && i < this.tagList.size())
		{
			NBTBase nbtbase = (NBTBase)this.tagList.get(i);
			return nbtbase.getId() == 6 ? ((NBTTagDouble)nbtbase).getDouble() : 0.0D;
		}
		else
		{
			return 0.0D;
		}
	}

	public float getFloat(int i)
	{
		if (i >= 0 && i < this.tagList.size())
		{
			NBTBase nbtbase = (NBTBase)this.tagList.get(i);
			return nbtbase.getId() == 5 ? ((NBTTagFloat)nbtbase).getFloat() : 0.0F;
		}
		else
		{
			return 0.0F;
		}
	}

	public String getStringTagAt(int i)
	{
		if (i >= 0 && i < this.tagList.size())
		{
			NBTBase nbtbase = (NBTBase)this.tagList.get(i);
			return nbtbase.getId() == 8 ? nbtbase.getString() : nbtbase.toString();
		}
		else
		{
			return "";
		}
	}

	public NBTBase get(int idx)
	{
		return (NBTBase)(idx >= 0 && idx < this.tagList.size() ? (NBTBase)this.tagList.get(idx) : new NBTTagEnd());
	}

	public int tagCount()
	{
		return this.tagList.size();
	}

	public NBTBase copy()
	{
		NBTTagList nbttaglist = new NBTTagList();
		nbttaglist.tagType = this.tagType;
		Iterator iterator = this.tagList.iterator();

		while (iterator.hasNext())
		{
			NBTBase nbtbase = (NBTBase)iterator.next();
			NBTBase nbtbase1 = nbtbase.copy();
			nbttaglist.tagList.add(nbtbase1);
		}

		return nbttaglist;
	}

	public boolean equals(Object p_equals_1_)
	{
		if (super.equals(p_equals_1_))
		{
			NBTTagList nbttaglist = (NBTTagList)p_equals_1_;

			if (this.tagType == nbttaglist.tagType)
			{
				return this.tagList.equals(nbttaglist.tagList);
			}
		}

		return false;
	}

	public int hashCode()
	{
		return super.hashCode() ^ this.tagList.hashCode();
	}

	public int getTagType()
	{
		return this.tagType;
	}
}