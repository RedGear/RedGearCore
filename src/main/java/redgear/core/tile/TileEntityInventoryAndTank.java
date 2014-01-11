package redgear.core.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import redgear.core.fluids.AdvFluidTank;
import redgear.core.fluids.FluidUtil;
import redgear.core.render.LiquidGauge;

public abstract class TileEntityInventoryAndTank extends TileEntityInventory implements IFluidHandler
{
	private List<AdvFluidTank> tanks = new ArrayList<AdvFluidTank>();

    /**
     * Adds the given LiquidTank to this tile
     * @param newTank New Tank to add
     * @return index of the new tank used for adding side mappings
     */
    public int addTank(AdvFluidTank newTank, int x, int y, int width, int height){
        tanks.add(newTank.addLiquidGauge(x, y, width, height, tanks.size()));
        return tanks.size() - 1;
    }
    
    public int tanks(){
    	return tanks.size();
    }
    
    private boolean validTank(int index)
    {
        return index >= 0 && index < tanks.size() && tanks.get(index) != null;
    }

    public AdvFluidTank getTank(int index){
        if (validTank(index))
            return tanks.get(index);
        else
            return null;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill){
    	int filled = 0;
    	
    	for(AdvFluidTank tank : tanks){
    		filled = tank.fill(resource, doFill);
    		
    		if(filled > 0){
    			if(doFill)
    				onInventoryChanged();
    			return filled;
    		}
    	}
    	
    	return 0;
    }
    
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
    	FluidStack removed = null;
    	
    	for(AdvFluidTank tank : tanks){
    		if(tank.contains(resource.getFluid())){
	    		removed = tank.drain(resource.amount, doDrain);
	    		
	    		if(removed.amount > 0){
	    			if(doDrain)
	    				onInventoryChanged();
	    			return removed;
	    		}
    		}
    	}
    	
    	return null;
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
    	FluidStack removed = null;
    	
    	for(AdvFluidTank tank : tanks){
    		removed = tank.drain(maxDrain, doDrain);
    		
    		if(removed != null && removed.amount > 0){
    			if(doDrain)
    				onInventoryChanged();
    			return removed;
    		}
    	}
    	
    	return null;
	}

    /**
     * Don't forget to override this function in all children if you want more vars!
     */
    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        NBTTagList tankList = new NBTTagList();

        for (int i = 0; i < tanks.size(); i++)
        {
        	AdvFluidTank tank = getTank(i);

            if (tank != null)
            {
                NBTTagCompound invTag = new NBTTagCompound();
                invTag.setByte("tank", (byte) i);
                tankList.appendTag(tank.writeToNBT(invTag));
            }
        }

        tag.setTag("Tanks", tankList);
    }

    /**
     * Don't forget to override this function in all children if you want more vars!
     */
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        NBTTagList tagList = tag.getTagList("Tanks");

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound invTag = (NBTTagCompound) tagList.tagAt(i);
            byte slot = invTag.getByte("tank");
            getTank(slot).readFromNBT(invTag);
        }
    }
    
    protected void writeFluidStack(NBTTagCompound tag, String name, FluidStack stack){
    	if(stack == null)
    		return;
    	
    	tag.setTag(name, stack.writeToNBT(new NBTTagCompound()));
    }
    
    protected FluidStack readFluidStack(NBTTagCompound tag, String name){
    	NBTTagCompound subTag = tag.getCompoundTag(name);
    	
    	if(subTag == null)
    		return null;
    	
    	return FluidStack.loadFluidStackFromNBT(subTag);
    }
    
    protected boolean fillTank(int slotFullIndex, int slotEmptyIndex, AdvFluidTank tank){
    	ItemStack fullSlot = getStackInSlot(slotFullIndex);
    	
    	if(fullSlot == null || tank == null || !validSlot(slotEmptyIndex))
    		return false;

    	FluidStack contents = FluidContainerRegistry.getFluidForFilledItem(fullSlot);
    	ItemStack emptyContainer = FluidUtil.getEmptyContainer(fullSlot);

    	if (canFill(contents, tank) && canAddStack(slotEmptyIndex, emptyContainer)){
    		tank.fill(contents, true);
    		
    		if(emptyContainer != null)
    			addStack(slotEmptyIndex, emptyContainer);
    		
    		decrStackSize(slotFullIndex, 1);
    		return true;
    	}
    	
    	return false;
    }
    
    protected boolean fillTank(int slotFullIndex, int slotEmptyIndex, int tankIndex){
    	return fillTank(slotFullIndex, slotEmptyIndex, getTank(tankIndex));
    }
    
    protected boolean emptyTank(int slotEmptyIndex, int slotFullIndex, AdvFluidTank tank){
    	ItemStack emptySlot = getStackInSlot(slotEmptyIndex);
    	
    	if(emptySlot == null || tank == null || !validSlot(slotFullIndex))
    		return false;

    	FluidStack contents = tank.getFluid();
    	
    	if(emptySlot != null && contents != null){
	    	ItemStack filled = FluidContainerRegistry.fillFluidContainer(contents.copy(), emptySlot.copy());
	    	
	    	if (filled != null && canAddStack(slotFullIndex, filled)){
	    		addStack(slotFullIndex, filled);
	    		tank.drain(FluidUtil.getContainerCapacity(contents, emptySlot), true);
	    		decrStackSize(slotEmptyIndex, 1);
	    		return true;
	    	}
    	}
    	return false;
    }
    
    protected boolean emptyTank(int slotEmptyIndex, int slotFullIndex, int tankIndex){
    	return emptyTank(slotEmptyIndex, slotFullIndex, getTank(tankIndex));
    }


	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		FluidStack testStack = new FluidStack(fluid, 1);

		for(AdvFluidTank tank : tanks){
			if(testStack != null && tank.fill(testStack, false) > 0)
				return true;
		}
		return false;
	}
	
	public boolean canFill(ForgeDirection from, FluidStack contents, AdvFluidTank tank){
		return tank.fill(contents, false) == contents.amount;
	}
	
	public boolean canFill(FluidStack contents, AdvFluidTank tank){
		return contents != null && tank.fill(contents, false) == contents.amount;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		for(AdvFluidTank tank : tanks){
			if(tank.canEject(fluid.getID()))
				return true;
		}
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		FluidTankInfo[] info = new FluidTankInfo[tanks.size()];
		
		for(int x = 0; x < tanks.size(); x++)
			info[x] = tanks.get(x).getInfo();
		
		
		return info;
	}
	
	public LiquidGauge[] getLiquidGauges(){
		LiquidGauge[] gauges = new LiquidGauge[tanks.size()];
		
		for(int i = 0; i < tanks.size(); i++)
			gauges[i] = tanks.get(i).getLiquidGauge();
		
		return gauges;
	}
}
