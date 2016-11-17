package sote.Jobs;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import sote.Main;

public class Job_WereWolf extends Job{

    public Job_WereWolf(Player player){
        this.owner = player;
    }

    @Override
    public void Night(){
        for(Map.Entry<Player,Boolean> e : Main.isLife.entrySet()){
            if(!(e.getKey().equals(this.owner))){
                if(Main.jobAfter.get(e.getKey()).getNumber() != 1){
                    Main.switchNPC(this.owner,e.getKey());
                }else{
                    Main.switchPlayer(this.owner,e.getKey());
                }
            }
        }
        Item item = new Item(Main.WereWolfItem);
        this.owner.getInventory().setHotbarSlotIndex(0,0);
        this.owner.getInventory().setItem(0,item);
        this.owner.getInventory().sendContents(this.owner);
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
    public String getDivinerResult(){
        return "人狼";
    }

    @Override
    public String getPsychicResult(){
        return "人狼";
    }

    @Override
    public String getName(){
        return "人狼";
    }

}