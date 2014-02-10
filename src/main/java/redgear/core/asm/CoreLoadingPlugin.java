package redgear.core.asm;

import java.io.File;
import java.util.Map;

import redgear.core.mod.CoreModUtils;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class CoreLoadingPlugin implements IFMLLoadingPlugin {

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"redgear.core.asm.GlassAndIceTransformer", "redgear.core.asm.FiniteWaterTransformer",
				"redgear.core.asm.ChunkRelightTransformer" };
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
		RedGearCore.mcLocation = (File) data.get("mcLocation");
		RedGearCore.util = new CoreModUtils("RedGear|Transformer");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
