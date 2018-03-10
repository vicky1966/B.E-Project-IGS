package igs;

public class ResponseData 
{
    private int counter;
    private double noReply;
    private double fastReply;
    private double slowReply;
    private double percentage;
    private String status;
    
    public ResponseData(double nR, double fR, double sR, double p)
    {
        counter = 1;
        noReply = nR;
        fastReply = fR;
        slowReply = sR;
        percentage = p;
        this.status = "";
    }
    
    public void incrementCounter()
    {
        counter++;
    }
    
    public int getCounter()
    {
        return counter;
    }
    
    public double getNoReplyPercentage()
    {
        return noReply;
    }
    
    public double getFastReplyPercentage()
    {
        return fastReply;
    }
    
    public double getSlowReplyPercentage()
    {
        return slowReply;
    }
    
    public double getPercentage()
    {
        return percentage;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setSlowReplyPercentage(double x)
    {
        slowReply = x;
    }
    
    public void setFastReplyPercentage(double x)
    {
        fastReply = x;
    }
    
    public void setNoReplyPercentage(double x)
    {
        noReply = x;
    }

    public void setPercentage(double x)
    {
        percentage = x;
    }

    public void setStatus(String s)
    {
        status = s.toUpperCase();
    }
    
    public void decrementPercentage()
    {
        if(percentage >0)
            percentage--;
    }

}//responseMetaData
