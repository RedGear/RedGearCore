package redgear.core.asm;

import java.io.File;
import java.util.Map;

import redgear.core.mod.CoreModUtils;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions(value={"redgear.core.asm", "redgear.core.mod"})
public class RedGearCoreLoadingPlugin implements IFMLLoadingPlugin
{
	public static CoreModUtils util;
    public static File mcLocation;

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[] {"redgear.core.asm.GlassAndIceTransformer", 
        		"redgear.core.asm.FiniteWaterTransformer", 
        		"redgear.core.asm.SnowTransformer", 
        		"redgear.core.asm.ChunkRelightTransformer",
        		"redgear.core.asm.MinableTransformer"};
    }

    @Override
    public String getModContainerClass()
    {
        return "redgear.core.asm.RedGearCore";
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data){
    	mcLocation = (File) data.get("mcLocation");
    	util = new CoreModUtils("RedGear|Transformer");
    }
}
