package redgear.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import redgear.core.util.StringHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * This class is great for blocks that don't do anything special, or a a parent for those that do
 * @author Blackhole
 *
 */
public class BlockGeneric extends Block
{
    protected final String name;
    protected final String modName;

    /**
     * Use this if your item has a typical icon
     * @param Id The typical ItemId
     * @param name Name of item's icon
     */
    public BlockGeneric(int Id, Material material, String name){
    	super(Id, material);
    	this.name = name;
    	this.modName = StringHelper.parseModAsset();
    }

    /**
     * Override this function if your item has an unusual icon/multiple icons
     */
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon(modName + name);
    }
}
