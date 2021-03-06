package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S3APacketTabComplete implements Packet
{
	private String[] field_149632_a;
	private static final String __OBFID = "CL_00001288";

	public S3APacketTabComplete() {}

	public S3APacketTabComplete(String[] p_i45178_1_)
	{
		this.field_149632_a = p_i45178_1_;
	}

	public void readPacketData(PacketBuffer buf) throws IOException
	{
		this.field_149632_a = new String[buf.readVarIntFromBuffer()];

		for (int i = 0; i < this.field_149632_a.length; ++i)
		{
			this.field_149632_a[i] = buf.readStringFromBuffer(32767);
		}
	}

	public void writePacketData(PacketBuffer buf) throws IOException
	{
		buf.writeVarIntToBuffer(this.field_149632_a.length);
		String[] astring = this.field_149632_a;
		int i = astring.length;

		for (int j = 0; j < i; ++j)
		{
			String s = astring[j];
			buf.writeString(s);
		}
	}

	public void processPacket(INetHandlerPlayClient handler)
	{
		handler.handleTabComplete(this);
	}

	@SideOnly(Side.CLIENT)
	public String[] func_149630_c()
	{
		return this.field_149632_a;
	}

	public void processPacket(INetHandler handler)
	{
		this.processPacket((INetHandlerPlayClient)handler);
	}
}