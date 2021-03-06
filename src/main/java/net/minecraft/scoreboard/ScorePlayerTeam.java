package net.minecraft.scoreboard;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ScorePlayerTeam extends Team
{
	private final Scoreboard theScoreboard;
	private final String registeredName;
	private final Set membershipSet = Sets.newHashSet();
	private String teamNameSPT;
	private String namePrefixSPT = "";
	private String colorSuffix = "";
	private boolean allowFriendlyFire = true;
	private boolean canSeeFriendlyInvisibles = true;
	private Team.EnumVisible field_178778_i;
	private Team.EnumVisible field_178776_j;
	private EnumChatFormatting field_178777_k;
	private static final String __OBFID = "CL_00000616";

	public ScorePlayerTeam(Scoreboard p_i2308_1_, String name)
	{
		this.field_178778_i = Team.EnumVisible.ALWAYS;
		this.field_178776_j = Team.EnumVisible.ALWAYS;
		this.field_178777_k = EnumChatFormatting.RESET;
		this.theScoreboard = p_i2308_1_;
		this.registeredName = name;
		this.teamNameSPT = name;
	}

	public String getRegisteredName()
	{
		return this.registeredName;
	}

	public String func_96669_c()
	{
		return this.teamNameSPT;
	}

	public void setTeamName(String name)
	{
		if (name == null)
		{
			throw new IllegalArgumentException("Name cannot be null");
		}
		else
		{
			this.teamNameSPT = name;
			this.theScoreboard.sendTeamUpdate(this);
		}
	}

	public Collection getMembershipCollection()
	{
		return this.membershipSet;
	}

	public String getColorPrefix()
	{
		return this.namePrefixSPT;
	}

	public void setNamePrefix(String prefix)
	{
		if (prefix == null)
		{
			throw new IllegalArgumentException("Prefix cannot be null");
		}
		else
		{
			this.namePrefixSPT = prefix;
			this.theScoreboard.sendTeamUpdate(this);
		}
	}

	public String getColorSuffix()
	{
		return this.colorSuffix;
	}

	public void setNameSuffix(String suffix)
	{
		this.colorSuffix = suffix;
		this.theScoreboard.sendTeamUpdate(this);
	}

	public String formatString(String input)
	{
		return this.getColorPrefix() + input + this.getColorSuffix();
	}

	public static String formatPlayerName(Team p_96667_0_, String p_96667_1_)
	{
		return p_96667_0_ == null ? p_96667_1_ : p_96667_0_.formatString(p_96667_1_);
	}

	public boolean getAllowFriendlyFire()
	{
		return this.allowFriendlyFire;
	}

	public void setAllowFriendlyFire(boolean friendlyFire)
	{
		this.allowFriendlyFire = friendlyFire;
		this.theScoreboard.sendTeamUpdate(this);
	}

	public boolean func_98297_h()
	{
		return this.canSeeFriendlyInvisibles;
	}

	public void setSeeFriendlyInvisiblesEnabled(boolean friendlyInvisibles)
	{
		this.canSeeFriendlyInvisibles = friendlyInvisibles;
		this.theScoreboard.sendTeamUpdate(this);
	}

	public Team.EnumVisible func_178770_i()
	{
		return this.field_178778_i;
	}

	public Team.EnumVisible func_178771_j()
	{
		return this.field_178776_j;
	}

	public void func_178772_a(Team.EnumVisible p_178772_1_)
	{
		this.field_178778_i = p_178772_1_;
		this.theScoreboard.sendTeamUpdate(this);
	}

	public void func_178773_b(Team.EnumVisible p_178773_1_)
	{
		this.field_178776_j = p_178773_1_;
		this.theScoreboard.sendTeamUpdate(this);
	}

	public int func_98299_i()
	{
		int i = 0;

		if (this.getAllowFriendlyFire())
		{
			i |= 1;
		}

		if (this.func_98297_h())
		{
			i |= 2;
		}

		return i;
	}

	@SideOnly(Side.CLIENT)
	public void func_98298_a(int p_98298_1_)
	{
		this.setAllowFriendlyFire((p_98298_1_ & 1) > 0);
		this.setSeeFriendlyInvisiblesEnabled((p_98298_1_ & 2) > 0);
	}

	public void func_178774_a(EnumChatFormatting p_178774_1_)
	{
		this.field_178777_k = p_178774_1_;
	}

	public EnumChatFormatting func_178775_l()
	{
		return this.field_178777_k;
	}
}