package sote.commands;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import sote.Main;

public class Command_Werewolf extends Command{

    public Command_Werewolf(Main plugin){
        super("werewolf");
        this.plugin = plugin;
        this.commandParameters.clear();
        this.commandParameters.put("join", new CommandParameter[]{
                new CommandParameter("join", CommandParameter.ARG_TYPE_STRING, false)
        });
        this.commandParameters.put("start", new CommandParameter[]{
                new CommandParameter("start", CommandParameter.ARG_TYPE_STRING, false),
                new CommandParameter("jobs", CommandParameter.ARG_TYPE_RAW_TEXT, false)
        });
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        switch(args[0]){
            case "join":
            break;
            case "start":
                Main.jobs = args[1];
                Main.Start();
            break;
        }
        return true;
    }

    Main plugin;
}
