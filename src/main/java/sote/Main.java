package sote;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import sote.Jobs.Job;
import sote.commands.Command_SendPacket;

public class Main extends PluginBase implements Listener{

    public Main(){
    }

    public void onEnable(){
        registerCommands();
        getServer().getPluginManager().registerEvents(this, this);
        super.onEnable();
    }

    public void registerCommands(){
        getServer().getCommandMap().register("packet",new Command_SendPacket(this));
    }

    public static Boolean Join(Player player){
        if(TimeType == 0){
            if(!isLife.containsKey(player)){
                
            }
        }
        return false;
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
            break;
            case 1:
            break;
            case 2:
            break;
        }
    }

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
    public static Integer TimeType = 0;// 0 NotGameNow // 10 Night // 11 Meeting // 12 Voting

}