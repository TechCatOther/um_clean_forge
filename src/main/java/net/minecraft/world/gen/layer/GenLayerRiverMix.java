package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerRiverMix extends GenLayer
{
	private GenLayer biomePatternGeneratorChain;
	private GenLayer riverPatternGeneratorChain;
	private static final String __OBFID = "CL_00000567";

	public GenLayerRiverMix(long p_i2129_1_, GenLayer p_i2129_3_, GenLayer p_i2129_4_)
	{
		super(p_i2129_1_);
		this.biomePatternGeneratorChain = p_i2129_3_;
		this.riverPatternGeneratorChain = p_i2129_4_;
	}

	public void initWorldGenSeed(long p_75905_1_)
	{
		this.biomePatternGeneratorChain.initWorldGenSeed(p_75905_1_);
		this.riverPatternGeneratorChain.initWorldGenSeed(p_75905_1_);
		super.initWorldGenSeed(p_75905_1_);
	}

	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		int[] aint = this.biomePatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
		int[] aint1 = this.riverPatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
		int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);

		for (int i1 = 0; i1 < areaWidth * areaHeight; ++i1)
		{
			if (aint[i1] != BiomeGenBase.ocean.biomeID && aint[i1] != BiomeGenBase.deepOcean.biomeID)
			{
				if (aint1[i1] == BiomeGenBase.river.biomeID)
				{
					if (aint[i1] == BiomeGenBase.icePlains.biomeID)
					{
						aint2[i1] = BiomeGenBase.frozenRiver.biomeID;
					}
					else if (aint[i1] != BiomeGenBase.mushroomIsland.biomeID && aint[i1] != BiomeGenBase.mushroomIslandShore.biomeID)
					{
						aint2[i1] = aint1[i1] & 255;
					}
					else
					{
						aint2[i1] = BiomeGenBase.mushroomIslandShore.biomeID;
					}
				}
				else
				{
					aint2[i1] = aint[i1];
				}
			}
			else
			{
				aint2[i1] = aint[i1];
			}
		}

		return aint2;
	}
}