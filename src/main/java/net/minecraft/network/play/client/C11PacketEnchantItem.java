package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class C11PacketEnchantItem implements Packet
{
	private int windowId;
	private int button;
	private static final String __OBFID = "CL_00001352";

	public C11PacketEnchantItem() {}

	@SideOnly(Side.CLIENT)
	public C11PacketEnchantItem(int windowId, int button)
	{
		this.windowId = windowId;
		this.button = button;
	}

	public void processPacket(INetHandlerPlayServer handler)
	{
		handler.processEnchantItem(this);
	}

	public void readPacketData(PacketBuffer buf) throws IOException
	{
		this.windowId = buf.readByte();
		this.button = buf.readByte();
	}

	public void writePacketData(PacketBuffer buf) throws IOException
	{
		buf.writeByte(this.windowId);
		buf.writeByte(this.button);
	}

	public int getWindowId()
	{
		return this.windowId;
	}

	public int getButton()
	{
		return this.button;
	}

	public void processPacket(INetHandler handler)
	{
		this.processPacket((INetHandlerPlayServer)handler);
	}
}