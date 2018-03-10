package igs;
import java.util.*;

public class SubjectManager 
{
    private static SubjectManager ref = null;
    HashMap<Integer, Subject> manySubjects;

    private SubjectManager()
    {
        manySubjects = new HashMap<Integer, Subject>();
    }
    
    private HashMap<Integer, SubjectScale> allotSubjectsForInterview(int iid)
    {
        LinkedList<String []> sidScale;
        HashMap<Integer,SubjectScale> allocation = new HashMap<Integer,SubjectScale>();
        DataManager dm = DataManager.getInstance();
        
        sidScale = dm.getSubjectIdsScaleInterviewWise(iid);
        int sid;
        double scale;
        
        for(String x[] : sidScale)
        {
            sid = Integer.parseInt(x[0]);
            scale = Double.parseDouble(x[1]);
            
            if(manySubjects.containsKey(sid))
                allocation.put(sid, new SubjectScale(manySubjects.get(sid), scale));
            else
            {//load the Subject
                Subject temp = new Subject(sid);
                manySubjects.put(sid, temp);
                allocation.put(sid, new SubjectScale(temp, scale));
            }
        }
        
        return allocation;
    }
    
    public void removeLoadedSubject(int sid)
    {
        if(manySubjects.containsKey(sid))
            manySubjects.remove(sid);
    }
    
    public HashMap<Integer, SubjectScale> getSubjectsInterviewWise(int arr[])
    {
        LinkedList<HashMap<Integer, SubjectScale>> allInterviews = new LinkedList<HashMap<Integer,SubjectScale>>();
        
        for(int x : arr)//each interview
            allInterviews.add(allotSubjectsForInterview(x));
        
        HashMap<Integer,SubjectScale> finalSubjects = reduceCommonSubjects(allInterviews);
        
        return finalSubjects;
    }
    
    private HashMap<Integer,SubjectScale> reduceCommonSubjects(LinkedList<HashMap<Integer,SubjectScale>> allInterviews)
    {
        HashMap<Integer, SubjectScale> set1 = allInterviews.remove();
        
        HashMap<Integer, SubjectScale> current;
        
        while(allInterviews.size() >0)
        {
            current = allInterviews.remove();
            union(set1, current);
        }
        return set1;
    }//reduceCommonSubjects
    
    private void union(HashMap<Integer, SubjectScale> a, HashMap <Integer, SubjectScale > b)
    {
        //unique elements of set b to be copied into set a
        Set<Integer> sidSet = b.keySet();
        for(Integer sid : sidSet)
        {
            if(! a.containsKey(sid))
                a.put(sid, b.get(sid));
            else if(b.get(sid).getCompanyScale() > a.get(sid).getCompanyScale())
                a.put(sid, b.get(sid));

        }
    }
    
    public static SubjectManager getInstance()
    {
        if(ref == null)
            ref = new SubjectManager();
        return ref;
    }
    
}
