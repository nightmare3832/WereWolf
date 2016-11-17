package sote.commands;

import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.IntPositionEntityData;
import cn.nukkit.network.protocol.AddPlayerPacket;
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
                new CommandParameter("cast", CommandParameter.ARG_TYPE_RAW_TEXT, false)
        });
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        switch(args[0]){
            case "join":
                if(!(sender instanceof Player)) return false;
                Player player = (Player) sender;
                UUID uuid = UUID.randomUUID();
                AddPlayerPacket pk = new AddPlayerPacket();
                long eid = Entity.entityCount++;
                pk.entityRuntimeId = eid;
                pk.entityUniqueId = eid;
                pk.uuid = uuid;
                pk.x = (float)player.x;
                pk.y = (float)player.y;
                pk.z = (float)player.z;
                pk.yaw = (float)0;
                pk.pitch = (float)0;
                pk.speedX = 0;
                pk.speedY = 0;
                pk.speedZ = 0;
                pk.username = "death";
                int flags = 0;
                flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
                flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
                flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
                int playerflags = 0;
                playerflags ^= 1 << 1;
                pk.metadata = new EntityMetadata()
                        //.putLong(Entity.DATA_FLAGS,flags)
                        .putString(Entity.DATA_NAMETAG, "")
                        .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1)
                        .putByte(Player.DATA_PLAYER_FLAGS,playerflags)
                        .put(new IntPositionEntityData(17,(int)player.x,(int)player.y,(int)player.z));
                player.dataPacket(pk);
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
