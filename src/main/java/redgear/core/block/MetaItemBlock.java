package redgear.core.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import redgear.core.util.StringHelper;

public class MetaItemBlock extends ItemBlock {
    private final MetaBlock myContainer;

    public MetaItemBlock(Block block){
        super(block);
        setHasSubtypes(true);
        myContainer = (MetaBlock) block;
    }
    @Override
    public int getMetadata(int damageValue){
        return damageValue;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack){
    	if(itemstack != null && myContainer.indexCheck(itemstack.getItemDamage()))
    		return StringHelper.concat("tile.", myContainer.name, ".", myContainer.getMetaBlock(itemstack.getItemDamage()).name);
    	else
    		return StringHelper.concat("tile.", myContainer.name, ".", myContainer.getMetaBlock(0).name);
    }
}
