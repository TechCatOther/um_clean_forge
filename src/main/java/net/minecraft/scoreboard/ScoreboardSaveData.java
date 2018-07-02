package net.minecraft.scoreboard;

import java.util.Collection;
import java.util.Iterator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldSavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScoreboardSaveData extends WorldSavedData
{
	private static final Logger logger = LogManager.getLogger();
	private Scoreboard theScoreboard;
	private NBTTagCompound delayedInitNbt;
	private static final String __OBFID = "CL_00000620";

	public ScoreboardSaveData()
	{
		this("scoreboard");
	}

	public ScoreboardSaveData(String name)
	{
		super(name);
	}

	public void setScoreboard(Scoreboard scoreboardIn)
	{
		this.theScoreboard = scoreboardIn;

		if (this.delayedInitNbt != null)
		{
			this.readFromNBT(this.delayedInitNbt);
		}
	}

	public void readFromNBT(NBTTagCompound nbt)
	{
		if (this.theScoreboard == null)
		{
			this.delayedInitNbt = nbt;
		}
		else
		{
			this.readObjectives(nbt.getTagList("Objectives", 10));
			this.readScores(nbt.getTagList("PlayerScores", 10));

			if (nbt.hasKey("DisplaySlots", 10))
			{
				this.readDisplayConfig(nbt.getCompoundTag("DisplaySlots"));
			}

			if (nbt.hasKey("Teams", 9))
			{
				this.readTeams(nbt.getTagList("Teams", 10));
			}
		}
	}

	protected void readTeams(NBTTagList p_96498_1_)
	{
		for (int i = 0; i < p_96498_1_.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound = p_96498_1_.getCompoundTagAt(i);
			ScorePlayerTeam scoreplayerteam = this.theScoreboard.createTeam(nbttagcompound.getString("Name"));
			scoreplayerteam.setTeamName(nbttagcompound.getString("DisplayName"));

			if (nbttagcompound.hasKey("TeamColor", 8))
			{
				scoreplayerteam.func_178774_a(EnumChatFormatting.getValueByName(nbttagcompound.getString("TeamColor")));
			}

			scoreplayerteam.setNamePrefix(nbttagcompound.getString("Prefix"));
			scoreplayerteam.setNameSuffix(nbttagcompound.getString("Suffix"));

			if (nbttagcompound.hasKey("AllowFriendlyFire", 99))
			{
				scoreplayerteam.setAllowFriendlyFire(nbttagcompound.getBoolean("AllowFriendlyFire"));
			}

			if (nbttagcompound.hasKey("SeeFriendlyInvisibles", 99))
			{
				scoreplayerteam.setSeeFriendlyInvisiblesEnabled(nbttagcompound.getBoolean("SeeFriendlyInvisibles"));
			}

			Team.EnumVisible enumvisible;

			if (nbttagcompound.hasKey("NameTagVisibility", 8))
			{
				enumvisible = Team.EnumVisible.func_178824_a(nbttagcompound.getString("NameTagVisibility"));

				if (enumvisible != null)
				{
					scoreplayerteam.func_178772_a(enumvisible);
				}
			}

			if (nbttagcompound.hasKey("DeathMessageVisibility", 8))
			{
				enumvisible = Team.EnumVisible.func_178824_a(nbttagcompound.getString("DeathMessageVisibility"));

				if (enumvisible != null)
				{
					scoreplayerteam.func_178773_b(enumvisible);
				}
			}

			this.func_96502_a(scoreplayerteam, nbttagcompound.getTagList("Players", 8));
		}
	}

	protected void func_96502_a(ScorePlayerTeam p_96502_1_, NBTTagList p_96502_2_)
	{
		for (int i = 0; i < p_96502_2_.tagCount(); ++i)
		{
			this.theScoreboard.addPlayerToTeam(p_96502_2_.getStringTagAt(i), p_96502_1_.getRegisteredName());
		}
	}

	protected void readDisplayConfig(NBTTagCompound p_96504_1_)
	{
		for (int i = 0; i < 19; ++i)
		{
			if (p_96504_1_.hasKey("slot_" + i, 8))
			{
				String s = p_96504_1_.getString("slot_" + i);
				ScoreObjective scoreobjective = this.theScoreboard.getObjective(s);
				this.theScoreboard.setObjectiveInDisplaySlot(i, scoreobjective);
			}
		}
	}

	protected void readObjectives(NBTTagList nbt)
	{
		for (int i = 0; i < nbt.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
			IScoreObjectiveCriteria iscoreobjectivecriteria = (IScoreObjectiveCriteria)IScoreObjectiveCriteria.INSTANCES.get(nbttagcompound.getString("CriteriaName"));

			if (iscoreobjectivecriteria != null)
			{
				ScoreObjective scoreobjective = this.theScoreboard.addScoreObjective(nbttagcompound.getString("Name"), iscoreobjectivecriteria);
				scoreobjective.setDisplayName(nbttagcompound.getString("DisplayName"));
				scoreobjective.setRenderType(IScoreObjectiveCriteria.EnumRenderType.func_178795_a(nbttagcompound.getString("RenderType")));
			}
		}
	}

	protected void readScores(NBTTagList nbt)
	{
		for (int i = 0; i < nbt.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
			ScoreObjective scoreobjective = this.theScoreboard.getObjective(nbttagcompound.getString("Objective"));
			Score score = this.theScoreboard.getValueFromObjective(nbttagcompound.getString("Name"), scoreobjective);
			score.setScorePoints(nbttagcompound.getInteger("Score"));

			if (nbttagcompound.hasKey("Locked"))
			{
				score.setLocked(nbttagcompound.getBoolean("Locked"));
			}
		}
	}

	public void writeToNBT(NBTTagCompound nbt)
	{
		if (this.theScoreboard == null)
		{
			logger.warn("Tried to save scoreboard without having a scoreboard...");
		}
		else
		{
			nbt.setTag("Objectives", this.objectivesToNbt());
			nbt.setTag("PlayerScores", this.scoresToNbt());
			nbt.setTag("Teams", this.func_96496_a());
			this.func_96497_d(nbt);
		}
	}

	protected NBTTagList func_96496_a()
	{
		NBTTagList nbttaglist = new NBTTagList();
		Collection collection = this.theScoreboard.getTeams();
		Iterator iterator = collection.iterator();

		while (iterator.hasNext())
		{
			ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam)iterator.next();
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Name", scoreplayerteam.getRegisteredName());
			nbttagcompound.setString("DisplayName", scoreplayerteam.func_96669_c());

			if (scoreplayerteam.func_178775_l().getColorIndex() >= 0)
			{
				nbttagcompound.setString("TeamColor", scoreplayerteam.func_178775_l().getFriendlyName());
			}

			nbttagcompound.setString("Prefix", scoreplayerteam.getColorPrefix());
			nbttagcompound.setString("Suffix", scoreplayerteam.getColorSuffix());
			nbttagcompound.setBoolean("AllowFriendlyFire", scoreplayerteam.getAllowFriendlyFire());
			nbttagcompound.setBoolean("SeeFriendlyInvisibles", scoreplayerteam.func_98297_h());
			nbttagcompound.setString("NameTagVisibility", scoreplayerteam.func_178770_i().field_178830_e);
			nbttagcompound.setString("DeathMessageVisibility", scoreplayerteam.func_178771_j().field_178830_e);
			NBTTagList nbttaglist1 = new NBTTagList();
			Iterator iterator1 = scoreplayerteam.getMembershipCollection().iterator();

			while (iterator1.hasNext())
			{
				String s = (String)iterator1.next();
				nbttaglist1.appendTag(new NBTTagString(s));
			}

			nbttagcompound.setTag("Players", nbttaglist1);
			nbttaglist.appendTag(nbttagcompound);
		}

		return nbttaglist;
	}

	protected void func_96497_d(NBTTagCompound p_96497_1_)
	{
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
		boolean flag = false;

		for (int i = 0; i < 19; ++i)
		{
			ScoreObjective scoreobjective = this.theScoreboard.getObjectiveInDisplaySlot(i);

			if (scoreobjective != null)
			{
				nbttagcompound1.setString("slot_" + i, scoreobjective.getName());
				flag = true;
			}
		}

		if (flag)
		{
			p_96497_1_.setTag("DisplaySlots", nbttagcompound1);
		}
	}

	protected NBTTagList objectivesToNbt()
	{
		NBTTagList nbttaglist = new NBTTagList();
		Collection collection = this.theScoreboard.getScoreObjectives();
		Iterator iterator = collection.iterator();

		while (iterator.hasNext())
		{
			ScoreObjective scoreobjective = (ScoreObjective)iterator.next();

			if (scoreobjective.getCriteria() != null)
			{
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setString("Name", scoreobjective.getName());
				nbttagcompound.setString("CriteriaName", scoreobjective.getCriteria().getName());
				nbttagcompound.setString("DisplayName", scoreobjective.getDisplayName());
				nbttagcompound.setString("RenderType", scoreobjective.getRenderType().func_178796_a());
				nbttaglist.appendTag(nbttagcompound);
			}
		}

		return nbttaglist;
	}

	protected NBTTagList scoresToNbt()
	{
		NBTTagList nbttaglist = new NBTTagList();
		Collection collection = this.theScoreboard.getScores();
		Iterator iterator = collection.iterator();

		while (iterator.hasNext())
		{
			Score score = (Score)iterator.next();

			if (score.getObjective() != null)
			{
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setString("Name", score.getPlayerName());
				nbttagcompound.setString("Objective", score.getObjective().getName());
				nbttagcompound.setInteger("Score", score.getScorePoints());
				nbttagcompound.setBoolean("Locked", score.isLocked());
				nbttaglist.appendTag(nbttagcompound);
			}
		}

		return nbttaglist;
	}
}