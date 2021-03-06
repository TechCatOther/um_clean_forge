package net.minecraft.client.resources.data;

import java.util.Collection;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LanguageMetadataSection implements IMetadataSection
{
	private final Collection languages;
	private static final String __OBFID = "CL_00001110";

	public LanguageMetadataSection(Collection p_i1311_1_)
	{
		this.languages = p_i1311_1_;
	}

	public Collection getLanguages()
	{
		return this.languages;
	}
}