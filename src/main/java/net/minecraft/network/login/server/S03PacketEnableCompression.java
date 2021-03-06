package net.minecraft.network.login.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S03PacketEnableCompression implements Packet
{
	private int compressionTreshold;
	private static final String __OBFID = "CL_00002279";

	public S03PacketEnableCompression() {}

	public S03PacketEnableCompression(int compressionTresholdIn)
	{
		this.compressionTreshold = compressionTresholdIn;
	}

	public void readPacketData(PacketBuffer buf) throws IOException
	{
		this.compressionTreshold = buf.readVarIntFromBuffer();
	}

	public void writePacketData(PacketBuffer buf) throws IOException
	{
		buf.writeVarIntToBuffer(this.compressionTreshold);
	}

	public void processPacket(INetHandlerLoginClient handler)
	{
		handler.handleEnableCompression(this);
	}

	@SideOnly(Side.CLIENT)
	public int getCompressionTreshold()
	{
		return this.compressionTreshold;
	}

	public void processPacket(INetHandler handler)
	{
		this.processPacket((INetHandlerLoginClient)handler);
	}
}