package net.minecraft.world.chunk.storage;

import com.google.common.collect.Maps;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class RegionFileCache
{
	private static final Map regionsByFilename = Maps.newHashMap();
	private static final String __OBFID = "CL_00000383";

	public static synchronized RegionFile createOrLoadRegionFile(File worldDir, int chunkX, int chunkZ)
	{
		File file2 = new File(worldDir, "region");
		File file3 = new File(file2, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca");
		RegionFile regionfile = (RegionFile)regionsByFilename.get(file3);

		if (regionfile != null)
		{
			return regionfile;
		}
		else
		{
			if (!file2.exists())
			{
				file2.mkdirs();
			}

			if (regionsByFilename.size() >= 256)
			{
				clearRegionFileReferences();
			}

			RegionFile regionfile1 = new RegionFile(file3);
			regionsByFilename.put(file3, regionfile1);
			return regionfile1;
		}
	}

	public static synchronized void clearRegionFileReferences()
	{
		Iterator iterator = regionsByFilename.values().iterator();

		while (iterator.hasNext())
		{
			RegionFile regionfile = (RegionFile)iterator.next();

			try
			{
				if (regionfile != null)
				{
					regionfile.close();
				}
			}
			catch (IOException ioexception)
			{
				ioexception.printStackTrace();
			}
		}

		regionsByFilename.clear();
	}

	public static DataInputStream getChunkInputStream(File worldDir, int chunkX, int chunkZ)
	{
		RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
		return regionfile.getChunkDataInputStream(chunkX & 31, chunkZ & 31);
	}

	public static DataOutputStream getChunkOutputStream(File worldDir, int chunkX, int chunkZ)
	{
		RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
		return regionfile.getChunkDataOutputStream(chunkX & 31, chunkZ & 31);
	}
}