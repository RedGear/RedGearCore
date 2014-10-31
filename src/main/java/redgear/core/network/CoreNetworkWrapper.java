package redgear.core.network;


import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class CoreNetworkWrapper {

    public static final SimpleNetworkWrapper wrapper = new SimpleNetworkWrapper("RedGearCore");
    private static byte nextKey = 0;

    /**
     * Register a message and it's associated handler. The message will have the supplied discriminator byte. The message handler will
     * be registered on the supplied side (this is the side where you want the message to be processed and acted upon).
     *
     * @param messageHandler the message handler type
     * @param requestMessageType the message type
     * @param side the side for the handler
     */
    public static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side){
        wrapper.registerMessage(messageHandler, requestMessageType, nextKey++, side);
    }
}
