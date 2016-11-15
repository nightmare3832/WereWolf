package sote.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.nukkit.network.protocol.AddEntityPacket;
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
                int dv = 15;
                double x0 = 128;
                double z0 = 128;
                double x1;
                double z1;
                double rabius = 8;
                for(int i = 0;i < dv;i++){
                    x1 = x0 + rabius * Math.sin(2 * Math.PI * i / dv);
                    z1 = z0 + rabius * Math.cos(2 * Math.PI * i / dv);
                    AddEntityPacket pk = new AddEntityPacket();
                    long eid = Entity.entityCount++;
                    pk.entityRuntimeId = eid;
                    pk.entityUniqueId = eid;
                    pk.type = 32;
                    pk.x = (float)x1;
                    pk.y = 5;
                    pk.z = (float)z1;
                    if(sender instanceof Player){
                        ((Player) sender).dataPacket(pk);
                    }
                }
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
