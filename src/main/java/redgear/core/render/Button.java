package redgear.core.render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import redgear.core.asm.RedGearCore;
import redgear.core.network.CoreClientProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Button extends GuiRegion implements GuiElement {

	public final int id;
	private final List<ButtonState> states = new ArrayList<ButtonState>();
	public int currState = 0;

	public Button(int id, int xPosition, int yPosition, int width, int height) {
		super(xPosition, yPosition, width, height);
		this.id = id;
	}

	public int addState(String displayString) {
		return addState(new ButtonState(states.size(), displayString));
	}

	public int addState(String displayString, String iconName) {
		return addState(new ButtonState(states.size(), displayString, iconName));
	}

	protected int addState(ButtonState newState) {
		states.add(newState);
		return newState.id;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void draw(GuiGeneric gui) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiButton but = new GuiButton(id, getX(), getY(), getWidth(), getHeight(), "");
		ButtonState nowState = states.get(currState);

		if (nowState.displayString != null)
			but.displayString = nowState.displayString;

		gui.drawButton(but);

		if (nowState.iconName != null && !nowState.iconName.isEmpty())
			gui.drawRectangleIcon(getX(), getY(), getX() + getWidth(), getY() + getHeight(), nowState.getIcon(),
					TextureMap.locationItemsTexture);
	}

	/**
	 * Call this when the button is clicked
	 * 
	 * @return The ID of the new button state, to be sent back to the tile for
	 * processing
	 */
	public int clickButton() {
		++currState;
		if (currState >= states.size()) //loop around when overflowed.
			currState = 0;
		return currState;
	}

	private class ButtonState {
		final int id;
		final String displayString;
		final String iconName;

		ButtonState(int id, String displayString, String iconName) {
			this.id = id;
			this.displayString = displayString;
			this.iconName = iconName;

			if (iconName != null && !iconName.isEmpty())
				RedGearCore.proxy.addIcon(iconName);
		}

		ButtonState(int id, String displayString) {
			this(id, displayString, null);
		}

		private IIcon getIcon() {
			return ((CoreClientProxy) RedGearCore.proxy).getIcon(iconName);
		}
	}
}
