package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S46PacketSetCompressionLevel implements Packet
{
	private int field_179761_a;
	private static final String __OBFID = "CL_00002300";

	public void readPacketData(PacketBuffer buf) throws IOException
	{
		this.field_179761_a = buf.readVarIntFromBuffer();
	}

	public void writePacketData(PacketBuffer buf) throws IOException
	{
		buf.writeVarIntToBuffer(this.field_179761_a);
	}

	public void func_179759_a(INetHandlerPlayClient p_179759_1_)
	{
		p_179759_1_.handleSetCompressionLevel(this);
	}

	@SideOnly(Side.CLIENT)
	public int func_179760_a()
	{
		return this.field_179761_a;
	}

	public void processPacket(INetHandler handler)
	{
		this.func_179759_a((INetHandlerPlayClient)handler);
	}
}