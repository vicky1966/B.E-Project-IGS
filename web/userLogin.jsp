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
        var req, result;
        function authenticate(a, b)
        {
            req = new XMLHttpRequest();
            var url = "authenticateUser.jsp?loginId="+a+"&loginPass="+b;
            //var data = "loginId="+a+"&loginPass="+b;
            req.onreadystatechange = authenticateResult;
            req.open("GET", url, false);
            req.send();//go
        }
        
        function authenticateResult()
        {
            if(req.readyState === 4)
            {
                if(req.status === 200)
                {
                    result = req.responseText;
                }
            }
        }
        
        function showMessage(ele, msg)
        {
            var e = document.getElementById(ele);
            e.innerHTML = msg;
        }
        
        function login()
        {
            var a, b;
            a = document.getElementById("loginId").value;
            b = document.getElementById("loginPass").value;
            
            if(a === "")
            {
                showMessage("divMessage", "Login ID Missing");
            }
            else if(b === "")
            {
                showMessage("divMessage", "Password Missing");
            }
            else
            {
                authenticate(a, b);
                if(eval(result) === 1)
                {//success
                    
                    //showMessage("divMessage", "Login SUCCESS");
                    document.f.submit();
                }
                else
                {//failure
                    showMessage("divMessage", "Login Failed");
                }
            }
        }
        
    </script>
</head>
<body>
    <jsp:include page="navbar.jsp"/>
    <!-- Content section -->
    <section class="py-5">
        <div class="container">
            <h1>Login</h1>

            <div id="divUserLogin">
                <table>
                    <form name = "f" method = "post" action="userHome.jsp">
                        <tr>
                            <td>User Name </td>
                            <td><input type ="text" id="loginId" name="loginId" ></td>
                        </tr>
                        <tr>
                            <td>Password</td>
                            <td><input type ="password" id="loginPass" name="loginPass" ></td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type ="button" id="bttnLogin" name="bttnLogin" value = "Login" onClick="login()"></td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <div id="divMessage"></div>
                            </td>
                        </tr>
                    </form>
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
