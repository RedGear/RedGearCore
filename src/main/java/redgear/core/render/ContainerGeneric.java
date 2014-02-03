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

public class ContainerGeneric extends Container {
	public final TileEntity myTile;
	public final int playerInvHeight;
	protected List<String> dataMap = new ArrayList<String>();
	public List<GuiElement> elements = new ArrayList<GuiElement>();

	public ContainerGeneric(InventoryPlayer inventoryPlayer, TileEntity tile, int playerInvHeight) {
		myTile = tile;
		this.playerInvHeight = playerInvHeight;
		//commonly used vanilla code that adds the player's inventory
		bindPlayerInventory(inventoryPlayer);

		if (myTile instanceof TileEntityInventory)
			addSlots((TileEntityInventory) myTile);

		if (myTile instanceof TileEntityTank)
			addTanks((TileEntityTank) myTile);

		if (myTile instanceof TileEntityGeneric)
			for (GuiElement element : ((TileEntityGeneric) myTile).getGuiElements())
				addElement(element);
	}

	public ContainerGeneric(InventoryPlayer inventoryPlayer, TileEntity tile) {
		this(inventoryPlayer, tile, 84);
	}

	protected void addSlots(TileEntityInventory tile) {
		for (InvSlot slot : tile.getSlots())
			addSlotToContainer(slot);
	}

	protected void addTanks(TileEntityTank tile) {
		for (LiquidGauge gauge : tile.getLiquidGauges()) {
			elements.add(gauge);
			dataMap.add("tankId" + gauge.tankId);
			dataMap.add("tankAmount" + gauge.tankId);
		}
	}

	private void addElement(GuiElement element) {
		if (element instanceof ProgressBar)
			addProgressBar((ProgressBar) element);

		if (element instanceof Button)
			addButton((Button) element);

		elements.add(element);
	}

	protected void addProgressBar(ProgressBar bar) {
		dataMap.add("progTot" + bar.id);
		dataMap.add("progVal" + bar.id);
	}

