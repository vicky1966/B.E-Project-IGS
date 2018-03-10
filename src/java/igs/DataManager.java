
package igs;
import java.sql.*;
import java.util.LinkedList;
import java.util.array;
public class DataManager 
{
    private Connection conn;
    private static DataManager reference = null;
    
    private DataManager() throws Exception
    {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String userName = "system";
        String password= "1";
        
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        conn = DriverManager.getConnection(url, userName, password );
    }
    
    public int[] questionCorrect()
    {
        int total[] = new int[10];
        int correct[]=new int [10];
        int percentage[]=new int[10];
        //percentage[1]=12;
       
        
        try{
            String sql = "select * from correctQuestion";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            //int colCount = rs.getMetaData().getColumnCount();
            int i=1;
           
            while(rs.next())
            {
                
                
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    total[i-1] = rs.getInt(2);
                    correct[i-1]=rs.getInt(3)*100;
                    System.out.println(total[i-1]+" "+correct[i-1]);
                i++;
            }
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        for(int i=0;i<10;i++)
        {
            percentage[i]=(correct[i]/total[i]);
            
            //=x*100;
        }
         return percentage;
    }
    public static DataManager getInstance()
    {
        try
        {
            if(reference == null)
                reference = new DataManager();

            return reference;
        }
        catch(Exception ex)
        {
            System.out.println("i am at get instance");
            return null;
        }
    }//getInstance

    //auto generation of the PK value
    //look up approach
    private int getNextId(String table, String column) throws Exception
    {
        String sql = "select max("+ column + ") from "+ table;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        if(rs.next())
        {//max fetched
            int id = rs.getInt(1);//1 means first column
            id++;//next id
            rs.close();
            stmt.close();
            return id;
        }
        else
        {//rs is empty ( table is empty)
            rs.close();
            stmt.close();
            return 1;//first value
        }
    }
    

/*
sys_user
(
 suid number primary key,
 suname varchar(50),
 suemail varchar(50),
 suphone varchar(20)
)
*/
    public String [] getUserDetails(int suid)
    {
        try
        {
            String arr[] = null;
            String sql = "select suname, suemail, suphone from sys_user where suid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, suid);
            
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next())
            {
                arr = new String[3];
                arr[0] = rs.getString(1);//suname
                arr[1] = rs.getString(2);//suemail
                arr[2] = rs.getString(3);//suphone
            }
            rs.close();
            
            return arr;
       }
        catch(Exception ex)
        {
            System.out.println(ex);
            return null;
        }
    }//getUserDetails
    

/*
usr_login
(
 suid number primary key references sys_user(suid),
 loginid varchar(30),
 loginpass varchar(30)
)    
 */   
    
    public int validateLogin(String uname, String upass)
    {
        try
        {
            int id = -1;
            String sql = "select suid from usr_login where lower(loginid) = lower(?) and loginpass = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            System.out.print(uname);
            pstmt.setString(1, uname);
            pstmt.setString(2, upass);
            
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next())
            {//data row present
                id = rs.getInt(1);//get the suid 
            }
            rs.close();
            return id;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return -1;
        }
    }
    
