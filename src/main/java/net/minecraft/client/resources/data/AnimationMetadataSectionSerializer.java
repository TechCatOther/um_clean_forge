package net.minecraft.client.resources.data;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.Validate;

@SideOnly(Side.CLIENT)
public class AnimationMetadataSectionSerializer extends BaseMetadataSectionSerializer implements JsonSerializer
{
	private static final String __OBFID = "CL_00001107";

	public AnimationMetadataSection deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
	{
		ArrayList arraylist = Lists.newArrayList();
		JsonObject jsonobject = JsonUtils.getElementAsJsonObject(p_deserialize_1_, "metadata section");
		int i = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "frametime", 1);

		if (i != 1)
		{
			Validate.inclusiveBetween(1L, 2147483647L, (long)i, "Invalid default frame time");
		}

		int j;

		if (jsonobject.has("frames"))
		{
			try
			{
				JsonArray jsonarray = JsonUtils.getJsonObjectJsonArrayField(jsonobject, "frames");

				for (j = 0; j < jsonarray.size(); ++j)
				{
					JsonElement jsonelement1 = jsonarray.get(j);
					AnimationFrame animationframe = this.parseAnimationFrame(j, jsonelement1);

					if (animationframe != null)
					{
						arraylist.add(animationframe);
					}
				}
			}
			catch (ClassCastException classcastexception)
			{
				throw new JsonParseException("Invalid animation->frames: expected array, was " + jsonobject.get("frames"), classcastexception);
			}
		}

		int k = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "width", -1);
		j = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "height", -1);

		if (k != -1)
		{
			Validate.inclusiveBetween(1L, 2147483647L, (long)k, "Invalid width");
		}

		if (j != -1)
		{
			Validate.inclusiveBetween(1L, 2147483647L, (long)j, "Invalid height");
		}

		boolean flag = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject, "interpolate", false);
		return new AnimationMetadataSection(arraylist, k, j, i, flag);
	}

	private AnimationFrame parseAnimationFrame(int p_110492_1_, JsonElement p_110492_2_)
	{
		if (p_110492_2_.isJsonPrimitive())
		{
			return new AnimationFrame(JsonUtils.getJsonElementIntegerValue(p_110492_2_, "frames[" + p_110492_1_ + "]"));
		}
		else if (p_110492_2_.isJsonObject())
		{
			JsonObject jsonobject = JsonUtils.getElementAsJsonObject(p_110492_2_, "frames[" + p_110492_1_ + "]");
			int j = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "time", -1);

			if (jsonobject.has("time"))
			{
				Validate.inclusiveBetween(1L, 2147483647L, (long)j, "Invalid frame time");
			}

			int k = JsonUtils.getJsonObjectIntegerFieldValue(jsonobject, "index");
			Validate.inclusiveBetween(0L, 2147483647L, (long)k, "Invalid frame index");
			return new AnimationFrame(k, j);
		}
		else
		{
			return null;
		}
	}

	public JsonElement serialize(AnimationMetadataSection p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
	{
		JsonObject jsonobject = new JsonObject();
		jsonobject.addProperty("frametime", Integer.valueOf(p_serialize_1_.getFrameTime()));

		if (p_serialize_1_.getFrameWidth() != -1)
		{
			jsonobject.addProperty("width", Integer.valueOf(p_serialize_1_.getFrameWidth()));
		}

		if (p_serialize_1_.getFrameHeight() != -1)
		{
			jsonobject.addProperty("height", Integer.valueOf(p_serialize_1_.getFrameHeight()));
		}

		if (p_serialize_1_.getFrameCount() > 0)
		{
			JsonArray jsonarray = new JsonArray();

			for (int i = 0; i < p_serialize_1_.getFrameCount(); ++i)
			{
				if (p_serialize_1_.frameHasTime(i))
				{
					JsonObject jsonobject1 = new JsonObject();
					jsonobject1.addProperty("index", Integer.valueOf(p_serialize_1_.getFrameIndex(i)));
					jsonobject1.addProperty("time", Integer.valueOf(p_serialize_1_.getFrameTimeSingle(i)));
					jsonarray.add(jsonobject1);
				}
				else
				{
					jsonarray.add(new JsonPrimitive(Integer.valueOf(p_serialize_1_.getFrameIndex(i))));
				}
			}

			jsonobject.add("frames", jsonarray);
		}

		return jsonobject;
	}

	public String getSectionName()
	{
		return "animation";
	}

	public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
	{
		return this.serialize((AnimationMetadataSection)p_serialize_1_, p_serialize_2_, p_serialize_3_);
	}
}