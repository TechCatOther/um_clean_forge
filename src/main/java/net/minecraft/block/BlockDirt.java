package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDirt extends Block
{
	public static final PropertyEnum VARIANT = PropertyEnum.create("variant", BlockDirt.DirtType.class);
	public static final PropertyBool SNOWY = PropertyBool.create("snowy");
	private static final String __OBFID = "CL_00000228";

	protected BlockDirt()
	{
		super(Material.ground);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockDirt.DirtType.DIRT).withProperty(SNOWY, Boolean.valueOf(false)));
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		if (state.getValue(VARIANT) == BlockDirt.DirtType.PODZOL)
		{
			Block block = worldIn.getBlockState(pos.up()).getBlock();
			state = state.withProperty(SNOWY, Boolean.valueOf(block == Blocks.snow || block == Blocks.snow_layer));
		}

		return state;
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(this, 1, BlockDirt.DirtType.DIRT.getMetadata()));
		list.add(new ItemStack(this, 1, BlockDirt.DirtType.COARSE_DIRT.getMetadata()));
		list.add(new ItemStack(this, 1, BlockDirt.DirtType.PODZOL.getMetadata()));
	}

	public int getDamageValue(World worldIn, BlockPos pos)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		return iblockstate.getBlock() != this ? 0 : ((BlockDirt.DirtType)iblockstate.getValue(VARIANT)).getMetadata();
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(VARIANT, BlockDirt.DirtType.byMetadata(meta));
	}

	public int getMetaFromState(IBlockState state)
	{
		return ((BlockDirt.DirtType)state.getValue(VARIANT)).getMetadata();
	}

	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] {VARIANT, SNOWY});
	}

	public int damageDropped(IBlockState state)
	{
		BlockDirt.DirtType dirttype = (BlockDirt.DirtType)state.getValue(VARIANT);

		if (dirttype == BlockDirt.DirtType.PODZOL)
		{
			dirttype = BlockDirt.DirtType.DIRT;
		}

		return dirttype.getMetadata();
	}

	public static enum DirtType implements IStringSerializable
	{
		DIRT(0, "dirt", "default"),
		COARSE_DIRT(1, "coarse_dirt", "coarse"),
		PODZOL(2, "podzol");
		private static final BlockDirt.DirtType[] METADATA_LOOKUP = new BlockDirt.DirtType[values().length];
		private final int metadata;
		private final String name;
		private final String unlocalizedName;

		private static final String __OBFID = "CL_00002125";

		private DirtType(int metadata, String name)
		{
			this(metadata, name, name);
		}

		private DirtType(int metadata, String name, String unlocalizedName)
		{
			this.metadata = metadata;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
		}

		public int getMetadata()
		{
			return this.metadata;
		}

		public String getUnlocalizedName()
		{
			return this.unlocalizedName;
		}

		public String toString()
		{
			return this.name;
		}

		public static BlockDirt.DirtType byMetadata(int metadata)
		{
			if (metadata < 0 || metadata >= METADATA_LOOKUP.length)
			{
				metadata = 0;
			}

			return METADATA_LOOKUP[metadata];
		}

		public String getName()
		{
			return this.name;
		}

		static
		{
			BlockDirt.DirtType[] var0 = values();
			int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2)
			{
				BlockDirt.DirtType var3 = var0[var2];
				METADATA_LOOKUP[var3.getMetadata()] = var3;
			}
		}
	}
}