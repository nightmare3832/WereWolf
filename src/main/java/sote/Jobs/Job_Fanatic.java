package sote.Jobs;

import java.util.Map;

import cn.nukkit.Player;
import sote.Main;

public class Job_Fanatic extends Job{

    public Job_Fanatic(Player player){
        this.owner = player;
    }

    @Override
    public void Night(){
        String wolfs = "";
        for(Map.Entry<Player,Boolean> e : Main.isLife.entrySet()){
            if(Main.jobAfter.get(e.getKey()).getNumber() == 1){
                if(wolfs.equals("")) wolfs += e.getKey().getName();
                else wolfs += ","+e.getKey().getName();
            }
            if(!(e.getKey().equals(this.owner))){
                Main.switchNPC(this.owner,e.getKey());
            }
        }
        if(Main.TimeType == 10) this.owner.sendMessage("人狼は"+wolfs+"です");
    }

    @Override
    public void setTarget(Player player){
    }

    @Override
    public int getSide(){
        return 1;
    }

    @Override
    public int getTalkRoom(){
        return 0;
    }

    @Override
    public int getNumber(){
        return 7;
    }

    @Override
    public String getDivinerResult(){
        return "人間";
    }

    @Override
    public String getPsychicResult(){
        return "人間";
    }

    @Override
    public String getName(){
        return "狂信者";
    }

}