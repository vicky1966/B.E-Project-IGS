<%@page import="java.util.LinkedList"%>
<jsp:useBean id="intw" scope="page" class="igs.Interview" />


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Interview Generation System</title>
    <!-- Bootstrap core CSS -->
    <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="css/full-width-pics.css" rel="stylesheet">

    <!-- Temporary navbar container fix -->
    <style>
    .navbar-toggler {
        z-index: 1;
    }
    
    @media (max-width: 576px) {
        nav > .container {
            width: 100%;
        }
    }
    </style>
    <script>
            
            function selectionLimitCheck()
            {
                var limit = 3;
                if(document.forms[0].intrws.length === undefined)
                {
                    return document.forms[0].intrws.checked;
                }
                else
                {
                    var sel = 0;
                    var i;
                    for(i =0; i < document.forms[0].intrws.length; i++)
                    {
                        if(document.forms[0].intrws[i].checked)
                        {
                            sel++;
                        }
                    }
                    if(sel ===0)
                    {
                        alert("Error : Select Minimum 1 Interview");
                        return false;
                    }
                    else if(sel > limit)
                    {
                        alert("Error : Select Maximum "+ limit+" Interviews ");
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }//else
                
            }
            
        </script>
    </head>
    <body>
    <jsp:include page="navbar1.jsp"/>
    <!-- Content section -->
    <section class="py-5">
        <div class="container">
        <h1>Interviews</h1>
        <div>
            <table width="100%">
                <%
                    LinkedList<String[]> lst = intw.fetchInterviews();
                    
                    if(lst.size() > 0)
                    {
                        String arr[] = intw.fetchHeader();
                        int x =  arr.length;
                    
                        out.println("<tr>");
                        
                        for(String s : arr)
                        {
                            out.println("<td> <b>" + s.toUpperCase() + " </b> </td>");
                        }
                        out.println("</tr>");

                        out.println("<form method=\"post\" action=\"scaling.jsp\" onsubmit=\"return selectionLimitCheck()\">");
                        int i;
                        boolean flag = false;
                        for(String sarr[] : lst)
                        {
                            if(flag)
                            {
                                out.println("<tr style=\"background-color: pink;\">");
                            }
                            else
                            {
                                out.println("<tr>");
                            }
                            
                            flag = !flag;
                            for(i =0; i < sarr.length; i++)
                            {
                                if(i == 0)
                                {
                                    out.println("<td> <input type = \"checkbox\" name=\"intrws\" value = \""+ sarr[i] +"\" /></td>");
                                }
                                else
                                {
                                    out.println("<td> "+ sarr[i]+"</td>");
                                }
                            }
                            out.println("</tr>");
                            
                        }//for
                        out.println("<tr>");
                        out.println("<td colspan = "+ x+">");
                        out.println("<input type = \"submit\" value = \"Appear\" />");
                        out.println("</td>");
                        out.println("</tr>");
                        
                        out.println("</form>");
                    }
                    else
                    {
                        out.println("No Interview Available");
                    }
                %>
                
            </table>
        </div>

        </div>
    </section>


    <!-- Bootstrap core JavaScript -->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/tether/tether.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>
