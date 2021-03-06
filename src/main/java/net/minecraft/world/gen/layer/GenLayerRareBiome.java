package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerRareBiome extends GenLayer
{
	private static final String __OBFID = "CL_00000562";

	public GenLayerRareBiome(long p_i45478_1_, GenLayer p_i45478_3_)
	{
		super(p_i45478_1_);
		this.parent = p_i45478_3_;
	}

	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
		int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

		for (int i1 = 0; i1 < areaHeight; ++i1)
		{
			for (int j1 = 0; j1 < areaWidth; ++j1)
			{
				this.initChunkSeed((long)(j1 + areaX), (long)(i1 + areaY));
				int k1 = aint[j1 + 1 + (i1 + 1) * (areaWidth + 2)];

				if (this.nextInt(57) == 0)
				{
					if (k1 == BiomeGenBase.plains.biomeID)
					{
						aint1[j1 + i1 * areaWidth] = BiomeGenBase.plains.biomeID + 128;
					}
					else
					{
						aint1[j1 + i1 * areaWidth] = k1;
					}
				}
				else
				{
					aint1[j1 + i1 * areaWidth] = k1;
				}
			}
		}

		return aint1;
	}
}