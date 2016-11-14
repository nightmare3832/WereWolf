package sote;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import sote.Jobs.Job;
import sote.Jobs.Job_Villager;
import sote.Jobs.Job_WereWolf;

public class Main extends PluginBase implements Listener{

    public Main(){
    }

    public void onEnable(){
        registerCommands();
        getServer().getPluginManager().registerEvents(this, this);
        super.onEnable();
    }

    public void registerCommands(){
        //getServer().getCommandMap().register("packet",new Command_SendPacket(this));
    }

    public static Boolean Join(Player player){
        if(TimeType == 0){
            if(!isLife.containsKey(player)){
                
            }
        }
        return false;
    }

    public static void Start(){
        if(isLife.size() < 3) return;
        String[] jobslist = jobs.split(",");
        if(jobslist.length != isLife.size()) return;
        Integer[] joblist = new Integer[]{};
        for(int i = 0;i <= jobslist.length;i++){
            joblist[i] = Integer.parseInt(jobslist[i]);
        }
        List<Integer> list=Arrays.asList(joblist);
        Collections.shuffle(list);
        joblist =(Integer[])list.toArray(new Integer[list.size()]);
        int count = 0;
        for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
            jobBefore.put(e.getKey(),getJobByNumber(joblist[count]));
            count++;
        }
        TimeType = 10;
        checkMember();
        Night();
    }

    public static Job getJobByNumber(int type){
        switch(type){
            case 0:
                return new Job_Villager();
            case 1:
                return new Job_WereWolf();
            default:
                return new Job_Villager();
        }
    }

    public static void Night(){
        for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
            if(e.getValue()){
                jobAfter.get(e.getKey()).Night();
            }
        }
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
                }
            }
        }
        death.put(WolfTarget,1);
        Death(death);
    }

    //Death Reason
    // 0 突然死   1 人狼にかまれる   2 ....
    public static void Death(HashMap<Player,Integer> death){
        for(Map.Entry<Player,Integer> e : death.entrySet()){
            isLife.put(e.getKey(),false);
        }
    }

    public static void checkMember(){
        int Village = 0;
        int Wolf = 0;
        int Fox = 0;
        for(Map.Entry<Player,Boolean> e : isLife.entrySet()){
            if(e.getValue()){
                switch(jobAfter.get(e.getKey()).getSide()){
                    case 0:
                        Village++;
                    break;
                    case 1:
                        Wolf++;
                    break;
                    case 2:
                        Fox++;
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
            if(Wolf > Village){
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
    }

    public static String jobs = "0,0,1";
    public static HashMap<Player,Job> jobAfter = new HashMap<Player,Job>();
    public static HashMap<Player,Job> jobBefore = new HashMap<Player,Job>();
    // 0 Villager (村人)                    1 WereWolf (人狼)                    2 Diviner (予言者)
    // 3 Psychic (霊媒師)                   4 Guard (騎士)                       5 Madman (狂人)
    // 6 Couple (共有者)                    7 Fanatic (狂信者)                   8 Poisoner (埋毒者)
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
    public static HashMap<Player,Boolean> isLife = new HashMap<Player,Boolean>();
    public static Integer TimeType = 0;
    // 0 NotGameNow
    //10 FirstNight   11 SecondsNight   12 Night
    //20 FirstMeeting   21 SecondsMeeting   22 Meeting
    //30 Voting

}