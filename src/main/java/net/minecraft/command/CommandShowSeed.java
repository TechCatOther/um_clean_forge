package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class CommandShowSeed extends CommandBase
{
	private static final String __OBFID = "CL_00001053";

	public boolean canCommandSenderUse(ICommandSender sender)
	{
		return MinecraftServer.getServer().isSinglePlayer() || super.canCommandSenderUse(sender);
	}

	public String getName()
	{
		return "seed";
	}

	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	public String getCommandUsage(ICommandSender sender)
	{
		return "commands.seed.usage";
	}

	public void execute(ICommandSender sender, String[] args) throws CommandException
	{
		Object object = sender instanceof EntityPlayer ? ((EntityPlayer)sender).worldObj : MinecraftServer.getServer().worldServerForDimension(0);
		sender.addChatMessage(new ChatComponentTranslation("commands.seed.success", new Object[] {Long.valueOf(((World)object).getSeed())}));
	}
}