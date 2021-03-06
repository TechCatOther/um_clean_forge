package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.RegistrySimple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundRegistry extends RegistrySimple
{
	private Map soundRegistry;
	private static final String __OBFID = "CL_00001151";

	protected Map createUnderlyingMap()
	{
		this.soundRegistry = Maps.newHashMap();
		return this.soundRegistry;
	}

	public void registerSound(SoundEventAccessorComposite p_148762_1_)
	{
		this.putObject(p_148762_1_.getSoundEventLocation(), p_148762_1_);
	}

	public void clearMap()
	{
		this.soundRegistry.clear();
	}
}