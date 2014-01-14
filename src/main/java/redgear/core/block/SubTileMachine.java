package redgear.core.block;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import redgear.core.tile.TileEntityGeneric;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubTileMachine extends SubTile {
	
	protected Icon sideIcon;
	protected final String sideName;

	public SubTileMachine(String name, String sideName, Class <? extends TileEntity > tile, int guiId){
        super(name, tile, guiId);
        this.sideName = sideName;
    }

    public SubTileMachine(String name, String sideName, Class <? extends TileEntity > tile){
        this(name, sideName, tile, -1);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(String modName, IconRegister par1IconRegister){
    	super.registerIcons(modName, par1IconRegister);
        this.sideIcon = par1IconRegister.registerIcon(modName + sideName);
    }
	
    @Override
	@SideOnly(Side.CLIENT)

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side){
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        int direction = 3; //3 because that's the face when it's in your inventory. I think it's South. (not sure)
        		
        if(tile instanceof TileEntityGeneric)
        	direction = ((TileEntityGeneric)tile).getDirectionId();
        
        return side == direction ? blockIcon : sideIcon;
    }
	
    @Override
	@SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int side)// called when block is in inventory
    {
        return side == 3 ? this.blockIcon : sideIcon;//3 because that's the face when it's in your inventory. I think it's South. (not sure)
    }
}
