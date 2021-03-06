package net.minecraft.client.resources;

import java.util.UUID;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DefaultPlayerSkin
{
	private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
	private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
	private static final String __OBFID = "CL_00002396";

	public static ResourceLocation getDefaultSkinLegacy()
	{
		return TEXTURE_STEVE;
	}

	public static ResourceLocation getDefaultSkin(UUID playerUUID)
	{
		return isSlimSkin(playerUUID) ? TEXTURE_ALEX : TEXTURE_STEVE;
	}

	public static String getSkinType(UUID playerUUID)
	{
		return isSlimSkin(playerUUID) ? "slim" : "default";
	}

	private static boolean isSlimSkin(UUID playerUUID)
	{
		return (playerUUID.hashCode() & 1) == 1;
	}
}