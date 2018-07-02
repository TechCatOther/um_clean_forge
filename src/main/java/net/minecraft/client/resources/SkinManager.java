package net.minecraft.client.resources;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.InsecureTextureException;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SkinManager
{
	private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue());
	private final TextureManager textureManager;
	private final File skinCacheDir;
	private final MinecraftSessionService sessionService;
	private final LoadingCache skinCacheLoader;
	private static final String __OBFID = "CL_00001830";

	public SkinManager(TextureManager textureManagerInstance, File skinCacheDirectory, MinecraftSessionService sessionService)
	{
		this.textureManager = textureManagerInstance;
		this.skinCacheDir = skinCacheDirectory;
		this.sessionService = sessionService;
		this.skinCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(15L, TimeUnit.SECONDS).build(new CacheLoader()
		{
			private static final String __OBFID = "CL_00001829";
			public Map func_152786_a(GameProfile p_152786_1_)
			{
				return Minecraft.getMinecraft().getSessionService().getTextures(p_152786_1_, false);
			}
			public Object load(Object p_load_1_)
			{
				return this.func_152786_a((GameProfile)p_load_1_);
			}
		});
	}

	public ResourceLocation loadSkin(MinecraftProfileTexture p_152792_1_, Type p_152792_2_)
	{
		return this.loadSkin(p_152792_1_, p_152792_2_, (SkinManager.SkinAvailableCallback)null);
	}

	public ResourceLocation loadSkin(final MinecraftProfileTexture p_152789_1_, final Type p_152789_2_, final SkinManager.SkinAvailableCallback p_152789_3_)
	{
		final ResourceLocation resourcelocation = new ResourceLocation("skins/" + p_152789_1_.getHash());
		ITextureObject itextureobject = this.textureManager.getTexture(resourcelocation);

		if (itextureobject != null)
		{
			if (p_152789_3_ != null)
			{
				p_152789_3_.skinAvailable(p_152789_2_, resourcelocation, p_152789_1_);
			}
		}
		else
		{
			File file1 = new File(this.skinCacheDir, p_152789_1_.getHash().substring(0, 2));
			File file2 = new File(file1, p_152789_1_.getHash());
			final ImageBufferDownload imagebufferdownload = p_152789_2_ == Type.SKIN ? new ImageBufferDownload() : null;
			ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(file2, p_152789_1_.getUrl(), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer()
			{
				private static final String __OBFID = "CL_00001828";
				public BufferedImage parseUserSkin(BufferedImage p_78432_1_)
				{
					if (imagebufferdownload != null)
					{
						p_78432_1_ = imagebufferdownload.parseUserSkin(p_78432_1_);
					}

					return p_78432_1_;
				}
				public void skinAvailable()
				{
					if (imagebufferdownload != null)
					{
						imagebufferdownload.skinAvailable();
					}

					if (p_152789_3_ != null)
					{
						p_152789_3_.skinAvailable(p_152789_2_, resourcelocation, p_152789_1_);
					}
				}
			});
			this.textureManager.loadTexture(resourcelocation, threaddownloadimagedata);
		}

		return resourcelocation;
	}

	public void loadProfileTextures(final GameProfile p_152790_1_, final SkinManager.SkinAvailableCallback p_152790_2_, final boolean p_152790_3_)
	{
		THREAD_POOL.submit(new Runnable()
		{
			private static final String __OBFID = "CL_00001827";
			public void run()
			{
				final HashMap hashmap = Maps.newHashMap();

				try
				{
					hashmap.putAll(SkinManager.this.sessionService.getTextures(p_152790_1_, p_152790_3_));
				}
				catch (InsecureTextureException insecuretextureexception)
				{
					;
				}

				if (hashmap.isEmpty() && p_152790_1_.getId().equals(Minecraft.getMinecraft().getSession().getProfile().getId()))
				{
					hashmap.putAll(SkinManager.this.sessionService.getTextures(SkinManager.this.sessionService.fillProfileProperties(p_152790_1_, false), false));
				}

				Minecraft.getMinecraft().addScheduledTask(new Runnable()
				{
					private static final String __OBFID = "CL_00001826";
					public void run()
					{
						if (hashmap.containsKey(Type.SKIN))
						{
							SkinManager.this.loadSkin((MinecraftProfileTexture)hashmap.get(Type.SKIN), Type.SKIN, p_152790_2_);
						}

						if (hashmap.containsKey(Type.CAPE))
						{
							SkinManager.this.loadSkin((MinecraftProfileTexture)hashmap.get(Type.CAPE), Type.CAPE, p_152790_2_);
						}
					}
				});
			}
		});
	}

	public Map loadSkinFromCache(GameProfile p_152788_1_)
	{
		return (Map)this.skinCacheLoader.getUnchecked(p_152788_1_);
	}

	@SideOnly(Side.CLIENT)
	public interface SkinAvailableCallback
	{
		void skinAvailable(Type p_180521_1_, ResourceLocation p_180521_2_, MinecraftProfileTexture p_180521_3_);
	}
}