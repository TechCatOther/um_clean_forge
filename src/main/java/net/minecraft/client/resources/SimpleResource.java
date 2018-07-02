package net.minecraft.client.resources;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

@SideOnly(Side.CLIENT)
public class SimpleResource implements IResource
{
	private final Map mapMetadataSections = Maps.newHashMap();
	private final String resourcePackName;
	private final ResourceLocation srResourceLocation;
	private final InputStream resourceInputStream;
	private final InputStream mcmetaInputStream;
	private final IMetadataSerializer srMetadataSerializer;
	private boolean mcmetaJsonChecked;
	private JsonObject mcmetaJson;
	private static final String __OBFID = "CL_00001093";

	public SimpleResource(String p_i46090_1_, ResourceLocation p_i46090_2_, InputStream p_i46090_3_, InputStream p_i46090_4_, IMetadataSerializer p_i46090_5_)
	{
		this.resourcePackName = p_i46090_1_;
		this.srResourceLocation = p_i46090_2_;
		this.resourceInputStream = p_i46090_3_;
		this.mcmetaInputStream = p_i46090_4_;
		this.srMetadataSerializer = p_i46090_5_;
	}

	public ResourceLocation getResourceLocation()
	{
		return this.srResourceLocation;
	}

	public InputStream getInputStream()
	{
		return this.resourceInputStream;
	}

	public boolean hasMetadata()
	{
		return this.mcmetaInputStream != null;
	}

	public IMetadataSection getMetadata(String p_110526_1_)
	{
		if (!this.hasMetadata())
		{
			return null;
		}
		else
		{
			if (this.mcmetaJson == null && !this.mcmetaJsonChecked)
			{
				this.mcmetaJsonChecked = true;
				BufferedReader bufferedreader = null;

				try
				{
					bufferedreader = new BufferedReader(new InputStreamReader(this.mcmetaInputStream));
					this.mcmetaJson = (new JsonParser()).parse(bufferedreader).getAsJsonObject();
				}
				finally
				{
					IOUtils.closeQuietly(bufferedreader);
				}
			}

			IMetadataSection imetadatasection = (IMetadataSection)this.mapMetadataSections.get(p_110526_1_);

			if (imetadatasection == null)
			{
				imetadatasection = this.srMetadataSerializer.parseMetadataSection(p_110526_1_, this.mcmetaJson);
			}

			return imetadatasection;
		}
	}

	public String getResourcePackName()
	{
		return this.resourcePackName;
	}

	public boolean equals(Object p_equals_1_)
	{
		if (this == p_equals_1_)
		{
			return true;
		}
		else if (!(p_equals_1_ instanceof SimpleResource))
		{
			return false;
		}
		else
		{
			SimpleResource simpleresource = (SimpleResource)p_equals_1_;

			if (this.srResourceLocation != null)
			{
				if (!this.srResourceLocation.equals(simpleresource.srResourceLocation))
				{
					return false;
				}
			}
			else if (simpleresource.srResourceLocation != null)
			{
				return false;
			}

			if (this.resourcePackName != null)
			{
				if (!this.resourcePackName.equals(simpleresource.resourcePackName))
				{
					return false;
				}
			}
			else if (simpleresource.resourcePackName != null)
			{
				return false;
			}

			return true;
		}
	}

	public int hashCode()
	{
		int i = this.resourcePackName != null ? this.resourcePackName.hashCode() : 0;
		i = 31 * i + (this.srResourceLocation != null ? this.srResourceLocation.hashCode() : 0);
		return i;
	}
}