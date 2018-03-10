<%@page import="igs.SubjectScale"%>
<jsp:useBean id="ih" scope="session" class="igs.InterviewHelper"/>
<jsp:setProperty name = "ih" property="qid" />
<jsp:setProperty name = "ih" property="userAns" param="ua" />
<jsp:setProperty name = "ih" property="userTime"  param="ti"/>


<%
    //String resp = "{\"qtext\":\"What is a constructor?\",\"qid\":7,\"qRank\":1,\"qoptions\":[  \"17\", \"A special Method of The Class\", \"18\", \"An Object Initializer\", \"19\", \"Both A and B\" ]}";
    //out.println(resp);
    String resp = ih.getNextQuestion();
    out.print(resp);
    
%>