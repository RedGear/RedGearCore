package redgear.core.block;

import net.minecraft.block.material.Material;
import redgear.core.util.SimpleItem;

public class MetaTileSpecialRenderer extends MetaTile {
	private final int renderId;

	public MetaTileSpecialRenderer(Material par2Material, String name, int renderId) {
		super(par2Material, name);
		this.renderId = renderId;
	}

	@Override
	public SimpleItem addMetaBlock(SubTile newBlock) {
		//ClientRegistry.bindTileEntitySpecialRenderer(newBlock.tile, tileRender);
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
