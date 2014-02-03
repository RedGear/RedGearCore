package redgear.core.network;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Use this as a simple way to access and store Icons, particularly
 * for things that can't use normal Block or Icon textures.
 * 
 * @author Blackhole
 */
@SideOnly(Side.CLIENT)
public class CoreIconRegistry {

	private static CoreIconRegistry instance;

	private final HashMap<String, Icon> icons = new HashMap<String, Icon>();
	private final HashMap<Fluid, String> fluids = new HashMap<Fluid, String>();
	private static final String flow = "_flow";
	private static final String still = "_still";

	private CoreIconRegistry() {
	}

	static CoreIconRegistry init() {
		if (instance == null) {
			instance = new CoreIconRegistry();
			MinecraftForge.EVENT_BUS.register(instance);
		}
		return instance;
	}

	

	@ForgeSubscribe
	public void registerIcons(TextureStitchEvent.Pre event) {
		IconRegister reg = event.map;

		for (Entry<String, Icon> set : icons.entrySet())
			set.setValue(reg.registerIcon(set.getKey()));

	}

	@ForgeSubscribe
	public void registerFluidIcons(TextureStitchEvent.Post event) {
		for (Entry<Fluid, String> set : fluids.entrySet())
			set.getKey().setIcons(getIcon(set.getValue() + still), getIcon(set.getValue() + flow));
	}

	/**
	 * Add an icon to the registry
	 * 
	 * @param name The FULL path of the icon, IE:
	 * assets/mod_name/textures/gui/button
	 */
	void addIcon(String name) {
		icons.put(name, null);
	}

	Icon getIcon(String name) {
		return icons.get(name);
	}

	void addFluid(String iconName, Fluid fluid) {
		addIcon(iconName + flow);
		addIcon(iconName + still);
		fluids.put(fluid, iconName);
	}

}