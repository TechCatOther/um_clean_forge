package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerAddMushroomIsland extends GenLayer
{
	private static final String __OBFID = "CL_00000552";

	public GenLayerAddMushroomIsland(long p_i2120_1_, GenLayer p_i2120_3_)
	{
		super(p_i2120_1_);
		this.parent = p_i2120_3_;
	}

	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		int i1 = areaX - 1;
		int j1 = areaY - 1;
		int k1 = areaWidth + 2;
		int l1 = areaHeight + 2;
		int[] aint = this.parent.getInts(i1, j1, k1, l1);
		int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

		for (int i2 = 0; i2 < areaHeight; ++i2)
		{
			for (int j2 = 0; j2 < areaWidth; ++j2)
			{
				int k2 = aint[j2 + 0 + (i2 + 0) * k1];
				int l2 = aint[j2 + 2 + (i2 + 0) * k1];
				int i3 = aint[j2 + 0 + (i2 + 2) * k1];
				int j3 = aint[j2 + 2 + (i2 + 2) * k1];
				int k3 = aint[j2 + 1 + (i2 + 1) * k1];
				this.initChunkSeed((long)(j2 + areaX), (long)(i2 + areaY));

				if (k3 == 0 && k2 == 0 && l2 == 0 && i3 == 0 && j3 == 0 && this.nextInt(100) == 0)
				{
					aint1[j2 + i2 * areaWidth] = BiomeGenBase.mushroomIsland.biomeID;
				}
				else
				{
					aint1[j2 + i2 * areaWidth] = k3;
				}
			}
		}

		return aint1;
	}
}