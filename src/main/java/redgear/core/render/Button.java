package redgear.core.render;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class Button extends GuiButton {
	
	private ArrayList<ButtonState> states = new ArrayList<ButtonState>();
	public int currState = 0;

	public Button(int id, int xPosition, int yPosition, int width, int height) {
		super(id, xPosition, yPosition, width, height, "");
	}

	public int addState(String displayString){
		return addState(new ButtonState(states.size(), displayString));
	}
	
	public int addState(ResourceLocation location, Icon icon){
		return addState(new ButtonState(states.size(), location, icon));
	}
	
	public int addState(String displayString, ResourceLocation location, Icon icon){
		return addState(new ButtonState(states.size(), displayString, location, icon));
	}
	
	protected int addState(ButtonState newState){
		states.add(newState);
		return states.size() - 1;
	}
	
	/**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft par1Minecraft, int par2, int par3){
    	ButtonState nowState = states.get(currState);
    	
    	if(nowState.displayString != null)
    		this.displayString = nowState.displayString;
    	
    	super.drawButton(par1Minecraft, par2, par3);
    		
		if(nowState.location != null && nowState.icon != null){
			par1Minecraft.getTextureManager().bindTexture(nowState.location);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModelRectFromIcon(xPosition, yPosition, nowState.icon, width, height);
		}	
    }
    
    /**
     * Call this when the button is clicked
     * @return The ID of the new button state, to be sent back to the tile for processing
     */
    public int clickButton(){
    	++currState;
    	if(currState >= states.size()) //loop around when overflowed.
    		currState = 0;
    	return currState;
    }
	
	
	private class ButtonState{
		final int id;
		final String displayString;
		final ResourceLocation location;
		final Icon icon;
		
		ButtonState(int id, String displayString, ResourceLocation location,  Icon icon){
			this.id = id;
			this.displayString = displayString;
			this.location = location;
			this.icon = icon;
		}
		
		ButtonState(int id, String displayString){
			this(id, displayString, null, null);
		}
		
		ButtonState(int id, ResourceLocation location, Icon icon){
			this(id, null, location, icon);
		}
	}
}
