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
        <title>Bihari Randi Hai</title>
    </head>
    <body>
        <h1>Hello World!</h1>
         <%
             
             String q=request.getParameter("question");
             String op1=request.getParameter("Op1");
             String op2=request.getParameter("Op2");
             String op3=request.getParameter("Op3");
             String op4=request.getParameter("Op4");
             int ca=Integer.parseInt(request.getParameter("ca"));
            
             
             
             int tid=1;
             
             String s = NaiveBayesExample.classify(q);//to get subject
             
             if(s=="ds")
                 tid=1;
             else if(s=="ads")
                 tid=2;
             else if(s=="os")
                 tid=3;
             else if(s=="sql")
                 tid=4;
             else if(s=="asql")
                 tid=5;
             
       
             DataManager ref =  igs.DataManager.getInstance();
            
             
             
             boolean x=ref.insertQuestion(q,ca,tid);
             boolean y=ref.insertOption(op1,op2,op3,op4);
             
             if(x)
             {
             out.print("gand me le lo in   aur");
             }
             else
             {
                   out.print("sab gadbad hai");
             }
             
        %>
        
        <a href="index.jsp">go back</a>
    </body>
</html>
