package net.minecraft.command;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CommandExecuteAt extends CommandBase
{
	private static final String __OBFID = "CL_00002344";

	public String getName()
	{
		return "execute";
	}

	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	public String getCommandUsage(ICommandSender sender)
	{
		return "commands.execute.usage";
	}

	public void execute(final ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length < 5)
		{
			throw new WrongUsageException("commands.execute.usage", new Object[0]);
		}
		else
		{
			final Entity entity = func_175759_a(sender, args[0], Entity.class);
			final double d0 = func_175761_b(entity.posX, args[1], false);
			final double d1 = func_175761_b(entity.posY, args[2], false);
			final double d2 = func_175761_b(entity.posZ, args[3], false);
			final BlockPos blockpos = new BlockPos(d0, d1, d2);
			byte b0 = 4;

			if ("detect".equals(args[4]) && args.length > 10)
			{
				World world = sender.getEntityWorld();
				double d3 = func_175761_b(d0, args[5], false);
				double d4 = func_175761_b(d1, args[6], false);
				double d5 = func_175761_b(d2, args[7], false);
				Block block = getBlockByText(sender, args[8]);
				int j = parseInt(args[9], -1, 15);
				BlockPos blockpos1 = new BlockPos(d3, d4, d5);
				IBlockState iblockstate = world.getBlockState(blockpos1);

				if (iblockstate.getBlock() != block || j >= 0 && iblockstate.getBlock().getMetaFromState(iblockstate) != j)
				{
					throw new CommandException("commands.execute.failed", new Object[] {"detect", entity.getName()});
				}

				b0 = 10;
			}

			String s = func_180529_a(args, b0);
			ICommandSender icommandsender = new ICommandSender()
			{
				private static final String __OBFID = "CL_00002343";
				public String getName()
				{
					return entity.getName();
				}
				public IChatComponent getDisplayName()
				{
					return entity.getDisplayName();
				}
				public void addChatMessage(IChatComponent message)
				{
					sender.addChatMessage(message);
				}
				public boolean canUseCommand(int permLevel, String commandName)
				{
					return sender.canUseCommand(permLevel, commandName);
				}
				public BlockPos getPosition()
				{
					return blockpos;
				}
				public Vec3 getPositionVector()
				{
					return new Vec3(d0, d1, d2);
				}
				public World getEntityWorld()
				{
					return entity.worldObj;
				}
				public Entity getCommandSenderEntity()
				{
					return entity;
				}
				public boolean sendCommandFeedback()
				{
					MinecraftServer minecraftserver = MinecraftServer.getServer();
					return minecraftserver == null || minecraftserver.worldServers[0].getGameRules().getGameRuleBooleanValue("commandBlockOutput");
				}
				public void setCommandStat(CommandResultStats.Type type, int amount)
				{
					entity.setCommandStat(type, amount);
				}
			};
			ICommandManager icommandmanager = MinecraftServer.getServer().getCommandManager();

			try
			{
				int i = icommandmanager.executeCommand(icommandsender, s);

				if (i < 1)
				{
					throw new CommandException("commands.execute.allInvocationsFailed", new Object[] {s});
				}
			}
			catch (Throwable throwable)
			{
				throw new CommandException("commands.execute.failed", new Object[] {s, entity.getName()});
			}
		}
	}

	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
	{
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : (args.length > 1 && args.length <= 4 ? func_175771_a(args, 1, pos) : (args.length > 5 && args.length <= 8 && "detect".equals(args[4]) ? func_175771_a(args, 5, pos) : (args.length == 9 && "detect".equals(args[4]) ? func_175762_a(args, Block.blockRegistry.getKeys()) : null)));
	}

	public boolean isUsernameIndex(String[] args, int index)
	{
		return index == 0;
	}
}