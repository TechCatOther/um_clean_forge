package net.minecraft.util;

import java.util.Random;
import java.util.UUID;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MathHelper
{
	public static final float SQRT_2 = sqrt_float(2.0F);
	private static final float[] SIN_TABLE = new float[65536];
	private static final int[] multiplyDeBruijnBitPosition;
	private static final String __OBFID = "CL_00001496";

	public static float sin(float p_76126_0_)
	{
		return SIN_TABLE[(int)(p_76126_0_ * 10430.378F) & 65535];
	}

	public static float cos(float p_76134_0_)
	{
		return SIN_TABLE[(int)(p_76134_0_ * 10430.378F + 16384.0F) & 65535];
	}

	public static float sqrt_float(float p_76129_0_)
	{
		return (float)Math.sqrt((double)p_76129_0_);
	}

	public static float sqrt_double(double p_76133_0_)
	{
		return (float)Math.sqrt(p_76133_0_);
	}

	public static int floor_float(float p_76141_0_)
	{
		int i = (int)p_76141_0_;
		return p_76141_0_ < (float)i ? i - 1 : i;
	}

	@SideOnly(Side.CLIENT)
	public static int truncateDoubleToInt(double p_76140_0_)
	{
		return (int)(p_76140_0_ + 1024.0D) - 1024;
	}

	public static int floor_double(double p_76128_0_)
	{
		int i = (int)p_76128_0_;
		return p_76128_0_ < (double)i ? i - 1 : i;
	}

	public static long floor_double_long(double p_76124_0_)
	{
		long i = (long)p_76124_0_;
		return p_76124_0_ < (double)i ? i - 1L : i;
	}

	@SideOnly(Side.CLIENT)
	public static int func_154353_e(double p_154353_0_)
	{
		return (int)(p_154353_0_ >= 0.0D ? p_154353_0_ : -p_154353_0_ + 1.0D);
	}

	public static float abs(float p_76135_0_)
	{
		return p_76135_0_ >= 0.0F ? p_76135_0_ : -p_76135_0_;
	}

	public static int abs_int(int p_76130_0_)
	{
		return p_76130_0_ >= 0 ? p_76130_0_ : -p_76130_0_;
	}

	public static int ceiling_float_int(float p_76123_0_)
	{
		int i = (int)p_76123_0_;
		return p_76123_0_ > (float)i ? i + 1 : i;
	}

	public static int ceiling_double_int(double p_76143_0_)
	{
		int i = (int)p_76143_0_;
		return p_76143_0_ > (double)i ? i + 1 : i;
	}

	public static int clamp_int(int p_76125_0_, int p_76125_1_, int p_76125_2_)
	{
		return p_76125_0_ < p_76125_1_ ? p_76125_1_ : (p_76125_0_ > p_76125_2_ ? p_76125_2_ : p_76125_0_);
	}

	public static float clamp_float(float p_76131_0_, float p_76131_1_, float p_76131_2_)
	{
		return p_76131_0_ < p_76131_1_ ? p_76131_1_ : (p_76131_0_ > p_76131_2_ ? p_76131_2_ : p_76131_0_);
	}

	public static double clamp_double(double p_151237_0_, double p_151237_2_, double p_151237_4_)
	{
		return p_151237_0_ < p_151237_2_ ? p_151237_2_ : (p_151237_0_ > p_151237_4_ ? p_151237_4_ : p_151237_0_);
	}

	public static double denormalizeClamp(double p_151238_0_, double p_151238_2_, double p_151238_4_)
	{
		return p_151238_4_ < 0.0D ? p_151238_0_ : (p_151238_4_ > 1.0D ? p_151238_2_ : p_151238_0_ + (p_151238_2_ - p_151238_0_) * p_151238_4_);
	}

	public static double abs_max(double p_76132_0_, double p_76132_2_)
	{
		if (p_76132_0_ < 0.0D)
		{
			p_76132_0_ = -p_76132_0_;
		}

		if (p_76132_2_ < 0.0D)
		{
			p_76132_2_ = -p_76132_2_;
		}

		return p_76132_0_ > p_76132_2_ ? p_76132_0_ : p_76132_2_;
	}

	@SideOnly(Side.CLIENT)
	public static int bucketInt(int p_76137_0_, int p_76137_1_)
	{
		return p_76137_0_ < 0 ? -((-p_76137_0_ - 1) / p_76137_1_) - 1 : p_76137_0_ / p_76137_1_;
	}

	public static int getRandomIntegerInRange(Random p_76136_0_, int p_76136_1_, int p_76136_2_)
	{
		return p_76136_1_ >= p_76136_2_ ? p_76136_1_ : p_76136_0_.nextInt(p_76136_2_ - p_76136_1_ + 1) + p_76136_1_;
	}

	public static float randomFloatClamp(Random p_151240_0_, float p_151240_1_, float p_151240_2_)
	{
		return p_151240_1_ >= p_151240_2_ ? p_151240_1_ : p_151240_0_.nextFloat() * (p_151240_2_ - p_151240_1_) + p_151240_1_;
	}

	public static double getRandomDoubleInRange(Random p_82716_0_, double p_82716_1_, double p_82716_3_)
	{
		return p_82716_1_ >= p_82716_3_ ? p_82716_1_ : p_82716_0_.nextDouble() * (p_82716_3_ - p_82716_1_) + p_82716_1_;
	}

	public static double average(long[] p_76127_0_)
	{
		long i = 0L;
		long[] along1 = p_76127_0_;
		int j = p_76127_0_.length;

		for (int k = 0; k < j; ++k)
		{
			long l = along1[k];
			i += l;
		}

		return (double)i / (double)p_76127_0_.length;
	}

	@SideOnly(Side.CLIENT)
	public static boolean func_180185_a(float p_180185_0_, float p_180185_1_)
	{
		return abs(p_180185_1_ - p_180185_0_) < 1.0E-5F;
	}

	@SideOnly(Side.CLIENT)
	public static int normalizeAngle(int p_180184_0_, int p_180184_1_)
	{
		return (p_180184_0_ % p_180184_1_ + p_180184_1_) % p_180184_1_;
	}

	public static float wrapAngleTo180_float(float p_76142_0_)
	{
		p_76142_0_ %= 360.0F;

		if (p_76142_0_ >= 180.0F)
		{
			p_76142_0_ -= 360.0F;
		}

		if (p_76142_0_ < -180.0F)
		{
			p_76142_0_ += 360.0F;
		}

		return p_76142_0_;
	}

	public static double wrapAngleTo180_double(double p_76138_0_)
	{
		p_76138_0_ %= 360.0D;

		if (p_76138_0_ >= 180.0D)
		{
			p_76138_0_ -= 360.0D;
		}

		if (p_76138_0_ < -180.0D)
		{
			p_76138_0_ += 360.0D;
		}

		return p_76138_0_;
	}

	public static int parseIntWithDefault(String p_82715_0_, int p_82715_1_)
	{
		try
		{
			return Integer.parseInt(p_82715_0_);
		}
		catch (Throwable throwable)
		{
			return p_82715_1_;
		}
	}

	public static int parseIntWithDefaultAndMax(String p_82714_0_, int p_82714_1_, int p_82714_2_)
	{
		return Math.max(p_82714_2_, parseIntWithDefault(p_82714_0_, p_82714_1_));
	}

	public static double parseDoubleWithDefault(String p_82712_0_, double p_82712_1_)
	{
		try
		{
			return Double.parseDouble(p_82712_0_);
		}
		catch (Throwable throwable)
		{
			return p_82712_1_;
		}
	}

	public static double parseDoubleWithDefaultAndMax(String p_82713_0_, double p_82713_1_, double p_82713_3_)
	{
		return Math.max(p_82713_3_, parseDoubleWithDefault(p_82713_0_, p_82713_1_));
	}

	public static int roundUpToPowerOfTwo(int p_151236_0_)
	{
		int j = p_151236_0_ - 1;
		j |= j >> 1;
		j |= j >> 2;
		j |= j >> 4;
		j |= j >> 8;
		j |= j >> 16;
		return j + 1;
	}

	private static boolean isPowerOfTwo(int p_151235_0_)
	{
		return p_151235_0_ != 0 && (p_151235_0_ & p_151235_0_ - 1) == 0;
	}

	private static int calculateLogBaseTwoDeBruijn(int p_151241_0_)
	{
		p_151241_0_ = isPowerOfTwo(p_151241_0_) ? p_151241_0_ : roundUpToPowerOfTwo(p_151241_0_);
		return multiplyDeBruijnBitPosition[(int)((long)p_151241_0_ * 125613361L >> 27) & 31];
	}

	public static int calculateLogBaseTwo(int p_151239_0_)
	{
		return calculateLogBaseTwoDeBruijn(p_151239_0_) - (isPowerOfTwo(p_151239_0_) ? 0 : 1);
	}

	public static int func_154354_b(int p_154354_0_, int p_154354_1_)
	{
		if (p_154354_1_ == 0)
		{
			return 0;
		}
		else if (p_154354_0_ == 0)
		{
			return p_154354_1_;
		}
		else
		{
			if (p_154354_0_ < 0)
			{
				p_154354_1_ *= -1;
			}

			int k = p_154354_0_ % p_154354_1_;
			return k == 0 ? p_154354_0_ : p_154354_0_ + p_154354_1_ - k;
		}
	}

	@SideOnly(Side.CLIENT)
	public static int func_180183_b(float p_180183_0_, float p_180183_1_, float p_180183_2_)
	{
		return func_180181_b(floor_float(p_180183_0_ * 255.0F), floor_float(p_180183_1_ * 255.0F), floor_float(p_180183_2_ * 255.0F));
	}

	@SideOnly(Side.CLIENT)
	public static int func_180181_b(int p_180181_0_, int p_180181_1_, int p_180181_2_)
	{
		int l = (p_180181_0_ << 8) + p_180181_1_;
		l = (l << 8) + p_180181_2_;
		return l;
	}

	@SideOnly(Side.CLIENT)
	public static int func_180188_d(int p_180188_0_, int p_180188_1_)
	{
		int k = (p_180188_0_ & 16711680) >> 16;
		int l = (p_180188_1_ & 16711680) >> 16;
		int i1 = (p_180188_0_ & 65280) >> 8;
		int j1 = (p_180188_1_ & 65280) >> 8;
		int k1 = (p_180188_0_ & 255) >> 0;
		int l1 = (p_180188_1_ & 255) >> 0;
		int i2 = (int)((float)k * (float)l / 255.0F);
		int j2 = (int)((float)i1 * (float)j1 / 255.0F);
		int k2 = (int)((float)k1 * (float)l1 / 255.0F);
		return p_180188_0_ & -16777216 | i2 << 16 | j2 << 8 | k2;
	}

	@SideOnly(Side.CLIENT)
	public static long getPositionRandom(Vec3i pos)
	{
		return getCoordinateRandom(pos.getX(), pos.getY(), pos.getZ());
	}

	public static UUID getRandomUuid(Random p_180182_0_)
	{
		long i = p_180182_0_.nextLong() & -61441L | 16384L;
		long j = p_180182_0_.nextLong() & 4611686018427387903L | Long.MIN_VALUE;
		return new UUID(i, j);
	}

	@SideOnly(Side.CLIENT)
	public static long getCoordinateRandom(int x, int y, int z)
	{
		long l = (long)(x * 3129871) ^ (long)z * 116129781L ^ (long)y;
		l = l * l * 42317861L + l * 11L;
		return l;
	}

	static
	{
		for (int var0 = 0; var0 < 65536; ++var0)
		{
			SIN_TABLE[var0] = (float)Math.sin((double)var0 * Math.PI * 2.0D / 65536.0D);
		}

		multiplyDeBruijnBitPosition = new int[] {0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
	}
}