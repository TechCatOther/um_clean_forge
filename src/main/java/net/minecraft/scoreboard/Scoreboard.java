package net.minecraft.scoreboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.util.EnumChatFormatting;

public class Scoreboard
{
	private final Map scoreObjectives = Maps.newHashMap();
	private final Map scoreObjectiveCriterias = Maps.newHashMap();
	private final Map entitiesScoreObjectives = Maps.newHashMap();
	private final ScoreObjective[] objectiveDisplaySlots = new ScoreObjective[19];
	private final Map teams = Maps.newHashMap();
	private final Map teamMemberships = Maps.newHashMap();
	private static String[] field_178823_g = null;
	private static final String __OBFID = "CL_00000619";

	public ScoreObjective getObjective(String name)
	{
		return (ScoreObjective)this.scoreObjectives.get(name);
	}

	public ScoreObjective addScoreObjective(String name, IScoreObjectiveCriteria criteria)
	{
		ScoreObjective scoreobjective = this.getObjective(name);

		if (scoreobjective != null)
		{
			throw new IllegalArgumentException("An objective with the name \'" + name + "\' already exists!");
		}
		else
		{
			scoreobjective = new ScoreObjective(this, name, criteria);
			Object object = (List)this.scoreObjectiveCriterias.get(criteria);

			if (object == null)
			{
				object = Lists.newArrayList();
				this.scoreObjectiveCriterias.put(criteria, object);
			}

			((List)object).add(scoreobjective);
			this.scoreObjectives.put(name, scoreobjective);
			this.func_96522_a(scoreobjective);
			return scoreobjective;
		}
	}

	public Collection getObjectivesFromCriteria(IScoreObjectiveCriteria criteria)
	{
		Collection collection = (Collection)this.scoreObjectiveCriterias.get(criteria);
		return collection == null ? Lists.newArrayList() : Lists.newArrayList(collection);
	}

	public boolean entityHasObjective(String name, ScoreObjective p_178819_2_)
	{
		Map map = (Map)this.entitiesScoreObjectives.get(name);

		if (map == null)
		{
			return false;
		}
		else
		{
			Score score = (Score)map.get(p_178819_2_);
			return score != null;
		}
	}

	public Score getValueFromObjective(String name, ScoreObjective objective)
	{
		Object object = (Map)this.entitiesScoreObjectives.get(name);

		if (object == null)
		{
			object = Maps.newHashMap();
			this.entitiesScoreObjectives.put(name, object);
		}

		Score score = (Score)((Map)object).get(objective);

		if (score == null)
		{
			score = new Score(this, objective, name);
			((Map)object).put(objective, score);
		}

		return score;
	}

	public Collection getSortedScores(ScoreObjective objective)
	{
		ArrayList arraylist = Lists.newArrayList();
		Iterator iterator = this.entitiesScoreObjectives.values().iterator();

		while (iterator.hasNext())
		{
			Map map = (Map)iterator.next();
			Score score = (Score)map.get(objective);

			if (score != null)
			{
				arraylist.add(score);
			}
		}

		Collections.sort(arraylist, Score.scoreComparator);
		return arraylist;
	}

	public Collection getScoreObjectives()
	{
		return this.scoreObjectives.values();
	}

	public Collection getObjectiveNames()
	{
		return this.entitiesScoreObjectives.keySet();
	}

	public void removeObjectiveFromEntity(String name, ScoreObjective objective)
	{
		Map map;

		if (objective == null)
		{
			map = (Map)this.entitiesScoreObjectives.remove(name);

			if (map != null)
			{
				this.func_96516_a(name);
			}
		}
		else
		{
			map = (Map)this.entitiesScoreObjectives.get(name);

			if (map != null)
			{
				Score score = (Score)map.remove(objective);

				if (map.size() < 1)
				{
					Map map1 = (Map)this.entitiesScoreObjectives.remove(name);

					if (map1 != null)
					{
						this.func_96516_a(name);
					}
				}
				else if (score != null)
				{
					this.func_178820_a(name, objective);
				}
			}
		}
	}

	public Collection getScores()
	{
		Collection collection = this.entitiesScoreObjectives.values();
		ArrayList arraylist = Lists.newArrayList();
		Iterator iterator = collection.iterator();

		while (iterator.hasNext())
		{
			Map map = (Map)iterator.next();
			arraylist.addAll(map.values());
		}

		return arraylist;
	}

	public Map getObjectivesForEntity(String name)
	{
		Object object = (Map)this.entitiesScoreObjectives.get(name);

		if (object == null)
		{
			object = Maps.newHashMap();
		}

		return (Map)object;
	}

	public void func_96519_k(ScoreObjective p_96519_1_)
	{
		this.scoreObjectives.remove(p_96519_1_.getName());

		for (int i = 0; i < 19; ++i)
		{
			if (this.getObjectiveInDisplaySlot(i) == p_96519_1_)
			{
				this.setObjectiveInDisplaySlot(i, (ScoreObjective)null);
			}
		}

		List list = (List)this.scoreObjectiveCriterias.get(p_96519_1_.getCriteria());

		if (list != null)
		{
			list.remove(p_96519_1_);
		}

		Iterator iterator = this.entitiesScoreObjectives.values().iterator();

		while (iterator.hasNext())
		{
			Map map = (Map)iterator.next();
			map.remove(p_96519_1_);
		}

		this.func_96533_c(p_96519_1_);
	}

