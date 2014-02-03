package redgear.core.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import redgear.core.render.Button;
import redgear.core.render.DrawSnippet;
import redgear.core.render.GuiElement;
import redgear.core.render.ProgressBar;

/**
 * TileEntityGeneric handles the basic necessities of TileEntities
 * These include a way of storing the direction a tile is facing, a name used
 * for saving, and basic packet and NBT data saving
 * 
 * @author Blackhole
 * 
 */

public abstract class TileEntityGeneric extends TileEntity {
	private ForgeDirection direction = ForgeDirection.SOUTH; //Default
	private boolean redstoneState = false;
	private final List<GuiElement> guiElements = new ArrayList<GuiElement>();

	/*
	 * protected List<DrawSnippet> snippets = new ArrayList<DrawSnippet>();
	 * protected List<ProgressBar> progressBars = new ArrayList<ProgressBar>();
	 * protected List<Button> buttons = new ArrayList<Button>();
	 */

	public final int getDirectionId() {
		return getDirection().ordinal();
	}

	public ForgeDirection getDirection() {
		return direction;
	}

	public final boolean setDirection(int id) {
		return setDirection(ForgeDirection.getOrientation(id));
	}

	/**
	 * Set the facing direction of this tile
	 * 
	 * @param side Side to set direction to
	 * @return True if direction, false if it wasn't
	 */
	public boolean setDirection(ForgeDirection side) {
		boolean ans = false;

		if (side != ForgeDirection.UNKNOWN) {
			ans = direction != side;
			direction = side;
		}

		return ans;
	}

	/**
	 * @return true if this Tile is NOT on the server side (worldObj could be
	 * null).
	 */
	protected boolean isClient() {
		return !isServer();
	}

	/**
	 * @return true if this Tile is on the server side.
	 */
	protected boolean isServer() {
		return worldObj != null && !worldObj.isRemote;
	}

	/**
	 * Called when a player right clicks this tile while holding a Buildcraft
	 * wrench
	 * 
	 * @param player The Player who clicked.
	 * @param side The Side the player clicked on.
	 * @return true if the gui should open, false if it should not.
	 */
	public boolean wrenched(EntityPlayer player, ForgeDirection side) {
		return true;
	}

	/**
	 * Called when a player right clicks this tile while holding a Buildcraft
	 * wrench and holding shift
	 * 
	 * @param player The Player who clicked.
	 * @param side The Side the player clicked on
	 * @return true if the gui should open, false if it should not.
	 */
	public boolean wrenchedShift(EntityPlayer player, ForgeDirection side) {
		return true;
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setByte("direction", (byte) direction.ordinal());
		tag.setBoolean("redState", redstoneState);
	}

	/**
	 * Don't forget to override this function in all children if you want more
	 * vars!
	 */
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		direction = ForgeDirection.getOrientation(tag.getByte("direction"));
		redstoneState = tag.getBoolean("redState");
	}

	@Override
	public final Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
	}

	@Override
	public final void onDataPacket(INetworkManager net, Packet132TileEntityData packet) {
		readFromNBT(packet.data);
	}

	protected int addProgressBar(int x, int y, int width, int height) {
		ProgressBar temp = new ProgressBar(guiElements.size(), x, y, width, height);
		guiElements.add(temp);
		return temp.id;
	}

	public ProgressBar updateProgressBars(ProgressBar prog) {
		return prog;
	}

	/**
	 * Copies a piece off this texture and draws the snippet somewhere else. IE:
	 * Furnace progress bar or burn time.
	 * 
	 * @param x - X Coord where the snippet WILL BE DRAWN.
	 * @param y - Y Coord where the snippet WILL BE DRAWN.
	 * @param width - Width of the snippet. TO and FROM are the same.
	 * @param height - Height of the snippet. TO and FROM are the same.
	 * @param snipX - X Coord of where to snip FROM.
	 * @param snipY - Y Coord of where to snip FROM.
	 */
	protected void addDrawSnippet(int x, int y, int width, int height, int snipX, int snipY) {
		guiElements.add(new DrawSnippet(x, y, width, height, snipX, snipY));
	}

	protected int addButton(int xPosition, int yPosition, int width, int height) {
		Button temp = new Button(guiElements.size(), xPosition, yPosition, width, height);
		guiElements.add(temp);
		return temp.id;
	}

	public Button getButton(int id) {
		for (GuiElement el : guiElements)
			if (el instanceof Button) {
				Button but = (Button) el;
				if (but.id == id)
					return but;
			}
		return null;
	}

	public List<GuiElement> getGuiElements() {
		return guiElements;
	}

	/**
	 * Called by the CorePacketHander when a button with this ID is clicked
	 * 
	 * @param id
	 */
	public final void clickButton(int id) {
		Button temp = getButton(id);
		if (temp != null) {
			temp.clickButton();
			buttonClicked(temp);
		}
	}

	/**
	 * Override this function if you have any buttons. This will be called
	 * whenever this button is clicked, so do whatever the button does.
	 * You can check the currState to see what state it is in now if that is
	 * needed.
	 * 
	 * @param button The button clicked.
	 */
	protected void buttonClicked(Button button) {
	}

	protected final boolean getRedstoneState() {
		return redstoneState;
	}

	/**
	 * Should be called by blocks to sync redstone state changes.
	 * 
	 * @param state
	 */
	public final void updateRedstone(boolean state) {
		if (redstoneState != state) {
			redstoneState = state;
			onRedstoneChange(state);
		}
	}

	/**
	 * Called whenever the redstone state of this block changes.
	 * True means redstone is now on (and was off).
	 * False means redstone is now off (and was on).
	 * 
	 * @param newState
	 */
	protected void onRedstoneChange(boolean newState) {
	}

	/**
	 * Use this to output a redstone signal to the given side
	 * 
	 * @return Strength of redstone 0 means none.
	 */
	public int redstoneSignal(ForgeDirection side) {
		return 0;
	}
}
