package redgear.core.item;

import java.util.Map.Entry;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import redgear.core.util.SimpleItem;
import redgear.core.world.Location;

public class MetaItemBucket extends MetaItem {
	
	public MetaItemBucket(int par1, String name) {
		super(par1, name);
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabMisc);
        MinecraftForge.EVENT_BUS.register(this);
    }
	
	
	public SimpleItem addMetaItem(SubItemBucket newItem){
		SimpleItem temp = super.addMetaItem(newItem);
		FluidContainerRegistry.registerFluidContainer(newItem.fluid, temp.getStack(), FluidContainerRegistry.EMPTY_BUCKET);
		return temp;
	}
	
	@Override
	public SimpleItem addMetaItem(SubItem newItem){
		if(!(newItem instanceof SubItemBucket))
    		throw new ClassCastException("MetaBucketContainer can only except MetaBucket!");
        return addMetaItem((SubItemBucket) newItem);
	}
	
	@ForgeSubscribe
	public void onBucketFill( FillBucketEvent event ){
		Location loc = new Location(event.target);
		
		int id = loc.getBlockId(event.world);
		int meta = getMeta(id);
		
		if (meta > -1)
			if (loc.getBlockMeta(event.world) == 0 ){ // Check that it is a source block
				loc.placeBlock(event.world, new SimpleItem(0, 0));; // Remove the fluid block
				
				event.result =  new ItemStack(itemID, 1, meta);
				event.setResult(Result.ALLOW);
			}
	}
 
 	private int getMeta(int blockId){
 		for(Entry<Integer, SubItem> set : items.entrySet())
 			if(((SubItemBucket)set.getValue()).fluid.getBlockID() == blockId)
 				return set.getKey();
 		return -1;
 	}

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer){
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, false);

        if (movingobjectposition == null)
            return par1ItemStack;
        else{
            FillBucketEvent event = new FillBucketEvent(par3EntityPlayer, par1ItemStack, par2World, movingobjectposition);
            if (MinecraftForge.EVENT_BUS.post(event))
                return par1ItemStack;

            if (event.getResult() == Event.Result.ALLOW){
                if (par3EntityPlayer.capabilities.isCreativeMode)
                    return par1ItemStack;

                if (--par1ItemStack.stackSize <= 0)
                    return event.result;

                if (!par3EntityPlayer.inventory.addItemStackToInventory(event.result))
                    par3EntityPlayer.dropPlayerItem(event.result);

                return par1ItemStack;
            }

            if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE){
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                if (!par2World.canMineBlock(par3EntityPlayer, i, j, k))
                    return par1ItemStack;

                if (movingobjectposition.sideHit == 0)
                    --j;

                if (movingobjectposition.sideHit == 1)
                    ++j;

                if (movingobjectposition.sideHit == 2)
                    --k;

                if (movingobjectposition.sideHit == 3)
                    ++k;

                if (movingobjectposition.sideHit == 4)
                    --i;

                if (movingobjectposition.sideHit == 5)
                    ++i;

                if (!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, par1ItemStack))
                    return par1ItemStack;

                if (this.tryPlaceContainedLiquid(par2World, par1ItemStack, i, j, k) && !par3EntityPlayer.capabilities.isCreativeMode)
                    return new ItemStack(Item.bucketEmpty);
            }
            return par1ItemStack;
        }
    }

    /**
     * Attempts to place the liquid contained inside the bucket.
     */
    public boolean tryPlaceContainedLiquid(World par1World, ItemStack bucket, int par2, int par3, int par4){
	    Material material = par1World.getBlockMaterial(par2, par3, par4);
	    boolean flag = !material.isSolid();
	
	    if (!par1World.isAirBlock(par2, par3, par4) && !flag)
	        return false;
	    else{
            if (!par1World.isRemote && flag && !material.isLiquid())
                par1World.destroyBlock(par2, par3, par4, true);

            Fluid working = ((SubItemBucket)getMetaItem(bucket.getItemDamage())).fluid;
            
            if(working == null || working.getBlockID() == -1)
            	return false;
            else
            	par1World.setBlock(par2, par3, par4, working.getBlockID(), 0, 3);
	        return true;
	    } 
    }
}
