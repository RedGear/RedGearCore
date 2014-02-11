package redgear.core.mod;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.Logger;

import redgear.core.asm.RedGearCore;
import redgear.core.util.CoreLocalization;
import redgear.core.util.SimpleItem;
import redgear.core.util.StringHelper;
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
	private final ArrayList<IPlugin> plugins = new ArrayList<IPlugin>();

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

		addPlugin(new CoreLocalization());
	}

	public void PreInitialization(FMLPreInitializationEvent event) {
		configFile = StringHelper.parseConfigFile(event.getModConfigurationDirectory(), event.getModMetadata().modId);
		config = new Configuration(configFile);
		myLogger = event.getModLog();
		isConfigLoaded = false;
		isDebugMode = getBoolean("debugMode", false);

		PreInit(event);

		for (IPlugin bit : plugins)
			bit.preInit(this);

		saveConfig();
	}

	protected abstract void PreInit(FMLPreInitializationEvent event);

	public void Initialization(FMLInitializationEvent event) {
		Init(event);

		for (IPlugin bit : plugins)
			bit.Init(this);

		saveConfig();
	}

	protected abstract void Init(FMLInitializationEvent event);

	public void PostInitialization(FMLPostInitializationEvent event) {
		PostInit(event);

		for (IPlugin bit : plugins)
			bit.postInit(this);

		saveConfig();
	}

	protected abstract void PostInit(FMLPostInitializationEvent event);

	public void addPlugin(IPlugin add) {
		plugins.add(add);
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

	public void logDebug(String message) {
		if (isDebugMode)
			myLogger.info("DEBUG: " + message);
	}

	public void logDebug(Object... message) {
		if (isDebugMode)
			logDebug(StringHelper.concat(message));
	}

	public void logDebug(String message, Exception e) {
		if (isDebugMode) {
			myLogger.warn("DEBUG: " + message);
			e.printStackTrace();
		}
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
}
