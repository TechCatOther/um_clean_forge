package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S39PacketPlayerAbilities implements Packet
{
	private boolean invulnerable;
	private boolean flying;
	private boolean allowFlying;
	private boolean creativeMode;
	private float flySpeed;
	private float walkSpeed;
	private static final String __OBFID = "CL_00001317";

	public S39PacketPlayerAbilities() {}

	public S39PacketPlayerAbilities(PlayerCapabilities capabilities)
	{
		this.setInvulnerable(capabilities.disableDamage);
		this.setFlying(capabilities.isFlying);
		this.setAllowFlying(capabilities.allowFlying);
		this.setCreativeMode(capabilities.isCreativeMode);
		this.setFlySpeed(capabilities.getFlySpeed());
		this.setWalkSpeed(capabilities.getWalkSpeed());
	}

	public void readPacketData(PacketBuffer buf) throws IOException
	{
		byte b0 = buf.readByte();
		this.setInvulnerable((b0 & 1) > 0);
		this.setFlying((b0 & 2) > 0);
		this.setAllowFlying((b0 & 4) > 0);
		this.setCreativeMode((b0 & 8) > 0);
		this.setFlySpeed(buf.readFloat());
		this.setWalkSpeed(buf.readFloat());
	}

	public void writePacketData(PacketBuffer buf) throws IOException
	{
		byte b0 = 0;

		if (this.isInvulnerable())
		{
			b0 = (byte)(b0 | 1);
		}

		if (this.isFlying())
		{
			b0 = (byte)(b0 | 2);
		}

		if (this.isAllowFlying())
		{
			b0 = (byte)(b0 | 4);
		}

		if (this.isCreativeMode())
		{
			b0 = (byte)(b0 | 8);
		}

		buf.writeByte(b0);
		buf.writeFloat(this.flySpeed);
		buf.writeFloat(this.walkSpeed);
	}

	public void func_180742_a(INetHandlerPlayClient p_180742_1_)
	{
		p_180742_1_.handlePlayerAbilities(this);
	}

	public boolean isInvulnerable()
	{
		return this.invulnerable;
	}

	public void setInvulnerable(boolean isInvulnerable)
	{
		this.invulnerable = isInvulnerable;
	}

	public boolean isFlying()
	{
		return this.flying;
	}

	public void setFlying(boolean isFlying)
	{
		this.flying = isFlying;
	}

	public boolean isAllowFlying()
	{
		return this.allowFlying;
	}

	public void setAllowFlying(boolean isAllowFlying)
	{
		this.allowFlying = isAllowFlying;
	}

	public boolean isCreativeMode()
	{
		return this.creativeMode;
	}

	public void setCreativeMode(boolean isCreativeMode)
	{
		this.creativeMode = isCreativeMode;
	}

	@SideOnly(Side.CLIENT)
	public float getFlySpeed()
	{
		return this.flySpeed;
	}

	public void setFlySpeed(float flySpeedIn)
	{
		this.flySpeed = flySpeedIn;
	}

	@SideOnly(Side.CLIENT)
	public float getWalkSpeed()
	{
		return this.walkSpeed;
	}

	public void setWalkSpeed(float walkSpeedIn)
	{
		this.walkSpeed = walkSpeedIn;
	}

	public void processPacket(INetHandler handler)
	{
		this.func_180742_a((INetHandlerPlayClient)handler);
	}
}