package igs;

import java.util.*;

public class Subject
{
    private int sid;
    private String name;
    private LinkedList<Topic> topics;
    
    public Subject(int x)
    {
        sid = x;
        DataManager dm = DataManager.getInstance();
        name = dm.getSubjectName(sid);
        topics = dm.getTopicObjectsSubjectwise(sid);
        Collections.sort(topics);
    }
    
    public int getSid()
    {
        return sid;
    }
    
    public String getSubjectName()
    {
        return name.toUpperCase();
    }
    
    public LinkedList<Topic> getSubjectTopics()
    {
        return topics;
    }
}
