<%@page import="igs.DataManager" %>
<% DataManager mn = igs.DataManager.getInstance();
int arr[]=new int[10];
        arr=mn.questionCorrect();
        out.print(arr[1]);
%>
<html>
  <head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {'packages':['bar']});
      google.charts.setOnLoadCallback(drawStuff);

      function drawStuff() {
        var data = new google.visualization.arrayToDataTable([
          ['Question', 'Percentage'],
          ["Question 1", <%=arr[0]%>],
          ["Question 2", <%=arr[1]%>],
          ["Question 3", <%=arr[2]%>],
          ["Question 4", <%=arr[3]%>],
          ["Question 5", <%=arr[4]%>],
          ["Question 6", <%=arr[5]%>],
          ["Question 7", <%=arr[6]%>],
          ["Question 8", <%=arr[7]%>],
          ["Question 9", <%=arr[8]%>],
          ["Question 10", <%=arr[9]%>]
          
        ]);

        var options = {
          width: 700,
          legend: { position: 'none' },
          chart: {
            title: 'Correct Percentage',
            subtitle: '{Percentage of correct answers}' },
          axes: {
            x: {
              0: { side: 'bottom', label: 'White to move'} // Top x-axis.
            }
          },
          bar: { groupWidth: "80%" },
          colors:['#0097A7'],
          //backgroundColor: '#B2EBF2'
        };

        var chart = new google.charts.Bar(document.getElementById('top_x_div'));
        // Convert the Classic options to Material options.
        chart.draw(data, google.charts.Bar.convertOptions(options));
      };
    </script>
  </head>
  <body>
      <div id="top_x_div" style="width: 800px; height: 600px; "></div>
  </body>
</html>