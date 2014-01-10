package redgear.core.fluids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import redgear.core.render.LiquidGauge;

public class AdvFluidTank extends FluidTank {
	
	int pressure; 
	LiquidGauge guage;
	ArrayList<Integer> fluidMap;
	ArrayList<ForgeDirection> sideMap;
	

    public AdvFluidTank(int capacity) {
    	this(null, capacity);
    }

    public AdvFluidTank(Fluid fluid, int amount, int capacity) {
    	this(new FluidStack(fluid, amount), capacity);
    } 
    
    public AdvFluidTank(FluidStack stack, int capacity) {
    	super(stack, capacity);
    }
    
    
    public AdvFluidTank setPressure(int pressure){
    	this.pressure = pressure;
    	return this;
    }
    
    public AdvFluidTank setFluidMap(ArrayList<Integer> fluidMap){
    	this.fluidMap = fluidMap;
    	return this;
    }
    
    public AdvFluidTank addFluidMapFluids(Collection<Fluid> fluids){
    	for(Fluid addFluid : fluids)
    		addFluidMap(addFluid);
    	return this;
    }
    
    public AdvFluidTank addFluidMap(Fluid addFluid){
    	if(fluidMap == null)
    		fluidMap = new ArrayList<Integer>(1);
    	
    	if(addFluid != null)
    		fluidMap.add(addFluid.getID());
    	return this;
    }
    
    public AdvFluidTank addFluidMapIds(Collection<Integer> fluidIds){
    	for(Integer fluidId : fluidIds)
    		addFluidMap(fluidId);
    	return this;
    }
    
    public AdvFluidTank addFluidMap(int addFluidId){
    	if(fluidMap == null)
    		fluidMap = new ArrayList<Integer>(1);
    	
    	if(addFluidId >= 0)
    		fluidMap.add(addFluidId);
    	return this;
    } 
    
    public AdvFluidTank setSideMap(ArrayList<ForgeDirection> sideMap){
    	this.sideMap = sideMap;
    	return this;
    }
    
    public AdvFluidTank addSideMap(ForgeDirection addSideMap){
    	if(sideMap == null)
    		sideMap = new ArrayList<ForgeDirection>(1);
    	
    	if(addSideMap != null)
    		sideMap.add(addSideMap);
    	return this;
    }
    
    public boolean isFull(){
    	return isEmpty() || getFluid().amount == capacity;
    }
    
    public boolean isEmpty(){
    	return fluid == null || fluid.amount == 0;
    }
    
    public boolean contains(Fluid other){
    	return other == null || isEmpty() ? false : fluid.fluidID == other.getID();
    }
    
    public boolean contains(FluidStack other){
    	return other == null || isEmpty() ? false : fluid.containsFluid(other);
    }
    
    public int getSpace(){
    	return capacity - getFluidAmount();
    }
    
    private boolean checkSide(ForgeDirection side){
    	return sideMap == null || side == ForgeDirection.UNKNOWN || sideMap.contains(side);
    }
    
    private boolean checkFluid(Fluid check){
    	return fluidMap == null || fluidMap.contains(check.getID());
    }
    
    private boolean checkFluid(FluidStack stack){
    	return stack == null ? false : checkFluid(stack.getFluid());
    }
    
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill){
    	if(pressure <= 0 && checkSide(from) && checkFluid(resource))
    		return super.fill(resource, doFill);
    	else
    		return 0;
    }
    
    @Override
    public int fill(FluidStack resource, boolean doFill){
    	return fill(ForgeDirection.UNKNOWN, resource, doFill);
    }
    
    /**
     * Fills the tank while ignoring any pressure, side or liquid mappings.
     * @param resource
     * @param doFill
     * @return
     */
    public int fillOverride(FluidStack resource, boolean doFill){
    	return super.fill(resource, doFill);
    }
    
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
    	if(checkFluid(resource))
    		return drain(from, resource.amount, doDrain);
    	else
    		return null;
    }
    
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
    	if(pressure >= 0 && checkSide(from))
    		return drain(maxDrain, doDrain);
    	else
    		return null;
    }
    
    public AdvFluidTank addLiquidGauge(int x, int y, int width, int height, int tankId){
    	guage = new LiquidGauge(capacity, x, y, width, height, tankId);
    	return this;
    }
    
    public LiquidGauge getLiquidGauge(){
    	return guage;
    }
}
