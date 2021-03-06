package net.minecraft.network.play.client;

import java.io.IOException;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.world.WorldServer;

public class C18PacketSpectate implements Packet
{
	private UUID id;
	private static final String __OBFID = "CL_00002280";

	public C18PacketSpectate() {}

	public C18PacketSpectate(UUID id)
	{
		this.id = id;
	}

	public void readPacketData(PacketBuffer buf) throws IOException
	{
		this.id = buf.readUuid();
	}

	public void writePacketData(PacketBuffer buf) throws IOException
	{
		buf.writeUuid(this.id);
	}

	public void handle(INetHandlerPlayServer handler)
	{
		handler.handleSpectate(this);
	}

	public Entity getEntity(WorldServer worldIn)
	{
		return worldIn.getEntityFromUuid(this.id);
	}

	public void processPacket(INetHandler handler)
	{
		this.handle((INetHandlerPlayServer)handler);
	}
}