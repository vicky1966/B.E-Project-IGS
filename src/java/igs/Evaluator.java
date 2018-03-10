package igs;

public class Evaluator 
{
    private int marksObtained, slowAnswerCounter, fastAnswerCounter, noAnswerCounter, correctAnswerCounter, wrongAnswerCounter, totalQuestionCounter, totalMarks;
    private DataManager dm;
    
    public Evaluator() 
    {
        dm = DataManager.getInstance();
        resetAll();
    }
    
    private void resetAll()
    {
        marksObtained = 0;
        slowAnswerCounter = 0;
        fastAnswerCounter = 0;
        noAnswerCounter = 0;
        correctAnswerCounter = 0;
        wrongAnswerCounter = 0;
        totalQuestionCounter =0;
        totalMarks = 0;
    }
    
    public void evaluateAnswer( int qid, int userAnswer, int userTime, int qFlag)
    {
        if(userAnswer == -1)
        {//time Out
            noAnswerCounter++;
            dm.updateQuestionRank(qid, 1);//bit difficult
        }
        else 
        {
            int totalTime, score;
            int qa = dm.getQuestionsAnswer(qid);
                
            totalTime = qFlag *30;
            
            if(qa == userAnswer)
            {//correct Answer
                //qFlag : 1 easy
                //qFlag : 2 moderate
                //qFlag : 3 difficult
                score = qFlag * 2;
                correctAnswerCounter++;
                dm.updateQuestionRank(qid, -1);
            }
            else
            {//wrong answer
                score = 0;
                wrongAnswerCounter++;
                dm.updateQuestionRank(qid, 1);
            }
            
            if(userTime < totalTime * 0.1)
            {//fast
                score = 0;
                fastAnswerCounter++;
            }
            else if(userTime > totalTime * 0.95)
            {//slow
                score /= 2;
                slowAnswerCounter++;
            }
            
            marksObtained += score;
            totalMarks += qFlag*2;
        }//else
        
        totalQuestionCounter++;
    }//evaluateAnswer
    
    public boolean evaluateTopicPortion(double companyScale)
    {
        double percentageScore;
        percentageScore = (double)(marksObtained *  100) / totalMarks;
        return percentageScore >= companyScale*10;
    }
    
    public ResponseData updateTopicEvaluation()
    {   
        double percentageScore, percentageFast, percentageSlow, percentageNoReply;
        
        percentageScore = (double)(marksObtained *  100) / totalMarks;
        percentageFast = (double)(fastAnswerCounter * 100 ) / totalQuestionCounter;
        percentageSlow = (double)(slowAnswerCounter * 100 ) / totalQuestionCounter;
        percentageNoReply = (double)(noAnswerCounter * 100) / totalQuestionCounter;
        resetAll();

        return new ResponseData(percentageNoReply, percentageFast, percentageSlow, percentageScore);
    }
}
