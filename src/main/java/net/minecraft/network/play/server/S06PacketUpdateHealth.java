package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S06PacketUpdateHealth implements Packet
{
	private float health;
	private int foodLevel;
	private float saturationLevel;
	private static final String __OBFID = "CL_00001332";

	public S06PacketUpdateHealth() {}

	public S06PacketUpdateHealth(float healthIn, int foodLevelIn, float saturationIn)
	{
		this.health = healthIn;
		this.foodLevel = foodLevelIn;
		this.saturationLevel = saturationIn;
	}

	public void readPacketData(PacketBuffer buf) throws IOException
	{
		this.health = buf.readFloat();
		this.foodLevel = buf.readVarIntFromBuffer();
		this.saturationLevel = buf.readFloat();
	}

	public void writePacketData(PacketBuffer buf) throws IOException
	{
		buf.writeFloat(this.health);
		buf.writeVarIntToBuffer(this.foodLevel);
		buf.writeFloat(this.saturationLevel);
	}

	public void func_180750_a(INetHandlerPlayClient p_180750_1_)
	{
		p_180750_1_.handleUpdateHealth(this);
	}

	@SideOnly(Side.CLIENT)
	public float getHealth()
	{
		return this.health;
	}

	public void processPacket(INetHandler handler)
	{
		this.func_180750_a((INetHandlerPlayClient)handler);
	}

	@SideOnly(Side.CLIENT)
	public int getFoodLevel()
	{
		return this.foodLevel;
	}

	@SideOnly(Side.CLIENT)
	public float getSaturationLevel()
	{
		return this.saturationLevel;
	}
}