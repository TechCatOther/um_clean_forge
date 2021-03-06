package net.minecraft.world.gen.layer;

public class GenLayerRiverInit extends GenLayer
{
	private static final String __OBFID = "CL_00000565";

	public GenLayerRiverInit(long p_i2127_1_, GenLayer p_i2127_3_)
	{
		super(p_i2127_1_);
		this.parent = p_i2127_3_;
	}

	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		int[] aint = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
		int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

		for (int i1 = 0; i1 < areaHeight; ++i1)
		{
			for (int j1 = 0; j1 < areaWidth; ++j1)
			{
				this.initChunkSeed((long)(j1 + areaX), (long)(i1 + areaY));
				aint1[j1 + i1 * areaWidth] = aint[j1 + i1 * areaWidth] > 0 ? this.nextInt(299999) + 2 : 0;
			}
		}

		return aint1;
	}
}