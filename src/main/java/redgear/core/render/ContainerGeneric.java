package redgear.core.render;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import redgear.core.asm.RedGearCore;
import redgear.core.fluids.AdvFluidTank;
import redgear.core.inventory.InvSlot;
import redgear.core.tile.TileEntityGeneric;
import redgear.core.tile.TileEntityInventory;
import redgear.core.tile.TileEntityTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerGeneric extends Container
{
    public final TileEntity myTile;
    public final int playerInvHeight;
    protected List<String> dataMap = new ArrayList();
    public List<LiquidGauge> liquidGauges = new ArrayList();
    public List<ProgressBar> progressBars = new ArrayList();
    public List<Button> buttons = new ArrayList();

    public ContainerGeneric(InventoryPlayer inventoryPlayer, TileEntity tile, int playerInvHeight){
        myTile = tile;
        this.playerInvHeight = playerInvHeight;
        //commonly used vanilla code that adds the player's inventory
        bindPlayerInventory(inventoryPlayer);
        
        if(myTile instanceof TileEntityInventory)
        	addSlots((TileEntityInventory) myTile);
        
        if(myTile instanceof TileEntityTank)
        	addTanks((TileEntityTank) myTile);
        
        if(myTile instanceof TileEntityGeneric){
        	addProgressBars((TileEntityGeneric) myTile);
        	addButtons((TileEntityGeneric) myTile);
        }
    }

    public ContainerGeneric(InventoryPlayer inventoryPlayer, TileEntity tile)
    {
        this(inventoryPlayer, tile, 84);
    }
    
    protected void addSlots(TileEntityInventory tile){
    	for(InvSlot slot : tile.getSlots())
    		addSlotToContainer(slot); 
    }
    
    protected void addTanks(TileEntityTank tile){
    	for(LiquidGauge gauge : tile.getLiquidGauges()){
    		liquidGauges.add(gauge);
    		dataMap.add("tankId" + gauge.tankId);
        	dataMap.add("tankAmount" + gauge.tankId);
    	}
    }
    
    protected void addProgressBars(TileEntityGeneric tile){
    	for(ProgressBar bar : tile.getProgressBars()){
    		progressBars.add(bar);
    		dataMap.add("progTot" + bar.id);
        	dataMap.add("progVal" + bar.id);
    	}
    }
    
    protected void addButtons(TileEntityGeneric tile){
    	for(Button button : tile.getButtons()){
    		buttons.add(button);
    		dataMap.add("button" + button.id);
    	}
    }
    
    /*protected void addLiquidGuage(int tankCapacity, int x, int y, int width, int height, int tankId){
    	liquidGauges.add(new LiquidGauge(tankCapacity, x, y, width, height, tankId));
    	dataMap.add("tankId" + tankId);
    	dataMap.add("tankAmount" + tankId);
    }
    
    protected int addProgressBar(int x, int y, int width, int height){
    	int id = progressBars.size();
    	progressBars.add(new ProgressBar(id, x, y, width, height));
    	dataMap.add("progTot" + id);
    	dataMap.add("progVal" + id);
    	return id;
    }
    
    protected void addItemSlot(int index, int x, int y){
    	if(myTile instanceof IInventory)
    		addSlotToContainer(new Slot((IInventory) myTile, index, x,  y));
    }*/

    @Override
    public boolean canInteractWith(EntityPlayer player)
    {
    	if(myTile instanceof IInventory)
    		return ((IInventory)myTile).isUseableByPlayer(player);
    	else
    		return true;
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
    {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
            {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, playerInvHeight + i * 18));
            }

        for (int i = 0; i < 9; i++)
        {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, playerInvHeight + 58));
        }
    }
    
    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges(){
    	super.detectAndSendChanges();

    	
    	if(!liquidGauges.isEmpty() && myTile instanceof TileEntityTank){
    			TileEntityTank tankTile = (TileEntityTank) myTile;
    			
    			for(LiquidGauge gauge : liquidGauges){
    				AdvFluidTank tileTank = tankTile.getTank(gauge.tankId);
    				FluidStack tankLiquid = tileTank.getFluid();
    				
    				if(gauge.liquidID == 0 && tankLiquid == null)
    					continue;
    				
    				if(tankLiquid == null){
    					gauge.liquidID = 0;
    					gauge.liquidAmount = 0;
    					sendGuiUpdate(dataMap.indexOf("tankId" + gauge.tankId), 0);
    					sendGuiUpdate(dataMap.indexOf("tankAmount" + gauge.tankId), 0);
    					continue;
    				}
    				
    				if(gauge.liquidID == 0){
    					gauge.liquidID = tankLiquid.fluidID;
    					gauge.liquidAmount = tankLiquid.amount;
    					sendGuiUpdate(dataMap.indexOf("tankId" + gauge.tankId), tankLiquid.fluidID);
    					sendGuiUpdate(dataMap.indexOf("tankAmount" + gauge.tankId), tankLiquid.amount);
    					continue;
    				}
    					
    				if(!(gauge.liquidID == tankLiquid.fluidID)){
    					gauge.liquidID = tankLiquid.fluidID;
    					sendGuiUpdate(dataMap.indexOf("tankId" + gauge.tankId), tankLiquid.fluidID);
    				}
    				if(!(gauge.liquidAmount == tankLiquid.amount)){
    					gauge.liquidAmount = tankLiquid.amount;
    					sendGuiUpdate(dataMap.indexOf("tankAmount" + gauge.tankId), tankLiquid.amount);
    				}
    			}
    			
    			
    	}
    	
    	if(!progressBars.isEmpty() && myTile instanceof TileEntityGeneric){
    		for(ProgressBar progress : progressBars){
    			int totTest = progress.total;
    			int valTest = progress.value;
    			
    			progress = ((TileEntityGeneric)myTile).updateProgressBars(progress);
    			
    			if(progress != null){
	    			if(totTest != progress.total)
	    				sendGuiUpdate(dataMap.indexOf("progTot" + progress.id), progress.total);
	    			
	    			
	    			if(valTest != progress.value)
	    				sendGuiUpdate(dataMap.indexOf("progVal" + progress.id), progress.value);
    			}
    		}
    		
    	}
    	
    	if(!buttons.isEmpty() && myTile instanceof TileEntityGeneric){
    		for(Button but : buttons){
    			Button tileBut = ((TileEntityGeneric)myTile).getButton(but.id);
    			
    			if(tileBut != null && tileBut.currState != but.currState){
    				but.currState = tileBut.currState;
    				sendGuiUpdate(dataMap.indexOf("button" + tileBut.id), tileBut.currState);	
    			}
    		}
    		
    	}
    	
    }
    
    protected void sendGuiUpdate(int mapKey, int data){
    	for (int i = 0; i < this.crafters.size(); ++i){
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            icrafting.sendProgressBarUpdate(this, mapKey, data);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int key, int data){
    	String dataBinding = dataMap.get(key);

    	try{
    	if(dataBinding.startsWith("tankId"))
    		liquidGauges.get(Integer.parseInt(dataBinding.substring(6))).liquidID = data;
    	if(dataBinding.startsWith("tankAmount"))
    		liquidGauges.get(Integer.parseInt(dataBinding.substring(10))).liquidAmount = data;
    	if(dataBinding.startsWith("progTot"))
    		progressBars.get(Integer.parseInt(dataBinding.substring(7))).total = data;
    	if(dataBinding.startsWith("progVal"))
    		progressBars.get(Integer.parseInt(dataBinding.substring(7))).value = data;
    	if(dataBinding.startsWith("button"))
    		buttons.get(Integer.parseInt(dataBinding.substring(6))).currState = data;
    	
		}
    	catch(Exception e){
    		RedGearCore.util.logDebug("Error parsing dataBinding: " + dataBinding + " from key: " + key + "with data: " + data, e);
    	}
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);
        if(!(myTile instanceof IInventory))
        	return null;
        IInventory invTile = (IInventory)myTile;

        //null checks and checks if the item can be stacked (maxStackSize > 1)
        if (slotObject != null && slotObject.getHasStack())
        {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            //merges the item into player inventory since its in the tileEntity
            if (slot < 9)
            {
                if (!this.mergeItemStack(stackInSlot, invTile.getSizeInventory(), invTile.getSizeInventory() + 36, true))
                {
                    return null;
                }
            }
            //places it into the tileEntity is possible since its in the player inventory
            else if (!this.mergeItemStack(stackInSlot, 0, invTile.getSizeInventory() - 1, false))
            {
                return null;
            }

            if (stackInSlot.stackSize == 0)
            {
                slotObject.putStack(null);
            }
            else
            {
                slotObject.onSlotChanged();
            }

            if (stackInSlot.stackSize == stack.stackSize)
            {
                return null;
            }

            slotObject.onPickupFromSlot(player, stackInSlot);
        }

        return stack;
    }
}
