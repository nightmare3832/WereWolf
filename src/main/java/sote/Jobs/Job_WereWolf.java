package sote.Jobs;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

public class Job_WereWolf extends Job{

    public Job_WereWolf(Player player){
        this.owner = player;
    }

    @Override
    public void Night(){
        this.owner.getInventory().addItem(new Item(268));
    }

    @Override
    public void setTarget(Player player){
        WereWolfTarget = player;
    }

    @Override
    public int getSide(){
        return 1;
    }

    @Override
    public int getTalkRoom(){
        return 1;
    }

    @Override
    public int getNumber(){
        return 1;
    }

    @Override
    public String getName(){
        return "人狼";
    }

}