<%-- 
result
--%>

<jsp:useBean id="ih" scope="session" class="igs.InterviewHelper"/>
<jsp:useBean id= "sw" scope="page" class ="igs.SMSWriter"/>

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
    </head>
    <body >
    <jsp:include page="navbar1.jsp"/>
    <!-- Content section -->
    <section class="py-5">
    <div class="container">
        <h1>Result</h1>
        <p class="lead">
            <div id="divResult">
                <%
                    String r = ih.getInterviewResultAsString(); 
                    out.println("<h2>" + r + "</h2>");

                    if(ih.passes())
                    {
                        out.println("<h2>RESULT : PASS</h2>");
                        HttpSession s = request.getSession(true);
                        int suid =  Integer.parseInt(s.getAttribute("suid").toString());
                        java.util.LinkedList<String[]> ls = ih.getInterviewDetails();
                        String person = ih.getPersonDetails(suid);

                        String message;
                        for(String arr[] : ls)
                        {//ititle, cname, cphone
                            message = person + "\n";
                            message = message + " cleared the interview " + arr[0];
                            message = message + " set by "+ arr[1];

                            sw.writeSMS(arr[2], message);
                        }
                    }
                    else
                    {
                        out.println("<h2>RESULT : FAILS</h2>");

                    }
                    ih.reset();
                %>
            </div>            
        </p>
    </div>
    </section>

    <!-- Bootstrap core JavaScript -->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/tether/tether.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.min.js"></script>
    
    </body>
</html>
