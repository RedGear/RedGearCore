package redgear.core.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import redgear.core.util.StringHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFluid extends BlockFluidClassic {

	private final String modName;
	private final String name;
	private final Fluid fluid;
	private IIcon iconStill;
	private IIcon iconFlowing;

	public BlockFluid(Fluid fluid, String name) {
		super(fluid, Material.water);
		modName = StringHelper.parseModAsset();
		this.name = name;
		this.fluid = fluid;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if (side <= 1)
			return iconStill;
		else
			return iconFlowing;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		if (fluid.getFlowingIcon() == null || fluid.getStillIcon() == null) {

			iconStill = par1IconRegister.registerIcon(modName + name + "_still");
			iconFlowing = par1IconRegister.registerIcon(modName + name + "_flow");

			fluid.setIcons(iconStill, iconFlowing);
		} else {
			iconStill = fluid.getStillIcon();
			iconFlowing = fluid.getFlowingIcon();
		}
	}
}
