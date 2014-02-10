package redgear.core.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import redgear.core.render.SimpleBlockRenderingHandler;
import redgear.core.util.SimpleItem;
import cpw.mods.fml.client.registry.ClientRegistry;

public class MetaTileSpecialRenderer extends MetaTile {
	private final int renderId;
	private final TileEntitySpecialRenderer tileRender;

	public MetaTileSpecialRenderer(Material par2Material, String name, SimpleBlockRenderingHandler blockRender,
			TileEntitySpecialRenderer tileRender) {
		super(par2Material, name);
		renderId = blockRender.getRenderId();
		this.tileRender = tileRender;
	}

	@Override
	public SimpleItem addMetaBlock(SubTile newBlock) {
		ClientRegistry.bindTileEntitySpecialRenderer(newBlock.tile, tileRender);
		return super.addMetaBlock(newBlock);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public final int getRenderType() {
		return renderId;
	}

}
