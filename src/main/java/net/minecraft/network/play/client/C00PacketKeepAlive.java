package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class C00PacketKeepAlive implements Packet
{
	private int key;
	private static final String __OBFID = "CL_00001359";

	public C00PacketKeepAlive() {}

	@SideOnly(Side.CLIENT)
	public C00PacketKeepAlive(int key)
	{
		this.key = key;
	}

	public void processPacket(INetHandlerPlayServer handler)
	{
		handler.processKeepAlive(this);
	}

	public void readPacketData(PacketBuffer buf) throws IOException
	{
		this.key = buf.readVarIntFromBuffer();
	}

	public void writePacketData(PacketBuffer buf) throws IOException
	{
		buf.writeVarIntToBuffer(this.key);
	}

	public int getKey()
	{
		return this.key;
	}

	public void processPacket(INetHandler handler)
	{
		this.processPacket((INetHandlerPlayServer)handler);
	}
}