package sote.Jobs;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import sote.Main;

public class Job_Diviner extends Job{

    public Job_Diviner(Player player){
        this.owner = player;
    }

    @Override
    public void Night(){
        Item item = new Item(Main.DivinerItem);
        this.owner.getInventory().setHotbarSlotIndex(0,0);
        this.owner.getInventory().setItem(0,item);
    }

    @Override
    public void setTarget(Player player){
        if(this.used) return;
        result.put(player,Main.jobAfter.get(player).getDivinerResult());
        this.owner.sendMessage(player.getName()+"は"+Main.jobAfter.get(player).getDivinerResult()+"です");
        this.used = true;
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
        return 2;
    }

    @Override
    public String getDivinerResult(){
        return "人間";
    }

    @Override
    public String getName(){
        return "予言者";
    }

}