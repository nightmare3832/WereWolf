package sote.Jobs;

public class Job_WereWolf extends Job{

    public Job_WereWolf(){
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