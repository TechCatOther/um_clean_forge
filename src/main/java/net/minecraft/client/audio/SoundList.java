package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundList
{
	private final List soundList = Lists.newArrayList();
	private boolean replaceExisting;
	private SoundCategory category;
	private static final String __OBFID = "CL_00001121";

	public List getSoundList()
	{
		return this.soundList;
	}

	public boolean canReplaceExisting()
	{
		return this.replaceExisting;
	}

	public void setReplaceExisting(boolean p_148572_1_)
	{
		this.replaceExisting = p_148572_1_;
	}

	public SoundCategory getSoundCategory()
	{
		return this.category;
	}

	public void setSoundCategory(SoundCategory p_148571_1_)
	{
		this.category = p_148571_1_;
	}

	@SideOnly(Side.CLIENT)
	public static class SoundEntry
		{
			private String name;
			private float volume = 1.0F;
			private float pitch = 1.0F;
			private int field_148565_d = 1;
			private SoundList.SoundEntry.Type field_148566_e;
			private boolean streaming;
			private static final String __OBFID = "CL_00001122";

			public SoundEntry()
			{
				this.field_148566_e = SoundList.SoundEntry.Type.FILE;
				this.streaming = false;
			}

			public String getSoundEntryName()
			{
				return this.name;
			}

			public void setSoundEntryName(String nameIn)
			{
				this.name = nameIn;
			}

			public float getSoundEntryVolume()
			{
				return this.volume;
			}

			public void setSoundEntryVolume(float volumeIn)
			{
				this.volume = volumeIn;
			}

			public float getSoundEntryPitch()
			{
				return this.pitch;
			}

			public void setSoundEntryPitch(float pitchIn)
			{
				this.pitch = pitchIn;
			}

			public int getSoundEntryWeight()
			{
				return this.field_148565_d;
			}

			public void setSoundEntryWeight(int p_148554_1_)
			{
				this.field_148565_d = p_148554_1_;
			}

			public SoundList.SoundEntry.Type getSoundEntryType()
			{
				return this.field_148566_e;
			}

			public void setSoundEntryType(SoundList.SoundEntry.Type p_148562_1_)
			{
				this.field_148566_e = p_148562_1_;
			}

			public boolean isStreaming()
			{
				return this.streaming;
			}

			public void setStreaming(boolean p_148557_1_)
			{
				this.streaming = p_148557_1_;
			}

			@SideOnly(Side.CLIENT)
			public static enum Type
			{
				FILE("file"),
				SOUND_EVENT("event");
				private final String field_148583_c;

				private static final String __OBFID = "CL_00001123";

				private Type(String p_i45109_3_)
				{
					this.field_148583_c = p_i45109_3_;
				}

				public static SoundList.SoundEntry.Type getType(String p_148580_0_)
				{
					SoundList.SoundEntry.Type[] atype = values();
					int i = atype.length;

					for (int j = 0; j < i; ++j)
					{
						SoundList.SoundEntry.Type type = atype[j];

						if (type.field_148583_c.equals(p_148580_0_))
						{
							return type;
						}
					}

					return null;
				}
			}
		}
}