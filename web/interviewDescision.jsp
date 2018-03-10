<%-- 
interview descision
--%>

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
    <body>
    <jsp:include page="navbar1.jsp"/>
    <!-- Content section -->
    <section class="py-5">
        <div class="container">
            <h1>Interview Rules</h1>
            <p class="lead">Lorem ipsum dolor sit amet, consectetur adipisicing elit.</p>
            <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquid, suscipit, rerum quos facilis repellat architecto commodi officia atque nemo facere eum non illo voluptatem quae delectus odit vel itaque amet.</p>

            <ul>
                <li> Rule 1</li>
                <li> Rule 2</li>
                <li> Rule 3</li>
                <li> Rule 4</li>
                <li> Rule 5</li>
                <li> Rule 6</li>
                <li> Rule 7</li>
            </ul>
            <form method ="post" action ="testUser.jsp">
                <input type ="submit" value="Accept and Continue" /> 
                
            </form>

        </div>
    </section>

    
    <!-- Bootstrap core JavaScript -->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/tether/tether.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.min.js"></script>
    </body>
</html>
