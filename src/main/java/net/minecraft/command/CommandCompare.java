package net.minecraft.command;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandCompare extends CommandBase
{
	private static final String __OBFID = "CL_00002346";

	public String getName()
	{
		return "testforblocks";
	}

	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	public String getCommandUsage(ICommandSender sender)
	{
		return "commands.compare.usage";
	}

	public void execute(ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length < 9)
		{
			throw new WrongUsageException("commands.compare.usage", new Object[0]);
		}
		else
		{
			sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
			BlockPos blockpos = func_175757_a(sender, args, 0, false);
			BlockPos blockpos1 = func_175757_a(sender, args, 3, false);
			BlockPos blockpos2 = func_175757_a(sender, args, 6, false);
			StructureBoundingBox structureboundingbox = new StructureBoundingBox(blockpos, blockpos1);
			StructureBoundingBox structureboundingbox1 = new StructureBoundingBox(blockpos2, blockpos2.add(structureboundingbox.func_175896_b()));
			int i = structureboundingbox.getXSize() * structureboundingbox.getYSize() * structureboundingbox.getZSize();

			if (i > 524288)
			{
				throw new CommandException("commands.compare.tooManyBlocks", new Object[] {Integer.valueOf(i), Integer.valueOf(524288)});
			}
			else if (structureboundingbox.minY >= 0 && structureboundingbox.maxY < 256 && structureboundingbox1.minY >= 0 && structureboundingbox1.maxY < 256)
			{
				World world = sender.getEntityWorld();

				if (world.isAreaLoaded(structureboundingbox) && world.isAreaLoaded(structureboundingbox1))
				{
					boolean flag = false;

					if (args.length > 9 && args[9].equals("masked"))
					{
						flag = true;
					}

					i = 0;
					BlockPos blockpos3 = new BlockPos(structureboundingbox1.minX - structureboundingbox.minX, structureboundingbox1.minY - structureboundingbox.minY, structureboundingbox1.minZ - structureboundingbox.minZ);

					for (int j = structureboundingbox.minZ; j <= structureboundingbox.maxZ; ++j)
					{
						for (int k = structureboundingbox.minY; k <= structureboundingbox.maxY; ++k)
						{
							for (int l = structureboundingbox.minX; l <= structureboundingbox.maxX; ++l)
							{
								BlockPos blockpos4 = new BlockPos(l, k, j);
								BlockPos blockpos5 = blockpos4.add(blockpos3);
								boolean flag1 = false;
								IBlockState iblockstate = world.getBlockState(blockpos4);

								if (!flag || iblockstate.getBlock() != Blocks.air)
								{
									if (iblockstate == world.getBlockState(blockpos5))
									{
										TileEntity tileentity = world.getTileEntity(blockpos4);
										TileEntity tileentity1 = world.getTileEntity(blockpos5);

										if (tileentity != null && tileentity1 != null)
										{
											NBTTagCompound nbttagcompound = new NBTTagCompound();
											tileentity.writeToNBT(nbttagcompound);
											nbttagcompound.removeTag("x");
											nbttagcompound.removeTag("y");
											nbttagcompound.removeTag("z");
											NBTTagCompound nbttagcompound1 = new NBTTagCompound();
											tileentity1.writeToNBT(nbttagcompound1);
											nbttagcompound1.removeTag("x");
											nbttagcompound1.removeTag("y");
											nbttagcompound1.removeTag("z");

											if (!nbttagcompound.equals(nbttagcompound1))
											{
												flag1 = true;
											}
										}
										else if (tileentity != null)
										{
											flag1 = true;
										}
									}
									else
									{
										flag1 = true;
									}

									++i;

									if (flag1)
									{
										throw new CommandException("commands.compare.failed", new Object[0]);
									}
								}
							}
						}
					}

					sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, i);
					notifyOperators(sender, this, "commands.compare.success", new Object[] {Integer.valueOf(i)});
				}
				else
				{
					throw new CommandException("commands.compare.outOfWorld", new Object[0]);
				}
			}
			else
			{
				throw new CommandException("commands.compare.outOfWorld", new Object[0]);
			}
		}
	}

	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
	{
		return args.length > 0 && args.length <= 3 ? func_175771_a(args, 0, pos) : (args.length > 3 && args.length <= 6 ? func_175771_a(args, 3, pos) : (args.length > 6 && args.length <= 9 ? func_175771_a(args, 6, pos) : (args.length == 10 ? getListOfStringsMatchingLastWord(args, new String[] {"masked", "all"}): null)));
	}
}