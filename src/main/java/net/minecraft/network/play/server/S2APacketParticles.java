package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S2APacketParticles implements Packet
{
	private EnumParticleTypes particleType;
	private float xCoord;
	private float yCoord;
	private float zCoord;
	private float xOffset;
	private float field_149230_f;
	private float field_149231_g;
	private float particleSpeed;
	private int particleCount;
	private boolean field_179752_j;
	private int[] particleArguments;
	private static final String __OBFID = "CL_00001308";

	public S2APacketParticles() {}

	public S2APacketParticles(EnumParticleTypes p_i45977_1_, boolean p_i45977_2_, float p_i45977_3_, float p_i45977_4_, float p_i45977_5_, float p_i45977_6_, float yOffset, float zOffset, float particleSpeedIn, int p_i45977_10_, int ... p_i45977_11_)
	{
		this.particleType = p_i45977_1_;
		this.field_179752_j = p_i45977_2_;
		this.xCoord = p_i45977_3_;
		this.yCoord = p_i45977_4_;
		this.zCoord = p_i45977_5_;
		this.xOffset = p_i45977_6_;
		this.field_149230_f = yOffset;
		this.field_149231_g = zOffset;
		this.particleSpeed = particleSpeedIn;
		this.particleCount = p_i45977_10_;
		this.particleArguments = p_i45977_11_;
	}

	public void readPacketData(PacketBuffer buf) throws IOException
	{
		this.particleType = EnumParticleTypes.getParticleFromId(buf.readInt());

		if (this.particleType == null)
		{
			this.particleType = EnumParticleTypes.BARRIER;
		}

		this.field_179752_j = buf.readBoolean();
		this.xCoord = buf.readFloat();
		this.yCoord = buf.readFloat();
		this.zCoord = buf.readFloat();
		this.xOffset = buf.readFloat();
		this.field_149230_f = buf.readFloat();
		this.field_149231_g = buf.readFloat();
		this.particleSpeed = buf.readFloat();
		this.particleCount = buf.readInt();
		int i = this.particleType.getArgumentCount();
		this.particleArguments = new int[i];

		for (int j = 0; j < i; ++j)
		{
			this.particleArguments[j] = buf.readVarIntFromBuffer();
		}
	}

	public void writePacketData(PacketBuffer buf) throws IOException
	{
		buf.writeInt(this.particleType.getParticleID());
		buf.writeBoolean(this.field_179752_j);
		buf.writeFloat(this.xCoord);
		buf.writeFloat(this.yCoord);
		buf.writeFloat(this.zCoord);
		buf.writeFloat(this.xOffset);
		buf.writeFloat(this.field_149230_f);
		buf.writeFloat(this.field_149231_g);
		buf.writeFloat(this.particleSpeed);
		buf.writeInt(this.particleCount);
		int i = this.particleType.getArgumentCount();

		for (int j = 0; j < i; ++j)
		{
			buf.writeVarIntToBuffer(this.particleArguments[j]);
		}
	}

	@SideOnly(Side.CLIENT)
	public EnumParticleTypes func_179749_a()
	{
		return this.particleType;
	}

	public void handleParticles(INetHandlerPlayClient netHandler)
	{
		netHandler.handleParticles(this);
	}

	public void processPacket(INetHandler handler)
	{
		this.handleParticles((INetHandlerPlayClient)handler);
	}

	@SideOnly(Side.CLIENT)
	public boolean func_179750_b()
	{
		return this.field_179752_j;
	}

	@SideOnly(Side.CLIENT)
	public double getXCoordinate()
	{
		return (double)this.xCoord;
	}

	@SideOnly(Side.CLIENT)
	public double getYCoordinate()
	{
		return (double)this.yCoord;
	}

	@SideOnly(Side.CLIENT)
	public double getZCoordinate()
	{
		return (double)this.zCoord;
	}

	@SideOnly(Side.CLIENT)
	public float getXOffset()
	{
		return this.xOffset;
	}

	@SideOnly(Side.CLIENT)
	public float getYOffset()
	{
		return this.field_149230_f;
	}

	@SideOnly(Side.CLIENT)
	public float getZOffset()
	{
		return this.field_149231_g;
	}

	@SideOnly(Side.CLIENT)
	public float getParticleSpeed()
	{
		return this.particleSpeed;
	}

	@SideOnly(Side.CLIENT)
	public int getParticleCount()
	{
		return this.particleCount;
	}

	@SideOnly(Side.CLIENT)
	public int[] getParticleArgs()
	{
		return this.particleArguments;
	}
}