package sote.Jobs;

import java.util.Map;

import cn.nukkit.Player;
import sote.Main;

public class Job_Immoral extends Job{

    public Job_Immoral(Player player){
        this.owner = player;
    }

    @Override
    public void Night(){
        String foxs = "";
        for(Map.Entry<Player,Boolean> e : Main.isLife.entrySet()){
            if(Main.jobAfter.get(e.getKey()).getNumber() == 62){
                if(foxs.equals("")) foxs += e.getKey().getName();
                else foxs += ","+e.getKey().getName();
            }
            if(!(e.getKey().equals(this.owner))){
                Main.switchNPC(this.owner,e.getKey());
            }
        }
        if(Main.TimeType == 10) this.owner.sendMessage("妖狐は"+foxs+"です");
    }

    @Override
    public void setTarget(Player player){
    }

    @Override
    public int getSide(){
        return 2;
    }

    @Override
    public int getTalkRoom(){
        return 0;
    }

    @Override
    public int getNumber(){
        return 64;
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
        return "背徳者";
    }

}