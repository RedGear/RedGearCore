package redgear.core.fluids;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import redgear.core.inventory.TransferRule;

public class AdvFluidTank extends FluidTank {

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

	/**
	 * Maps a collection of fluids to one transfer rule.
	 * 
	 * @param fluids Fluids to give this rule.
	 * @param direct Direction these fluids are allowed to move.
	 * @return this object.
	 */
	public AdvFluidTank addFluidMapFluids(Collection<Fluid> fluids, TransferRule direct) {
		for (Fluid addFluid : fluids)
			addFluidMap(addFluid, direct);
		return this;
	}

	/**
	 * Maps a fluid to a transfer rule.
	 * 
	 * @param addFluid Fluid to give this rule.
	 * @param direct Direction this fluid is allowed to move.
	 * @return this object.
	 */
	public AdvFluidTank addFluidMap(Fluid addFluid, TransferRule direct) {
		addFluidMap(addFluid == null ? -1 : addFluid.getID(), direct);
		return this;
	}

	/**
	 * Maps a collection of fluid IDs to one transfer rule.
	 * 
	 * @param fluids Fluids to give this rule.
	 * @param direct Direction these fluids are allowed to move.
	 * @return this object.
	 */
	public AdvFluidTank addFluidMapIds(Collection<Integer> fluidIds, TransferRule direct) {
		for (Integer fluidId : fluidIds)
			addFluidMap(fluidId, direct);
		return this;
	}

	/**
	 * Maps a fluid id to a transfer rule.
	 * 
	 * @param addFluid ID of the fluid to give this rule. (-1 to refer to all
	 * fluids with no explicit mapping).
	 * @param direct Direction this fluid is allowed to move.
	 * @return this object.
	 */
	public AdvFluidTank addFluidMap(int addFluidId, TransferRule direct) {
		fluidMap.put(addFluidId, direct);
		return this;
	}

	/**
	 * @return True if this tank is full
	 */
	public boolean isFull() {
		return fluid != null && getFluid().amount == capacity;
	}

	/**
	 * @return true if this tank contains no fluid
	 */
	public boolean isEmpty() {
		return fluid == null || fluid.amount == 0;
	}

	/**
	 * @return Amount of empty space in this tank.
	 */
	public int getSpace() {
		return capacity - getFluidAmount();
	}

	/**
	 * @return Shortcut for amount of fluid in this tank.
	 */
	public int getAmount() {
		return getFluidAmount();
	}

	/**
	 * @param other FluidStack to try to add
	 * @return True if other could be added to this tank with the fill()
	 * method.
	 */
	public boolean canFill(FluidStack other, boolean fully) {
		return other != null && isEmpty() ? true : fluid.isFluidEqual(other) && (!fully || canFill(other.amount));
	}

	/**
	 * @param other Fluid to try to add
	 * @return True if this tank could have this fluid added. Does not check if
	 * tank is full.
	 */
	public boolean canFill(Fluid other) {
		return other == null || isEmpty() ? true : fluid.fluidID == other.getID();
	}

	/**
	 * @param amount Amount of fluid to try to add
	 * @return True if there is enough room in this tank to put this much fluid.
	 */
	public boolean canFill(int amount) {
		return getSpace() >= amount;
	}

	/**
	 * @param other FluidStack to try to remove
	 * @return True if other could be removed from this tank with the
	 * drain()
	 * method.
	 */
	public boolean canDrain(FluidStack other, boolean fully) {
		return other != null && isEmpty() ? false : fully ? fluid.containsFluid(other) : fluid.isFluidEqual(other);
	}

	/**
	 * @param other Fluid to try to add
	 * @return True if this tank could have this fluid removed; AKA it contains
	 * some of this fluid.
	 */
	public boolean canDrain(Fluid other) {
		return other == null || isEmpty() ? false : fluid.fluidID == other.getID();
	}

	/**
	 * @param amount Amount to try and remove
	 * @return true if this tank contains at least this much fluid.
	 */
	public boolean canDrain(int amount) {
		return getAmount() >= amount;
	}

	/**
	 * @param fluidId Fluid ID to try to add
	 * @return True if this tank could accept this type of fluid through the
	 * fillWithMap() method
	 */
	public boolean canAccept(int fluidId) {
		TransferRule direct = getRule(fluidId);
		return direct == TransferRule.INPUT || direct == TransferRule.BOTH;
	}

	/**
	 * @param fluidId Fluid ID to try and remove
	 * @return True if this fluid could be removed through the drainWithMap()
	 * method
	 */
	public boolean canEject(int fluidId) {
		TransferRule direct = getRule(fluidId);
		return direct == TransferRule.OUTPUT || direct == TransferRule.BOTH;
	}

	/**
	 * @param other FluidStack to try to add
	 * @return True if other could be added to this tank with the
	 * fillWithMap()
	 * method.
	 */
	public boolean canFillWithMap(FluidStack other, boolean fully) {
		return other == null || canAccept(other.fluidID) && canFill(other, fully);
	}

	/**
	 * @param other FluidStack to try to add
	 * @return True if other could be added to this tank with the fill()
	 * method.
	 */
	public boolean canDrainWithMap(FluidStack other, boolean fully) {
		return other != null && canEject(other.fluidID) && canDrain(other, fully);
	}

	/**
	 * @param amount Amount to try and remove
	 * @return true if this tank contains at least this much fluid.
	 */
	public boolean canDrainWithMap(int amount) {
		return canEject(fluid.fluidID) && canDrain(amount);
	}

	/**
	 * @param fluidId Fluid ID to get rule for
	 * @return The TransferRule for this fluid, or the value of default fluid,
	 * or TransferRule.NEITHER if not found.
	 */
	private TransferRule getRule(int fluidId) {
		TransferRule temp = fluidMap.get(fluidId);

		if (temp == null) {
			temp = fluidMap.get(-1);
			if (temp == null)
				temp = TransferRule.NEITHER;
		}

		return temp;
	}

	/**
	 * Fills this tank with as much of the FluidStack as possible, using the
	 * fluidMappings
	 * 
	 * @param resource FluidStack to fill
	 * @param doFill True means the action will be done, false means it will
	 * only be simulated.
	 * @return amount of fluid accepted, or would have been moved.
	 */
	public int fillWithMap(FluidStack resource, boolean doFill) {
		if (canFillWithMap(resource, false))
			return super.fill(resource, doFill);
		else
			return 0;
	}

	/**
	 * Drains this tank with up to the amount in maxDrain, using the
	 * fluidMappings
	 * 
	 * @param maxDrain Maximum amount of fluid to try and drain
	 * @param doDrain if true fluid will be removed, false means it will only be
	 * simulated
	 * @return FluidStack representing the fluid drained or would have been
	 * drained. Null if no fluid would be drained.
	 */
	public FluidStack drainWithMap(int maxDrain, boolean doDrain) {
		if (!isEmpty() && canDrainWithMap(fluid, false))
			return super.drain(maxDrain, doDrain);
		else
			return null;
	}

	/**
	 * Drains this tank with up to the amount in maxDrain, using the
	 * fluidMappings
	 * 
	 * @param maxDrain Maximum amount of fluid to try and drain
	 * @param doDrain if true fluid will be removed, false means it will only be
	 * simulated
	 * @return FluidStack representing the fluid drained or would have been
	 * drained.
	 */
	public FluidStack drainWithMap(FluidStack resource, boolean doDrain) {
		FluidStack drained = null;
		if (canDrain(resource, false))
			drained = drain(resource.amount, doDrain);
		return drained;
	}
}
