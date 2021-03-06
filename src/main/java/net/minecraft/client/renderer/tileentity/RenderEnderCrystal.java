package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEnderCrystal extends Render
{
	private static final ResourceLocation enderCrystalTextures = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
	private ModelBase modelEnderCrystal = new ModelEnderCrystal(0.0F, true);
	private static final String __OBFID = "CL_00000987";

	public RenderEnderCrystal(RenderManager p_i46184_1_)
	{
		super(p_i46184_1_);
		this.shadowSize = 0.5F;
	}

	public void doRender(EntityEnderCrystal entity, double x, double y, double z, float p_76986_8_, float partialTicks)
	{
		float f2 = (float)entity.innerRotation + partialTicks;
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x, (float)y, (float)z);
		this.bindTexture(enderCrystalTextures);
		float f3 = MathHelper.sin(f2 * 0.2F) / 2.0F + 0.5F;
		f3 += f3 * f3;
		this.modelEnderCrystal.render(entity, 0.0F, f2 * 3.0F, f3 * 0.2F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
	}

	protected ResourceLocation getEnderCrystalTextures(EntityEnderCrystal p_180554_1_)
	{
		return enderCrystalTextures;
	}

	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return this.getEnderCrystalTextures((EntityEnderCrystal)entity);
	}

	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
	{
		this.doRender((EntityEnderCrystal)entity, x, y, z, p_76986_8_, partialTicks);
	}
}