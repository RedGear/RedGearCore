package redgear.core.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubItem
{
    /** Icon index in the icons table. */
	protected Icon itemIcon;
	protected final String name;
	protected final String displayName;

    public SubItem(String name, String displayName){
        this.name = name;
        this.displayName = displayName;
    }
    
    /**
     * Gets an icon index based on an item's damage value
     */
    public Icon getIcon(){
    	return itemIcon;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(String modName, IconRegister par1IconRegister){
        this.itemIcon = par1IconRegister.registerIcon(modName + name);
    }
}
