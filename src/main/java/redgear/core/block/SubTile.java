package redgear.core.block;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import redgear.core.tile.ITileFactory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubTile extends SubBlock implements IHasTile {
	public final ITileFactory factory;

	@Override
	public TileEntity createTile() {
		return factory.createTile();
	}

	@Override
	public boolean hasGui() {
		return factory.hasGui();
	}

	@Override
	public int guiId() {
		return factory.guiId();
	}

	public SubTile(String name, ITileFactory factory) {
		super(name);
		this.factory = factory;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(String modName, IIconRegister par1IconRegister) {
		blockIcon = par1IconRegister.registerIcon(modName + name);
	}
}