	public void setObjectiveInDisplaySlot(int p_96530_1_, ScoreObjective p_96530_2_)
	{
		this.objectiveDisplaySlots[p_96530_1_] = p_96530_2_;
	}

	public ScoreObjective getObjectiveInDisplaySlot(int p_96539_1_)
	{
		return this.objectiveDisplaySlots[p_96539_1_];
	}

	public ScorePlayerTeam getTeam(String p_96508_1_)
	{
		return (ScorePlayerTeam)this.teams.get(p_96508_1_);
	}

	public ScorePlayerTeam createTeam(String p_96527_1_)
	{
		ScorePlayerTeam scoreplayerteam = this.getTeam(p_96527_1_);

		if (scoreplayerteam != null)
		{
			throw new IllegalArgumentException("A team with the name \'" + p_96527_1_ + "\' already exists!");
		}
		else
		{
			scoreplayerteam = new ScorePlayerTeam(this, p_96527_1_);
			this.teams.put(p_96527_1_, scoreplayerteam);
			this.broadcastTeamCreated(scoreplayerteam);
			return scoreplayerteam;
		}
	}

	public void removeTeam(ScorePlayerTeam p_96511_1_)
	{
		this.teams.remove(p_96511_1_.getRegisteredName());
		Iterator iterator = p_96511_1_.getMembershipCollection().iterator();

		while (iterator.hasNext())
		{
			String s = (String)iterator.next();
			this.teamMemberships.remove(s);
		}

		this.func_96513_c(p_96511_1_);
	}

	public boolean addPlayerToTeam(String player, String newTeam)
	{
		if (!this.teams.containsKey(newTeam))
		{
			return false;
		}
		else
		{
			ScorePlayerTeam scoreplayerteam = this.getTeam(newTeam);

			if (this.getPlayersTeam(player) != null)
			{
				this.removePlayerFromTeams(player);
			}

			this.teamMemberships.put(player, scoreplayerteam);
			scoreplayerteam.getMembershipCollection().add(player);
			return true;
		}
	}

	public boolean removePlayerFromTeams(String p_96524_1_)
	{
		ScorePlayerTeam scoreplayerteam = this.getPlayersTeam(p_96524_1_);

		if (scoreplayerteam != null)
		{
			this.removePlayerFromTeam(p_96524_1_, scoreplayerteam);
			return true;
		}
		else
		{
			return false;
		}
	}

	public void removePlayerFromTeam(String p_96512_1_, ScorePlayerTeam p_96512_2_)
	{
		if (this.getPlayersTeam(p_96512_1_) != p_96512_2_)
		{
			throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team \'" + p_96512_2_.getRegisteredName() + "\'.");
		}
		else
		{
			this.teamMemberships.remove(p_96512_1_);
			p_96512_2_.getMembershipCollection().remove(p_96512_1_);
		}
	}

	public Collection getTeamNames()
	{
		return this.teams.keySet();
	}

	public Collection getTeams()
	{
		return this.teams.values();
	}

	public ScorePlayerTeam getPlayersTeam(String p_96509_1_)
	{
		return (ScorePlayerTeam)this.teamMemberships.get(p_96509_1_);
	}

	public void func_96522_a(ScoreObjective p_96522_1_) {}

	public void func_96532_b(ScoreObjective p_96532_1_) {}

	public void func_96533_c(ScoreObjective p_96533_1_) {}

	public void func_96536_a(Score p_96536_1_) {}

	public void func_96516_a(String p_96516_1_) {}

	public void func_178820_a(String p_178820_1_, ScoreObjective p_178820_2_) {}

	public void broadcastTeamCreated(ScorePlayerTeam playerTeam) {}

	public void sendTeamUpdate(ScorePlayerTeam playerTeam) {}

	public void func_96513_c(ScorePlayerTeam playerTeam) {}

	public static String getObjectiveDisplaySlot(int p_96517_0_)
	{
		switch (p_96517_0_)
		{
			case 0:
				return "list";
			case 1:
				return "sidebar";
			case 2:
				return "belowName";
			default:
				if (p_96517_0_ >= 3 && p_96517_0_ <= 18)
				{
					EnumChatFormatting enumchatformatting = EnumChatFormatting.func_175744_a(p_96517_0_ - 3);

					if (enumchatformatting != null && enumchatformatting != EnumChatFormatting.RESET)
					{
						return "sidebar.team." + enumchatformatting.getFriendlyName();
					}
				}

				return null;
		}
	}

	public static int getObjectiveDisplaySlotNumber(String p_96537_0_)
	{
		if (p_96537_0_.equalsIgnoreCase("list"))
		{
			return 0;
		}
		else if (p_96537_0_.equalsIgnoreCase("sidebar"))
		{
			return 1;
		}
		else if (p_96537_0_.equalsIgnoreCase("belowName"))
		{
			return 2;
		}
		else
		{
			if (p_96537_0_.startsWith("sidebar.team."))
			{
				String s1 = p_96537_0_.substring("sidebar.team.".length());
				EnumChatFormatting enumchatformatting = EnumChatFormatting.getValueByName(s1);

				if (enumchatformatting != null && enumchatformatting.getColorIndex() >= 0)
				{
					return enumchatformatting.getColorIndex() + 3;
				}
			}

			return -1;
		}
	}

	public static String[] func_178821_h()
	{
		if (field_178823_g == null)
		{
			field_178823_g = new String[19];

			for (int i = 0; i < 19; ++i)
			{
				field_178823_g[i] = getObjectiveDisplaySlot(i);
			}
		}

		return field_178823_g;
	}
}