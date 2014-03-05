package redgear.core.mod;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.Logger;

import redgear.core.api.util.ReflectionHelper;
import redgear.core.asm.RedGearCore;
import redgear.core.util.SimpleItem;
import redgear.core.util.StringHelper;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

public abstract class ModUtils {
	public Logger myLogger;
	public boolean isDebugMode;

	private Configuration config;
	private File configFile;
	private boolean isConfigLoaded;
	private final List<IPlugin> plugins = new LinkedList<IPlugin>();

	public final String modId;
	public final String modName;
	public final String modVersion;
	public final String modDepend;

	public ModUtils() {

		String modId = "Unknown";
		String modName = "Unknown";
		String modVersion = "Unknown";
		String modDepend = "Unknown";

		for (Annotation a : this.getClass().getDeclaredAnnotations())
			if (a instanceof Mod) {
				Mod m = (Mod) a;
				modId = m.modid();
				modName = m.name();
				modVersion = m.version();
				modDepend = m.dependencies();
				break;
			}

		this.modId = modId;
		this.modName = modName;
		this.modVersion = modVersion;
		this.modDepend = modDepend;
	}

	public void PreInitialization(FMLPreInitializationEvent event) {
		configFile = StringHelper.parseConfigFile(event.getModConfigurationDirectory());
		config = new Configuration(configFile);
		myLogger = event.getModLog();
		isConfigLoaded = false;
		isDebugMode = getBoolean("debugMode", false);

		try {
			try {
				PreInit(event);
			} catch (Throwable e) {
				throwFatal("PreInitialization", e);
			}

			for (IPlugin bit : plugins)
				try {
					if(bit.shouldRun(this, ModState.PREINITIALIZED))
						bit.preInit(this);
				} catch (Throwable e) {
					throwPlugin("PreInitialization", bit, e);
				}
		} finally { //Save the config. No matter what. 
			saveConfig();
		}
	}

	protected abstract void PreInit(FMLPreInitializationEvent event);

	public void Initialization(FMLInitializationEvent event) {
		try {
			try {
				Init(event);
			} catch (Throwable e) {
				throwFatal("Initialization", e);
			}

			for (IPlugin bit : plugins)
				try {
					if(bit.shouldRun(this, ModState.INITIALIZED))
						bit.Init(this);
				} catch (Throwable e) {
					throwPlugin("Initialization", bit, e);
				}
		} finally { //Save the config. No matter what. 
			saveConfig();
		}
	}

	protected abstract void Init(FMLInitializationEvent event);

	public void PostInitialization(FMLPostInitializationEvent event) {
		try {
			try {
				PostInit(event);
			} catch (Throwable e) {
				throwFatal("PostInitialization", e);
			}

			for (IPlugin bit : plugins)
				try {
					if(bit.shouldRun(this, ModState.POSTINITIALIZED))
						bit.postInit(this);
				} catch (Throwable e) {
					throwPlugin("PostInitialization", bit, e);
				}
		} finally { //Save the config. No matter what. 
			saveConfig();
		}
		
		plugins.clear(); //Clear the plugins because they aren't needed anymore. Let them be garbage collected. 
	}

	protected abstract void PostInit(FMLPostInitializationEvent event);

	public void addPlugin(IPlugin add) {
		if(add != null)
			plugins.add(add);
	}
	
	public void addPlugin(IPlugin add, Side side) {
		if(side == this.getSide())
			addPlugin(add);
	}
	
	public void addPlugin(String add, Side side, Object[]...params) {
		if(side == this.getSide()){
			Object obj = ReflectionHelper.constructObjectNullFail(add, (Object[])params);
			if(obj instanceof IPlugin)
				addPlugin((IPlugin) obj);
		}
	}

	public File getConfigDirectory() {
		return configFile.getParentFile();
	}

	/**
	 * Loads up the config if needed. You do not need to call this, it is called
	 * by the functions that need it
	 */
	public void loadConfig() {
		if (!isConfigLoaded) {
			config.load();
			isConfigLoaded = true;
		}
	}

	/**
	 * Saves the config if it is open. YOU MUST CALL THIS TO SAVE ANY CHANGES TO
	 * THE CONFIG!!!!
	 */
	public void saveConfig() {
		if (isConfigLoaded && config.hasChanged())
			config.save();
		//isConfigLoaded = false;
	}

	public Configuration getConfig() {
		return config;
	}

	public void set(String category, String key, String value) {
		if (value == null || value.length() < 1)
			return;
		String old = getString(category, key, null, value);

		if (old != value)
			config.getCategory(category).get(key).set(value);
	}

