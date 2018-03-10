package igs;

import java.util.*;

public class QuestionManager 
{
    Evaluator evaluator;
    LinkedList<String> questionSet;
    int qFlag, qSet;
    double companyScale;
    
    private static Random rnd = new Random();
    
    public QuestionManager() 
    {
        evaluator = new Evaluator();
        questionSet = new LinkedList<String>();
    }
    
    private static String generateJSONString(LinkedList<String> l, int qFlag)
    {
        //qid, qtext, qoid, qoption, qoid, qoption, ...
        //{"qtext":"What is a constructor?","qid":7,"qRank":1,"qoptions":[  "17", "A special Method of The Class", "18", "An Object Initializer", "19", "Both A and B" ]}";
        StringBuffer sbuff = new StringBuffer();
        sbuff.append("{\"qtext\":\"");
        sbuff.append(l.remove(1));
        sbuff.append("?\",\"qid\":");
        sbuff.append(l.remove(0));
        sbuff.append(",\"qRank\":");
        sbuff.append(qFlag);
        sbuff.append(",\"qoptions\":[");
        
        
        while(l.size() > 0)
        {
            sbuff.append("\"");
            sbuff.append(l.remove(0));
            sbuff.append("\"");
            
            if(l.size() >0)
                sbuff.append(",");
        }
        
        sbuff.append("]}");
        return sbuff.toString();
     
    }
    
    public void setCompanyScale(double companyScale)
    {
        this.companyScale = companyScale;
    }
    
    public void setBasicSet(Topic t)
    {//Portion A : 2,4,4 and Portion B : 4,6,6
        
        DataManager dm = DataManager.getInstance();
        
        dm.updateTopicRank(t.getTid());
        LinkedList<LinkedList<String>> all = dm.getQuestionsByTopic(t.getTid());
        LinkedList<LinkedList<String>> easy = new LinkedList<LinkedList<String>>();
        LinkedList<LinkedList<String>> moderate = new LinkedList<LinkedList<String>>();
        LinkedList<LinkedList<String>> difficult = new LinkedList<LinkedList<String>>();
        
        //25% Easy + 50% Moderate + 25% Difficult
        
        int i, s1,s2;
        s1 = (all.size() * 25) / 100;
        s2 = all.size() - s1 - s1;
        
        for(i =0; i < s1; i++)
            easy.add(all.remove());
        
        for(i =0; i < s2; i++)
            moderate.add(all.remove());
        
        for(i =0; i < s1; i++)
            difficult.add(all.remove());
        
        //1 easy question
        LinkedList<String> e1 = easy.remove(rnd.nextInt(easy.size()));
        
        //3 moderate questions
        LinkedList<String> m1 = moderate.remove(rnd.nextInt(moderate.size()));
        LinkedList<String> m2 = moderate.remove(rnd.nextInt(moderate.size()));
        LinkedList<String> m3 = moderate.remove(rnd.nextInt(moderate.size()));
        
        //2 difficult questions
        LinkedList<String> d1 = difficult.remove(rnd.nextInt(difficult.size()));
        LinkedList<String> d2 = difficult.remove(rnd.nextInt(difficult.size()));
        
        questionSet.clear();
        //portion A
        questionSet.add( generateJSONString(e1,1));
        questionSet.add( generateJSONString(m1,2));
        questionSet.add( generateJSONString(m2,2));
        
        //portion B
        questionSet.add( generateJSONString(m3,2));
        questionSet.add( generateJSONString(d1,3));
        questionSet.add( generateJSONString(d2,3));
        
        qSet = 1;
    }

    public void setModerateSet(Topic t)
    {//Portion A : 4,4,4 and Portion B : 6,6,6
        
        DataManager dm = DataManager.getInstance();
        dm.updateTopicRank(t.getTid());
        
        LinkedList<LinkedList<String>> all = dm.getQuestionsByTopic(t.getTid());
        LinkedList<LinkedList<String>> moderate = new LinkedList<LinkedList<String>>();
        LinkedList<LinkedList<String>> difficult = new LinkedList<LinkedList<String>>();
        
        //50% Moderate + 50% Difficult
        
        int i, s1, s2;
        s1 = all.size() / 2;
        s2 = all.size() - s1;
        
        for(i =0; i < s1; i++)
            moderate.add(all.remove());
        
        for(i =0; i < s2; i++)
            difficult.add(all.remove());
        
        //3 moderate questions
        LinkedList<String> m1 = moderate.remove(rnd.nextInt(moderate.size()));
        LinkedList<String> m2 = moderate.remove(rnd.nextInt(moderate.size()));
        LinkedList<String> m3 = moderate.remove(rnd.nextInt(moderate.size()));
        
        //3 difficult questions
        LinkedList<String> d1 = difficult.remove(rnd.nextInt(difficult.size()));
        LinkedList<String> d2 = difficult.remove(rnd.nextInt(difficult.size()));
        LinkedList<String> d3 = difficult.remove(rnd.nextInt(difficult.size()));
        
        questionSet.clear();
        //portion A
        questionSet.add( generateJSONString(m1,2));
        questionSet.add( generateJSONString(m2,2));
        questionSet.add( generateJSONString(m3,2));
        
        //portion B
        questionSet.add( generateJSONString(d1,3));
        questionSet.add( generateJSONString(d2,3));
        questionSet.add( generateJSONString(d3,3));
    
        qSet = 2;
    }
 
    public void clearQuestionSet()
    {
        questionSet.clear();
    }
    
    public String getNextQuestion()
    {//returns a question or null when questionSet completes
        try
        {
            int size = questionSet.size(); 
            if(qSet ==1 )
            {//basic set : e,m,m,m,d,d
                if(size == 6)
                    qFlag = 1;
                else if(size ==3 || size == 4 ||size ==5)
                    qFlag = 2;
                else if(size == 1 || size == 2)
                    qFlag = 3;
            }
            else if(qSet == 2)
            {//moderate set : m,m,m,d,d,d
                if(size ==4 || size == 5 || size ==6)
                    qFlag = 2;
                else if(size == 1 || size == 2 || size <= 3)
                    qFlag = 3;
            }
            return questionSet.remove();
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    
    public int evaluateReply(int qid, int userAnswer, int userTime)
    {//0 : normal exit
     //1 : portion evaluation pass
     //-1 : portion evaluation fails
        
        int size = questionSet.size();
        evaluator.evaluateAnswer(qid, userAnswer, userTime, qFlag);
        if(size == 3 || /*size == 1 ||*/ size == 0)
            return evaluator.evaluateTopicPortion(companyScale) ? 1 : -1;
        return 0;
    }

    public ResponseData updateTopicEvaluation()
    {
        return evaluator.updateTopicEvaluation();
    }
}