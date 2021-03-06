package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerWolfCollar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderWolf extends RenderLiving
{
	private static final ResourceLocation wolfTextures = new ResourceLocation("textures/entity/wolf/wolf.png");
	private static final ResourceLocation tamedWolfTextures = new ResourceLocation("textures/entity/wolf/wolf_tame.png");
	private static final ResourceLocation anrgyWolfTextures = new ResourceLocation("textures/entity/wolf/wolf_angry.png");
	private static final String __OBFID = "CL_00001036";

	public RenderWolf(RenderManager p_i46128_1_, ModelBase p_i46128_2_, float p_i46128_3_)
	{
		super(p_i46128_1_, p_i46128_2_, p_i46128_3_);
		this.addLayer(new LayerWolfCollar(this));
	}

	protected float func_180593_a(EntityWolf p_180593_1_, float p_180593_2_)
	{
		return p_180593_1_.getTailRotation();
	}

	public void func_177135_a(EntityWolf p_177135_1_, double p_177135_2_, double p_177135_4_, double p_177135_6_, float p_177135_8_, float p_177135_9_)
	{
		if (p_177135_1_.isWolfWet())
		{
			float f2 = p_177135_1_.getBrightness(p_177135_9_) * p_177135_1_.getShadingWhileWet(p_177135_9_);
			GlStateManager.color(f2, f2, f2);
		}

		super.doRender((EntityLiving)p_177135_1_, p_177135_2_, p_177135_4_, p_177135_6_, p_177135_8_, p_177135_9_);
	}

	protected ResourceLocation getEntityTexture(EntityWolf entity)
	{
		return entity.isTamed() ? tamedWolfTextures : (entity.isAngry() ? anrgyWolfTextures : wolfTextures);
	}

	public void doRender(EntityLiving entity, double x, double y, double z, float p_76986_8_, float partialTicks)
	{
		this.func_177135_a((EntityWolf)entity, x, y, z, p_76986_8_, partialTicks);
	}

	protected float handleRotationFloat(EntityLivingBase p_77044_1_, float p_77044_2_)
	{
		return this.func_180593_a((EntityWolf)p_77044_1_, p_77044_2_);
	}

	public void doRender(EntityLivingBase entity, double x, double y, double z, float p_76986_8_, float partialTicks)
	{
		this.func_177135_a((EntityWolf)entity, x, y, z, p_76986_8_, partialTicks);
	}

	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return this.getEntityTexture((EntityWolf)entity);
	}

	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
	{
		this.func_177135_a((EntityWolf)entity, x, y, z, p_76986_8_, partialTicks);
	}
}