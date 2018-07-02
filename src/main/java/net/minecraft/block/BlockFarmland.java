package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFarmland extends Block
{
	public static final PropertyInteger MOISTURE = PropertyInteger.create("moisture", 0, 7);
	private static final String __OBFID = "CL_00000241";

	protected BlockFarmland()
	{
		super(Material.ground);
		this.setDefaultState(this.blockState.getBaseState().withProperty(MOISTURE, Integer.valueOf(0)));
		this.setTickRandomly(true);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
		this.setLightOpacity(255);
	}

	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 1), (double)(pos.getZ() + 1));
	}

	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean isFullCube()
	{
		return false;
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		int i = ((Integer)state.getValue(MOISTURE)).intValue();

		if (!this.hasWater(worldIn, pos) && !worldIn.canLightningStrike(pos.up()))
		{
			if (i > 0)
			{
				worldIn.setBlockState(pos, state.withProperty(MOISTURE, Integer.valueOf(i - 1)), 2);
			}
			else if (!this.hasCrops(worldIn, pos))
			{
				worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
			}
		}
		else if (i < 7)
		{
			worldIn.setBlockState(pos, state.withProperty(MOISTURE, Integer.valueOf(7)), 2);
		}
	}

	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
	{
		if (entityIn instanceof EntityLivingBase)
		{
			if (!worldIn.isRemote && worldIn.rand.nextFloat() < fallDistance - 0.5F)
			{
				if (!(entityIn instanceof EntityPlayer) && !worldIn.getGameRules().getGameRuleBooleanValue("mobGriefing"))
				{
					return;
				}

				worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
			}

			super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
		}
	}

	private boolean hasCrops(World worldIn, BlockPos pos)
	{
		Block block = worldIn.getBlockState(pos.up()).getBlock();
		return block instanceof net.minecraftforge.common.IPlantable && canSustainPlant(worldIn, pos, net.minecraft.util.EnumFacing.UP, (net.minecraftforge.common.IPlantable)block);
	}

	private boolean hasWater(World worldIn, BlockPos pos)
	{
		Iterator iterator = BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4)).iterator();
		BlockPos.MutableBlockPos mutableblockpos;

		do
		{
			if (!iterator.hasNext())
			{
				return false;
			}

			mutableblockpos = (BlockPos.MutableBlockPos)iterator.next();
		}
		while (worldIn.getBlockState(mutableblockpos).getBlock().getMaterial() != Material.water);

		return true;
	}

	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);

		if (worldIn.getBlockState(pos.up()).getBlock().getMaterial().isSolid())
		{
			worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
		}
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(MOISTURE, Integer.valueOf(meta & 7));
	}

	@SideOnly(Side.CLIENT)
	public Item getItem(World worldIn, BlockPos pos)
	{
		return Item.getItemFromBlock(Blocks.dirt);
	}

	public int getMetaFromState(IBlockState state)
	{
		return ((Integer)state.getValue(MOISTURE)).intValue();
	}

	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] {MOISTURE});
	}
}