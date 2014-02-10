package redgear.core.block;

import redgear.core.util.StringHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

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
        return StringHelper.concat("tile.", myContainer.name, ".", myContainer.getMetaBlock(itemstack.getItemDamage()).name);
    }
}
