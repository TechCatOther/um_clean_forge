package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class SoundHandler implements IResourceManagerReloadListener, IUpdatePlayerListBox
{
	private static final Logger logger = LogManager.getLogger();
	private static final Gson field_147699_c = (new GsonBuilder()).registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();
	private static final ParameterizedType field_147696_d = new ParameterizedType()
	{
		private static final String __OBFID = "CL_00001148";
		public Type[] getActualTypeArguments()
		{
			return new Type[] {String.class, SoundList.class};
		}
		public Type getRawType()
		{
			return Map.class;
		}
		public Type getOwnerType()
		{
			return null;
		}
	};
	public static final SoundPoolEntry missing_sound = new SoundPoolEntry(new ResourceLocation("meta:missing_sound"), 0.0D, 0.0D, false);
	private final SoundRegistry sndRegistry = new SoundRegistry();
	private final SoundManager sndManager;
	private final IResourceManager mcResourceManager;
	private static final String __OBFID = "CL_00001147";

	public SoundHandler(IResourceManager manager, GameSettings p_i45122_2_)
	{
		this.mcResourceManager = manager;
		this.sndManager = new SoundManager(this, p_i45122_2_);
	}

	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		this.sndManager.reloadSoundSystem();
		this.sndRegistry.clearMap();
		Iterator iterator = resourceManager.getResourceDomains().iterator();

		while (iterator.hasNext())
		{
			String s = (String)iterator.next();

			try
			{
				List list = resourceManager.getAllResources(new ResourceLocation(s, "sounds.json"));
				Iterator iterator1 = list.iterator();

				while (iterator1.hasNext())
				{
					IResource iresource = (IResource)iterator1.next();

					try
					{
						Map map = this.getSoundMap(iresource.getInputStream());
						Iterator iterator2 = map.entrySet().iterator();

						while (iterator2.hasNext())
						{
							Entry entry = (Entry)iterator2.next();
							this.loadSoundResource(new ResourceLocation(s, (String)entry.getKey()), (SoundList)entry.getValue());
						}
					}
					catch (RuntimeException runtimeexception)
					{
						logger.warn("Invalid sounds.json", runtimeexception);
					}
				}
			}
			catch (IOException ioexception)
			{
				;
			}
		}
	}

	protected Map getSoundMap(InputStream p_175085_1_)
	{
		Map map;

		try
		{
			map = (Map)field_147699_c.fromJson(new InputStreamReader(p_175085_1_), field_147696_d);
		}
		finally
		{
			IOUtils.closeQuietly(p_175085_1_);
		}

		return map;
	}

	private void loadSoundResource(ResourceLocation p_147693_1_, SoundList p_147693_2_)
	{
		boolean flag = !this.sndRegistry.containsKey(p_147693_1_);
		SoundEventAccessorComposite soundeventaccessorcomposite;

		if (!flag && !p_147693_2_.canReplaceExisting())
		{
			soundeventaccessorcomposite = (SoundEventAccessorComposite)this.sndRegistry.getObject(p_147693_1_);
		}
		else
		{
			if (!flag)
			{
				logger.debug("Replaced sound event location {}", new Object[] {p_147693_1_});
			}

			soundeventaccessorcomposite = new SoundEventAccessorComposite(p_147693_1_, 1.0D, 1.0D, p_147693_2_.getSoundCategory());
			this.sndRegistry.registerSound(soundeventaccessorcomposite);
		}

		Iterator iterator = p_147693_2_.getSoundList().iterator();

		while (iterator.hasNext())
		{
			final SoundList.SoundEntry soundentry = (SoundList.SoundEntry)iterator.next();
			String s = soundentry.getSoundEntryName();
			ResourceLocation resourcelocation1 = new ResourceLocation(s);
			final String s1 = s.contains(":") ? resourcelocation1.getResourceDomain() : p_147693_1_.getResourceDomain();
			Object object;

			switch (SoundHandler.SwitchType.field_148765_a[soundentry.getSoundEntryType().ordinal()])
			{
				case 1:
					ResourceLocation resourcelocation2 = new ResourceLocation(s1, "sounds/" + resourcelocation1.getResourcePath() + ".ogg");
					InputStream inputstream = null;

					try
					{
						inputstream = this.mcResourceManager.getResource(resourcelocation2).getInputStream();
					}
					catch (FileNotFoundException filenotfoundexception)
					{
						logger.warn("File {} does not exist, cannot add it to event {}", new Object[] {resourcelocation2, p_147693_1_});
						continue;
					}
					catch (IOException ioexception)
					{
						logger.warn("Could not load sound file " + resourcelocation2 + ", cannot add it to event " + p_147693_1_, ioexception);
						continue;
					}
					finally
					{
						IOUtils.closeQuietly(inputstream);
					}

					object = new SoundEventAccessor(new SoundPoolEntry(resourcelocation2, (double)soundentry.getSoundEntryPitch(), (double)soundentry.getSoundEntryVolume(), soundentry.isStreaming()), soundentry.getSoundEntryWeight());
					break;
				case 2:
					object = new ISoundEventAccessor()
					{
						final ResourceLocation field_148726_a = new ResourceLocation(s1, soundentry.getSoundEntryName());
						private static final String __OBFID = "CL_00001149";
						public int getWeight()
						{
							SoundEventAccessorComposite soundeventaccessorcomposite1 = (SoundEventAccessorComposite)SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
							return soundeventaccessorcomposite1 == null ? 0 : soundeventaccessorcomposite1.getWeight();
						}
						public SoundPoolEntry getEntry()
						{
							SoundEventAccessorComposite soundeventaccessorcomposite1 = (SoundEventAccessorComposite)SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
							return soundeventaccessorcomposite1 == null ? SoundHandler.missing_sound : soundeventaccessorcomposite1.cloneEntry();
						}
						public Object cloneEntry()
						{
							return this.getEntry();
						}
					};
					break;
				default:
					throw new IllegalStateException("IN YOU FACE");
			}

			soundeventaccessorcomposite.addSoundToEventPool((ISoundEventAccessor)object);
		}
	}

	public SoundEventAccessorComposite getSound(ResourceLocation p_147680_1_)
	{
		return (SoundEventAccessorComposite)this.sndRegistry.getObject(p_147680_1_);
	}

	public void playSound(ISound p_147682_1_)
	{
		this.sndManager.playSound(p_147682_1_);
	}

	public void playDelayedSound(ISound p_147681_1_, int p_147681_2_)
	{
		this.sndManager.playDelayedSound(p_147681_1_, p_147681_2_);
	}

	public void setListener(EntityPlayer p_147691_1_, float p_147691_2_)
	{
		this.sndManager.setListener(p_147691_1_, p_147691_2_);
	}

	public void pauseSounds()
	{
		this.sndManager.pauseAllSounds();
	}

	public void stopSounds()
	{
		this.sndManager.stopAllSounds();
	}

	public void unloadSounds()
	{
		this.sndManager.unloadSoundSystem();
	}

	public void update()
	{
		this.sndManager.updateAllSounds();
	}

	public void resumeSounds()
	{
		this.sndManager.resumeAllSounds();
	}

	public void setSoundLevel(SoundCategory p_147684_1_, float volume)
	{
		if (p_147684_1_ == SoundCategory.MASTER && volume <= 0.0F)
		{
			this.stopSounds();
		}

		this.sndManager.setSoundCategoryVolume(p_147684_1_, volume);
	}

	public void stopSound(ISound p_147683_1_)
	{
		this.sndManager.stopSound(p_147683_1_);
	}

	public SoundEventAccessorComposite getRandomSoundFromCategories(SoundCategory ... p_147686_1_)
	{
		ArrayList arraylist = Lists.newArrayList();
		Iterator iterator = this.sndRegistry.getKeys().iterator();

		while (iterator.hasNext())
		{
			ResourceLocation resourcelocation = (ResourceLocation)iterator.next();
			SoundEventAccessorComposite soundeventaccessorcomposite = (SoundEventAccessorComposite)this.sndRegistry.getObject(resourcelocation);

			if (ArrayUtils.contains(p_147686_1_, soundeventaccessorcomposite.getSoundCategory()))
			{
				arraylist.add(soundeventaccessorcomposite);
			}
		}

		if (arraylist.isEmpty())
		{
			return null;
		}
		else
		{
			return (SoundEventAccessorComposite)arraylist.get((new Random()).nextInt(arraylist.size()));
		}
	}

	public boolean isSoundPlaying(ISound p_147692_1_)
	{
		return this.sndManager.isSoundPlaying(p_147692_1_);
	}

	@SideOnly(Side.CLIENT)

	static final class SwitchType
		{
			static final int[] field_148765_a = new int[SoundList.SoundEntry.Type.values().length];
			private static final String __OBFID = "CL_00001150";

			static
			{
				try
				{
					field_148765_a[SoundList.SoundEntry.Type.FILE.ordinal()] = 1;
				}
				catch (NoSuchFieldError var2)
				{
					;
				}

				try
				{
					field_148765_a[SoundList.SoundEntry.Type.SOUND_EVENT.ordinal()] = 2;
				}
				catch (NoSuchFieldError var1)
				{
					;
				}
			}
		}
}