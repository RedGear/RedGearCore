package redgear.core.block;

import net.minecraft.block.material.Material;
import redgear.core.util.SimpleItem;

public class MetaTileSpecialRenderer extends MetaTile {
	private final int renderId;

	public MetaTileSpecialRenderer(Material par2Material, String name, int render) {
		super(par2Material, name);
		renderId = render;
	}

	@Override
	public SimpleItem addMetaBlock(SubTile newBlock) {
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
