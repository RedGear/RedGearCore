package redgear.core.simpleitem;

import java.lang.reflect.Type;

import redgear.core.api.item.ISimpleItem;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SimpleItemTypeAdapter implements JsonSerializer<ISimpleItem>, JsonDeserializer<ISimpleItem> {
	
	@Override
	public JsonElement serialize(ISimpleItem src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(SimpleItemData.create(src), SimpleItemData.class);
	}

	@Override
	public ISimpleItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		SimpleItemData item = context.deserialize(json, SimpleItemData.class);
		
		return item.parse();
	}
}
