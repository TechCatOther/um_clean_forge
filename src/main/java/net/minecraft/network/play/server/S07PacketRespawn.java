package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S07PacketRespawn implements Packet
{
	private int field_149088_a;
	private EnumDifficulty field_149086_b;
	private WorldSettings.GameType field_149087_c;
	private WorldType field_149085_d;
	private static final String __OBFID = "CL_00001322";

	public S07PacketRespawn() {}

	public S07PacketRespawn(int p_i45213_1_, EnumDifficulty p_i45213_2_, WorldType p_i45213_3_, WorldSettings.GameType p_i45213_4_)
	{
		this.field_149088_a = p_i45213_1_;
		this.field_149086_b = p_i45213_2_;
		this.field_149087_c = p_i45213_4_;
		this.field_149085_d = p_i45213_3_;
	}

	public void processPacket(INetHandlerPlayClient handler)
	{
		handler.handleRespawn(this);
	}

	public void readPacketData(PacketBuffer buf) throws IOException
	{
		this.field_149088_a = buf.readInt();
		this.field_149086_b = EnumDifficulty.getDifficultyEnum(buf.readUnsignedByte());
		this.field_149087_c = WorldSettings.GameType.getByID(buf.readUnsignedByte());
		this.field_149085_d = WorldType.parseWorldType(buf.readStringFromBuffer(16));

		if (this.field_149085_d == null)
		{
			this.field_149085_d = WorldType.DEFAULT;
		}
	}

	public void writePacketData(PacketBuffer buf) throws IOException
	{
		buf.writeInt(this.field_149088_a);
		buf.writeByte(this.field_149086_b.getDifficultyId());
		buf.writeByte(this.field_149087_c.getID());
		buf.writeString(this.field_149085_d.getWorldTypeName());
	}

	@SideOnly(Side.CLIENT)
	public int func_149082_c()
	{
		return this.field_149088_a;
	}

	public void processPacket(INetHandler handler)
	{
		this.processPacket((INetHandlerPlayClient)handler);
	}

	@SideOnly(Side.CLIENT)
	public EnumDifficulty func_149081_d()
	{
		return this.field_149086_b;
	}

	@SideOnly(Side.CLIENT)
	public WorldSettings.GameType func_149083_e()
	{
		return this.field_149087_c;
	}

	@SideOnly(Side.CLIENT)
	public WorldType func_149080_f()
	{
		return this.field_149085_d;
	}
}