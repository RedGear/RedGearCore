package redgear.core.mod;

import cpw.mods.fml.common.LoaderState.ModState;

public interface IPlugin {

	/**
	 * @return A human-readable name for this plugin. Used for crash logs if the
	 * plugin throws an exception.
	 */
	String getName();

	/**
	 * @param state The current FML loader state.
	 * @return True if this plugin should be run for the coming state. You can
	 * use this to check if prerequiste mods are installed or just return false
	 * for states that are unusued. The calling mod is provided in case this 
	 * plugin needs access to the config.
	 */
	boolean shouldRun(ModUtils mod, ModState state);

	/**
	 * Is the plugin required for this mod to function?
	 * 
	 * @return true if the mod cannot run without this plugin, and any
	 * exceptions caught by it should crash the game, false if exceptions should
	 * be logged, but allow the game to load anyway, for example, a plugin
	 * designed for mod compatibility should return false and will allow the
	 * game to load even if something fails.
	 */
	boolean isRequired();

	/**
	 * The preinit phase for this plugin. Runs after the mods own preinit phase. 
	 * @param mod
	 */
	void preInit(ModUtils mod);

	/**
	 * The init phase for this plugin. Runs after the mods own init phase. 
	 * @param mod
	 */
	void Init(ModUtils mod);

	/**
	 * The postinit phase for this plugin. Runs after the mods own postinit phase. 
	 * @param mod
	 */
	void postInit(ModUtils mod);



}
