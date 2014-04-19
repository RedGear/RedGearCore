package redgear.core.network;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Use this as a simple way to access and store Icons, particularly
 * for things that can't use normal Block or Item textures.
 *
 * @author Blackhole
 */
@SideOnly(Side.CLIENT)
public class CoreIconRegistry {

	private static CoreIconRegistry instance;

	private final HashMap<String, IIcon> icons = new HashMap<String, IIcon>();
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

	@SubscribeEvent
	public void registerIcons(TextureStitchEvent.Pre event) {
		TextureMap reg = event.map;

		if (reg.getTextureType() == 0)
			for (Entry<String, IIcon> set : icons.entrySet())
				set.setValue(reg.registerIcon(set.getKey()));

	}

	@SubscribeEvent
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

	IIcon getIcon(String name) {
		return icons.get(name);
	}

	void addFluid(String iconName, Fluid fluid) {
		addIcon(iconName + flow);
		addIcon(iconName + still);
		fluids.put(fluid, iconName);
	}

}
