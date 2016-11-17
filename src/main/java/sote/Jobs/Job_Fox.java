package sote.Jobs;

import java.util.Map;

import cn.nukkit.Player;
import sote.Main;

public class Job_Fox extends Job{

    public Job_Fox(Player player){
        this.owner = player;
    }

    @Override
    public void Night(){
        for(Map.Entry<Player,Boolean> e : Main.isLife.entrySet()){
            if(!(e.getKey().equals(this.owner))){
                Main.switchNPC(this.owner,e.getKey());
            }
        }
    }

    @Override
    public void setTarget(Player player){
        WereWolfTarget = player;
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
        return 62;
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
        return "妖狐";
    }

}