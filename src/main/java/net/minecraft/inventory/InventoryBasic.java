package net.minecraft.inventory;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InventoryBasic implements IInventory
{
	private String inventoryTitle;
	private int slotsCount;
	private ItemStack[] inventoryContents;
	private List field_70480_d;
	private boolean hasCustomName;
	private static final String __OBFID = "CL_00001514";

	public InventoryBasic(String title, boolean customName, int slotCount)
	{
		this.inventoryTitle = title;
		this.hasCustomName = customName;
		this.slotsCount = slotCount;
		this.inventoryContents = new ItemStack[slotCount];
	}

	@SideOnly(Side.CLIENT)
	public InventoryBasic(IChatComponent title, int slotCount)
	{
		this(title.getUnformattedText(), true, slotCount);
	}

	public void func_110134_a(IInvBasic p_110134_1_)
	{
		if (this.field_70480_d == null)
		{
			this.field_70480_d = Lists.newArrayList();
		}

		this.field_70480_d.add(p_110134_1_);
	}

	public void func_110132_b(IInvBasic p_110132_1_)
	{
		this.field_70480_d.remove(p_110132_1_);
	}

	public ItemStack getStackInSlot(int index)
	{
		return index >= 0 && index < this.inventoryContents.length ? this.inventoryContents[index] : null;
	}

	public ItemStack decrStackSize(int index, int count)
	{
		if (this.inventoryContents[index] != null)
		{
			ItemStack itemstack;

			if (this.inventoryContents[index].stackSize <= count)
			{
				itemstack = this.inventoryContents[index];
				this.inventoryContents[index] = null;
				this.markDirty();
				return itemstack;
			}
			else
			{
				itemstack = this.inventoryContents[index].splitStack(count);

				if (this.inventoryContents[index].stackSize == 0)
				{
					this.inventoryContents[index] = null;
				}

				this.markDirty();
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	public ItemStack func_174894_a(ItemStack p_174894_1_)
	{
		ItemStack itemstack1 = p_174894_1_.copy();

		for (int i = 0; i < this.slotsCount; ++i)
		{
			ItemStack itemstack2 = this.getStackInSlot(i);

			if (itemstack2 == null)
			{
				this.setInventorySlotContents(i, itemstack1);
				this.markDirty();
				return null;
			}

			if (ItemStack.areItemsEqual(itemstack2, itemstack1))
			{
				int j = Math.min(this.getInventoryStackLimit(), itemstack2.getMaxStackSize());
				int k = Math.min(itemstack1.stackSize, j - itemstack2.stackSize);

				if (k > 0)
				{
					itemstack2.stackSize += k;
					itemstack1.stackSize -= k;

					if (itemstack1.stackSize <= 0)
					{
						this.markDirty();
						return null;
					}
				}
			}
		}

		if (itemstack1.stackSize != p_174894_1_.stackSize)
		{
			this.markDirty();
		}

		return itemstack1;
	}

	public ItemStack getStackInSlotOnClosing(int index)
	{
		if (this.inventoryContents[index] != null)
		{
			ItemStack itemstack = this.inventoryContents[index];
			this.inventoryContents[index] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.inventoryContents[index] = stack;

		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}

		this.markDirty();
	}

	public int getSizeInventory()
	{
		return this.slotsCount;
	}

	public String getName()
	{
		return this.inventoryTitle;
	}

	public boolean hasCustomName()
	{
		return this.hasCustomName;
	}

	public void setCustomName(String p_110133_1_)
	{
		this.hasCustomName = true;
		this.inventoryTitle = p_110133_1_;
	}

	public IChatComponent getDisplayName()
	{
		return (IChatComponent)(this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
	}

	public int getInventoryStackLimit()
	{
		return 64;
	}

	public void markDirty()
	{
		if (this.field_70480_d != null)
		{
			for (int i = 0; i < this.field_70480_d.size(); ++i)
			{
				((IInvBasic)this.field_70480_d.get(i)).onInventoryChanged(this);
			}
		}
	}

	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	public void openInventory(EntityPlayer player) {}

	public void closeInventory(EntityPlayer player) {}

	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return true;
	}

	public int getField(int id)
	{
		return 0;
	}

	public void setField(int id, int value) {}

	public int getFieldCount()
	{
		return 0;
	}

	public void clear()
	{
		for (int i = 0; i < this.inventoryContents.length; ++i)
		{
			this.inventoryContents[i] = null;
		}
	}
}