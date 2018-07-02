package net.minecraft.world.storage;

import java.util.List;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.util.IProgressUpdate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ISaveFormat
{
	@SideOnly(Side.CLIENT)
	String getName();

	ISaveHandler getSaveLoader(String p_75804_1_, boolean p_75804_2_);

	@SideOnly(Side.CLIENT)
	List getSaveList() throws AnvilConverterException;

	void flushCache();

	@SideOnly(Side.CLIENT)
	WorldInfo getWorldInfo(String saveName);

	@SideOnly(Side.CLIENT)
	boolean func_154335_d(String p_154335_1_);

	boolean deleteWorldDirectory(String p_75802_1_);

	@SideOnly(Side.CLIENT)
	void renameWorld(String dirName, String newName);

	@SideOnly(Side.CLIENT)
	boolean func_154334_a(String saveName);

	boolean isOldMapFormat(String saveName);

	boolean convertMapFormat(String filename, IProgressUpdate p_75805_2_);

	@SideOnly(Side.CLIENT)
	boolean canLoadWorld(String p_90033_1_);
}