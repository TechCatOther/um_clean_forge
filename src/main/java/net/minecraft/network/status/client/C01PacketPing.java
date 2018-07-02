package net.minecraft.network.status.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class C01PacketPing implements Packet
{
	private long clientTime;
	private static final String __OBFID = "CL_00001392";

	public C01PacketPing() {}

	@SideOnly(Side.CLIENT)
	public C01PacketPing(long ping)
	{
		this.clientTime = ping;
	}

	public void readPacketData(PacketBuffer buf) throws IOException
	{
		this.clientTime = buf.readLong();
	}

	public void writePacketData(PacketBuffer buf) throws IOException
	{
		buf.writeLong(this.clientTime);
	}

	public void processPacket(INetHandlerStatusServer handler)
	{
		handler.processPing(this);
	}

	public long getClientTime()
	{
		return this.clientTime;
	}

	public void processPacket(INetHandler handler)
	{
		this.processPacket((INetHandlerStatusServer)handler);
	}
}