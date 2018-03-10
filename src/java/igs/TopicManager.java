package igs;

import java.util.*;
public class TopicManager 
{
    //update this to value 6 for real time use
    private static final int MAX_TOPIC_COUNT = 1;
    
    public static void getTopicsToInterview(LinkedList<Topic> toInterview, Subject sub)
    {
        LinkedList<Topic> allTopics = sub.getSubjectTopics();
        if(allTopics.size() <= MAX_TOPIC_COUNT)
        {
            toInterview.clear();
            toInterview.addAll(allTopics);
        }
        else
        {//algorithm here
            LinkedList<Topic> set = new LinkedList<Topic>();
            set.addAll(allTopics);
            shake(set);
            int i;
            toInterview.clear();
            for(i =0 ; i < MAX_TOPIC_COUNT; i++)
                toInterview.add(set.remove());
        }
        
        System.out.println("topic len : " + toInterview.size());
    }
    
    private static void shake(LinkedList<Topic> set)
    {
        
        LinkedList<Topic> smallSet = new LinkedList<Topic>();
        int size = set.size();//17
        int setSize;
        int i,j ;
        
        
        size = size%2==0 ? size : size-1;
        
        for(setSize = size/2; setSize > 1; setSize/=2)//8;4,2
        {
            for(i = 0; i< size; i+=setSize)//{0,8}{0,4,8,12}{0,2,4,6,8,10,12,14}
            {
                smallSet.clear();
                for(j =0; j < setSize; j++)
                {
                        smallSet.addFirst(set.remove(i));
                }
                set.addAll(i, smallSet);
            }
        }
    }

}
