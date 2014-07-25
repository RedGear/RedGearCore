package redgear.core.block;

import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import redgear.core.block.SubBlockConnected;

public class SubBlockAntiConnected extends SubBlockConnected{
	
	protected final String texture;
	
	public SubBlockAntiConnected(String name, String texture) {
		super(name);
		this.texture = texture;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(String modName, IIconRegister par1IconRegister) {
		for(int i = 0; i < 16; i++)
			icons[i] = par1IconRegister.registerIcon(modName + texture + i);
	}

}
