package redgear.core.network;


public class CorePacketHandler /*implements  IPacketHandler*/{
	/*
	public static final String buttonChannel = "ChemButton";
	
	public static final HashMap<String, PacketHandler> channelMap = new HashMap<String, PacketHandler>();
	
	public static String[] getChannels(){
		return (String[]) channelMap.values().toArray();
	}
	
	static{
		addHandler(new ButtonHandler());
	}
	
	private static void addHandler(PacketHandler handler){
		channelMap.put(handler.channel, handler);
	}
	
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player){
    	channelMap.get(packet.channel).handle(new DataInputStream(new ByteArrayInputStream(packet.data)), ((Entity)player).worldObj);
    }
	
    public static abstract class PacketHandler{
    	public final String channel;
    	
    	public PacketHandler(String channel){
    		this.channel = channel;
    	}
    	
    	public abstract void handle(DataInputStream inStream, World world);	
    }
    
    public static class ButtonHandler extends PacketHandler{
    	ButtonHandler(){
    		super(buttonChannel);
    	}
    	
    	public void handle(DataInputStream inStream, World world){	
    		try{
    			int x = inStream.readInt();
    			int y = inStream.readInt();
    			int z = inStream.readInt();
    			int button = inStream.readInt();
    			
    			TileEntity tile = world.getBlockTileEntity(x, y, z);
    			
    			if(tile instanceof TileEntityGeneric){
    				((TileEntityGeneric) tile).clickButton(button);
    			}
    			
    		}catch (Exception e){
    			RedGearCore.util.logDebug("Something went wrong reading from a packet!", e);
    		}
    	}
    }
	*/
    		
}