/*
company
(
 cid number primary key,
 cname varchar(30),
 curl varchar(30),
 cdescription varchar(500) 
);
*/

    public boolean addCompany(String cName, String cUrl, String cDesc)  
    {
        try
        {
            int id = getNextId("company", "cid");
            String sql = "insert into company values(?,?,?,?)";
            
            //to avoid sql injection, as PreparedStatement gets the SQL compiled from the database before execution.
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);//first ? gets value of id
            pstmt.setString(2, cName);//second ? gets value of cName
            pstmt.setString(3, cUrl);//third ? gets value of cUrl
            pstmt.setString(4, cDesc);//fourth ? gets value of cDesc

            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//addCompany


    public boolean deleteCompany(int cid)  
    {
        try
        {
            String sql = "delete from company where cid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//deleteCompany
    
    public LinkedList<String[]> getCompanies()
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            String sql = "select * from company";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }
    
/*
interview
(
 iid number primary key,
 ititle varchar(100),
 idescription varchar(500),
 ipaypackage varchar(20),
 cid number references company(cid)
)

*/

    public boolean addInterview(String iTitle, String iDesc, String iPayPackage, int cid)  
    {
        try
        {
            int id = getNextId("interview", "iid");
            String sql = "insert into interview values(?,?,?,?,?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, iTitle);
            pstmt.setString(3, iDesc);
            pstmt.setString(4, iPayPackage);
            pstmt.setInt(5, cid);

            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//addInterview

    public boolean deleteInterview(int iid)  
    {
        try
        {
            String sql = "delete from interview where iid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, iid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//deleteInterview
    
    public String [] getInterviewDetails(int q)
    {
        String arr[] = null;
        try
        {
            String sql = "select ititle, cname, cphone from company inner join interview on company.cid = interview.cid where iid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, q);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
            {
                arr = new String[3];
                arr[0]= rs.getString(1);
                arr[1]= rs.getString(2);
                arr[2]= rs.getString(3);
            }
            return arr;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return null;
    }
    public LinkedList<String[]> getInterviews()
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            String sql = "select iid , ititle, idescription, ipaypackage, cname from interview inner join company on interview.cid = company.cid order by cname";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            //pstmt.setInt(1, from);
            //pstmt.setInt(2, to);
            
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }
    
    
    
    
    public String getInterviewName(int iid)
    {
        String iname = null;
            
        try
        {
            String sql = "select ititle, cname from interview inner join company on interview.cid = company.cid where iid = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, iid);
            
            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
            {
               iname = rs.getString(1).toUpperCase() + " (" +  rs.getString(2).toUpperCase() +  ")";
                
            }//if
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return iname;
    }        
    
/*
subject
(
 sid number primary key,
 sname varchar(30),
 sdescription varchar(500)
)

*/
 
    public boolean addSubject(String sName)  
    {
        try
        {
            int id = getNextId("subject", "sid");
            String sql = "insert into subject values(?,?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, sName);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//addSubject


    public boolean deleteSubject(int sid)  
    {
        try
        {
            String sql = "delete from subject where sid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//deleteSubject
    
    public LinkedList<String[]> getSubjects()
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            String sql = "select * from subject";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getSubjects
 
    public String getSubjectName(int sid)
    {
        String name="";    
        try
        {
            String sql = "select sname from subject where sid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sid);
            ResultSet rs = pstmt.executeQuery();
            
            //fetch resultset content
            if(rs.next())
            {
                name = rs.getString(1);
            }
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return name;
    }
    
    public int getSubjectScale(int sid, int iid)
    {
        int scale = -1;    
        try
        {
            String sql = "select scale from scaling  where sid = ? and iid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sid);
            pstmt.setInt(2, iid);
            
            ResultSet rs = pstmt.executeQuery();
            
            //fetch resultset content
            if(rs.next())
            {
                scale = rs.getInt(1);
            }
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return scale;
    }
    
    public LinkedList<String []> getSubjectIdsScaleInterviewWise(int iid)
    {//sidScale
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            String sql = "select sid, scale from scaling where iid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, iid);
            ResultSet rs = pstmt.executeQuery();
            
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                arr = new String[2];
                arr[0] = rs.getString(1);
                arr[1] = rs.getString(2);
                
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getSubjectIdScaleInterviewWise
    
    public LinkedList<String[]> getSubjectsInterviewWise(int iid)
    {//subject.sid, sname, scale 
        
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            String sql = "select subject.sid, sname, scale from subject inner join scaling on subject.sid = scaling.sid where scaling.iid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, iid);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getSubjectsInterviewWise
    
    /*
scaling
(
 scid number primary key,
 sid number references subject(sid),
 iid number references interview(iid),
 scale number
)
*/

    public boolean addScaling(int sid, int iid, double scale)  
    {
        try
        {
            int id = getNextId("scaling", "scid");
            String sql = "insert into scaling values(?,?,?,?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setInt(2, sid);
            pstmt.setInt(3, iid);
            pstmt.setDouble(4, scale);
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//addScaling


    public boolean deleteScaling(int scid)  
    {
        try
        {
            String sql = "delete from scaling where scid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, scid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//deleteScaling
    
    public LinkedList<String[]> getScaling()
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            String sql = "select * from scaling";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getScaling
 
/*
topic
(
 tid number primary key,
 tname varchar(30),
 trank number,
 sid number references subject(sid)
)
*/
    public boolean addTopic(String tName, double tRank, int sid)  
    {
        try
        {
            int id = getNextId("topic", "tid");
            String sql = "insert into topic values(?,?,?,?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, tName);
            pstmt.setDouble(3, tRank);
            pstmt.setInt(4, sid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//addTopic


    public boolean deleteTopic(int tid)  
    {
        try
        {
            String sql = "delete from topic where tid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, tid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//deleteTopic

    public boolean updateRankForAllTopics()
    {
        try
        {
            String sql1 = "Select tid from topic";
            String sql2 = "update topic set trank = ( select avg(qrank) from question where tid = ? ) where tid = ?";
            int tid;
            
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            
            ResultSet rs = pstmt1.executeQuery();
            
            while(rs.next())
            {
                tid = rs.getInt(1);//data of first col
                pstmt2.setInt(1, tid);
                pstmt2.setInt(2, tid);
                pstmt2.executeUpdate();
            }//while
            rs.close();
            pstmt1.close();
            pstmt2.close();
            return true;
        }
        catch(Exception ex)
        {
            System.out.println(ex); 
            return false;
        }
    }//updateRankForAllTopics
    
    public boolean updateTopicRank(int tid)
    {
        try
        {
            String sql = "update topic set trank = ( select avg(qrank) from question where tid = ? ) where tid = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, tid);
            pstmt.setInt(2, tid);
            pstmt.executeUpdate();
            pstmt.close();
            return true;
        }
        catch(Exception ex)
        {
            System.out.println(ex); 
            return false;
        }
    }//updateTopicRank    
    
    
    public String getTopicAndSubjectName(int tid)
    {
        String reply = "";        
        try
        {
            String sql = "select tname, sname from subject inner topic on subject.sid = topic.sid where tid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next())
            {
                 reply = rs.getString(1);
                 reply = reply + " in ";
                 reply = rs.getString(2);
            }//if
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return reply;
    }
            
    public LinkedList<String[]> getTopics()
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            String sql = "select * from topic";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getTopics

    public LinkedList<String[]> getTopicsSubjectwise(int subjectId)
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            String sql = "select tid, tname, trank from topic where sid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, subjectId);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getTopicsSubjectwise

    public LinkedList<Topic> getTopicObjectsSubjectwise(int subjectId)
    {
        LinkedList<Topic> rows = new LinkedList<Topic>();
        try
        {
            String sql = "select tid, tname, trank from topic where sid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, subjectId);
            ResultSet rs = pstmt.executeQuery();
            
            //fetch resultset content
            Topic t;
            while(rs.next())
            {
                //to hold data of this row of the resultset
                t = new Topic(rs.getInt(1), rs.getString(2), rs.getDouble(3));
                //put the array into the linkedlist
                rows.add(t);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getTopicObjectsSubjectwise

    
/*
question
(
 qid number primary key,
 qtext varchar(500),
 qca number,
 qrank number,
 tid number references topic(tid)
)
*/
    
    public boolean addQuestion(String qText, int qca, int qRank, int tid)  
    {
        try
        {
            int id = getNextId("question", "qid");
            String sql = "insert into question values(?,?,?,?,?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, qText);
            pstmt.setInt(3, qca);
            pstmt.setInt(4, qRank);
            pstmt.setInt(5, tid);
            
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//addQuestion


    public boolean deleteQuestion(int qid)  
    {
        try
        {
            String sql = "delete from question where qid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, qid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//deleteQuestion
    
    public boolean updateQuestionRank(int qid, int val)
    {
        try
        {
            String sql = "update question set qrank = qrank + ? where qid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, val);
            pstmt.setInt(2, qid);
            
            int temp = pstmt.executeUpdate();
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }
    
    public LinkedList<String[]> getQuestions()
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            String sql = "select * from question";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getQuestions

    public int getQuestionsAnswer(int qid)
    {
        int ans = -1;
            
        try
        {
            String sql = "select qca from question where qid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, qid);
            
            ResultSet rs = pstmt.executeQuery();
            //fetch resultset content
            if(rs.next())
            {
                ans = rs.getInt(1);
            }//if
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return ans;
    }//getQuestionsAnswer

    public LinkedList<LinkedList<String>> getQuestionsByTopic(int tid)
    {//qid, qtext, qoid, qoption , qoid, qoption, ...
        LinkedList<LinkedList<String>> rows = new LinkedList<LinkedList<String>>();
            
        try
        {
            String sql1 = "select qid, qtext from question where tid = ? order by qrank";
            String sql2 = "select qoid, qoption from qoption where qid = ?";
                    
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            
            pstmt1.setInt(1, tid);
            
            ResultSet rs1 = pstmt1.executeQuery();
            ResultSet rs2;
            
            //fetch resultset content
            LinkedList <String> col;
            while(rs1.next())
            {
                col = new LinkedList<String>();
                col.add(rs1.getString(1));//qid
                col.add(rs1.getString(2));//qtext
                
                //fetching options
                pstmt2.setInt(1, Integer.parseInt(col.get(0)));
                rs2 = pstmt2.executeQuery();
                
                while(rs2.next())
                {//qoid, qoption
                    col.add(rs2.getString(1));
                    col.add(rs2.getString(2));
                }
                
                rs2.close();
                rows.add(col);
            }//while
            
            rs1.close();
            pstmt1.close();
            pstmt2.close();
            
        }//while
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getQuestionsByTopic

    
/*
qoption
(
 qoid number primary key,
 qoption varchar(500),
 qid number references question(qid) 
)
    
*/
    public boolean addQoption(String qOption, int qid)  
    {
        try
        {
            int id = getNextId("qoption", "qoid");
            String sql = "insert into qoption values(?,?,?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.setString(2, qOption);
            pstmt.setInt(3,qid);
            
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//addQoption


    public boolean deleteQoption(int qoid)  
    {
        try
        {
            String sql = "delete from question where qid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, qoid);
            
            //execute it
            int temp = pstmt.executeUpdate();
            pstmt.close();
            
            return temp > 0;
        }
        catch(Exception ex)
        {
            System.out.println(ex);
            return false;
        }
    }//deleteQoption
    
     public static int insertQuestion(String q , String a,String b,String c,String d,String subject)
     {
         int x;
 
         return 1;
     }
    public LinkedList<String[]> getQoptionsForQuestion(int qid)
    {
        LinkedList<String[]> rows = new LinkedList<String[]>();
            
        try
        {
            String sql = "select * from qoption where qid = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int colCount = rs.getMetaData().getColumnCount();
            int i;
            //fetch resultset content
            String arr[];
            while(rs.next())
            {
                //to hold data of this row of the resultset
                arr = new String[colCount];
                for(i =1; i <=colCount; i++)
                {
                    //arr[i-1] gets the data of rs.thisrow.col(i) in string form
                    arr[i-1] = rs.getString(i);
                }
                //put the array into the linkedlist
                rows.add(arr);
            }//while
            
            rs.close();
            pstmt.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return rows;
    }//getQoptionsForQuestion  
    
    public boolean setCorrect()
    {
        try{
            String sql="insert into correctQuestion values(?,?,?)";
        }catch(Exception ex)
        {
            System.out.print(ex);
        }
        return true;
    }
	
		
     public  boolean insertQuestion(String q ,int ca,int tid)
     {
         int temp=0;
         int id=0;
        try
        {
            id =  getMaxQoptionId();
            id=ca+id;
            
            String stm = "insert into question (qtext,qca,qrank,tid) values (?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(stm);
            pstmt.setString(1, q);
            pstmt.setInt(2, id);
            pstmt.setInt(3, 5);
            pstmt.setInt(4, tid);
            
            System.out.println(pstmt.toString());
            
            temp = pstmt.executeUpdate();
            pstmt.close();
            commit();
            return temp>0;
         
        }
        catch(Exception ex)
        {
            System.out.println("ex");
             return false;
        }
     }
    
     
      public  boolean insertOption(String op1,String op2,String op3,String op4)
      {
          int temp;
          try
          {
            String stm = "insert into qoption (qoption,qid)  values (?,?)";
            PreparedStatement pstmt1 = conn.prepareStatement(stm);
            PreparedStatement pstmt2 = conn.prepareStatement(stm);
            PreparedStatement pstmt3 = conn.prepareStatement(stm);
            PreparedStatement pstmt4 = conn.prepareStatement(stm);
            
            int id=getMaxQid();
            System.out.println(id);
            pstmt1.setString(1, op1);
            pstmt2.setString(1, op2);
            pstmt3.setString(1, op3);
            pstmt4.setString(1, op4);
            
            pstmt1.setInt(2, id);
            pstmt2.setInt(2, id);
            pstmt3.setInt(2, id);
            pstmt4.setInt(2, id);
           
            temp =  pstmt1.executeUpdate();
            temp =  pstmt2.executeUpdate();
            temp =  pstmt3.executeUpdate();
            temp =  pstmt4.executeUpdate();
           
            pstmt1.close();
            pstmt2.close();
            pstmt3.close();
            pstmt4.close();
                  commit();
            return temp>0;
              
          }    
          catch(Exception ex)
          {
             System.out.println(ex);
             return false;
          }    
      
          
      }
     
      
      public int getMaxQoptionId()
      {
          int id=0; 
          try
          {
            String sql = "select max(qoid) from qoption ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery(); 
            while(rs.next())
            {
                id=rs.getInt(1);
            }   
            return id;
          }
          catch(Exception ex)
          {
              System.out.println(ex);
              return id; 
          }
 
      }        
      
         
      public int getMaxQid()
      {
          int id=0;
                  
          try
          {
            String sql = "select max(qid) from question ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery(); 
            while(rs.next())
            {
                id=rs.getInt(1);
            }   
            return id;
          }
          catch(Exception ex)
          {
              System.out.println(ex);
              return id; 
          }
      }        
      
      public void commit()
      {
          try
          {
                String sql = "commit";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.executeQuery();
                pstmt.close();
        }
          catch(Exception ex)
          {
              System.out.println(ex);
          }    
      
      }
}