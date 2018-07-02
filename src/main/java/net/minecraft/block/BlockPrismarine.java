package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPrismarine extends Block
{
	public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockPrismarine.EnumType.class);
	public static final int ROUGH_META = BlockPrismarine.EnumType.ROUGH.getMetadata();
	public static final int BRICKS_META = BlockPrismarine.EnumType.BRICKS.getMetadata();
	public static final int DARK_META = BlockPrismarine.EnumType.DARK.getMetadata();
	private static final String __OBFID = "CL_00002077";

	public BlockPrismarine()
	{
		super(Material.rock);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPrismarine.EnumType.ROUGH));
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	public int damageDropped(IBlockState state)
	{
		return ((BlockPrismarine.EnumType)state.getValue(VARIANT)).getMetadata();
	}

	public int getMetaFromState(IBlockState state)
	{
		return ((BlockPrismarine.EnumType)state.getValue(VARIANT)).getMetadata();
	}

	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] {VARIANT});
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(VARIANT, BlockPrismarine.EnumType.byMetadata(meta));
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(itemIn, 1, ROUGH_META));
		list.add(new ItemStack(itemIn, 1, BRICKS_META));
		list.add(new ItemStack(itemIn, 1, DARK_META));
	}

	public static enum EnumType implements IStringSerializable
	{
		ROUGH(0, "prismarine", "rough"),
		BRICKS(1, "prismarine_bricks", "bricks"),
		DARK(2, "dark_prismarine", "dark");
		private static final BlockPrismarine.EnumType[] META_LOOKUP = new BlockPrismarine.EnumType[values().length];
		private final int meta;
		private final String name;
		private final String unlocalizedName;

		private static final String __OBFID = "CL_00002076";

		private EnumType(int meta, String name, String unlocalizedName)
		{
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
		}

		public int getMetadata()
		{
			return this.meta;
		}

		public String toString()
		{
			return this.name;
		}

		public static BlockPrismarine.EnumType byMetadata(int meta)
		{
			if (meta < 0 || meta >= META_LOOKUP.length)
			{
				meta = 0;
			}

			return META_LOOKUP[meta];
		}

		public String getName()
		{
			return this.name;
		}

		public String getUnlocalizedName()
		{
			return this.unlocalizedName;
		}

		static
		{
			BlockPrismarine.EnumType[] var0 = values();
			int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2)
			{
				BlockPrismarine.EnumType var3 = var0[var2];
				META_LOOKUP[var3.getMetadata()] = var3;
			}
		}
	}
}