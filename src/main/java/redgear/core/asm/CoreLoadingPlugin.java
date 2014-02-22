package redgear.core.asm;

import java.io.File;
import java.util.Map;

import redgear.core.mod.CoreModUtils;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions(value = {"redgear.core.asm", "redgear.core.mod" })
public class CoreLoadingPlugin implements IFMLLoadingPlugin {

	public static File mcLocation;
	public static CoreModUtils util;
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"redgear.core.asm.GlassAndIceTransformer", "redgear.core.asm.FiniteWaterTransformer",
				"redgear.core.asm.ChunkRelightTransformer", "redgear.core.asm.SafeCactusTransformer" };
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		mcLocation = (File) data.get("mcLocation");
		util = new CoreModUtils("RedGear|Transformer");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
