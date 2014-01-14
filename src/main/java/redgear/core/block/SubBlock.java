package redgear.core.block;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubBlock {
	
	/** Icon index in the icons table. */
    protected Icon blockIcon;
    public final String name;
    
    public SubBlock(String name){
        this.name = name;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(String modName, IconRegister par1IconRegister){
        this.blockIcon = par1IconRegister.registerIcon(modName + name);
    }
    
    @SideOnly(Side.CLIENT)

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side){
        return this.getIcon(side);
    }
    
    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int side){
    	return this.blockIcon;
    }
}
