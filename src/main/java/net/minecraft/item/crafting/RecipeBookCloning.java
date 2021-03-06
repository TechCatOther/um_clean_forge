package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeBookCloning implements IRecipe
{
	private static final String __OBFID = "CL_00000081";

	public boolean matches(InventoryCrafting p_77569_1_, World worldIn)
	{
		int i = 0;
		ItemStack itemstack = null;

		for (int j = 0; j < p_77569_1_.getSizeInventory(); ++j)
		{
			ItemStack itemstack1 = p_77569_1_.getStackInSlot(j);

			if (itemstack1 != null)
			{
				if (itemstack1.getItem() == Items.written_book)
				{
					if (itemstack != null)
					{
						return false;
					}

					itemstack = itemstack1;
				}
				else
				{
					if (itemstack1.getItem() != Items.writable_book)
					{
						return false;
					}

					++i;
				}
			}
		}

		return itemstack != null && i > 0;
	}

	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_)
	{
		int i = 0;
		ItemStack itemstack = null;

		for (int j = 0; j < p_77572_1_.getSizeInventory(); ++j)
		{
			ItemStack itemstack1 = p_77572_1_.getStackInSlot(j);

			if (itemstack1 != null)
			{
				if (itemstack1.getItem() == Items.written_book)
				{
					if (itemstack != null)
					{
						return null;
					}

					itemstack = itemstack1;
				}
				else
				{
					if (itemstack1.getItem() != Items.writable_book)
					{
						return null;
					}

					++i;
				}
			}
		}

		if (itemstack != null && i >= 1 && ItemEditableBook.getGeneration(itemstack) < 2)
		{
			ItemStack itemstack2 = new ItemStack(Items.written_book, i);
			itemstack2.setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
			itemstack2.getTagCompound().setInteger("generation", ItemEditableBook.getGeneration(itemstack) + 1);

			if (itemstack.hasDisplayName())
			{
				itemstack2.setStackDisplayName(itemstack.getDisplayName());
			}

			return itemstack2;
		}
		else
		{
			return null;
		}
	}

	public int getRecipeSize()
	{
		return 9;
	}

	public ItemStack getRecipeOutput()
	{
		return null;
	}

	public ItemStack[] getRemainingItems(InventoryCrafting p_179532_1_)
	{
		ItemStack[] aitemstack = new ItemStack[p_179532_1_.getSizeInventory()];

		for (int i = 0; i < aitemstack.length; ++i)
		{
			ItemStack itemstack = p_179532_1_.getStackInSlot(i);

			if (itemstack != null && itemstack.getItem() instanceof ItemEditableBook)
			{
				aitemstack[i] = itemstack;
				break;
			}
		}

		return aitemstack;
	}
}