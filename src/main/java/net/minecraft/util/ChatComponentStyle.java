package net.minecraft.util;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ChatComponentStyle implements IChatComponent
{
	protected List siblings = Lists.newArrayList();
	private ChatStyle style;
	private static final String __OBFID = "CL_00001257";

	public IChatComponent appendSibling(IChatComponent component)
	{
		component.getChatStyle().setParentStyle(this.getChatStyle());
		this.siblings.add(component);
		return this;
	}

	public List getSiblings()
	{
		return this.siblings;
	}

	public IChatComponent appendText(String text)
	{
		return this.appendSibling(new ChatComponentText(text));
	}

	public IChatComponent setChatStyle(ChatStyle style)
	{
		this.style = style;
		Iterator iterator = this.siblings.iterator();

		while (iterator.hasNext())
		{
			IChatComponent ichatcomponent = (IChatComponent)iterator.next();
			ichatcomponent.getChatStyle().setParentStyle(this.getChatStyle());
		}

		return this;
	}

	public ChatStyle getChatStyle()
	{
		if (this.style == null)
		{
			this.style = new ChatStyle();
			Iterator iterator = this.siblings.iterator();

			while (iterator.hasNext())
			{
				IChatComponent ichatcomponent = (IChatComponent)iterator.next();
				ichatcomponent.getChatStyle().setParentStyle(this.style);
			}
		}

		return this.style;
	}

	public Iterator iterator()
	{
		return Iterators.concat(Iterators.forArray(new ChatComponentStyle[] {this}), createDeepCopyIterator(this.siblings));
	}

	public final String getUnformattedText()
	{
		StringBuilder stringbuilder = new StringBuilder();
		Iterator iterator = this.iterator();

		while (iterator.hasNext())
		{
			IChatComponent ichatcomponent = (IChatComponent)iterator.next();
			stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
		}

		return stringbuilder.toString();
	}

	@SideOnly(Side.CLIENT)
	public final String getFormattedText()
	{
		StringBuilder stringbuilder = new StringBuilder();
		Iterator iterator = this.iterator();

		while (iterator.hasNext())
		{
			IChatComponent ichatcomponent = (IChatComponent)iterator.next();
			stringbuilder.append(ichatcomponent.getChatStyle().getFormattingCode());
			stringbuilder.append(ichatcomponent.getUnformattedTextForChat());
			stringbuilder.append(EnumChatFormatting.RESET);
		}

		return stringbuilder.toString();
	}

	public static Iterator createDeepCopyIterator(Iterable components)
	{
		Iterator iterator = Iterators.concat(Iterators.transform(components.iterator(), new Function()
		{
			private static final String __OBFID = "CL_00001258";
			public Iterator apply(IChatComponent p_apply_1_)
			{
				return p_apply_1_.iterator();
			}
			public Object apply(Object p_apply_1_)
			{
				return this.apply((IChatComponent)p_apply_1_);
			}
		}));
		iterator = Iterators.transform(iterator, new Function()
		{
			private static final String __OBFID = "CL_00001259";
			public IChatComponent apply(IChatComponent p_apply_1_)
			{
				IChatComponent ichatcomponent1 = p_apply_1_.createCopy();
				ichatcomponent1.setChatStyle(ichatcomponent1.getChatStyle().createDeepCopy());
				return ichatcomponent1;
			}
			public Object apply(Object p_apply_1_)
			{
				return this.apply((IChatComponent)p_apply_1_);
			}
		});
		return iterator;
	}

	public boolean equals(Object p_equals_1_)
	{
		if (this == p_equals_1_)
		{
			return true;
		}
		else if (!(p_equals_1_ instanceof ChatComponentStyle))
		{
			return false;
		}
		else
		{
			ChatComponentStyle chatcomponentstyle = (ChatComponentStyle)p_equals_1_;
			return this.siblings.equals(chatcomponentstyle.siblings) && this.getChatStyle().equals(chatcomponentstyle.getChatStyle());
		}
	}

	public int hashCode()
	{
		return 31 * this.style.hashCode() + this.siblings.hashCode();
	}

	public String toString()
	{
		return "BaseComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
	}
}