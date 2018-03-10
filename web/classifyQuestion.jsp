<%-- 
    Document   : classifyQuestion
    Created on : Mar 1, 2018, 4:03:24 PM
    Author     : Shailendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="datumbox.opensource.examples.*"%>
<%@page import="igs.DataManager"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
         <%
             
             String q=request.getParameter("question");
             String a=request.getParameter("Op1");
             String b=request.getParameter("Op2");
             String c=request.getParameter("Op3");
             String d=request.getParameter("Op4");
            
             String s = NaiveBayesExample.classify(q);//to get subject
             String t = NaiveBayesExample2.classify(q);//to get topic
            
             int x=igs.DataManager.insertQuestion(q,a,b,c,d,s);
             if(x==1)
             {
             out.print("gand me le lo in "+s +"  aur" +t);
             }
             
        %>
        
        <a href="index.jsp">go back</a>
    </body>
</html>
