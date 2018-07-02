package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.layers.LayerMooshroomMushroom;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMooshroom extends RenderLiving
{
	private static final ResourceLocation mooshroomTextures = new ResourceLocation("textures/entity/cow/mooshroom.png");
	private static final String __OBFID = "CL_00001016";

	public RenderMooshroom(RenderManager p_i46152_1_, ModelBase p_i46152_2_, float p_i46152_3_)
	{
		super(p_i46152_1_, p_i46152_2_, p_i46152_3_);
		this.addLayer(new LayerMooshroomMushroom(this));
	}

	protected ResourceLocation func_180582_a(EntityMooshroom p_180582_1_)
	{
		return mooshroomTextures;
	}

	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return this.func_180582_a((EntityMooshroom)entity);
	}
}