package net.minecraft.server.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class NetHandlerHandshakeTCP implements INetHandlerHandshakeServer
{
	private final MinecraftServer server;
	private final NetworkManager networkManager;
	private static final String __OBFID = "CL_00001456";

	public NetHandlerHandshakeTCP(MinecraftServer serverIn, NetworkManager netManager)
	{
		this.server = serverIn;
		this.networkManager = netManager;
	}

	public void processHandshake(C00Handshake packetIn)
	{
		if (!net.minecraftforge.fml.common.FMLCommonHandler.instance().handleServerHandshake(packetIn, this.networkManager)) return;

		switch (NetHandlerHandshakeTCP.SwitchEnumConnectionState.VALUES[packetIn.getRequestedState().ordinal()])
		{
			case 1:
				this.networkManager.setConnectionState(EnumConnectionState.LOGIN);
				ChatComponentText chatcomponenttext;

				if (packetIn.getProtocolVersion() > 47)
				{
					chatcomponenttext = new ChatComponentText("Outdated server! I\'m still on 1.8");
					this.networkManager.sendPacket(new S00PacketDisconnect(chatcomponenttext));
					this.networkManager.closeChannel(chatcomponenttext);
				}
				else if (packetIn.getProtocolVersion() < 47)
				{
					chatcomponenttext = new ChatComponentText("Outdated client! Please use 1.8");
					this.networkManager.sendPacket(new S00PacketDisconnect(chatcomponenttext));
					this.networkManager.closeChannel(chatcomponenttext);
				}
				else
				{
					this.networkManager.setNetHandler(new NetHandlerLoginServer(this.server, this.networkManager));
				}

				break;
			case 2:
				this.networkManager.setConnectionState(EnumConnectionState.STATUS);
				this.networkManager.setNetHandler(new NetHandlerStatusServer(this.server, this.networkManager));
				break;
			default:
				throw new UnsupportedOperationException("Invalid intention " + packetIn.getRequestedState());
		}
	}

	public void onDisconnect(IChatComponent reason) {}

	static final class SwitchEnumConnectionState
		{
			static final int[] VALUES = new int[EnumConnectionState.values().length];
			private static final String __OBFID = "CL_00001457";

			static
			{
				try
				{
					VALUES[EnumConnectionState.LOGIN.ordinal()] = 1;
				}
				catch (NoSuchFieldError var2)
				{
					;
				}

				try
				{
					VALUES[EnumConnectionState.STATUS.ordinal()] = 2;
				}
				catch (NoSuchFieldError var1)
				{
					;
				}
			}
		}
}