	protected void addButton(Button button) {
		dataMap.add("button" + button.id);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		if (myTile instanceof IInventory)
			return ((IInventory) myTile).isUseableByPlayer(player);
		else
			return true;
	}

	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, playerInvHeight + i * 18));

		for (int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, playerInvHeight + 58));
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (GuiElement element : elements)
			detectAndSendChanges(element);

	}

	private void detectAndSendChanges(GuiElement element) {
		if (myTile instanceof TileEntityGeneric) {

			if (element instanceof ProgressBar)
				detectAndSendChanges((ProgressBar) element);

			if (element instanceof Button)
				detectAndSendChanges((Button) element);

			if (element instanceof LiquidGauge && myTile instanceof TileEntityTank)
				detectAndSendChanges((LiquidGauge) element);

		}
	}

	private void detectAndSendChanges(LiquidGauge gauge) {
		TileEntityTank tankTile = (TileEntityTank) myTile;

		AdvFluidTank tileTank = tankTile.getTank(gauge.tankId);
		FluidStack tankLiquid = tileTank.getFluid();

		if (gauge.liquidID == 0 && tankLiquid == null)
			return;

		if (tankLiquid == null) {
			gauge.liquidID = 0;
			gauge.liquidAmount = 0;
			sendGuiUpdate(dataMap.indexOf("tankId" + gauge.tankId), 0);
			sendGuiUpdate(dataMap.indexOf("tankAmount" + gauge.tankId), 0);
			return;
		}

		if (gauge.liquidID == 0) {
			gauge.liquidID = tankLiquid.fluidID;
			gauge.liquidAmount = tankLiquid.amount;
			sendGuiUpdate(dataMap.indexOf("tankId" + gauge.tankId), tankLiquid.fluidID);
			sendGuiUpdate(dataMap.indexOf("tankAmount" + gauge.tankId), tankLiquid.amount);
			return;
		}

		if (!(gauge.liquidID == tankLiquid.fluidID)) {
			gauge.liquidID = tankLiquid.fluidID;
			sendGuiUpdate(dataMap.indexOf("tankId" + gauge.tankId), tankLiquid.fluidID);
		}
		if (!(gauge.liquidAmount == tankLiquid.amount)) {
			gauge.liquidAmount = tankLiquid.amount;
			sendGuiUpdate(dataMap.indexOf("tankAmount" + gauge.tankId), tankLiquid.amount);
		}
	}

	private void detectAndSendChanges(ProgressBar progress) {

		int totTest = progress.total;
		int valTest = progress.value;

		progress = ((TileEntityGeneric) myTile).updateProgressBars(progress);

		if (progress != null) {
			if (totTest != progress.total)
				sendGuiUpdate(dataMap.indexOf("progTot" + progress.id), progress.total);

			if (valTest != progress.value)
				sendGuiUpdate(dataMap.indexOf("progVal" + progress.id), progress.value);
		}

	}

	private void detectAndSendChanges(Button button) {
		Button tileBut = ((TileEntityGeneric) myTile).getButton(button.id);

		if (tileBut != null && tileBut.currState != button.currState) {
			button.currState = tileBut.currState;
			sendGuiUpdate(dataMap.indexOf("button" + tileBut.id), tileBut.currState);
		}
	}

	protected void sendGuiUpdate(int mapKey, int data) {
		for (int i = 0; i < crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting) crafters.get(i);

			icrafting.sendProgressBarUpdate(this, mapKey, data);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int key, int data) {
		String dataBinding = dataMap.get(key);

		try {
			if (dataBinding.startsWith("tankId"))
				getLiquidGauge(Integer.parseInt(dataBinding.substring(6))).liquidID = data;
			if (dataBinding.startsWith("tankAmount"))
				getLiquidGauge(Integer.parseInt(dataBinding.substring(10))).liquidAmount = data;
			if (dataBinding.startsWith("progTot"))
				getProgressBar(Integer.parseInt(dataBinding.substring(7))).total = data;
			if (dataBinding.startsWith("progVal"))
				getProgressBar(Integer.parseInt(dataBinding.substring(7))).value = data;
			if (dataBinding.startsWith("button"))
				getButton(Integer.parseInt(dataBinding.substring(6))).currState = data;

		} catch (Exception e) {
			RedGearCore.util.logDebug("Error parsing dataBinding: " + dataBinding + " from key: " + key + "with data: "
					+ data, e);
		}
	}

	private LiquidGauge getLiquidGauge(int id) {
		for (GuiElement el : elements)
			if (el instanceof LiquidGauge) {
				LiquidGauge gauge = (LiquidGauge) el;
				if (gauge.tankId == id)
					return gauge;
			}
		return null;
	}

	private ProgressBar getProgressBar(int id) {
		for (GuiElement el : elements)
			if (el instanceof ProgressBar) {
				ProgressBar bar = (ProgressBar) el;
				if (bar.id == id)
					return bar;
			}
		return null;
	}

	public Button getButton(int id) {
		for (GuiElement el : elements)
			if (el instanceof Button) {
				Button but = (Button) el;
				if (but.id == id)
					return but;
			}
		return null;
	}

	public List<Button> getButtonList() {
		List<Button> buttons = new ArrayList<Button>();

		for (GuiElement el : elements)
			if (el instanceof Button)
				buttons.add((Button) el);

		return buttons;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);
		if (!(myTile instanceof IInventory))
			return null;
		IInventory invTile = (IInventory) myTile;

		//null checks and checks if the item can be stacked (maxStackSize > 1)
		if (slotObject != null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
			stack = stackInSlot.copy();

			//merges the item into player inventory since its in the tileEntity
			if (slot < 9) {
				if (!mergeItemStack(stackInSlot, invTile.getSizeInventory(), invTile.getSizeInventory() + 36, true))
					return null;
			}
			//places it into the tileEntity is possible since its in the player inventory
			else if (!mergeItemStack(stackInSlot, 0, invTile.getSizeInventory() - 1, false))
				return null;

			if (stackInSlot.stackSize == 0)
				slotObject.putStack(null);
			else
				slotObject.onSlotChanged();

			if (stackInSlot.stackSize == stack.stackSize)
				return null;

			slotObject.onPickupFromSlot(player, stackInSlot);
		}

		return stack;
	}
}
