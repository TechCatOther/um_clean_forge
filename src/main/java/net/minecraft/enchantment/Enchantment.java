package net.minecraft.enchantment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public abstract class Enchantment
{
	private static final Enchantment[] enchantmentsList = new Enchantment[256];
	public static final Enchantment[] enchantmentsBookList;
	private static final Map locationEnchantments = Maps.newHashMap();
	public static final Enchantment protection = new EnchantmentProtection(0, new ResourceLocation("protection"), 10, 0);
	public static final Enchantment fireProtection = new EnchantmentProtection(1, new ResourceLocation("fire_protection"), 5, 1);
	public static final Enchantment featherFalling = new EnchantmentProtection(2, new ResourceLocation("feather_falling"), 5, 2);
	public static final Enchantment blastProtection = new EnchantmentProtection(3, new ResourceLocation("blast_protection"), 2, 3);
	public static final Enchantment projectileProtection = new EnchantmentProtection(4, new ResourceLocation("projectile_protection"), 5, 4);
	public static final Enchantment respiration = new EnchantmentOxygen(5, new ResourceLocation("respiration"), 2);
	public static final Enchantment aquaAffinity = new EnchantmentWaterWorker(6, new ResourceLocation("aqua_affinity"), 2);
	public static final Enchantment thorns = new EnchantmentThorns(7, new ResourceLocation("thorns"), 1);
	public static final Enchantment depthStrider = new EnchantmentWaterWalker(8, new ResourceLocation("depth_strider"), 2);
	public static final Enchantment sharpness = new EnchantmentDamage(16, new ResourceLocation("sharpness"), 10, 0);
	public static final Enchantment smite = new EnchantmentDamage(17, new ResourceLocation("smite"), 5, 1);
	public static final Enchantment baneOfArthropods = new EnchantmentDamage(18, new ResourceLocation("bane_of_arthropods"), 5, 2);
	public static final Enchantment knockback = new EnchantmentKnockback(19, new ResourceLocation("knockback"), 5);
	public static final Enchantment fireAspect = new EnchantmentFireAspect(20, new ResourceLocation("fire_aspect"), 2);
	public static final Enchantment looting = new EnchantmentLootBonus(21, new ResourceLocation("looting"), 2, EnumEnchantmentType.WEAPON);
	public static final Enchantment efficiency = new EnchantmentDigging(32, new ResourceLocation("efficiency"), 10);
	public static final Enchantment silkTouch = new EnchantmentUntouching(33, new ResourceLocation("silk_touch"), 1);
	public static final Enchantment unbreaking = new EnchantmentDurability(34, new ResourceLocation("unbreaking"), 5);
	public static final Enchantment fortune = new EnchantmentLootBonus(35, new ResourceLocation("fortune"), 2, EnumEnchantmentType.DIGGER);
	public static final Enchantment power = new EnchantmentArrowDamage(48, new ResourceLocation("power"), 10);
	public static final Enchantment punch = new EnchantmentArrowKnockback(49, new ResourceLocation("punch"), 2);
	public static final Enchantment flame = new EnchantmentArrowFire(50, new ResourceLocation("flame"), 2);
	public static final Enchantment infinity = new EnchantmentArrowInfinite(51, new ResourceLocation("infinity"), 1);
	public static final Enchantment luckOfTheSea = new EnchantmentLootBonus(61, new ResourceLocation("luck_of_the_sea"), 2, EnumEnchantmentType.FISHING_ROD);
	public static final Enchantment lure = new EnchantmentFishingSpeed(62, new ResourceLocation("lure"), 2, EnumEnchantmentType.FISHING_ROD);
	public final int effectId;
	private final int weight;
	public EnumEnchantmentType type;
	protected String name;
	private static final String __OBFID = "CL_00000105";

	public static Enchantment getEnchantmentById(int enchID)
	{
		return enchID >= 0 && enchID < enchantmentsList.length ? enchantmentsList[enchID] : null;
	}

	protected Enchantment(int enchID, ResourceLocation enchName, int enchWeight, EnumEnchantmentType enchType)
	{
		this.effectId = enchID;
		this.weight = enchWeight;
		this.type = enchType;

		if (enchantmentsList[enchID] != null)
		{
			throw new IllegalArgumentException("Duplicate enchantment id! " + this.getClass() + " and " + enchantmentsList[enchID].getClass() + " Enchantment ID:" + enchID);
		}
		else
		{
			enchantmentsList[enchID] = this;
			locationEnchantments.put(enchName, this);
		}
	}

	public static Enchantment getEnchantmentByLocation(String location)
	{
		return (Enchantment)locationEnchantments.get(new ResourceLocation(location));
	}

	public static String[] getLocationNames()
	{
		String[] astring = new String[locationEnchantments.size()];
		int i = 0;
		ResourceLocation resourcelocation;

		for (Iterator iterator = locationEnchantments.keySet().iterator(); iterator.hasNext(); astring[i++] = resourcelocation.toString())
		{
			resourcelocation = (ResourceLocation)iterator.next();
		}

		return astring;
	}

	public int getWeight()
	{
		return this.weight;
	}

	public int getMinLevel()
	{
		return 1;
	}

	public int getMaxLevel()
	{
		return 1;
	}

	public int getMinEnchantability(int enchantmentLevel)
	{
		return 1 + enchantmentLevel * 10;
	}

	public int getMaxEnchantability(int enchantmentLevel)
	{
		return this.getMinEnchantability(enchantmentLevel) + 5;
	}

	public int calcModifierDamage(int level, DamageSource source)
	{
		return 0;
	}

	public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType)
	{
		return 0.0F;
	}

	public boolean canApplyTogether(Enchantment ench)
	{
		return this != ench;
	}

	public Enchantment setName(String enchName)
	{
		this.name = enchName;
		return this;
	}

	public String getName()
	{
		return "enchantment." + this.name;
	}

	public String getTranslatedName(int level)
	{
		String s = StatCollector.translateToLocal(this.getName());
		return s + " " + StatCollector.translateToLocal("enchantment.level." + level);
	}

	public boolean canApply(ItemStack stack)
	{
		return canApplyAtEnchantingTable(stack);
	}

	public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {}

	public void onUserHurt(EntityLivingBase user, Entity attacker, int level) {}

	/**
	 * This applies specifically to applying at the enchanting table. The other method {@link #canApply(ItemStack)}
	 * applies for <i>all possible</i> enchantments.
	 * @param stack
	 * @return
	 */
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return this.type.canEnchantItem(stack.getItem());
	}

	private static final java.lang.reflect.Field bookSetter = Enchantment.class.getDeclaredFields()[1];
	/**
	 * Add to the list of enchantments applicable by the anvil from a book
	 *
	 * @param enchantment
	 */
	public static void addToBookList(Enchantment enchantment)
	{
		try
		{
			net.minecraftforge.common.util.EnumHelper.setFailsafeFieldValue(bookSetter, null,
				com.google.common.collect.ObjectArrays.concat(enchantmentsBookList, enchantment));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e); //Rethrow see what happens
		}
	}

	/**
	 * Is this enchantment allowed to be enchanted on books via Enchantment Table
	 * @return false to disable the vanilla feature
	 */
	public boolean isAllowedOnBooks()
	{
		return true;
	}

	static
	{
		ArrayList var0 = Lists.newArrayList();
		Enchantment[] var1 = enchantmentsList;
		int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3)
		{
			Enchantment var4 = var1[var3];

			if (var4 != null)
			{
				var0.add(var4);
			}
		}

		enchantmentsBookList = (Enchantment[])var0.toArray(new Enchantment[var0.size()]);
	}
}