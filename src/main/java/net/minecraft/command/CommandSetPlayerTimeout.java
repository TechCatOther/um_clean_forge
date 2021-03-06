package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandSetPlayerTimeout extends CommandBase
{
	private static final String __OBFID = "CL_00000999";

	public String getName()
	{
		return "setidletimeout";
	}

	public int getRequiredPermissionLevel()
	{
		return 3;
	}

	public String getCommandUsage(ICommandSender sender)
	{
		return "commands.setidletimeout.usage";
	}

	public void execute(ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length != 1)
		{
			throw new WrongUsageException("commands.setidletimeout.usage", new Object[0]);
		}
		else
		{
			int i = parseInt(args[0], 0);
			MinecraftServer.getServer().setPlayerIdleTimeout(i);
			notifyOperators(sender, this, "commands.setidletimeout.success", new Object[] {Integer.valueOf(i)});
		}
	}
}