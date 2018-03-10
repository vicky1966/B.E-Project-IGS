package igs;

public class SubjectScale 
{
    private Subject sub;
    private double selfRate, companyRate;
    
    public SubjectScale(Subject s, double sysR)
    {
        sub = s;
        selfRate = 0;
        companyRate = sysR;
    }
    
    public String getSubjectName()
    {
        return sub.getSubjectName();
    }
    
    public int getSubjectId()
    {
        return sub.getSid();
    }
    
    public double getCompanyScale()
    {
        return companyRate;
    }
    
    public double getSelfScale()
    {
        return selfRate;
    }
    
    public Subject getSubject()
    {
        return sub;
    }
    
    public void setSelfScale(double x)
    {
        selfRate = x;
    }
}
