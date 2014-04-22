package redgear.core.block;

import net.minecraft.block.material.Material;

public class MetaTileSpecialRenderer extends MetaTile {
	public final int renderId;

	public MetaTileSpecialRenderer(Material par2Material, String name, int renderId) {
		super(par2Material, name);
		this.renderId = renderId;
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
