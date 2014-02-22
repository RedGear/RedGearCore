package redgear.core.mod;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redgear.core.asm.CoreLoadingPlugin;
import redgear.core.util.StringHelper;

/**
 * Performs many of the same functions as ModUtils, but is independent of the
 * Forge Configuration class,
 * meaning this can run during ASM transformations.
 * 
 * @author Blackhole
 */
public class CoreModUtils {
	public Logger myLogger;
	public boolean isDebugMode;

	private final CoreModConfiguration config;
	private boolean isConfigLoaded;

	/**
	 * Pass an instance of the ModContainer to use the default config and Logger
	 * settings
	 * 
	 * @param event
	 * @param itemIdDefault
	 * @param blockIdDefault
	 */
	public CoreModUtils(String modId) {
		this(StringHelper.parseConfigFile(new File(CoreLoadingPlugin.mcLocation, "config"), modId), LogManager
				.getLogger(modId));
	}

	/**
	 * Use this constructor if you want a different config file
	 * 
	 * @param configFile String of file location to create file
	 * @param logger The Logger this ModUtils should use
	 * @param itemIdDefault the default item id
	 * @param blockIdDefaultt the default block id
	 */
	public CoreModUtils(File configFile, Logger logger) {
		config = new CoreModConfiguration(configFile, this);
		myLogger = logger;
		isConfigLoaded = false;
		isDebugMode = getBoolean("debugMode", false);
		saveConfig();
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
		if (isConfigLoaded && config.hasChanged()) {
			config.save();
			isConfigLoaded = false;
		}
	}

	/**
	 * Returns boolean value form General category in config
	 * 
	 * @param name String name to use as key in config. Must be unique to file
	 * @param Default value to save in file if name is not found
	 * @return
	 */
	public boolean getBoolean(String name, boolean Default) {
		loadConfig();
		return config.getBoolean(name, Default);
	}

	/**
	 * Returns boolean value form General category in config
	 * 
	 * @param name String name to use as key in config. Must be unique to file
	 * @return
	 */
	public boolean getBoolean(String name) {
		return getBoolean(name, true);
	}

	public int getInt(String name, int Default) {
		loadConfig();
		return config.getInt(name, Default);
	}

	public void logDebug(String message) {
		if (isDebugMode)
			myLogger.log(Level.INFO, "DEBUG: " + message);
	}

	public void logDebug(String message, Exception e) {
		if (isDebugMode) {
			myLogger.log(Level.WARN, "DEBUG: " + message);
			e.printStackTrace();
		}
	}
}
