package redgear.core.fluids;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import redgear.core.inventory.TransferRule;
import redgear.core.render.LiquidGauge;

public class AdvFluidTank extends FluidTank {

	private LiquidGauge guage;
	private final Map<Integer, TransferRule> fluidMap = new HashMap<Integer, TransferRule>();

	/**
	 * Creates a new AdvFluidTank with the given capacity.
	 * 
	 * @param capacity Capacity of this tank in mb.
	 */
	public AdvFluidTank(int capacity) {
		this(null, capacity);
	}

	/**
	 * Creates a new AdvFluidTank with the gives capacity and FluidStack.
	 * 
	 * @param fluid Fluid to fill the new tank with.
	 * @param amount Amount of fluid to fill.
	 * @param capacity Capacity of this tank in mb.
	 */
	public AdvFluidTank(Fluid fluid, int amount, int capacity) {
		this(new FluidStack(fluid, amount), capacity);
	}

	/**
	 * Creates a new AdvFluidTank with the gives capacity and FluidStack.
	 * 
	 * @param stack FluidStack to fill the new tank with.
	 * @param capacity Capacity of this tank in mb.
	 */
	public AdvFluidTank(FluidStack stack, int capacity) {
		super(stack, capacity);
	}

	public AdvFluidTank addFluidMapFluids(Collection<Fluid> fluids, TransferRule direct) {
		for (Fluid addFluid : fluids)
			addFluidMap(addFluid, direct);
		return this;
	}

	public AdvFluidTank addFluidMap(Fluid addFluid, TransferRule direct) {
		addFluidMap(addFluid == null ? -1 : addFluid.getID(), direct);
		return this;
	}

	public AdvFluidTank addFluidMapIds(Collection<Integer> fluidIds, TransferRule direct) {
		for (Integer fluidId : fluidIds)
			addFluidMap(fluidId, direct);
		return this;
	}

	public AdvFluidTank addFluidMap(int addFluidId, TransferRule direct) {
		fluidMap.put(addFluidId, direct);
		return this;
	}

	public boolean isFull() {
		return fluid != null && getFluid().amount == capacity;
	}

	public boolean isEmpty() {
		return fluid == null || fluid.amount == 0;
	}

	public boolean contains(Fluid other) {
		return other == null || isEmpty() ? false : fluid.fluidID == other.getID();
	}

	public boolean contains(FluidStack other) {
		return other == null || isEmpty() ? false : fluid.containsFluid(other);
	}

	public boolean canFill(FluidStack other) {
		return other != null && canExcept(other.fluidID) && contains(other) && getSpace() > other.amount;
	}

	public boolean canExcept(FluidStack stack) {
		return canExcept(stack.fluidID);
	}

	public boolean canExcept(Fluid fluid) {
		return canExcept(fluid.getID());
	}

	public boolean canExcept(int fluidId) {
		TransferRule direct = getRule(fluidId);
		return direct == TransferRule.INPUT || direct == TransferRule.BOTH;
	}

	public boolean canEmpty(int amount) {
		return canEject(fluid.fluidID) && getAmount() > amount;
	}

	public boolean canEject(int fluidId) {
		TransferRule direct = getRule(fluidId);
		return direct == TransferRule.OUTPUT || direct == TransferRule.BOTH;
	}

	public int getSpace() {
		return capacity - getFluidAmount();
	}

	public int getAmount() {
		return getFluidAmount();
	}

	private TransferRule getRule(int fluidId) {
		TransferRule temp = fluidMap.get(fluidId);

		if (temp == null) {
			temp = fluidMap.get(-1);
			if (temp == null)
				temp = TransferRule.NEITHER;
		}

		return temp;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (canExcept(resource))
			return super.fill(resource, doFill);
		else
			return 0;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (canEject(maxDrain))
			return super.drain(maxDrain, doDrain);
		else
			return null;
	}

	/**
	 * Fills the tank while ignoring any pressure, side or liquid mappings.
	 * 
	 * @param resource
	 * @param doFill
	 * @return
	 */
	public int fillOverride(FluidStack resource, boolean doFill) {
		return super.fill(resource, doFill);
	}

	public AdvFluidTank addLiquidGauge(int x, int y, int width, int height, int tankId) {
		guage = new LiquidGauge(capacity, x, y, width, height, tankId);
		return this;
	}

	public LiquidGauge getLiquidGauge() {
		return guage;
	}
}
