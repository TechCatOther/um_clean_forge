package net.minecraft.inventory;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class SlotFurnaceFuel extends Slot
{
	private static final String __OBFID = "CL_00002184";

	public SlotFurnaceFuel(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition)
	{
		super(inventoryIn, slotIndex, xPosition, yPosition);
	}

	public boolean isItemValid(ItemStack stack)
	{
		return TileEntityFurnace.isItemFuel(stack) || isBucket(stack);
	}

	public int getItemStackLimit(ItemStack stack)
	{
		return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
	}

	public static boolean isBucket(ItemStack stack)
	{
		return stack != null && stack.getItem() != null && stack.getItem() == Items.bucket;
	}
}