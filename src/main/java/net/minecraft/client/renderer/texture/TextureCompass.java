package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextureCompass extends TextureAtlasSprite
{
	public double currentAngle;
	public double angleDelta;
	public static String field_176608_l;
	private static final String __OBFID = "CL_00001071";

	public TextureCompass(String iconName)
	{
		super(iconName);
		field_176608_l = iconName;
	}

	public void updateAnimation()
	{
		Minecraft minecraft = Minecraft.getMinecraft();

		if (minecraft.theWorld != null && minecraft.thePlayer != null)
		{
			this.updateCompass(minecraft.theWorld, minecraft.thePlayer.posX, minecraft.thePlayer.posZ, (double)minecraft.thePlayer.rotationYaw, false, false);
		}
		else
		{
			this.updateCompass((World)null, 0.0D, 0.0D, 0.0D, true, false);
		}
	}

	public void updateCompass(World worldIn, double p_94241_2_, double p_94241_4_, double p_94241_6_, boolean p_94241_8_, boolean p_94241_9_)
	{
		if (!this.framesTextureData.isEmpty())
		{
			double d3 = 0.0D;

			if (worldIn != null && !p_94241_8_)
			{
				BlockPos blockpos = worldIn.getSpawnPoint();
				double d4 = (double)blockpos.getX() - p_94241_2_;
				double d5 = (double)blockpos.getZ() - p_94241_4_;
				p_94241_6_ %= 360.0D;
				d3 = -((p_94241_6_ - 90.0D) * Math.PI / 180.0D - Math.atan2(d5, d4));

				if (!worldIn.provider.isSurfaceWorld())
				{
					d3 = Math.random() * Math.PI * 2.0D;
				}
			}

			if (p_94241_9_)
			{
				this.currentAngle = d3;
			}
			else
			{
				double d6;

				for (d6 = d3 - this.currentAngle; d6 < -Math.PI; d6 += (Math.PI * 2D))
				{
					;
				}

				while (d6 >= Math.PI)
				{
					d6 -= (Math.PI * 2D);
				}

				d6 = MathHelper.clamp_double(d6, -1.0D, 1.0D);
				this.angleDelta += d6 * 0.1D;
				this.angleDelta *= 0.8D;
				this.currentAngle += this.angleDelta;
			}

			int i;

			for (i = (int)((this.currentAngle / (Math.PI * 2D) + 1.0D) * (double)this.framesTextureData.size()) % this.framesTextureData.size(); i < 0; i = (i + this.framesTextureData.size()) % this.framesTextureData.size())
			{
				;
			}

			if (i != this.frameCounter)
			{
				this.frameCounter = i;
				TextureUtil.uploadTextureMipmap((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
			}
		}
	}
}