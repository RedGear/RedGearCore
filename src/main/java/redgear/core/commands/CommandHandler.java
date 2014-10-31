package redgear.core.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Blackhole
 *         Created on 10/25/2014.
 */
public class CommandHandler extends CommandBase {


    private Map<String, ICommand> subCommands = new HashMap<String, ICommand>();


    public void addCommand(ICommand command){
        subCommands.put(command.getCommandName(), command);
    }


    @Override
    public String getCommandName() {
        return "redgear";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Use /redgear help for more information";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args == null || args.length == 0)
            sender.addChatMessage(new ChatComponentText("Invalid command. " + getCommandUsage(sender)));

        String key = args[0];

        ICommand command = subCommands.get(key);

        if(command == null)
            sender.addChatMessage(new ChatComponentText("Invalid command: " + key + getCommandUsage(sender)));
        else
            command.processCommand(sender, Arrays.copyOfRange(args, 1, args.length - 1));
    }
}
