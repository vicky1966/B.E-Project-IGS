package igs;

import java.util.*;
import org.json.*;

public class InterviewHelper 
{
    private int qid, userAnswer, userTime;
    private int intrws[];
    private HashMap<Integer, SubjectScale> allSubjectsScale;
    private Iterator<Integer> itrAllSubjectsScale;
    private LinkedList<Topic> currentSubjectTopics;
    private int topicFlag, topicFailCounter, sid;
    private Topic thisTopic;
    private QuestionManager qm;
    private BehaviourTracker tracker;
    private boolean passes;
    
    public InterviewHelper()
    {
        qm = new QuestionManager();
        tracker = new BehaviourTracker();
        currentSubjectTopics = new LinkedList<Topic>();
        topicFlag = 1;
        topicFailCounter =0;
        sid = -1;
        passes = false;
    }
    
    public void reset()
    {
        intrws = null;
        qid = userAnswer = userTime = -1;
        qm = new QuestionManager();
        tracker = new BehaviourTracker();
        currentSubjectTopics = new LinkedList<Topic>();
        topicFlag = 1;
        topicFailCounter =0;
        sid = -1;
        thisTopic = null;
        passes = false;
    }
    
    public void setQid(int x)
    {
        qid = x;
    }
    
    public void setUserAns(int x)
    {
        userAnswer = x;
    }
    
    public void setUserTime(int x)
    {
        userTime = x;
    }
    
    public void setIntrws(int arr[])
    {
        intrws = arr;
    }
    
    public void setSelfScale(String s)
    {
        try
        {
            JSONObject obj = new JSONObject(s);
            JSONArray array = obj.getJSONArray("scaling");
            Iterator itr;
            int  k;
            
            for(int i =0; i < array.length(); i++)
            {
                itr = array.getJSONObject(i).keys();
                if(itr.hasNext())
                {
                    k = Integer.parseInt(itr.next().toString());
                    if(allSubjectsScale.containsKey(k))
                        allSubjectsScale.get(k).setSelfScale(array.getJSONObject(i).getDouble(String.valueOf(k)));
                }
            }//for
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }
    
    public double getCompanyScaleForSubject(int s)
    {
        return allSubjectsScale.containsKey(s) ? allSubjectsScale.get(s).getCompanyScale() : -1;
    }
   
    public boolean evaluateSelfScale()
    {
        try
        {
            Collection<SubjectScale> c = allSubjectsScale.values();
            for(SubjectScale ss : c)
            {
                if(ss.getCompanyScale() > ss.getSelfScale())
                    return false;
            }
            return true;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }
   
    public String getNextQuestion()
    {//returns a question or "OVER"
        if(qid !=-1)
            evaluateReply();
        
        String s;
        s = qm.getNextQuestion();
        if(s == null)
        {
            topicFailCounter = 0;
            if(currentSubjectTopics.isEmpty())
            {
                //load next subject
                if(itrAllSubjectsScale.hasNext())
                {
                    if(sid != -1)
                    {
                        tracker.updateToSubjectBehaviour(sid, qm.updateTopicEvaluation());
                        tracker.finalizeSubjectBehaviour(sid, allSubjectsScale.get(sid).getSubjectName(), allSubjectsScale.get(sid).getCompanyScale());
                    }
                    sid = itrAllSubjectsScale.next();
                    TopicManager.getTopicsToInterview(currentSubjectTopics, allSubjectsScale.get(sid).getSubject());
                }
                else
                {//interview over
                    tracker.updateToSubjectBehaviour(sid, qm.updateTopicEvaluation());
                    tracker.finalizeSubjectBehaviour(sid, allSubjectsScale.get(sid).getSubjectName(), allSubjectsScale.get(sid).getCompanyScale());
                    return "OVER";
                }
            }//if
            
            if(!currentSubjectTopics.isEmpty())
            {//topic available
                thisTopic = currentSubjectTopics.remove();
                if(topicFlag ==1)
                    qm.setBasicSet(thisTopic);
                else if(topicFlag == 2)
                    qm.setModerateSet(thisTopic);
                
                //get next question
                s = qm.getNextQuestion();
            }
        }//if(s== null)
        return s;
    }
    
    public int getQid()
    {
        return -1;
    }
    
    public void evaluateReply()
    {
        int x;
        x = qm.evaluateReply(qid, userAnswer, userTime);
        //0 : normal exit
        //1 : portion evaluation pass
        //-1 : portion evaluation fails
        if(x == -1)
        {//topic fails
            tracker.updateToSubjectBehaviour(sid,qm.updateTopicEvaluation());
            topicFlag =1;
            topicFailCounter++;
            if(topicFailCounter == 3)
            {
                currentSubjectTopics.clear();
                qm.clearQuestionSet();
            }
        }
        else if(x == 1)
        {//topic pass
            topicFlag =2;
        }
    }
    
    public HashMap<Integer, ResponseData> getInterviewResult()
    {
        return tracker.getInterviewBehaviour();
    }

    public String getInterviewResultAsString()
    {
        StringBuffer sb = new StringBuffer();
        HashMap<Integer, ResponseData> x = tracker.getInterviewBehaviour();
        
        Set<Integer> ks  = x.keySet();
        ResponseData rd;
        passes = true;
        for(int i : ks)
        {
            rd = x.get(i);
            sb.append("<br>");
            sb.append(rd.getStatus().replaceAll("\n", "<br>"));
            if(rd.getStatus().toLowerCase().contains("fail"))
            {
                passes = false;
            }
        }
        return sb.toString();
    }
    
    public boolean passes()
    {
        return passes;
    }
    
    public LinkedList<String []> getInterviewDetails()
    {
        LinkedList<String[]> ls = new LinkedList<String[]>();
        if(intrws == null)
            return ls;
        
        DataManager dm = DataManager.getInstance();
        String arr[];
        for(int x : intrws)
        {
            arr = dm.getInterviewDetails(x);
            if(arr != null)
                ls.add(arr);
        }
        return ls;
    }
    
    public String getPersonDetails(int uid)
    {
        DataManager dm = DataManager.getInstance();
        String arr[] = dm.getUserDetails(uid);
        //suname, suemail, suphone 
        return arr[0] + ", " + arr[1] + "  " + arr[2]; 
    }
    
    public String[] getInterviewTitle()
    {
        String interviews[] = new String[intrws.length];
        
        int i;
        for( i =0 ; i <  intrws.length; i++)
        {
            interviews[i] = DataManager.getInstance().getInterviewName(intrws[i]);
        }
        
        return interviews;        
    }
    
    public String[] fetchHeader()
    {
        String arr[] = { "Subject", "Scale(1-10)"};
        return arr;
    }
    
    public HashMap<Integer, SubjectScale> getSubjectsInterviewWise()
    {
        allSubjectsScale = SubjectManager.getInstance().getSubjectsInterviewWise(intrws);
        itrAllSubjectsScale = allSubjectsScale.keySet().iterator();
        return allSubjectsScale;
    }   
}
