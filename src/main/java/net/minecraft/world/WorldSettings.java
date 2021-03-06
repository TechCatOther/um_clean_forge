package net.minecraft.world;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class WorldSettings
{
	private final long seed;
	private final WorldSettings.GameType theGameType;
	private final boolean mapFeaturesEnabled;
	private final boolean hardcoreEnabled;
	private final WorldType terrainType;
	private boolean commandsAllowed;
	private boolean bonusChestEnabled;
	private String worldName;
	private static final String __OBFID = "CL_00000147";

	public WorldSettings(long seedIn, WorldSettings.GameType gameType, boolean enableMapFeatures, boolean hardcoreMode, WorldType worldTypeIn)
	{
		this.worldName = "";
		this.seed = seedIn;
		this.theGameType = gameType;
		this.mapFeaturesEnabled = enableMapFeatures;
		this.hardcoreEnabled = hardcoreMode;
		this.terrainType = worldTypeIn;
	}

	public WorldSettings(WorldInfo info)
	{
		this(info.getSeed(), info.getGameType(), info.isMapFeaturesEnabled(), info.isHardcoreModeEnabled(), info.getTerrainType());
	}

	public WorldSettings enableBonusChest()
	{
		this.bonusChestEnabled = true;
		return this;
	}

	public WorldSettings setWorldName(String name)
	{
		this.worldName = name;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public WorldSettings enableCommands()
	{
		this.commandsAllowed = true;
		return this;
	}

	public boolean isBonusChestEnabled()
	{
		return this.bonusChestEnabled;
	}

	public long getSeed()
	{
		return this.seed;
	}

	public WorldSettings.GameType getGameType()
	{
		return this.theGameType;
	}

	public boolean getHardcoreEnabled()
	{
		return this.hardcoreEnabled;
	}

	public boolean isMapFeaturesEnabled()
	{
		return this.mapFeaturesEnabled;
	}

	public WorldType getTerrainType()
	{
		return this.terrainType;
	}

	public boolean areCommandsAllowed()
	{
		return this.commandsAllowed;
	}

	public static WorldSettings.GameType getGameTypeById(int id)
	{
		return WorldSettings.GameType.getByID(id);
	}

	public String getWorldName()
	{
		return this.worldName;
	}

	public static enum GameType
	{
		NOT_SET(-1, ""),
		SURVIVAL(0, "survival"),
		CREATIVE(1, "creative"),
		ADVENTURE(2, "adventure"),
		SPECTATOR(3, "spectator");
		int id;
		String name;

		private static final String __OBFID = "CL_00000148";

		private GameType(int typeId, String nameIn)
		{
			this.id = typeId;
			this.name = nameIn;
		}

		public int getID()
		{
			return this.id;
		}

		public String getName()
		{
			return this.name;
		}

		public void configurePlayerCapabilities(PlayerCapabilities capabilities)
		{
			if (this == CREATIVE)
			{
				capabilities.allowFlying = true;
				capabilities.isCreativeMode = true;
				capabilities.disableDamage = true;
			}
			else if (this == SPECTATOR)
			{
				capabilities.allowFlying = true;
				capabilities.isCreativeMode = false;
				capabilities.disableDamage = true;
				capabilities.isFlying = true;
			}
			else
			{
				capabilities.allowFlying = false;
				capabilities.isCreativeMode = false;
				capabilities.disableDamage = false;
				capabilities.isFlying = false;
			}

			capabilities.allowEdit = !this.isAdventure();
		}

		public boolean isAdventure()
		{
			return this == ADVENTURE || this == SPECTATOR;
		}

		public boolean isCreative()
		{
			return this == CREATIVE;
		}

		public boolean isSurvivalOrAdventure()
		{
			return this == SURVIVAL || this == ADVENTURE;
		}

		public static WorldSettings.GameType getByID(int idIn)
		{
			WorldSettings.GameType[] agametype = values();
			int j = agametype.length;

			for (int k = 0; k < j; ++k)
			{
				WorldSettings.GameType gametype = agametype[k];

				if (gametype.id == idIn)
				{
					return gametype;
				}
			}

			return SURVIVAL;
		}

		@SideOnly(Side.CLIENT)
		public static WorldSettings.GameType getByName(String p_77142_0_)
		{
			WorldSettings.GameType[] agametype = values();
			int i = agametype.length;

			for (int j = 0; j < i; ++j)
			{
				WorldSettings.GameType gametype = agametype[j];

				if (gametype.name.equals(p_77142_0_))
				{
					return gametype;
				}
			}

			return SURVIVAL;
		}
	}
}