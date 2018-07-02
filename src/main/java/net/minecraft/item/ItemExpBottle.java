package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemExpBottle extends Item
{
	private static final String __OBFID = "CL_00000028";

	public ItemExpBottle()
	{
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}

	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		if (!playerIn.capabilities.isCreativeMode)
		{
			--itemStackIn.stackSize;
		}

		worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!worldIn.isRemote)
		{
			worldIn.spawnEntityInWorld(new EntityExpBottle(worldIn, playerIn));
		}

		playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
		return itemStackIn;
	}
}