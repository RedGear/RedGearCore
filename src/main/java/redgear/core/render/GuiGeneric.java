package redgear.core.render;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
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

	public GuiGeneric(Container par1Container, int xSize, int ySize, String fileDirectory, String fileName,
			String guiName) {
		super(par1Container);
		myContainer = (ContainerGeneric) par1Container;
		if (xSize > 0 && ySize > 0) {
			this.xSize = xSize;
			this.ySize = ySize;
		}

		guiLocation = new ResourceLocation(fileDirectory, fileName);
		this.guiName = guiName;
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	@Override
	protected void actionPerformed(GuiButton but) {
		if (but instanceof Button) {
			Button butt = (Button) but; //I like big butts and I cannot lie.
			butt.clickButton(); //Changes the state clientside. Should make it render correct until the bounce comes back from the server
			sendButtonPacket(myContainer.myTile, butt.id); //Actually send the data to server so it can perform action.
		}

	}

	protected void sendButtonPacket(TileEntity tile, int data) {
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
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawString(guiName, 8, 4, 4210752);
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8,
				myContainer.playerInvHeight - 9, 4210752);

		for (GuiRegion elem : myContainer.elements)
			if (elem instanceof LiquidGauge)
				drawLiquidGaugeToolTip((LiquidGauge) elem, mouseX, mouseY);

	}

	protected void drawLiquidGaugeToolTip(LiquidGauge gauge, int mouseX, int mouseY) {
		if (gauge == null)
			return; //What the? How?

		if (!isPointInRegion(gauge.getX(), gauge.getY(), gauge.getWidth(), gauge.getHeight(), mouseX, mouseY))
			return;

		ArrayList<String> lines = new ArrayList<String>(2);

		if (gauge.liquidID != 0) {
			Fluid fluid = FluidRegistry.getFluid(gauge.liquidID);
			if (fluid == null)
				lines.add("?????");//What is this wizardry?!?!
			else
				lines.add(fluid.getLocalizedName());
		}

		lines.add(StringHelper.concat(gauge.liquidAmount, "/", gauge.tankCapacity, "mb"));// 1000/4000mb
		drawHoverText(lines, 0, 0);
	}

	/**
	 * Just a better named and described redirect for a vanilla method.
	 * 
	 * @param lines
	 * @param mouseX
	 * @param mouseY
	 */
	protected void drawHoverText(List<String> lines, int mouseX, int mouseY) {
		func_102021_a(lines, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		buttonList = myContainer.buttons;

		guiX = (width - xSize) / 2;
		guiY = (height - ySize) / 2;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(guiLocation);
		drawTexturedModalRect(guiX, guiY, 0, 0, xSize, ySize);

		for (GuiRegion elem : myContainer.elements)
			elem.draw(this);
	}

	protected void drawRectangleSnip(int x1, int y1, int x2, int y2, int snipX, int snipY, ResourceLocation resource) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(resource);
		this.drawRectangleSnip(x1, y1, x2 - x1, y2 - y1, snipX, snipY);
	}

	protected void drawRectangleSnip(int x1, int y1, int x2, int y2, int snipX, int snipY) {
		drawTexturedModalRect(x1 + guiX, y1 + guiY, snipX, snipY, x2 - x1, y2 - y1);
	}

	protected void drawRectangleIcon(int x1, int y1, int x2, int y2, Icon ico, ResourceLocation resource) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(resource);
		this.drawRectangleIcon(x1, y1, x2, y2, ico);
	}

	protected void drawRectangleIcon(int x1, int y1, int x2, int y2, Icon ico) {
		drawTexturedModelRectFromIcon(x1 + guiX, y1 + guiY, ico, x2 - x1, y2 - y1);
	}

	public void drawRectangleSolid(int x1, int y1, int x2, int y2, int color) {
		super.drawRect(x1 + guiX, y1 + guiY, x2 + guiX, y2 + guiY, color);
	}
}
