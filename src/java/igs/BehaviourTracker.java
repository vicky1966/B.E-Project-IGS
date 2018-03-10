package igs;
import java.util.*;

public class BehaviourTracker 
{
    HashMap<Integer, ResponseData> behaviourSubjectwise;
    
    public BehaviourTracker()
    {
        behaviourSubjectwise = new HashMap<Integer, ResponseData>();
    }
    
    public void updateToSubjectBehaviour(int sid, ResponseData rd)
    {
        ResponseData data;
        if(behaviourSubjectwise.containsKey(sid))
        {
            data = behaviourSubjectwise.get(sid);
            data.setFastReplyPercentage(data.getFastReplyPercentage() + rd.getFastReplyPercentage());
            data.setSlowReplyPercentage(data.getSlowReplyPercentage() + rd.getSlowReplyPercentage());
            data.setNoReplyPercentage(data.getNoReplyPercentage() + rd.getNoReplyPercentage());
            data.setPercentage(data.getPercentage() + rd.getPercentage());
            data.incrementCounter();
        }
        else
            behaviourSubjectwise.put(sid, rd);
        
    }
    
    public void finalizeSubjectBehaviour(int sid, String sName, double companyScale)
    {
        boolean flag1, flag2, flag3;
        flag1 = flag2 = flag3 = false;
        
        ResponseData rd = behaviourSubjectwise.get(sid);
        
        rd.setFastReplyPercentage(rd.getFastReplyPercentage()/rd.getCounter());
        rd.setSlowReplyPercentage(rd.getSlowReplyPercentage()/rd.getCounter());
        rd.setNoReplyPercentage(rd.getNoReplyPercentage()/rd.getCounter());
        rd.setPercentage(rd.getPercentage()/rd.getCounter());
        
        
        if(rd.getFastReplyPercentage() > rd.getCounter())
        {
            rd.decrementPercentage();
            flag1 = true;
        }
        if(rd.getSlowReplyPercentage() > rd.getCounter())
        {
            rd.decrementPercentage();
            flag2 = true;
        }
        if(rd.getNoReplyPercentage() > rd.getCounter())
        {
            rd.decrementPercentage();
            flag3 = true;
        }

        
        StringBuffer status = new StringBuffer();
        
        status.append(sName);
        status.append(" : ");
        if(rd.getPercentage() >= companyScale * 10)
            status.append("Pass");
        else
            status.append("Fail");
        
        status.append("\nPercentage Obtained : ");
        status.append(rd.getPercentage());
        status.append("%");

        status.append("\nFollowing tendencies were observed :");

        if(flag1 == true)
            status.append("\nBlind Answers");
        else
            status.append("\nNo Blind Answers");

        if(flag2 == true)
            status.append("\nLast Moment Answers");
        else
            status.append("\nNo Last Moment Answers");

        if(flag3 == true)
            status.append("\nNo Reply To Some Questions");
        else
            status.append("\nAttempts Reply To Every Question");

        rd.setStatus(status.toString());
        
    }
    
    public HashMap<Integer, ResponseData> getInterviewBehaviour()
    {
        return behaviourSubjectwise;
    }
}
