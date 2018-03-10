package igs;

public class Topic implements Comparable<Topic>
{
    private int tid;
    private String tname;
    private double trank;
    
    public Topic(int tid, String tname, double trank)
    {
        this.tid = tid;
        this.tname = tname;
        this.trank = trank;
    }
    
    public int getTid()
    {
        return tid;
    }
    
    public double getRank()
    {
        return trank;
    }
    
    public String getName()
    {
        return tname.toUpperCase();
    }

    public int compareTo(Topic o) 
    {
        return Double.compare(trank, o.trank);
    }
    
    
}
