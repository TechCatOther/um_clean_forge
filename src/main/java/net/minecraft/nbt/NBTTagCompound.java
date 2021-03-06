package net.minecraft.nbt;

import com.google.common.collect.Maps;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagCompound extends NBTBase
{
	private static final Logger logger = LogManager.getLogger();
	private Map tagMap = Maps.newHashMap();
	private static final String __OBFID = "CL_00001215";

	void write(DataOutput output) throws IOException
	{
		Iterator iterator = this.tagMap.keySet().iterator();

		while (iterator.hasNext())
		{
			String s = (String)iterator.next();
			NBTBase nbtbase = (NBTBase)this.tagMap.get(s);
			writeEntry(s, nbtbase, output);
		}

		output.writeByte(0);
	}

	void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
	{
		if (depth > 512)
		{
			throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
		}
		else
		{
			this.tagMap.clear();
			byte b0;

			while ((b0 = readType(input, sizeTracker)) != 0)
			{
				String s = readKey(input, sizeTracker);
				NBTSizeTracker.readUTF(sizeTracker, s); // Forge: Correctly read String length including header.
				NBTBase nbtbase = readNBT(b0, s, input, depth + 1, sizeTracker);
				this.tagMap.put(s, nbtbase);
			}
		}
	}

	public Set getKeySet()
	{
		return this.tagMap.keySet();
	}

	public byte getId()
	{
		return (byte)10;
	}

	public void setTag(String key, NBTBase value)
	{
		this.tagMap.put(key, value);
	}

	public void setByte(String key, byte value)
	{
		this.tagMap.put(key, new NBTTagByte(value));
	}

	public void setShort(String key, short value)
	{
		this.tagMap.put(key, new NBTTagShort(value));
	}

	public void setInteger(String key, int value)
	{
		this.tagMap.put(key, new NBTTagInt(value));
	}

	public void setLong(String key, long value)
	{
		this.tagMap.put(key, new NBTTagLong(value));
	}

	public void setFloat(String key, float value)
	{
		this.tagMap.put(key, new NBTTagFloat(value));
	}

	public void setDouble(String key, double value)
	{
		this.tagMap.put(key, new NBTTagDouble(value));
	}

	public void setString(String key, String value)
	{
		this.tagMap.put(key, new NBTTagString(value));
	}

	public void setByteArray(String key, byte[] value)
	{
		this.tagMap.put(key, new NBTTagByteArray(value));
	}

	public void setIntArray(String key, int[] value)
	{
		this.tagMap.put(key, new NBTTagIntArray(value));
	}

	public void setBoolean(String key, boolean value)
	{
		this.setByte(key, (byte)(value ? 1 : 0));
	}

	public NBTBase getTag(String key)
	{
		return (NBTBase)this.tagMap.get(key);
	}

	public byte getTagType(String key)
	{
		NBTBase nbtbase = (NBTBase)this.tagMap.get(key);
		return nbtbase != null ? nbtbase.getId() : 0;
	}

	public boolean hasKey(String key)
	{
		return this.tagMap.containsKey(key);
	}

	public boolean hasKey(String key, int type)
	{
		byte b0 = this.getTagType(key);

		if (b0 == type)
		{
			return true;
		}
		else if (type != 99)
		{
			if (b0 > 0)
			{
				;
			}

			return false;
		}
		else
		{
			return b0 == 1 || b0 == 2 || b0 == 3 || b0 == 4 || b0 == 5 || b0 == 6;
		}
	}

	public byte getByte(String key)
	{
		try
		{
			return !this.hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getByte();
		}
		catch (ClassCastException classcastexception)
		{
			return (byte)0;
		}
	}

	public short getShort(String key)
	{
		try
		{
			return !this.hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getShort();
		}
		catch (ClassCastException classcastexception)
		{
			return (short)0;
		}
	}

	public int getInteger(String key)
	{
		try
		{
			return !this.hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getInt();
		}
		catch (ClassCastException classcastexception)
		{
			return 0;
		}
	}

	public long getLong(String key)
	{
		try
		{
			return !this.hasKey(key, 99) ? 0L : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getLong();
		}
		catch (ClassCastException classcastexception)
		{
			return 0L;
		}
	}

	public float getFloat(String key)
	{
		try
		{
			return !this.hasKey(key, 99) ? 0.0F : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getFloat();
		}
		catch (ClassCastException classcastexception)
		{
			return 0.0F;
		}
	}

	public double getDouble(String key)
	{
		try
		{
			return !this.hasKey(key, 99) ? 0.0D : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).getDouble();
		}
		catch (ClassCastException classcastexception)
		{
			return 0.0D;
		}
	}

	public String getString(String key)
	{
		try
		{
			return !this.hasKey(key, 8) ? "" : ((NBTBase)this.tagMap.get(key)).getString();
		}
		catch (ClassCastException classcastexception)
		{
			return "";
		}
	}

	public byte[] getByteArray(String key)
	{
		try
		{
			return !this.hasKey(key, 7) ? new byte[0] : ((NBTTagByteArray)this.tagMap.get(key)).getByteArray();
		}
		catch (ClassCastException classcastexception)
		{
			throw new ReportedException(this.createCrashReport(key, 7, classcastexception));
		}
	}

	public int[] getIntArray(String key)
	{
		try
		{
			return !this.hasKey(key, 11) ? new int[0] : ((NBTTagIntArray)this.tagMap.get(key)).getIntArray();
		}
		catch (ClassCastException classcastexception)
		{
			throw new ReportedException(this.createCrashReport(key, 11, classcastexception));
		}
	}

	public NBTTagCompound getCompoundTag(String key)
	{
		try
		{
			return !this.hasKey(key, 10) ? new NBTTagCompound() : (NBTTagCompound)this.tagMap.get(key);
		}
		catch (ClassCastException classcastexception)
		{
			throw new ReportedException(this.createCrashReport(key, 10, classcastexception));
		}
	}

	public NBTTagList getTagList(String key, int type)
	{
		try
		{
			if (this.getTagType(key) != 9)
			{
				return new NBTTagList();
			}
			else
			{
				NBTTagList nbttaglist = (NBTTagList)this.tagMap.get(key);
				return nbttaglist.tagCount() > 0 && nbttaglist.getTagType() != type ? new NBTTagList() : nbttaglist;
			}
		}
		catch (ClassCastException classcastexception)
		{
			throw new ReportedException(this.createCrashReport(key, 9, classcastexception));
		}
	}

	public boolean getBoolean(String key)
	{
		return this.getByte(key) != 0;
	}

	public void removeTag(String key)
	{
		this.tagMap.remove(key);
	}

	public String toString()
	{
		String s = "{";
		String s1;

		for (Iterator iterator = this.tagMap.keySet().iterator(); iterator.hasNext(); s = s + s1 + ':' + this.tagMap.get(s1) + ',')
		{
			s1 = (String)iterator.next();
		}

		return s + "}";
	}

	public boolean hasNoTags()
	{
		return this.tagMap.isEmpty();
	}

	private CrashReport createCrashReport(final String key, final int expectedType, ClassCastException ex)
	{
		CrashReport crashreport = CrashReport.makeCrashReport(ex, "Reading NBT data");
		CrashReportCategory crashreportcategory = crashreport.makeCategoryDepth("Corrupt NBT tag", 1);
		crashreportcategory.addCrashSectionCallable("Tag type found", new Callable()
		{
			private static final String __OBFID = "CL_00001216";
			public String call()
			{
				return NBTBase.NBT_TYPES[((NBTBase)NBTTagCompound.this.tagMap.get(key)).getId()];
			}
		});
		crashreportcategory.addCrashSectionCallable("Tag type expected", new Callable()
		{
			private static final String __OBFID = "CL_00001217";
			public String call()
			{
				return NBTBase.NBT_TYPES[expectedType];
			}
		});
		crashreportcategory.addCrashSection("Tag name", key);
		return crashreport;
	}

	public NBTBase copy()
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		Iterator iterator = this.tagMap.keySet().iterator();

		while (iterator.hasNext())
		{
			String s = (String)iterator.next();
			nbttagcompound.setTag(s, ((NBTBase)this.tagMap.get(s)).copy());
		}

		return nbttagcompound;
	}

	public boolean equals(Object p_equals_1_)
	{
		if (super.equals(p_equals_1_))
		{
			NBTTagCompound nbttagcompound = (NBTTagCompound)p_equals_1_;
			return this.tagMap.entrySet().equals(nbttagcompound.tagMap.entrySet());
		}
		else
		{
			return false;
		}
	}

	public int hashCode()
	{
		return super.hashCode() ^ this.tagMap.hashCode();
	}

	private static void writeEntry(String name, NBTBase data, DataOutput output) throws IOException
	{
		output.writeByte(data.getId());

		if (data.getId() != 0)
		{
			output.writeUTF(name);
			data.write(output);
		}
	}

	private static byte readType(DataInput input, NBTSizeTracker sizeTracker) throws IOException
	{
		sizeTracker.read(8);
		return input.readByte();
	}

	private static String readKey(DataInput input, NBTSizeTracker sizeTracker) throws IOException
	{
		return input.readUTF();
	}

	static NBTBase readNBT(byte id, String key, DataInput input, int depth, NBTSizeTracker sizeTracker)
	{
		sizeTracker.read(32); //Forge: 4 extra bytes for the object allocation.
		NBTBase nbtbase = NBTBase.createNewByType(id);

		try
		{
			nbtbase.read(input, depth, sizeTracker);
			return nbtbase;
		}
		catch (IOException ioexception)
		{
			CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
			crashreportcategory.addCrashSection("Tag name", key);
			crashreportcategory.addCrashSection("Tag type", Byte.valueOf(id));
			throw new ReportedException(crashreport);
		}
	}

	public void merge(NBTTagCompound other)
	{
		Iterator iterator = other.tagMap.keySet().iterator();

		while (iterator.hasNext())
		{
			String s = (String)iterator.next();
			NBTBase nbtbase = (NBTBase)other.tagMap.get(s);

			if (nbtbase.getId() == 10)
			{
				if (this.hasKey(s, 10))
				{
					NBTTagCompound nbttagcompound1 = this.getCompoundTag(s);
					nbttagcompound1.merge((NBTTagCompound)nbtbase);
				}
				else
				{
					this.setTag(s, nbtbase.copy());
				}
			}
			else
			{
				this.setTag(s, nbtbase.copy());
			}
		}
	}
}