	public void set(String category, String key, double value) {
		double old = getDouble(category, key, value);

		if (old != value)
			config.getCategory(category).get(key).set(value);
	}

	public void set(String category, String key, int value) {

		int old = getInt(category, key, value);

		if (old != value)
			config.getCategory(category).get(key).set(value);
	}

	public void set(String category, String key, boolean value) {
		boolean old = getBoolean(category, key, value);

		if (old != value)
			config.getCategory(category).get(key).set(value);
	}

	/**
	 * Returns boolean value from General category in config
	 * 
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @return
	 */
	public String getString(String name) {
		return getString(name, "");
	}

	/**
	 * Returns boolean value from the given category in config
	 * 
	 * @param category Name of the category to use. Must be unique to file
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @return
	 */
	public String getString(String category, String name) {
		return getString(category, name, "");
	}

	/**
	 * Returns boolean value from the given category in config
	 * 
	 * @param category Name of the category to use. Must be unique to file
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @param comment String comment to be saved in file
	 * @return
	 */
	public String getString(String category, String name, String comment) {
		return getString(category, name, comment, "");
	}

	/**
	 * Returns boolean value from the given category in config
	 * 
	 * @param category Name of the category to use. Must be unique to file
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @param comment String comment to be saved in file
	 * @param Default value to save in file if name is not found
	 * @return
	 */
	public String getString(String category, String name, String comment, String Default) {
		loadConfig();

		Property prop = config.get(category, name, Default);

		if (prop == null)
			return Default;

		if (comment != null && comment.length() > 0)
			prop.comment = comment;
		return prop.getString();
	}

	/**
	 * Returns boolean value from General category in config
	 * 
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @param Default value to save in file if name is not found
	 * @return
	 */
	public double getDouble(String name, double Default) {
		return getDouble(Configuration.CATEGORY_GENERAL, name, Default);
	}

	/**
	 * Returns boolean value from General category in config
	 * 
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @return
	 */
	public double getDouble(String name) {
		return getDouble(name, 0);
	}

	/**
	 * Returns boolean value from the given category in config
	 * 
	 * @param category Name of the category to use. Must be unique to file
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @return
	 */
	public double getDouble(String category, String name) {
		return getDouble(category, name, 0);
	}

	/**
	 * Returns boolean value from the given category in config
	 * 
	 * @param category Name of the category to use. Must be unique to file
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @param Default value to save in file if name is not found
	 * @return
	 */
	public double getDouble(String category, String name, double Default) {
		return getDouble(category, name, null, Default);
	}

	/**
	 * Returns boolean value from the given category in config
	 * 
	 * @param category Name of the category to use. Must be unique to file
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @param comment String comment to be saved in file
	 * @return
	 */
	public double getDouble(String category, String name, String comment) {
		return getDouble(category, name, comment, 0);
	}

	/**
	 * Returns boolean value from the given category in config
	 * 
	 * @param category Name of the category to use. Must be unique to file
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @param comment String comment to be saved in file
	 * @param Default value to save in file if name is not found
	 * @return
	 */
	public double getDouble(String category, String name, String comment, double Default) {
		loadConfig();

		Property prop = config.get(category, name, Default);
		if (comment != null)
			prop.comment = comment;
		return prop.getDouble(Default);
	}

	/**
	 * Returns boolean value from General category in config
	 * 
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @param Default value to save in file if name is not found
	 * @return
	 */
	public int getInt(String name, int Default) {
		return getInt(Configuration.CATEGORY_GENERAL, name, Default);
	}

	/**
	 * Returns boolean value from General category in config
	 * 
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @return
	 */
	public int getInt(String name) {
		return getInt(name, 0);
	}

	/**
	 * Returns boolean value from the given category in config
	 * 
	 * @param category Name of the category to use. Must be unique to file
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @return
	 */
	public int getInt(String category, String name) {
		return getInt(category, name, 0);
	}

	/**
	 * Returns boolean value from the given category in config
	 * 
	 * @param category Name of the category to use. Must be unique to file
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @param Default value to save in file if name is not found
	 * @return
	 */
	public int getInt(String category, String name, int Default) {
		return getInt(category, name, null, Default);
	}

	/**
	 * Returns boolean value from the given category in config
	 * 
	 * @param category Name of the category to use. Must be unique to file
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @param comment String comment to be saved in file
	 * @return
	 */
	public int getInt(String category, String name, String comment) {
		return getInt(category, name, comment, 0);
	}

