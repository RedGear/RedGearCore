package redgear.core.network;

import cpw.mods.fml.relauncher.Side;

public class ClientProxyGeneric extends CommonProxyGeneric{
	
	
	@Override
	public Side getSide(){
		return Side.CLIENT;
	}
}
