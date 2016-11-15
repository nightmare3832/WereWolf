package sote.Jobs;

import cn.nukkit.Player;

public class Job_Villager extends Job{

    public Job_Villager(Player player){
        this.owner = player;
    }

    @Override
    public void Night(){
    }

    @Override
    public int getSide(){
        return 0;
    }

    @Override
    public int getTalkRoom(){
        return 0;
    }

    @Override
    public int getNumber(){
        return 0;
    }

    @Override
    public String getName(){
        return "村人";
    }

}