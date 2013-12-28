package redgear.core.render;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import org.lwjgl.opengl.GL11;

import redgear.core.asm.RedGearCore;
import redgear.core.network.CorePacketHandler;
import redgear.core.util.StringHelper;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiGeneric extends GuiContainer {
	
    protected int guiX = 0;
    protected int guiY = 0;
    protected final String guiName;
    protected final ContainerGeneric myContainer;
    protected final ResourceLocation guiLocation;

    
	public GuiGeneric(Container par1Container, int xSize, int ySize, String fileDirectory, String fileName, String guiName){
		super(par1Container);
		this.myContainer = (ContainerGeneric) par1Container;
		if(xSize > 0 && ySize > 0){
			this.xSize = xSize;
			this.ySize = ySize;
		}

		guiLocation = new ResourceLocation(fileDirectory,  fileName);
		this.guiName = guiName;
	}
	
	/**
	 * DON'T USE THIS CONSTRUCTOR!!!
	 */
	@Deprecated
	protected GuiGeneric(Container par1Container) {
		this(par1Container, 0, 0, "", "", "");
	}
	
	/**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
	@Override
    protected void actionPerformed(GuiButton but){
		if(but instanceof Button){
			Button butt = (Button) but; //I like big butts and I cannot lie.
			butt.clickButton(); //Changes the state clientside. Should make it render correct until the bounce comes back from the server
			sendButtonPacket(myContainer.myTile, butt.id); //Actually send the data to server so it can perform action.
		}
		
    }
	
	protected void sendButtonPacket(TileEntity tile, int data){
		ByteArrayOutputStream bos = new ByteArrayOutputStream(Integer.SIZE * 4);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeInt(tile.xCoord);
			outputStream.writeInt(tile.yCoord);
			outputStream.writeInt(tile.zCoord);
			outputStream.writeInt(data);
		} catch (Exception e) {
			RedGearCore.util.logDebug("Something went wrong trying to send a packet from the client to server", e);
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = CorePacketHandler.buttonChannel;
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		PacketDispatcher.sendPacketToServer(packet);
	}
	
	 @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
        fontRenderer.drawString(guiName, 8, 4, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, myContainer.playerInvHeight - 9, 4210752);
        
        if(!myContainer.liquidGauges.isEmpty())
    		for(LiquidGauge gauge : myContainer.liquidGauges)
    			drawLiquidGaugeToolTip(gauge, mouseX, mouseY);
        
    }
	 
	 protected void drawLiquidGaugeToolTip(LiquidGauge gauge, int mouseX, int mouseY){
		 if(gauge == null)
			 return; //What the? How?
		 
		 if(!isPointInRegion(gauge.x, gauge.y, gauge.width, gauge.height, mouseX, mouseY))
			 return;
		 
		 ArrayList<String> lines = new ArrayList<String>(2);
		 
		 if(gauge.liquidID != 0){
			 Fluid fluid = FluidRegistry.getFluid(gauge.liquidID);
			 if(fluid == null)
				 lines.add("?????");//What is this wizardry?!?!
			 else
				 lines.add(fluid.getLocalizedName());
		 }
			 
		 lines.add(StringHelper.concat(gauge.liquidAmount, "/", gauge.tankCapacity, "mb"));// 1000/4000mb
		 drawHoverText(lines, 0, 0);
	 }
	 
	 /**
	  * Just a better named and described redirect for a vanilla method.
	  * @param lines
	  * @param mouseX
	  * @param mouseY
	  */
	 protected void drawHoverText(ArrayList<String> lines, int mouseX, int mouseY){
		 func_102021_a(lines, mouseX, mouseY);
	 }
	 
	
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3){
    	this.buttonList = myContainer.buttons;
    	
    	this.guiX = (width - xSize) / 2;
        this.guiY = (height - ySize) / 2;
    	
    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(guiLocation);
        this.drawTexturedModalRect(guiX, guiY, 0, 0, xSize, ySize);

    	
    	if(!myContainer.liquidGauges.isEmpty())
    		for(LiquidGauge gauge : myContainer.liquidGauges)
    			drawLiquid(gauge);
    		
    	
    	if(!myContainer.progressBars.isEmpty())
    		for(ProgressBar progress : myContainer.progressBars)
    			drawProgress(progress);     
    }
    
    protected void drawProgress(ProgressBar progress){
    	drawProgress(progress.getX1() + guiX, progress.getY1() + guiY, progress.getX2() + guiX, progress.getY2() + guiY, progress.getColor());
    }
 
    protected void drawProgress(int x1, int y1, int x2, int y2, int color){
    	if(y1 != y2)
    		this.drawRect(x1, y1, x2, y2, color);
    }
	
    protected void drawLiquid(LiquidGauge gauge){
    	drawLiquid(gauge.x + guiX, gauge.y + guiY, gauge.width, gauge.height, gauge.liquidID, gauge.liquidAmount, gauge.tankCapacity);
    }
	
	protected void drawLiquid(int x, int y, int width, int height, int liquidID, int liquidAmount, int tankCapacity){
		
		if(liquidAmount == 0)
			return;
		
		Fluid fluid = FluidRegistry.getFluid(liquidID);
		if(fluid == null)
			return;
		Icon liquidIcon = fluid.getIcon();
		
		if(liquidIcon == null && fluid.getBlockID() > 0) //frustrating
			liquidIcon = Block.blocksList[fluid.getBlockID()].getBlockTextureFromSide(0);
		
		if(liquidIcon == null)
			return;
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture );
		
		int scale = (int) ((double)height * ((double) liquidAmount / (double)tankCapacity));
		
		this.drawTexturedModelRectFromIcon(x, y + height - scale, liquidIcon, width, scale);
	}

}
