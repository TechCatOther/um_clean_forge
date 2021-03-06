package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCarrotOnAStick extends Item
{
	private static final String __OBFID = "CL_00000001";

	public ItemCarrotOnAStick()
	{
		this.setCreativeTab(CreativeTabs.tabTransport);
		this.setMaxStackSize(1);
		this.setMaxDamage(25);
	}

	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldRotateAroundWhenRendering()
	{
		return true;
	}

	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		if (playerIn.isRiding() && playerIn.ridingEntity instanceof EntityPig)
		{
			EntityPig entitypig = (EntityPig)playerIn.ridingEntity;

			if (entitypig.getAIControlledByPlayer().isControlledByPlayer() && itemStackIn.getMaxDamage() - itemStackIn.getMetadata() >= 7)
			{
				entitypig.getAIControlledByPlayer().boostSpeed();
				itemStackIn.damageItem(7, playerIn);

				if (itemStackIn.stackSize == 0)
				{
					ItemStack itemstack1 = new ItemStack(Items.fishing_rod);
					itemstack1.setTagCompound(itemStackIn.getTagCompound());
					return itemstack1;
				}
			}
		}

		playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
		return itemStackIn;
	}
}