	/**
	 * Returns boolean value from the given category in config
	 * 
	 * @param category Name of the category to use. Must be unique to file
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @param comment String comment to be saved in file
	 * @param Default value to save in file if name is not found
	 * @return
	 */
	public int getInt(String category, String name, String comment, int Default) {
		loadConfig();

		Property prop = config.get(category, name, Default);
		if (comment != null)
			prop.comment = comment;
		return prop.getInt(Default);
	}

	/**
	 * Returns boolean value from General category in config
	 * 
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @param Default value to save in file if name is not found
	 * @return
	 */
	public boolean getBoolean(String name, boolean Default) {
		return getBoolean(Configuration.CATEGORY_GENERAL, name, Default);
	}

	/**
	 * Returns boolean value from General category in config
	 * 
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @return
	 */
	public boolean getBoolean(String name) {
		return getBoolean(name, true);
	}

	/**
	 * Returns boolean value from the given category in config
	 * 
	 * @param category Name of the category to use. Must be unique to file
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @return
	 */
	public boolean getBoolean(String category, String name) {
		return getBoolean(category, name, true);
	}

	/**
	 * Returns boolean value from the given category in config
	 * 
	 * @param category Name of the category to use. Must be unique to file
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @param Default value to save in file if name is not found
	 * @return
	 */
	public boolean getBoolean(String category, String name, boolean Default) {
		return getBoolean(category, name, null, Default);
	}

	/**
	 * Returns boolean value from the given category in config
	 * 
	 * @param category Name of the category to use. Must be unique to file
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @param comment String comment to be saved in file
	 * @return
	 */
	public boolean getBoolean(String category, String name, String comment) {
		return getBoolean(category, name, comment, true);
	}

	/**
	 * Returns boolean value from the given category in config
	 * 
	 * @param category Name of the category to use. Must be unique to file
	 * @param name String name to use as key in config. Must be unique to
	 * category
	 * @param comment String comment to be saved in file
	 * @param Default value to save in file if name is not found
	 * @return
	 */
	public boolean getBoolean(String category, String name, String comment, boolean Default) {
		loadConfig();

		Property prop = config.get(category, name, Default);
		if (comment != null)
			prop.comment = comment;
		return prop.getBoolean(Default);
	}
	
	public void addSmelting(SimpleItem input, SimpleItem result) {
		this.addSmelting(input.getStack(), result.getStack());
	}

	public void addSmelting(Item input, ItemStack result) {
		addSmelting(new ItemStack(input), result);
	}

	public void addSmelting(Block input, ItemStack result) {
		addSmelting(new ItemStack(input), result);
	}

	public void addSmelting(ItemStack input, ItemStack result) {
		addSmelting(input, result, 0F);
	}

	public void addSmelting(ItemStack input, ItemStack result, float exp) {
		FurnaceRecipes.smelting().func_151394_a(input, result, exp);
	}

	public void registerOre(String name, SimpleItem ore) {
		registerOre(name, ore.getStack());
	}

	public void registerOre(String name, ItemStack ore) {
		OreDictionary.registerOre(name, ore);
	}
	
	public boolean inOreDict(String ore) {
		return !OreDictionary.getOres(ore).isEmpty();
	}

	public void logDebug(String message) {
		if (isDebugMode)
			myLogger.info("DEBUG: " + message);
	}

	public void logDebug(Object... message) {
		if (isDebugMode)
			logDebug(StringHelper.concat(message));
	}

	public void logDebug(String message, Throwable e) {
		if (isDebugMode)
			logWarning("DEBUG: " + message, e);
	}

	public void logWarning(String message, Throwable e) {
		myLogger.warn(message);
		e.printStackTrace();
	}

	public Side getSide() {
		return RedGearCore.proxy.getSide();
	}

	public boolean isServer() {
		return getSide() == Side.SERVER;
	}

	public boolean isClient() {
		return getSide() == Side.CLIENT;
	}

	private void throwFatal(String phase, Throwable e) {
		throw new RuntimeException(StringHelper.concat(modName, " crashed during the ", phase, " phase. "), e);
	}

	private void throwPlugin(String phase, IPlugin plug, Throwable e) {
		if (plug.isRequired())
			throw new RuntimeException(StringHelper.concat(modName, " crashed during the ", phase,
					" phase while attempting to run plugin: ", plug.getName(), "."), e);
		else
			logWarning(StringHelper.concat("Plugin ", plug.getName(), " failed during the ", phase, "."), e);
	}
}
