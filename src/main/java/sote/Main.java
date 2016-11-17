package sote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.IntPositionEntityData;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.InteractPacket;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlayerListPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.Task;
import sote.Jobs.Job;
import sote.Jobs.Job_Couple;
import sote.Jobs.Job_Diviner;
import sote.Jobs.Job_Fanatic;
import sote.Jobs.Job_Guard;
import sote.Jobs.Job_Madman;
import sote.Jobs.Job_Psychic;
import sote.Jobs.Job_Villager;
import sote.Jobs.Job_WereWolf;
import sote.commands.Command_Werewolf;

public class Main extends PluginBase implements Listener{

    public Main(){
    }

    public void onEnable(){
        registerCommands();
        getServer().getPluginManager().registerEvents(this, this);
        Center = new Vector3(128,4,128);
        super.onEnable();
    }

    public void registerCommands(){
        getServer().getCommandMap().register("werewolf",new Command_Werewolf(this));
    }

    @EventHandler
    public static void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        player.setFoodEnabled(false);
        player.getFoodData().sendFoodLevel(20);
        Join(player);
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent event){
        Quit(event.getPlayer());
    }

    @EventHandler
    public static void onChat(PlayerChatEvent event){
        Player player = event.getPlayer();
        if(TimeType != 0){
            if(isLife.containsKey(player)){
                if(isLife.get(player)){
                    if(TimeType == 10 || TimeType == 11 || TimeType == 12){
                        event.setCancelled();
                        if(jobAfter.get(player).getTalkRoom() != 0){
                            for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
                                if(e.getValue()){
                                    if(jobAfter.get(e.getKey()).getTalkRoom() == jobAfter.get(player).getTalkRoom()){
                                        e.getKey().sendMessage("["+jobAfter.get(player).getName()+"]"+player.getName()+" :"+event.getMessage());
                                    }
                                }
                            }
                        }
                    }
                }else{
                    event.setCancelled();
                }
            }
        }
    }

    @EventHandler
    public static void onPacket(DataPacketReceiveEvent event){
        DataPacket pk = event.getPacket();
        if(pk instanceof InteractPacket){
            InteractPacket packet = (InteractPacket) pk;
            Long eid = packet.target;
            Player player = null;
            for(Map.Entry<Player,Long> e : EntityId.entrySet()){
                if(e.getValue().equals(eid)) player = e.getKey();
            }
            if(player == null) return;
            if(player instanceof Player){
                Player damager = event.getPlayer();
                if(jobAfter.containsKey(damager) && jobAfter.containsKey(player)){
                    if(TimeType == 30){
                        if(isLife.get(damager)){
                            if(isLife.get(player)){
                                Vote(damager,player);
                            }
                        }
                    }
                    if(TimeType == 31){
                        if(isLife.get(damager)){
                            if(isLife.get(player)){
                                DecisiveVote(damager,player);
                            }
                        }
                    }
                    if(TimeType == 10 || TimeType == 11 || TimeType == 12){
                        if(isLife.get(damager)){
                            if(isLife.get(player)){
                                if(jobAfter.get(damager).getNumber() == 1){
                                    if(jobAfter.get(player).getNumber() != 1){
                                        if(damager.getInventory().getItemInHand().getId() == WereWolfItem){
                                            jobAfter.get(damager).setTarget(player);
                                            for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
                                                if(e.getValue()){
                                                    if(jobAfter.get(e.getKey()).getNumber() == 1){
                                                        e.getKey().sendMessage(player.getName()+"を選択しました");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }else if(jobAfter.get(damager).getNumber() == 2){
                                    if(!jobAfter.get(damager).result.containsKey(player)){
                                        if(damager.getInventory().getItemInHand().getId() == DivinerItem){
                                            jobAfter.get(damager).setTarget(player);
                                        }
                                    }
                                }else if(jobAfter.get(damager).getNumber() == 4){
                                    //if(!jobAfter.get(damager).result.containsKey(player)){
                                        if(damager.getInventory().getItemInHand().getId() == GuardItem){
                                            jobAfter.get(damager).setTarget(player);
                                        }
                                    //}
                                }
                            }else{
                                if(jobAfter.get(damager).getNumber() == 3){
                                    if(!jobAfter.get(damager).result.containsKey(player)){
                                        if(damager.getInventory().getItemInHand().getId() == PsychicItem){
                                            jobAfter.get(damager).setTarget(player);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*@EventHandler
    public static void onAttack(EntityDamageEvent event){
        Entity entity = event.getEntity();
        event.setCancelled();
        if(event instanceof EntityDamageByEntityEvent){
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
            Entity d = ev.getDamager();
            if(entity instanceof Player && d instanceof Player){
                Player player = (Player) entity;
                Player damager = (Player) d;
                if(jobAfter.containsKey(damager) && jobAfter.containsKey(player)){
                    if(TimeType == 30){
                        if(isLife.get(damager)){
                            if(isLife.get(player)){
                                Vote(damager,player);
                            }
                        }
                    }
                    if(TimeType == 31){
                        if(isLife.get(damager)){
                            if(isLife.get(player)){
                                DecisiveVote(damager,player);
                            }
                        }
                    }
                    if(TimeType == 10 || TimeType == 11 || TimeType == 12){
                        if(isLife.get(damager)){
                            if(isLife.get(player)){
                                if(jobAfter.get(damager).getNumber() == 1){
                                    if(jobAfter.get(player).getNumber() != 1){
                                        if(damager.getInventory().getItemInHand().getId() == WereWolfItem){
                                            jobAfter.get(damager).setTarget(player);
                                            for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
                                                if(e.getValue()){
                                                    if(jobAfter.get(e.getKey()).getNumber() == 1){
                                                        e.getKey().sendMessage(player.getName()+"を選択しました");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }else if(jobAfter.get(damager).getNumber() == 2){
                                    if(!jobAfter.get(damager).result.containsKey(player)){
                                        if(damager.getInventory().getItemInHand().getId() == DivinerItem){
                                            jobAfter.get(damager).setTarget(player);
                                        }
                                    }
                                }
                            }else{
                                if(jobAfter.get(damager).getNumber() == 3){
                                    if(!jobAfter.get(damager).result.containsKey(player)){
                                        if(damager.getInventory().getItemInHand().getId() == PsychicItem){
                                            jobAfter.get(damager).setTarget(player);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }*/

    public static Boolean Join(Player player){
        if(TimeType == 0){
            if(!isLife.containsKey(player)){
                isLife.put(player, true);
                player.sendMessage("人狼ゲームに参加しました");
                return true;
            }
        }
        return false;
    }

    public static void Quit(Player player){
        if(TimeType == 0) isLife.remove(player);
        SuddenDeath.put(player,0);
    }

    public static void Start(){
        if(TimeType != 0) return;
        if(isLife.size() < 2) return;//3
        String[] jobslist = jobs.split(",");
        //TODO Automatic correspondence of cast
        if(jobslist.length != isLife.size()) return;
        Integer[] joblist = new Integer[jobslist.length];
        for(int i = 0;i < jobslist.length;i++){
            joblist[i] = Integer.parseInt(jobslist[i]);
        }
        List<Integer> list=Arrays.asList(joblist);
        Collections.shuffle(list);
        joblist =(Integer[])list.toArray(new Integer[list.size()]);
        int count = 0;
        Job job;
        for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
            job = getJobByNumber(joblist[count],e.getKey());
            jobAfter.put(e.getKey(),job);
            jobBefore.put(e.getKey(),job);
            isVoted.put(e.getKey(),false);
            EntityId.put(e.getKey(),Entity.entityCount++);
            e.getKey().sendMessage(jobAfter.get(e.getKey()).getName());
            count++;
        }
        TimeType = 10;
        checkMember();
        if(TimeType == 0) return;
        SuddenDeath.clear();
        NightCount = 0;
        MeetingCount = 0;
        tpCenter();
        Night();
    }

    public static Job getJobByNumber(int type,Player player){
        switch(type){
            case 0:
                return new Job_Villager(player);
            case 1:
                return new Job_WereWolf(player);
            case 2:
                return new Job_Diviner(player);
            case 3:
                return new Job_Psychic(player);
            case 4:
                return new Job_Guard(player);
            case 5:
                return new Job_Madman(player);
            case 6:
                return new Job_Couple(player);
            case 7:
                return new Job_Fanatic(player);
            default:
                return new Job_Villager(player);
        }
    }

    public static void Night(){
        NightCount++;
        checkMember();
        if(TimeType == 0) return;
        for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
            if(e.getValue()){
                jobAfter.get(e.getKey()).Night();
            }
            e.getKey().sendMessage("恐ろしい夜がやってきました\nこれより各プレイヤーは夜のアクションを完了してください");
        }
        tpCenter();
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackNight(),600);
    }

    public static void finishNight(){
        Player WolfTarget = null;
        Player GuardTarget = null;
        Player DivinerTarget = null;
        HashMap<Player,Integer> death = new HashMap<Player,Integer>();
        Job job;
        for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
            if(e.getValue()){
                job = jobAfter.get(e.getKey());
                switch(job.getNumber()){
                    case 1:
                        WolfTarget = job.getWereWolfTarget();
                    break;
                    case 2:
                        DivinerTarget = job.getDivinerTarget();
                    break;
                    case 4:
                        GuardTarget = job.getGuardTarget();
                    break;
                }
                job.reset();
                e.getKey().getInventory().clearAll();
            }
        }
        if(DivinerTarget != null && jobAfter.get(DivinerTarget).getNumber() == 62) death.put(DivinerTarget,3);
        if(WolfTarget != null && !(WolfTarget.equals(GuardTarget))) death.put(WolfTarget,1);
        for(Map.Entry<Player,Integer> ee : SuddenDeath.entrySet()){
            if(!death.containsKey(ee.getKey())) death.put(ee.getKey(),0);
        }
        SuddenDeath.clear();
        Job.reset2();
        tpCenter();
        Death(death);
        Meeting();
    }

    public static void Meeting(){
        MeetingCount++;
        TimeType = 22;
        if(MeetingCount == 1) TimeType = 20;
        else if(MeetingCount == 2) TimeType = 21;
        checkMember();
        if(TimeType == 0) return;
        for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
            for(Map.Entry<Player,Boolean> ee : Main.isLife.entrySet()){
                if(!(e.getKey().equals(ee.getKey()))){
                    Main.switchPlayer(e.getKey(),ee.getKey());
                }
            }
            e.getKey().sendMessage("朝がやってきました\nこれより話し合いを始めて今夜処刑する人を一人選んでください");
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackMeeting(),600);
    }

    public static void finishMeeting(){
        startVote();
    }

    public static void startVote(){
        TimeType = 30;
        for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
            if(e.getValue()){
                VoteCount.put(e.getKey(),0);
                isVoted.put(e.getKey(),false);
            }
            for(Map.Entry<Player,Boolean> ee : isLife.entrySet()){
                if(!(e.getKey().equals(ee.getKey()))){
                    switchNPC(e.getKey(),ee.getKey());
                }
            }
            e.getKey().sendMessage("話し合いの時間が終了しました\nこれより処刑したい人物を殴って選択してください");
        }
        tpCenter();
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackVote(),600);
    }

    public static void Vote(Player player,Player target){
        if(isVoted.get(player)) return;
        int vote = 1;
        VoteCount.put(target,VoteCount.get(target) + vote);
        isVoted.put(player, true);
        player.sendMessage(target.getName()+"に投票しました");
    }

    public static void finishVote(){
        for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
            e.getKey().sendMessage("投票を締め切りました");
        }
        HashMap<Player,Integer> remove = new HashMap<Player,Integer>();
        for(Map.Entry<Player,Integer> e : VoteCount.entrySet()){
            if(!isLife.get(e.getKey())) remove.put(e.getKey(),0);
        }
        for(Map.Entry<Player,Integer> e : remove.entrySet()){
            VoteCount.remove(e.getKey());
        }
        List<Map.Entry<Player,Integer>> sort = Sort(VoteCount);
        int max = sort.get(0).getValue();
        HashMap<Player,Integer> more = new HashMap<Player,Integer>();
        for(int i = 0;i < sort.size();i++){
            if(sort.get(i).getValue() == max){
                more.put(sort.get(i).getKey(),max);
            }
        }
        HashMap<Player,Integer> death = new HashMap<Player,Integer>();
        if(more.size() > 1){
            startDecisiveVote(more);
        }else{
            death.put(sort.get(0).getKey(),2);
            Death(death);
            Night();
        }
    }

    public static void startDecisiveVote(HashMap<Player,Integer> entry){
        TimeType = 31;
        DecisiveEntry = entry;
        String entries = "";
        for(Map.Entry<Player,Integer> e : entry.entrySet()){
            if(entries.equals("")) entries += e.getKey().getName();
            else entries += ","+e.getKey().getName();
        }
        for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
            if(e.getValue()){
                VoteCount.put(e.getKey(),0);
                isVoted.put(e.getKey(),false);
            }
            e.getKey().sendMessage(entries+"の票が同数のため決選投票を行います\nこれより処刑したい人物を殴って選択してください");
        }
        Server.getInstance().getScheduler().scheduleDelayedTask(new CallbackDecisiveVote(),600);
    }

    public static void DecisiveVote(Player player,Player target){
        if(isVoted.get(player)) return;
        if(!DecisiveEntry.containsKey(target)) return;
        int vote = 1;
        VoteCount.put(target,VoteCount.get(target) + vote);
        isVoted.put(player, true);
        player.sendMessage(target.getName()+"に投票");
    }

    public static void finishDecisiveVote(){
        for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
            e.getKey().sendMessage("投票を締め切りました");
        }
        HashMap<Player,Integer> remove = new HashMap<Player,Integer>();
        for(Map.Entry<Player,Integer> e : VoteCount.entrySet()){
            if(!DecisiveEntry.containsKey(e.getKey())) remove.put(e.getKey(),0);
        }
        for(Map.Entry<Player,Integer> e : remove.entrySet()){
            VoteCount.remove(e.getKey());
        }
        List<Map.Entry<Player,Integer>> sort = Sort(VoteCount);
        int max = sort.get(0).getValue();
        HashMap<Player,Integer> more = new HashMap<Player,Integer>();
        for(int i = 0;i < sort.size();i++){
            if(sort.get(i).getValue() == max){
                more.put(sort.get(i).getKey(),max);
            }
        }
        HashMap<Player,Integer> death = new HashMap<Player,Integer>();
        if(more.size() > 1){
            int count = more.size();
            int random = (int)(Math.random() * (count - 1));
            death.put(sort.get(random).getKey(),2);
            Death(death);
            Night();
        }else{
            death.put(sort.get(0).getKey(),2);
            Death(death);
            Night();
        }
    }

    public static List Sort(Map<Player,Integer> map){
        Map<Player, Integer> hashMap = new HashMap<Player, Integer>();
        for (Map.Entry<Player,Integer> e : map.entrySet()){
            hashMap.put(e.getKey(),e.getValue());
        }
        List<Map.Entry<Player,Integer>> entries =
              new ArrayList<Map.Entry<Player,Integer>>(hashMap.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<Player,Integer>>() {
            @Override
            public int compare(
                  Entry<Player,Integer> entry1, Entry<Player,Integer> entry2) {
                return ((Integer)entry2.getValue()).compareTo((Integer)entry1.getValue());
            }
        });
        return entries;
    }

    //Death Reason
    // 0 突然死   1 人狼にかまれる   2 投票   3 呪殺   4 ....
    public static void Death(HashMap<Player,Integer> death){
        if(death.size() == 0){
            for(Map.Entry<Player,Boolean> ee : isLife.entrySet()){
                ee.getKey().sendMessage("今夜の犠牲者はいませんでした");
            }
            return;
        }
        for(Map.Entry<Player,Integer> e : death.entrySet()){
            isLife.put(e.getKey(),false);
            for(Map.Entry<Player,Boolean> ee : isLife.entrySet()){
                switch(e.getValue()){
                    case 0:
                        ee.getKey().sendMessage(e.getKey().getName()+"が突然死しました");
                    break;
                    case 1:
                        ee.getKey().sendMessage(e.getKey().getName()+"が無残な姿で発見されました");
                    break;
                    case 2:
                        ee.getKey().sendMessage(e.getKey().getName()+"が投票によって処刑されました");
                    break;
                    case 3:
                        ee.getKey().sendMessage(e.getKey().getName()+"が無残な姿で発見されました");
                    break;
                    default:
                        ee.getKey().sendMessage(e.getKey().getName()+"が謎の死を遂げました");
                    break;
                }
            }
        }
    }

    public static void checkMember(){
        int Village = 0;
        int Wolf = 0;
        int Fox = 0;
        for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
            if(e.getValue()){
                switch(jobAfter.get(e.getKey()).getNumber()){
                    case 1:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                    case 48:
                    case 49:
                    case 50:
                    case 51:
                    case 52:
                    case 53:
                        Wolf++;
                    break;
                    case 62:
                    case 63:
                        Fox++;
                    break;
                    default:
                        Village++;
                    break;
                }
            }
        }
        if(Wolf == 0){
            if(Fox == 0){
                Win(0);
            }else{
                Win(2);
            }
        }else{
            if(Wolf > Village){//=
                Win(1);
            }
        }
    }

    public static void Win(int side){
        switch(side){
            case 0:
                for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
                    e.getKey().sendMessage("村の勝ち");
                }
            break;
            case 1:
                for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
                    e.getKey().sendMessage("人狼の勝ち");
                }
            break;
            case 2:
                for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
                    e.getKey().sendMessage("狐の勝ち");
                }
            break;
        }
        reset();
    }

    public static void tpCenter(){
        int dv = isLife.size();
        double x0 = Center.x;
        double z0 = Center.z;
        double x1;
        double z1;
        double rabius = 8;
        int i = 0;
        double mx;
        double mz;
        for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
            x1 = x0 + rabius * Math.sin(2 * Math.PI * i / dv);
            z1 = z0 + rabius * Math.cos(2 * Math.PI * i / dv);
            System.out.println(x1+":"+z1);
            mx = Center.x - x1;
            mz = Center.z - z1;
            double yaw = getYaw(mx,mz);
            pos.put(e.getKey(),new Location(x1,Center.y,z1,yaw,0,Server.getInstance().getDefaultLevel()));
            e.getKey().teleport(new Location(x1,Center.y,z1,yaw,0,Server.getInstance().getDefaultLevel()));
            i++;
        }
    }

    public static double getYaw(double mx,double mz) {
        double yaw = 0;
        if (mz == 0) {
            if (mx < 0) {
                yaw = -90;
            }else {
                yaw = 90;
            }
        }else {
            if (mx >= 0 && mz > 0) {
                double atan = Math.atan(mx/mz);
                yaw = rad2deg(atan);
            }else if (mx >= 0 && mz < 0) {
                double atan = Math.atan(mx/Math.abs(mz));
                yaw = 180 - rad2deg(atan);
            }else if (mx < 0 && mz < 0) {
                double atan = Math.atan(mx/mz);
                yaw = -(180 - rad2deg(atan));
            }else if (mx < 0 && mz > 0) {
                double atan = Math.atan(Math.abs(mx)/mz);
                yaw = -(rad2deg(atan));
            }
        }

        yaw = - yaw;
        return yaw;
    }

    public static double rad2deg(double radian) {
        return radian * (180f / Math.PI);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static void switchNPC(Player player,Player target){
        if(isLife.get(target)){
            player.hidePlayer(target);
            UUID uuid = UUID.randomUUID();
            AddPlayerPacket pk = new AddPlayerPacket();
            pk.entityRuntimeId = EntityId.get(target);
            pk.entityUniqueId = EntityId.get(target);
            pk.uuid = uuid;
            pk.x = (float)pos.get(target).x;
            pk.y = (float)pos.get(target).y;
            pk.z = (float)pos.get(target).z;
            pk.yaw = (float)pos.get(target).yaw;
            pk.pitch = (float)pos.get(target).pitch;
            pk.username = "";
            int flags = 0;
            flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
            flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
            //flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
            pk.metadata = new EntityMetadata()
                    .putLong(Entity.DATA_FLAGS,flags)
                    .putString(Entity.DATA_NAMETAG, target.getNameTag())
                    .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1)
                    .putByte(Entity.DATA_LEAD, 0);
            player.dataPacket(pk);
            PlayerListPacket pkk = new PlayerListPacket();
            pkk.type = PlayerListPacket.TYPE_ADD;
            pkk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid, pk.entityRuntimeId, "",target.getSkin())};
            player.dataPacket(pkk);
            PlayerListPacket pkkk = new PlayerListPacket();
            pkkk.type = PlayerListPacket.TYPE_REMOVE;
            pkkk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid)};
            player.dataPacket(pkkk);
        }else{
            player.hidePlayer(target);
            UUID uuid = UUID.randomUUID();
            AddPlayerPacket pk = new AddPlayerPacket();
            pk.entityRuntimeId = EntityId.get(target);
            pk.entityUniqueId = EntityId.get(target);
            pk.uuid = uuid;
            pk.x = (float)pos.get(target).x;
            pk.y = (float)pos.get(target).y;
            pk.z = (float)pos.get(target).z;
            pk.yaw = (float)pos.get(target).yaw;
            pk.pitch = (float)pos.get(target).pitch;
            pk.username = "";
            int flags = 0;
            flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
            flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
            //flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
            int playerflags = 0;
            playerflags ^= 1 << EntityHuman.DATA_PLAYER_FLAG_SLEEP;
            pk.metadata = new EntityMetadata()
                    .putLong(Entity.DATA_FLAGS,flags)
                    .putString(Entity.DATA_NAMETAG, target.getNameTag())
                    .putLong(Entity.DATA_LEAD_HOLDER_EID, (long)-1)
                    .putByte(Entity.DATA_LEAD, 0)
                    .putByte(EntityHuman.DATA_PLAYER_FLAGS,playerflags)
                    .put(new IntPositionEntityData(EntityHuman.DATA_PLAYER_BED_POSITION,(int)pk.x,(int)pk.y,(int)pk.z));
            player.dataPacket(pk);
            PlayerListPacket pkk = new PlayerListPacket();
            pkk.type = PlayerListPacket.TYPE_ADD;
            pkk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid, pk.entityRuntimeId, "",target.getSkin())};
            player.dataPacket(pkk);
            PlayerListPacket pkkk = new PlayerListPacket();
            pkkk.type = PlayerListPacket.TYPE_REMOVE;
            pkkk.entries = new PlayerListPacket.Entry[]{new PlayerListPacket.Entry(uuid)};
            player.dataPacket(pkkk);
            MovePlayerPacket pkkkk = new MovePlayerPacket();
            pkkkk.eid = pk.entityRuntimeId;
            pkkkk.x = (float)pk.x;
            pkkkk.y = (float)(pk.y+0.5);
            pkkkk.z = (float)pk.z;
            pkkkk.yaw = 0;
            pkkkk.pitch = 0;
            pkkkk.mode = MovePlayerPacket.MODE_NORMAL;
        }
    }

    public static void switchPlayer(Player player,Player target){
        if(isLife.get(target)){
            player.showPlayer(target);
            RemoveEntityPacket pk = new RemoveEntityPacket();
            pk.eid = EntityId.get(target);
            player.dataPacket(pk);
        }else{
            switchNPC(player,target);
        }
    }

    public static void resetPlayer(Player player,Player target){
        player.showPlayer(target);
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = EntityId.get(target);
        player.dataPacket(pk);
    }

    public static void reset(){
        for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
            for(Map.Entry<Player,Boolean> ee : Main.isLife.entrySet()){
                if(!(e.getKey().equals(ee.getKey()))){
                    Main.resetPlayer(e.getKey(),ee.getKey());
                }
            }
        }
        jobAfter = new HashMap<Player,Job>();
        jobBefore = new HashMap<Player,Job>();
        pos = new HashMap<Player,Location>();
        EntityId = new HashMap<Player,Long>();
        SuddenDeath = new HashMap<Player,Integer>();
        isLife = new HashMap<Player,Boolean>();
        isVoted = new HashMap<Player,Boolean>();
        VoteCount = new HashMap<Player,Integer>();
        NightCount = 0;
        MeetingCount = 0;
        TimeType = 0;
        for(Map.Entry<UUID,Player> e : Server.getInstance().getOnlinePlayers().entrySet()){
            Join(e.getValue());
        }
    }

    public static final int WereWolfItem = 268;
    public static final int DivinerItem = 340;
    public static final int PsychicItem = 340;
    public static final int GuardItem = 272;
    public static Vector3 Center;
    public static String jobs = "0,0,1";
    public static HashMap<Player,Job> jobAfter = new HashMap<Player,Job>();
    public static HashMap<Player,Job> jobBefore = new HashMap<Player,Job>();
    // 0 *Villager (村人)                   1 *WereWolf (人狼)                   2 *Diviner (予言者)
    // 3 *Psychic (霊媒師)                  4 *Guard (騎士)                      5 *Madman (狂人)
    // 6 *Couple (共有者)                   7 Fanatic (狂信者)                   8 Poisoner (埋毒者)
    // 9 Cat (猫又)                        10 ToughGuy (タフガイ)               11 Dog (犬)
    // 12 Noble (貴族)                     13 Slave (奴隷)                      14 Magician (魔術師)
    // 15 Fugitive (逃亡者)                16 Merchant (商人)                   17 QueenSpectator (女王観戦者)
    // 18 Liar (嘘つき)                    19 ApprenticeSeer (見習い予言者)     20 Diseased (病人)
    // 21 Cursed (呪われた者)              22 Spellcaster (呪いをかける者)      23 Lycan (狼憑き)
    // 24 Priest (聖職者)                  25 Prince (プリンス)                 26 PI (超常現象研究者)
    // 27 Witch (魔女)                     28 OldMan (老人)                     29 Dictator (独裁者)
    // 30 SeersMother (予言者のママ)       31 Trapper (罠師)                    32 OccultMania (オカルトマニア)
    // 33 Counselor (カウンセラー)         34 Miko (巫女)                       35 RedHood (赤ずきん)
    // 36 WanderingGuard (風来狩人)        37 TroubleMaker (トラブルメーカー)   38 FrankensteinsMonster (フランケンシュタインの怪物)
    // 39 King (王様)                      40 Phantom (怪盗)                    41 DrawGirl (看板娘)
    // 42 Baker (パン屋)                   43 WolfDiviner (人狼占い)            44 BigWolf (大狼)
    // 45 WolfCub (狼の子)                 46 MedWolf (狂人狼)                  47 LoneWolf (一匹狼)
    // 48 GreedyWolf (欲張りな狼)          49 FascinatingWolf (誘惑する女狼)    50 SolitudeWolf (孤独な狼)
    // 51 ToughWolf (一途な狼)             52 ThreateningWolf (威嚇する狼)      53 CautiousWolf (慎重な狼)
    // 54 WolfBoy (狼少年)                 55 Sorcerer (妖術師)                 56 WhisperingMad (囁き狂人)
    // 57 ObstructiveMad (邪魔狂人)        58 Spy (スパイ)                      59 Spy2 (スパイⅡ)
    // 60 PsychoKiller (サイコキラー)      61 Bomber (爆弾魔)                   62 Fox (妖狐)
    // 63 TinyFox (子狐)                   64 Immoral (背徳者)                  65 Defiler (冒涜者)
    // 66 Devil (悪魔くん)                 67 Vampire (ヴァンパイア)            68 Cupid (キューピッド)
    // 69 Lover (求愛者)                   70 BadLady (悪女)                    71 Pathisie (パティシエール)
    // 72 CultLeader (カルトリーダー)      73 Tanner (皮なめし職人)             74 Bat (こうもり)
    // 75 Hoodlum (ならず者)               76 Stalker (ストーカー)              77 Copier (コピー)
    // 78 Doppleganger (ドッペルゲンガー)
    public static HashMap<Player,Location> pos = new HashMap<Player,Location>();
    public static HashMap<Player,Long> EntityId = new HashMap<Player,Long>();
    public static HashMap<Player,Boolean> isLife = new HashMap<Player,Boolean>();
    public static HashMap<Player,Integer> SuddenDeath = new HashMap<Player,Integer>();
    public static HashMap<Player,Boolean> isVoted = new HashMap<Player,Boolean>();
    public static HashMap<Player,Integer> VoteCount = new HashMap<Player,Integer>();
    public static HashMap<Player,Integer> DecisiveEntry = new HashMap<Player,Integer>();
    public static Integer NightCount = 0;
    public static Integer MeetingCount = 0;
    public static Integer TimeType = 0;
    //  0 NotGameNow
    // 10 FirstNight   11 SecondsNight   12 Night
    // 20 FirstMeeting   21 SecondsMeeting   22 Meeting
    // 30 Voting   31 DecisiveVoting

}
class CallbackNight extends Task{

    public CallbackNight(){
    }

    public void onRun(int d){
        Main.finishNight();
    }
}
class CallbackMeeting extends Task{

    public CallbackMeeting(){
    }

    public void onRun(int d){
        Main.finishMeeting();
    }
}
class CallbackVote extends Task{

    public CallbackVote(){
    }

    public void onRun(int d){
        Main.finishVote();
    }
}
class CallbackDecisiveVote extends Task{

    public CallbackDecisiveVote(){
    }

    public void onRun(int d){
        Main.finishDecisiveVote();
    }
}

