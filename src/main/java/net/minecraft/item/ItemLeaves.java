package net.minecraft.item;

import net.minecraft.block.BlockLeaves;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLeaves extends ItemBlock
{
	private final BlockLeaves leaves;
	private static final String __OBFID = "CL_00000046";

	public ItemLeaves(BlockLeaves block)
	{
		super(block);
		this.leaves = block;
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	public int getMetadata(int damage)
	{
		return damage | 4;
	}

	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int renderPass)
	{
		return this.leaves.getRenderColor(this.leaves.getStateFromMeta(stack.getMetadata()));
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName() + "." + this.leaves.getWoodType(stack.getMetadata()).getUnlocalizedName();
	}
}