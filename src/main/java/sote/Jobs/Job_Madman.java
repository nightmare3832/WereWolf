package sote.Jobs;

import java.util.Map;

import cn.nukkit.Player;
import sote.Main;

public class Job_Madman extends Job{

    public Job_Madman(Player player){
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
        return 5;
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
        return "狂人";
    }

}