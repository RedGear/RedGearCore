package redgear.core.imc;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;

public class IMCEventHandler {

	Map<String, IMCMessageHandler> handlers = new HashMap<String, IMCMessageHandler>();
	
	public void addHandler(String key, IMCMessageHandler handler){
		handlers.put(key, handler);
	}
	
	public void handle(IMCEvent event){
		for(IMCMessage message : event.getMessages()){
			IMCMessageHandler handler = handlers.get(message.key);
			
			if(handler != null){
				if(message.isStringMessage())
					handler.handle(message.getStringValue());
				
				if(message.isItemStackMessage())
					handler.handle(message.getItemStackValue());
				
				if(message.isNBTMessage())
					handler.handle(message.getNBTValue());
			}
		}
	}
}
