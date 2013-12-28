package redgear.core.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import redgear.core.render.SimpleBlockRenderingHandler;
import redgear.core.util.SimpleItem;
import cpw.mods.fml.client.registry.ClientRegistry;

public class MetaTileSpecialRenderer extends MetaTile {
	private final int renderId;
	private final TileEntitySpecialRenderer tileRender;

	public MetaTileSpecialRenderer(int ID, Material par2Material, String name,
			SimpleBlockRenderingHandler blockRender,
			TileEntitySpecialRenderer tileRender) {
		super(ID, par2Material, name);
		renderId = blockRender.getRenderId();
		this.tileRender = tileRender;
	}

	@Override
	public SimpleItem addMetaBlock(SubTile newBlock) {
		ClientRegistry.bindTileEntitySpecialRenderer(newBlock.tile, tileRender);
		return super.addMetaBlock(newBlock);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public final int getRenderType() {
		return renderId;
	}

}
