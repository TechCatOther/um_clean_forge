package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S2BPacketChangeGameState implements Packet
{
	public static final String[] MESSAGE_NAMES = new String[] {"tile.bed.notValid"};
	private int state;
	private float field_149141_c;
	private static final String __OBFID = "CL_00001301";

	public S2BPacketChangeGameState() {}

	public S2BPacketChangeGameState(int stateIn, float p_i45194_2_)
	{
		this.state = stateIn;
		this.field_149141_c = p_i45194_2_;
	}

	public void readPacketData(PacketBuffer buf) throws IOException
	{
		this.state = buf.readUnsignedByte();
		this.field_149141_c = buf.readFloat();
	}

	public void writePacketData(PacketBuffer buf) throws IOException
	{
		buf.writeByte(this.state);
		buf.writeFloat(this.field_149141_c);
	}

	public void processPacket(INetHandlerPlayClient handler)
	{
		handler.handleChangeGameState(this);
	}

	@SideOnly(Side.CLIENT)
	public int func_149138_c()
	{
		return this.state;
	}

	public void processPacket(INetHandler handler)
	{
		this.processPacket((INetHandlerPlayClient)handler);
	}

	@SideOnly(Side.CLIENT)
	public float func_149137_d()
	{
		return this.field_149141_c;
	}
}