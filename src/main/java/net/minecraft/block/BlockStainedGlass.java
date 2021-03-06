package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockStainedGlass extends BlockBreakable
{
	public static final PropertyEnum COLOR = PropertyEnum.create("color", EnumDyeColor.class);
	private static final String __OBFID = "CL_00000312";

	public BlockStainedGlass(Material p_i45427_1_)
	{
		super(p_i45427_1_, false);
		this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	public int damageDropped(IBlockState state)
	{
		return ((EnumDyeColor)state.getValue(COLOR)).getMetadata();
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
		int i = aenumdyecolor.length;

		for (int j = 0; j < i; ++j)
		{
			EnumDyeColor enumdyecolor = aenumdyecolor[j];
			list.add(new ItemStack(itemIn, 1, enumdyecolor.getMetadata()));
		}
	}

	public MapColor getMapColor(IBlockState state)
	{
		return ((EnumDyeColor)state.getValue(COLOR)).getMapColor();
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.TRANSLUCENT;
	}

	public int quantityDropped(Random random)
	{
		return 0;
	}

	protected boolean canSilkHarvest()
	{
		return true;
	}

	public boolean isFullCube()
	{
		return false;
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			BlockBeacon.updateColorAsync(worldIn, pos);
		}
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			BlockBeacon.updateColorAsync(worldIn, pos);
		}
	}

	public int getMetaFromState(IBlockState state)
	{
		return ((EnumDyeColor)state.getValue(COLOR)).getMetadata();
	}

	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] {COLOR});
	}
}