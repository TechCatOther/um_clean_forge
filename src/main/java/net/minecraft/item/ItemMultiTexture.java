package net.minecraft.item;

import com.google.common.base.Function;
import net.minecraft.block.Block;

public class ItemMultiTexture extends ItemBlock
{
	protected final Block theBlock;
	protected final Function nameFunction;
	private static final String __OBFID = "CL_00000051";

	public ItemMultiTexture(Block block, Block block2, Function nameFunction)
	{
		super(block);
		this.theBlock = block2;
		this.nameFunction = nameFunction;
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	public ItemMultiTexture(Block block, Block block2, final String[] namesByMeta)
	{
		this(block, block2, new Function()
		{
			private static final String __OBFID = "CL_00002161";
			public String apply(ItemStack stack)
			{
				int i = stack.getMetadata();

				if (i < 0 || i >= namesByMeta.length)
				{
					i = 0;
				}

				return namesByMeta[i];
			}
			public Object apply(Object p_apply_1_)
			{
				return this.apply((ItemStack)p_apply_1_);
			}
		});
	}

	public int getMetadata(int damage)
	{
		return damage;
	}

	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName() + "." + (String)this.nameFunction.apply(stack);
	}
}