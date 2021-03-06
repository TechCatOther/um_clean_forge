package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0APacketAnimation implements Packet
{
	private static final String __OBFID = "CL_00001345";

	public void readPacketData(PacketBuffer buf) throws IOException {}

	public void writePacketData(PacketBuffer buf) throws IOException {}

	public void handle(INetHandlerPlayServer handler)
	{
		handler.handleAnimation(this);
	}

	public void processPacket(INetHandler handler)
	{
		this.handle((INetHandlerPlayServer)handler);
	}
}