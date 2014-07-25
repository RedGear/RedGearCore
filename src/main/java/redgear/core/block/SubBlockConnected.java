package redgear.core.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import redgear.core.world.Location;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SubBlockConnected extends SubBlock{
	
	/*
	 *   1
	 * 2 0 3
	 *   4
	 * 
	 * 0000 = 00 = normal
	 * 0001 = 01 = top
	 * 0010 = 02 = left
	 * 0011 = 03 = top + left
	 * 0100 = 04 = right
	 * 0101 = 05 = top + right
	 * 0110 = 06 = left + right
	 * 0111 = 07 = top + left + right
	 * 1000 = 08 = bottom
	 * 1001 = 09 = top + bottom
	 * 1010 = 10 = left + bottom
	 * 1011 = 11 = top + left + bottom
	 * 1100 = 12 = right + bottom
	 * 1101 = 13 = top + right + bottom
	 * 1110 = 14 = left + right + bottom
	 * 1111 = 15 = top + left + right + bottom
	 * 
	 */
	
	private IIcon[] icons = new IIcon[16];
	
	private static final byte[][] rotationMatrix = {
		{2, 4, 5, 3},
		{2, 4, 5, 3},
		{1, 5, 4, 0},
		{1, 4, 5, 0},
		{1, 2, 3, 0},
		{1, 3, 2, 0}
	};

	public SubBlockConnected(String name) {
		super(name);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(String modName, IIconRegister par1IconRegister) {
		for(int i = 0; i < 16; i++)
			icons[i] = par1IconRegister.registerIcon(modName + name + i);
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
	 */
	public IIcon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		Location loc = new Location(x, y, z);
		Location trans;
		Block me = loc.getBlock(world);
		int meta = loc.getBlockMeta(world);
		int index = 0;
		
		for(int i = 0; i < 4; i++){
			trans = loc.translate(rotationMatrix[side][i], 1);
			
			index |= me == trans.getBlock(world) && meta == trans.getBlockMeta(world) ? 1 << i : 0;
		}
		
		return icons[index];
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
	 */
	public IIcon getIcon(int side) {
		return icons[0];
	}

}
