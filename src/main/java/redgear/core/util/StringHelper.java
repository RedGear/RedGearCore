package redgear.core.util;

import java.io.File;

import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.Loader;

public class StringHelper {
	
	public final static String slash = File.separator;

	public static String camelCase(String init){
		if (init==null)
	        return null;

	    final StringBuilder ret = new StringBuilder(init.length());

	    for (final String word : init.split(" ")) {
	        if (!word.isEmpty()) {
	            ret.append(word.substring(0, 1).toUpperCase());
	            ret.append(word.substring(1).toLowerCase());
	        }
	    }

	    return ret.toString();
	}
	
	public static String concat(Object... values){
		StringBuilder build = new StringBuilder();
		
		for(Object bit : values)
			build.append(bit);
		
		return build.toString();
	}
	
	public static String getModId(){
		return Loader.instance().activeModContainer().getModId();
	}
	
	public static String parseModAsset(){
		return parseModAsset(getModId());
	}
	
	public static String parseModAsset(String modName){
		return  concat(parseModName(modName), ":");
	}
	
	public static String parseModName(){
		return parseModName(getModId());
	}
	
	public static String parseModName(String modName){
		return modName.toLowerCase().replace('|', '_');
	}
	
	public static String parseLangFile(String lang){
		return parseLangFile(getModId(), lang);
	}
	
	public static String parseLangFile(String modId, String lang){
		return concat("/assets/", parseModName(modId), "/lang/", lang, ".xml");
	}	
	
	public static String parseTextureFile(String modId, String folder, String textureName){
		return concat("/assets/", parseModName(modId), "/", folder, "/", textureName, ".png");
	}
	
	public static File parseConfigFile(File configDir, String modID ){
    	return new File(configDir, modID.replace("|", slash) + ".cfg");
    }
	
	public static String parseUnLocalName(String name){
		return getModId().toLowerCase().replace('|', '.') + name;
	}
	
	public static ResourceLocation parseModelTexture(String modId, String textureName){
		return new ResourceLocation(parseModName(modId), concat("textures/models/", textureName, ".png"));
	}
}
