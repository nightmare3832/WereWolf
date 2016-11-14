package sote.Jobs;

import cn.nukkit.Player;

public abstract class Job{

    public Job(){
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

    public int getNumber(){
        return 0;
    }

    public String getName(){
        return "";
    }

    public static void reset(){
        WereWolfTarget = null;
    }

    public static Player WereWolfTarget = null;

}