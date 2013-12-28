package redgear.core.mod;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import redgear.core.asm.RedGearCoreLoadingPlugin;
import redgear.core.util.StringHelper;
import cpw.mods.fml.common.FMLLog;

/**
 * Performs many of the same functions as ModUtils, but is independent of the Forge Configuration class,
 * meaning this can run during ASM transformations.
 * @author Blackhole
 */
public class CoreModUtils {
	public Logger myLogger;
    public boolean isDebugMode;

    private CoreModConfiguration config;
    private boolean isConfigLoaded;
    private int itemIdDefault;
    private int blockIdDefault;

    /**
     * Pass an instance of the ModContainer to use the default config and Logger settings
     * @param event
     * @param itemIdDefault
     * @param blockIdDefault
     */
    public CoreModUtils(String modId, int itemIdDefault, int blockIdDefault){
        this(StringHelper.parseConfigFile(new File(RedGearCoreLoadingPlugin.mcLocation, "config"), modId), Logger.getLogger(modId), itemIdDefault, blockIdDefault);
    }

    /**
     * Use this constructor if you want a different config file
     * @param configFile String of file location to create file
     * @param logger The Logger this ModUtils should use
     * @param itemIdDefault the default item id
     * @param blockIdDefaultt the default block id
     */
    public CoreModUtils(File configFile, Logger logger, int itemIdDefault, int blockIdDefault)
    {
    	config = new CoreModConfiguration(configFile, this);
        myLogger = logger;
        myLogger.setParent(FMLLog.getLogger());
        isConfigLoaded = false;
        isDebugMode = getBoolean("debugMode", false);
        saveConfig();
        this.itemIdDefault = itemIdDefault;
        this.blockIdDefault = blockIdDefault;
    }

    /**
     * Pass an instance of the ModContainer to use the default config and Logger settings
     * DO NOT USE THIS CONSTRUCTOR IF YOU WANT TO USE THE ITEM OR BLOCK CONFIG FUNCTIONS!
     * @param event
     */
    public CoreModUtils(String modId){
        this(modId, 0, 0);
    }

    /**
     * Loads up the config if needed. You do not need to call this, it is called by the functions that need it
     */
    public void loadConfig(){
        if (!isConfigLoaded)
        {
            config.load();
            isConfigLoaded = true;
        }
    }

    /**
     * Saves the config if it is open. YOU MUST CALL THIS TO SAVE ANY CHANGES TO THE CONFIG!!!!
     */
    public void saveConfig(){
        if (isConfigLoaded && config.hasChanged())
        {
            config.save();
            isConfigLoaded = false;
        }
    }

    /**
     * Returns boolean value form General category in config
     * @param name String name to use as key in config. Must be unique to file
     * @param Default value to save in file if name is not found
     * @return
     */
    public boolean getBoolean(String name, boolean Default){
        loadConfig();
        return config.getBoolean(name, Default);
    }

    /**
     * Returns boolean value form General category in config
     * @param name String name to use as key in config. Must be unique to file
     * @return
     */
    public boolean getBoolean(String name)
    {
        return getBoolean(name, true);
    }

    public int getItemId(String name){
    	if(itemIdDefault > 0)
    		return getInt(name, itemIdDefault++);
    	else
    		return 0;
    }

    public int getBlockId(String name){
    	if(blockIdDefault > 0)
    		return getInt(name, blockIdDefault++);
    	else
    		return 0;
    }
    
    public int getInt(String name, int Default){
    	loadConfig();
    	return config.getInt(name, Default);
    }

    public void logDebug(String message){
        if (isDebugMode)
            myLogger.log(Level.INFO, "DEBUG: " + message);
    }
    
    public void logDebug(String message, Exception e){
    	if (isDebugMode){
            myLogger.log(Level.WARNING, "DEBUG: " + message);
            e.printStackTrace();
        }
    }
}
