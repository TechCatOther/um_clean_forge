package net.minecraft.network.rcon;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public abstract class RConThreadBase implements Runnable
{
	private static final AtomicInteger THREAD_ID = new AtomicInteger(0);
	protected boolean running;
	protected IServer server;
	protected final String threadName;
	protected Thread rconThread;
	protected int field_72615_d = 5;
	protected List socketList = Lists.newArrayList();
	protected List serverSocketList = Lists.newArrayList();
	private static final String __OBFID = "CL_00001801";

	protected RConThreadBase(IServer p_i45300_1_, String threadName)
	{
		this.server = p_i45300_1_;
		this.threadName = threadName;

		if (this.server.isDebuggingEnabled())
		{
			this.logWarning("Debugging is enabled, performance maybe reduced!");
		}
	}

	public synchronized void startThread()
	{
		this.rconThread = new Thread(this, this.threadName + " #" + THREAD_ID.incrementAndGet());
		this.rconThread.start();
		this.running = true;
	}

	public boolean isRunning()
	{
		return this.running;
	}

	protected void logDebug(String msg)
	{
		this.server.logDebug(msg);
	}

	protected void logInfo(String msg)
	{
		this.server.logInfo(msg);
	}

	protected void logWarning(String msg)
	{
		this.server.logWarning(msg);
	}

	protected void logSevere(String msg)
	{
		this.server.logSevere(msg);
	}

	protected int getNumberOfPlayers()
	{
		return this.server.getCurrentPlayerCount();
	}

	protected void registerSocket(DatagramSocket socket)
	{
		this.logDebug("registerSocket: " + socket);
		this.socketList.add(socket);
	}

	protected boolean closeSocket(DatagramSocket socket, boolean removeFromList)
	{
		this.logDebug("closeSocket: " + socket);

		if (null == socket)
		{
			return false;
		}
		else
		{
			boolean flag1 = false;

			if (!socket.isClosed())
			{
				socket.close();
				flag1 = true;
			}

			if (removeFromList)
			{
				this.socketList.remove(socket);
			}

			return flag1;
		}
	}

	protected boolean closeServerSocket(ServerSocket socket)
	{
		return this.closeServerSocket_do(socket, true);
	}

	protected boolean closeServerSocket_do(ServerSocket socket, boolean removeFromList)
	{
		this.logDebug("closeSocket: " + socket);

		if (null == socket)
		{
			return false;
		}
		else
		{
			boolean flag1 = false;

			try
			{
				if (!socket.isClosed())
				{
					socket.close();
					flag1 = true;
				}
			}
			catch (IOException ioexception)
			{
				this.logWarning("IO: " + ioexception.getMessage());
			}

			if (removeFromList)
			{
				this.serverSocketList.remove(socket);
			}

			return flag1;
		}
	}

	protected void closeAllSockets()
	{
		this.closeAllSockets_do(false);
	}

	protected void closeAllSockets_do(boolean logWarning)
	{
		int i = 0;
		Iterator iterator = this.socketList.iterator();

		while (iterator.hasNext())
		{
			DatagramSocket datagramsocket = (DatagramSocket)iterator.next();

			if (this.closeSocket(datagramsocket, false))
			{
				++i;
			}
		}

		this.socketList.clear();
		iterator = this.serverSocketList.iterator();

		while (iterator.hasNext())
		{
			ServerSocket serversocket = (ServerSocket)iterator.next();

			if (this.closeServerSocket_do(serversocket, false))
			{
				++i;
			}
		}

		this.serverSocketList.clear();

		if (logWarning && 0 < i)
		{
			this.logWarning("Force closed " + i + " sockets");
		}
	}
}