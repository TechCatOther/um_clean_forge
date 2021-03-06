package net.minecraft.util;

public class IntHashMap
{
	private transient IntHashMap.Entry[] slots = new IntHashMap.Entry[16];
	private transient int count;
	private int threshold = 12;
	private final float growFactor = 0.75F;
	private static final String __OBFID = "CL_00001490";

	private static int computeHash(int integer)
	{
		integer ^= integer >>> 20 ^ integer >>> 12;
		return integer ^ integer >>> 7 ^ integer >>> 4;
	}

	private static int getSlotIndex(int hash, int slotCount)
	{
		return hash & slotCount - 1;
	}

	public Object lookup(int p_76041_1_)
	{
		int j = computeHash(p_76041_1_);

		for (IntHashMap.Entry entry = this.slots[getSlotIndex(j, this.slots.length)]; entry != null; entry = entry.nextEntry)
		{
			if (entry.hashEntry == p_76041_1_)
			{
				return entry.valueEntry;
			}
		}

		return null;
	}

	public boolean containsItem(int p_76037_1_)
	{
		return this.lookupEntry(p_76037_1_) != null;
	}

	final IntHashMap.Entry lookupEntry(int p_76045_1_)
	{
		int j = computeHash(p_76045_1_);

		for (IntHashMap.Entry entry = this.slots[getSlotIndex(j, this.slots.length)]; entry != null; entry = entry.nextEntry)
		{
			if (entry.hashEntry == p_76045_1_)
			{
				return entry;
			}
		}

		return null;
	}

	public void addKey(int p_76038_1_, Object p_76038_2_)
	{
		int j = computeHash(p_76038_1_);
		int k = getSlotIndex(j, this.slots.length);

		for (IntHashMap.Entry entry = this.slots[k]; entry != null; entry = entry.nextEntry)
		{
			if (entry.hashEntry == p_76038_1_)
			{
				entry.valueEntry = p_76038_2_;
				return;
			}
		}

		this.insert(j, p_76038_1_, p_76038_2_, k);
	}

	private void grow(int p_76047_1_)
	{
		IntHashMap.Entry[] aentry = this.slots;
		int j = aentry.length;

		if (j == 1073741824)
		{
			this.threshold = Integer.MAX_VALUE;
		}
		else
		{
			IntHashMap.Entry[] aentry1 = new IntHashMap.Entry[p_76047_1_];
			this.copyTo(aentry1);
			this.slots = aentry1;
			this.threshold = (int)((float)p_76047_1_ * this.growFactor);
		}
	}

	private void copyTo(IntHashMap.Entry[] p_76048_1_)
	{
		IntHashMap.Entry[] aentry = this.slots;
		int i = p_76048_1_.length;

		for (int j = 0; j < aentry.length; ++j)
		{
			IntHashMap.Entry entry = aentry[j];

			if (entry != null)
			{
				aentry[j] = null;
				IntHashMap.Entry entry1;

				do
				{
					entry1 = entry.nextEntry;
					int k = getSlotIndex(entry.slotHash, i);
					entry.nextEntry = p_76048_1_[k];
					p_76048_1_[k] = entry;
					entry = entry1;
				}
				while (entry1 != null);
			}
		}
	}

	public Object removeObject(int p_76049_1_)
	{
		IntHashMap.Entry entry = this.removeEntry(p_76049_1_);
		return entry == null ? null : entry.valueEntry;
	}

	final IntHashMap.Entry removeEntry(int p_76036_1_)
	{
		int j = computeHash(p_76036_1_);
		int k = getSlotIndex(j, this.slots.length);
		IntHashMap.Entry entry = this.slots[k];
		IntHashMap.Entry entry1;
		IntHashMap.Entry entry2;

		for (entry1 = entry; entry1 != null; entry1 = entry2)
		{
			entry2 = entry1.nextEntry;

			if (entry1.hashEntry == p_76036_1_)
			{
				--this.count;

				if (entry == entry1)
				{
					this.slots[k] = entry2;
				}
				else
				{
					entry.nextEntry = entry2;
				}

				return entry1;
			}

			entry = entry1;
		}

		return entry1;
	}

	public void clearMap()
	{
		IntHashMap.Entry[] aentry = this.slots;

		for (int i = 0; i < aentry.length; ++i)
		{
			aentry[i] = null;
		}

		this.count = 0;
	}

	private void insert(int p_76040_1_, int p_76040_2_, Object p_76040_3_, int p_76040_4_)
	{
		IntHashMap.Entry entry = this.slots[p_76040_4_];
		this.slots[p_76040_4_] = new IntHashMap.Entry(p_76040_1_, p_76040_2_, p_76040_3_, entry);

		if (this.count++ >= this.threshold)
		{
			this.grow(2 * this.slots.length);
		}
	}

	static class Entry
		{
			final int hashEntry;
			Object valueEntry;
			IntHashMap.Entry nextEntry;
			final int slotHash;
			private static final String __OBFID = "CL_00001491";

			Entry(int p_i1552_1_, int p_i1552_2_, Object p_i1552_3_, IntHashMap.Entry p_i1552_4_)
			{
				this.valueEntry = p_i1552_3_;
				this.nextEntry = p_i1552_4_;
				this.hashEntry = p_i1552_2_;
				this.slotHash = p_i1552_1_;
			}

			public final int getHash()
			{
				return this.hashEntry;
			}

			public final Object getValue()
			{
				return this.valueEntry;
			}

			public final boolean equals(Object p_equals_1_)
			{
				if (!(p_equals_1_ instanceof IntHashMap.Entry))
				{
					return false;
				}
				else
				{
					IntHashMap.Entry entry = (IntHashMap.Entry)p_equals_1_;
					Integer integer = Integer.valueOf(this.getHash());
					Integer integer1 = Integer.valueOf(entry.getHash());

					if (integer == integer1 || integer != null && integer.equals(integer1))
					{
						Object object1 = this.getValue();
						Object object2 = entry.getValue();

						if (object1 == object2 || object1 != null && object1.equals(object2))
						{
							return true;
						}
					}

					return false;
				}
			}

			public final int hashCode()
			{
				return IntHashMap.computeHash(this.hashEntry);
			}

			public final String toString()
			{
				return this.getHash() + "=" + this.getValue();
			}
		}
}