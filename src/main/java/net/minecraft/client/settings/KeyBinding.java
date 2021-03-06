package net.minecraft.client.settings;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IntHashMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyBinding implements Comparable
{
	private static final List keybindArray = Lists.newArrayList();
	private static final IntHashMap hash = new IntHashMap();
	private static final Set keybindSet = Sets.newHashSet();
	private final String keyDescription;
	private final int keyCodeDefault;
	private final String keyCategory;
	private int keyCode;
	private boolean pressed;
	private int pressTime;
	private static final String __OBFID = "CL_00000628";

	public static void onTick(int keyCode)
	{
		if (keyCode != 0)
		{
			KeyBinding keybinding = (KeyBinding)hash.lookup(keyCode);

			if (keybinding != null)
			{
				++keybinding.pressTime;
			}
		}
	}

	public static void setKeyBindState(int keyCode, boolean pressed)
	{
		if (keyCode != 0)
		{
			KeyBinding keybinding = (KeyBinding)hash.lookup(keyCode);

			if (keybinding != null)
			{
				keybinding.pressed = pressed;
			}
		}
	}

	public static void unPressAllKeys()
	{
		Iterator iterator = keybindArray.iterator();

		while (iterator.hasNext())
		{
			KeyBinding keybinding = (KeyBinding)iterator.next();
			keybinding.unpressKey();
		}
	}

	public static void resetKeyBindingArrayAndHash()
	{
		hash.clearMap();
		Iterator iterator = keybindArray.iterator();

		while (iterator.hasNext())
		{
			KeyBinding keybinding = (KeyBinding)iterator.next();
			hash.addKey(keybinding.keyCode, keybinding);
		}
	}

	public static Set getKeybinds()
	{
		return keybindSet;
	}

	public KeyBinding(String description, int keyCode, String category)
	{
		this.keyDescription = description;
		this.keyCode = keyCode;
		this.keyCodeDefault = keyCode;
		this.keyCategory = category;
		keybindArray.add(this);
		hash.addKey(keyCode, this);
		keybindSet.add(category);
	}

	public boolean isKeyDown()
	{
		return this.pressed;
	}

	public String getKeyCategory()
	{
		return this.keyCategory;
	}

	public boolean isPressed()
	{
		if (this.pressTime == 0)
		{
			return false;
		}
		else
		{
			--this.pressTime;
			return true;
		}
	}

	private void unpressKey()
	{
		this.pressTime = 0;
		this.pressed = false;
	}

	public String getKeyDescription()
	{
		return this.keyDescription;
	}

	public int getKeyCodeDefault()
	{
		return this.keyCodeDefault;
	}

	public int getKeyCode()
	{
		return this.keyCode;
	}

	public void setKeyCode(int keyCode)
	{
		this.keyCode = keyCode;
	}

	public int compareTo(KeyBinding p_compareTo_1_)
	{
		int i = I18n.format(this.keyCategory, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyCategory, new Object[0]));

		if (i == 0)
		{
			i = I18n.format(this.keyDescription, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyDescription, new Object[0]));
		}

		return i;
	}

	public int compareTo(Object p_compareTo_1_)
	{
		return this.compareTo((KeyBinding)p_compareTo_1_);
	}
}