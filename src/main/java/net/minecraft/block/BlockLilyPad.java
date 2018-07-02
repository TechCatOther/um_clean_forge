package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLilyPad extends BlockBush
{
	private static final String __OBFID = "CL_00000332";

	protected BlockLilyPad()
	{
		float f = 0.5F;
		float f1 = 0.015625F;
		this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
	{
		if (collidingEntity == null || !(collidingEntity instanceof EntityBoat))
		{
			super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
		}
	}

	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
	{
		return new AxisAlignedBB((double)pos.getX() + this.minX, (double)pos.getY() + this.minY, (double)pos.getZ() + this.minZ, (double)pos.getX() + this.maxX, (double)pos.getY() + this.maxY, (double)pos.getZ() + this.maxZ);
	}

	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		return 7455580;
	}

	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state)
	{
		return 7455580;
	}

	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
	{
		return 2129968;
	}

	protected boolean canPlaceBlockOn(Block ground)
	{
		return ground == Blocks.water;
	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		if (pos.getY() >= 0 && pos.getY() < 256)
		{
			IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
			return iblockstate1.getBlock().getMaterial() == Material.water && ((Integer)iblockstate1.getValue(BlockLiquid.LEVEL)).intValue() == 0;
		}
		else
		{
			return false;
		}
	}

	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
}