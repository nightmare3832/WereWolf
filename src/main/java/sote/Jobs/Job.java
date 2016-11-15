package sote.Jobs;

import java.util.HashMap;

import cn.nukkit.Player;

public abstract class Job{

    public Job(){
    }

    public void setOwner(Player player){
        this.owner = player;
    }

    public int getSide(){
        return 0;// 0 村  1 狼  2 狐  3 ...
    }

    public int getTalkRoom(){
        return 0;// 0 null  1 狼  2 共有者  3 ....
    }

    public void Night(){
    }

    public void setTarget(Player player){
    }

    public Player getWereWolfTarget(){
        return WereWolfTarget;
    }

    public Player getDivinerTarget(){
        return DivinerTarget;
    }

    public int getNumber(){
        return 0;
    }

    public String getDivinerResult(){
        return "人間";
    }

    public String getName(){
        return "村人";
    }

    public static void reset(){
        WereWolfTarget = null;
    }

    public static Player WereWolfTarget = null;
    public Player DivinerTarget = null;
    public Boolean used = false;
    public HashMap<Player,String> result = new HashMap<Player,String>();
    public Player owner = null;

}