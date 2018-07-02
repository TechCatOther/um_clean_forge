package net.minecraft.server.dedicated;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.SERVER)
public class PropertyManager
{
	private static final Logger LOGGER = LogManager.getLogger();
	private final Properties serverProperties = new Properties();
	private final File serverPropertiesFile;
	private static final String __OBFID = "CL_00001782";

	public PropertyManager(File propertiesFile)
	{
		this.serverPropertiesFile = propertiesFile;

		if (propertiesFile.exists())
		{
			FileInputStream fileinputstream = null;

			try
			{
				fileinputstream = new FileInputStream(propertiesFile);
				this.serverProperties.load(fileinputstream);
			}
			catch (Exception exception)
			{
				LOGGER.warn("Failed to load " + propertiesFile, exception);
				this.generateNewProperties();
			}
			finally
			{
				if (fileinputstream != null)
				{
					try
					{
						fileinputstream.close();
					}
					catch (IOException ioexception)
					{
						;
					}
				}
			}
		}
		else
		{
			LOGGER.warn(propertiesFile + " does not exist");
			this.generateNewProperties();
		}
	}

	public void generateNewProperties()
	{
		LOGGER.info("Generating new properties file");
		this.saveProperties();
	}

	public void saveProperties()
	{
		FileOutputStream fileoutputstream = null;

		try
		{
			fileoutputstream = new FileOutputStream(this.serverPropertiesFile);
			this.serverProperties.store(fileoutputstream, "Minecraft server properties");
		}
		catch (Exception exception)
		{
			LOGGER.warn("Failed to save " + this.serverPropertiesFile, exception);
			this.generateNewProperties();
		}
		finally
		{
			if (fileoutputstream != null)
			{
				try
				{
					fileoutputstream.close();
				}
				catch (IOException ioexception)
				{
					;
				}
			}
		}
	}

	public File getPropertiesFile()
	{
		return this.serverPropertiesFile;
	}

	public String getStringProperty(String key, String defaultValue)
	{
		if (!this.serverProperties.containsKey(key))
		{
			this.serverProperties.setProperty(key, defaultValue);
			this.saveProperties();
			this.saveProperties();
		}

		return this.serverProperties.getProperty(key, defaultValue);
	}

	public int getIntProperty(String key, int defaultValue)
	{
		try
		{
			return Integer.parseInt(this.getStringProperty(key, "" + defaultValue));
		}
		catch (Exception exception)
		{
			this.serverProperties.setProperty(key, "" + defaultValue);
			this.saveProperties();
			return defaultValue;
		}
	}

	public long getLongProperty(String key, long defaultValue)
	{
		try
		{
			return Long.parseLong(this.getStringProperty(key, "" + defaultValue));
		}
		catch (Exception exception)
		{
			this.serverProperties.setProperty(key, "" + defaultValue);
			this.saveProperties();
			return defaultValue;
		}
	}

	public boolean getBooleanProperty(String key, boolean defaultValue)
	{
		try
		{
			return Boolean.parseBoolean(this.getStringProperty(key, "" + defaultValue));
		}
		catch (Exception exception)
		{
			this.serverProperties.setProperty(key, "" + defaultValue);
			this.saveProperties();
			return defaultValue;
		}
	}

	public void setProperty(String key, Object value)
	{
		this.serverProperties.setProperty(key, "" + value);
	}
}