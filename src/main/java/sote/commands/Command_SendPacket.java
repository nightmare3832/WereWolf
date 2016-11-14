package sote.commands;

import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.PlayerListPacket;
import sote.Main;

public class Command_SendPacket extends Command{

    public static UUID uuid;
    public static Long eid;

    public Command_SendPacket(Main plugin){
        super("packet");
        this.plugin = plugin;
        uuid = UUID.randomUUID();
        eid = Entity.entityCount++;
    }

    public boolean execute(CommandSender sender, String label, String args[]){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(args.length == 1){
                packet(player,args[0]);
            }else{
                sender.sendMessage("§f[§cERROR§f] §cERROR");
            }
        }else{
            sender.sendMessage("§f[§cERROR§f] §cこのコマンドはゲーム内で実行してください");
        }
        return true;
    }

    public static void packet(Player player,String data){
        switch(data){
            case "AddPlayerPacket":
                AddPlayerPacket pk = new AddPlayerPacket();
                pk.entityUniqueId = eid;
                pk.entityRuntimeId = eid;
                pk.username = "";
                pk.uuid = uuid;
                pk.x = (float)player.x;
                pk.y = (float)player.y;
                pk.z = (float)player.z;
                pk.yaw = 0;
                pk.pitch = 0;
                pk.metadata = new EntityMetadata()
                    .putLong(38, (long)-1)
                    .putByte(Entity.DATA_LEAD, 0)
                    .putFloat(39,(float) 0.4)
                    .putString(40,"ボタン");
                player.dataPacket(pk);
            break;
            case "AddEntityPacket":
                AddEntityPacket pk1 = new AddEntityPacket();
                pk1.entityUniqueId = eid;
                pk1.entityRuntimeId = eid;
                pk1.x = (float)player.x;
                pk1.y = (float)player.y;
                pk1.z = (float)player.z;
                pk1.type = 52;
                pk1.yaw = 0;
                pk1.pitch = 0;
                pk1.metadata = new EntityMetadata()
                        .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1)
                        .putByte(Entity.DATA_LEAD, 0)
                        .putFloat(Entity.DATA_SCALE,(float) 0.4)
                        .putString(Entity.DATA_INTERACTIVE_TAG,"Button")
                        .putString(Entity.DATA_URL_TAG,"soteserver.com");
                player.dataPacket(pk1);
            break;
            case "AddList":
                PlayerListPacket pkk = new PlayerListPacket();
                pkk.type = PlayerListPacket.TYPE_ADD;
                pkk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid, eid, "nightmare",player.getSkin())};
            break;
            case "RemoveList":
                PlayerListPacket pkkk = new PlayerListPacket();
                pkkk.type = PlayerListPacket.TYPE_REMOVE;
                pkkk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid)};
            break;
        }
    }

    Main plugin;
}
