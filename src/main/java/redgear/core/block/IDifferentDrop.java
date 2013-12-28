package redgear.core.block;

import java.util.Random;

public interface IDifferentDrop {

	public int getIdDropped(int meta, Random rand, int fortune);
	
	public int getQuantityDropped(int meta, int fortume, Random rand);
	
	public int getMetaDropped(int meta);
}
