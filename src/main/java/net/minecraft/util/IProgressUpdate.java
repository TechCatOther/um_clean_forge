package net.minecraft.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IProgressUpdate
{
	void displaySavingString(String message);

	@SideOnly(Side.CLIENT)
	void resetProgressAndMessage(String p_73721_1_);

	void displayLoadingString(String message);

	void setLoadingProgress(int progress);

	@SideOnly(Side.CLIENT)
	void setDoneWorking();
}