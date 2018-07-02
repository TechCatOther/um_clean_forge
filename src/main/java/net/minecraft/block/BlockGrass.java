package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGrass extends Block implements IGrowable
{
	public static final PropertyBool SNOWY = PropertyBool.create("snowy");
	private static final String __OBFID = "CL_00000251";

	protected BlockGrass()
	{
		super(Material.grass);
		this.setDefaultState(this.blockState.getBaseState().withProperty(SNOWY, Boolean.valueOf(false)));
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		Block block = worldIn.getBlockState(pos.up()).getBlock();
		return state.withProperty(SNOWY, Boolean.valueOf(block == Blocks.snow || block == Blocks.snow_layer));
	}

	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		return ColorizerGrass.getGrassColor(0.5D, 1.0D);
	}

	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state)
	{
		return this.getBlockColor();
	}

	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
	{
		return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			if (worldIn.getLightFromNeighbors(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getBlock().getLightOpacity(worldIn, pos.up()) > 2)
			{
				worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
			}
			else
			{
				if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
				{
					for (int i = 0; i < 4; ++i)
					{
						BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
						Block block = worldIn.getBlockState(blockpos1.up()).getBlock();
						IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

						if (iblockstate1.getBlock() == Blocks.dirt && iblockstate1.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && worldIn.getLightFromNeighbors(blockpos1.up()) >= 4 && block.getLightOpacity(worldIn, blockpos1.up()) <= 2)
						{
							worldIn.setBlockState(blockpos1, Blocks.grass.getDefaultState());
						}
					}
				}
			}
		}
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
	}

	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
	{
		return true;
	}

	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}

	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
	{
		BlockPos blockpos1 = pos.up();
		int i = 0;

		while (i < 128)
		{
			BlockPos blockpos2 = blockpos1;
			int j = 0;

			while (true)
			{
				if (j < i / 16)
				{
					blockpos2 = blockpos2.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);

					if (worldIn.getBlockState(blockpos2.down()).getBlock() == Blocks.grass && !worldIn.getBlockState(blockpos2).getBlock().isNormalCube())
					{
						++j;
						continue;
					}
				}
				else if (worldIn.isAirBlock(blockpos2))
				{
					if (rand.nextInt(8) == 0)
					{
						worldIn.getBiomeGenForCoords(blockpos2).plantFlower(worldIn, rand, blockpos2);
					}
					else
					{
						IBlockState iblockstate2 = Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);

						if (Blocks.tallgrass.canBlockStay(worldIn, blockpos2, iblockstate2))
						{
							worldIn.setBlockState(blockpos2, iblockstate2, 3);
						}
					}
				}

				++i;
				break;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}

	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}

	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] {SNOWY});
	}
}