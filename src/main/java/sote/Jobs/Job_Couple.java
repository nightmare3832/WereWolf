package sote.Jobs;

import java.util.Map;

import cn.nukkit.Player;
import sote.Main;

public class Job_Couple extends Job{

    public Job_Couple(Player player){
        this.owner = player;
    }

    @Override
    public void Night(){
        for(Map.Entry<Player,Boolean> e : Main.isLife.entrySet()){
            if(!(e.getKey().equals(this.owner))){
                if(Main.jobAfter.get(e.getKey()).getNumber() != 6){
                    Main.switchNPC(this.owner,e.getKey());
                }else{
                    Main.switchPlayer(this.owner,e.getKey());
                }
            }
        }
    }

    @Override
    public void setTarget(Player player){
    }

    @Override
    public int getSide(){
        return 0;
    }

    @Override
    public int getTalkRoom(){
        return 2;
    }

    @Override
    public int getNumber(){
        return 6;
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