package net.minecraft.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class WeightedRandom
{
	private static final String __OBFID = "CL_00001503";

	public static int getTotalWeight(Collection collection)
	{
		int i = 0;
		WeightedRandom.Item item;

		for (Iterator iterator = collection.iterator(); iterator.hasNext(); i += item.itemWeight)
		{
			item = (WeightedRandom.Item)iterator.next();
		}

		return i;
	}

	public static WeightedRandom.Item getRandomItem(Random random, Collection collection, int p_76273_2_)
	{
		if (p_76273_2_ <= 0)
		{
			throw new IllegalArgumentException();
		}
		else
		{
			int j = random.nextInt(p_76273_2_);
			return getRandomItem(collection, j);
		}
	}

	public static WeightedRandom.Item getRandomItem(Collection collection, int p_180166_1_)
	{
		Iterator iterator = collection.iterator();
		WeightedRandom.Item item;

		do
		{
			if (!iterator.hasNext())
			{
				return null;
			}

			item = (WeightedRandom.Item)iterator.next();
			p_180166_1_ -= item.itemWeight;
		}
		while (p_180166_1_ >= 0);

		return item;
	}

	public static WeightedRandom.Item getRandomItem(Random random, Collection collection)
	{
		return getRandomItem(random, collection, getTotalWeight(collection));
	}

	public static class Item
		{
			public int itemWeight;
			private static final String __OBFID = "CL_00001504";

			public Item(int itemWeightIn)
			{
				this.itemWeight = itemWeightIn;
			}
		}
}