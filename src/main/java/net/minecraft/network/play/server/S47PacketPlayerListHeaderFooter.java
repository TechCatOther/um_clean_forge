package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S47PacketPlayerListHeaderFooter implements Packet
{
	private IChatComponent field_179703_a;
	private IChatComponent field_179702_b;
	private static final String __OBFID = "CL_00002285";

	public S47PacketPlayerListHeaderFooter() {}

	public S47PacketPlayerListHeaderFooter(IChatComponent p_i45950_1_)
	{
		this.field_179703_a = p_i45950_1_;
	}

	public void readPacketData(PacketBuffer buf) throws IOException
	{
		this.field_179703_a = buf.readChatComponent();
		this.field_179702_b = buf.readChatComponent();
	}

	public void writePacketData(PacketBuffer buf) throws IOException
	{
		buf.writeChatComponent(this.field_179703_a);
		buf.writeChatComponent(this.field_179702_b);
	}

	public void func_179699_a(INetHandlerPlayClient p_179699_1_)
	{
		p_179699_1_.handlePlayerListHeaderFooter(this);
	}

	@SideOnly(Side.CLIENT)
	public IChatComponent func_179700_a()
	{
		return this.field_179703_a;
	}

	public void processPacket(INetHandler handler)
	{
		this.func_179699_a((INetHandlerPlayClient)handler);
	}

	@SideOnly(Side.CLIENT)
	public IChatComponent func_179701_b()
	{
		return this.field_179702_b;
	}
}