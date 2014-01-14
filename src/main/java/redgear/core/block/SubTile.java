package redgear.core.block;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubTile extends SubBlock implements IHasTile{
    public final boolean hasGui;
    public final int guiId;
    public final Class <? extends TileEntity > tile; 

    @Override
    public Class <? extends TileEntity > getTile(){
    	return tile;
    }
    
    @Override
    public boolean hasGui(){
    	return hasGui;
    }
	
    @Override
	public int guiId(){
    	return guiId;
    }
    
    public SubTile(String name, Class <? extends TileEntity > tile, int guiId){
    	super(name);

        if (guiId < 0)
            hasGui = false;
        else
            hasGui = true;

        this.guiId = guiId;
        
        this.tile = tile;
    }

    public SubTile(String name, Class <? extends TileEntity > tile){
        this(name, tile, -1);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(String modName, IconRegister par1IconRegister){
        this.blockIcon = par1IconRegister.registerIcon(modName + name);
    }
}
