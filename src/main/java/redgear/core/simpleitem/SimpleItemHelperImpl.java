package redgear.core.simpleitem;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import redgear.core.api.item.ISimpleItem;
import redgear.core.asm.RedGearCore;
import redgear.core.mod.Mods;
import redgear.core.util.ItemRegUtil;
import redgear.core.util.SimpleItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class SimpleItemHelperImpl implements SimpleItemHelper {

	public ItemStack findStack(String mod, String name, int meta) {
		ItemStack value = findStack(mod, name);

		if (value != null)
			value.setItemDamage(meta);

		return value;
	}

	public ItemStack findStack(String mod, String name) {
		if (mod == null || name == null)
			return null;

		if (mod.equalsIgnoreCase("minecraft") || Loader.isModLoaded(mod)) {
			ItemStack stack = GameRegistry.findItemStack(mod, name, 1);
			if (stack != null && stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
				stack.setItemDamage(0);
			return stack;
		} else {
			RedGearCore.inst.logDebug("SimpleItemHelper: Mod: ", mod, " was not found, skipping item: ", name);
			return null;
		}
	}
	private final Gson gson = new GsonBuilder().registerTypeAdapter(ISimpleItem.class, new SimpleItemTypeAdapter()).setPrettyPrinting().create();

	public ISimpleItem parseFromJson(String name) throws JsonSyntaxException {
		if (name == null)
			return null;
		else
			return gson.fromJson(name, SimpleItemData.class).parse();
	}

	public String saveToJson(ISimpleItem item) {
		return gson.toJson(SimpleItemData.create(item));
	}

	public boolean isInOreDict(String name) {
		List<String> ores = Arrays.asList(OreDictionary.getOreNames());
		return ores.contains(name);
	}

	public ISimpleItem findItem(Mods mod, String name) {
		return findItem(mod, name);
	}

	public ISimpleItem findItem(Mods mod, String name, int meta) {
		return findItem(mod, name, meta);
	}

	public ISimpleItem findItem(String mod, String name) {
		return wrap(findStack(mod, name));
	}

	public ISimpleItem findItem(String mod, String name, int meta) {
		return new SimpleItem(findStack(mod, name, meta));
	}

	public ISimpleItem findItem(String name) {
		ISimpleItem ans = null;
		
		try{
			ans = parse(name);
		}catch(JsonSyntaxException e){
			ans = ItemRegUtil.findItem(name);
		}
		
		return ans;
	}

	public ISimpleItem wrap(ItemStack stack) {
		return new SimpleItem(stack);
	}

	public ISimpleItem parse(Object obj) {
		if (obj instanceof ISimpleItem)
			return (ISimpleItem) obj;

		if (obj instanceof Block)
			return new SimpleItem((Block) obj);

		if (obj instanceof Item)
			return new SimpleItem((Item) obj);

		if (obj instanceof ItemStack)
			return ItemRegUtil.wrap((ItemStack) obj);

		if (obj instanceof String) {
			ISimpleItem item = findItem((String) obj);
			if (item != null)
				return item;
		}

		return null;
	}

}
