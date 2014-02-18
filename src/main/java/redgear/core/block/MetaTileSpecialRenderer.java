package redgear.core.block;

import net.minecraft.block.material.Material;
import redgear.core.util.SimpleItem;

public class MetaTileSpecialRenderer extends MetaTile {
	private final int renderId;

	public MetaTileSpecialRenderer(int ID, Material par2Material, String name, int render) {
		super(ID, par2Material, name);
		renderId = render;
	}

	@Override
	public SimpleItem addMetaBlock(SubTile newBlock) {
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
