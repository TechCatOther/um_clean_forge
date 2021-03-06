package net.minecraft.world.gen.layer;

public class GenLayerIsland extends GenLayer
{
	private static final String __OBFID = "CL_00000558";

	public GenLayerIsland(long p_i2124_1_)
	{
		super(p_i2124_1_);
	}

	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
	{
		int[] aint = IntCache.getIntCache(areaWidth * areaHeight);

		for (int i1 = 0; i1 < areaHeight; ++i1)
		{
			for (int j1 = 0; j1 < areaWidth; ++j1)
			{
				this.initChunkSeed((long)(areaX + j1), (long)(areaY + i1));
				aint[j1 + i1 * areaWidth] = this.nextInt(10) == 0 ? 1 : 0;
			}
		}

		if (areaX > -areaWidth && areaX <= 0 && areaY > -areaHeight && areaY <= 0)
		{
			aint[-areaX + -areaY * areaWidth] = 1;
		}

		return aint;
	}
}