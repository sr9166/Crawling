<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>

<%
	String startdate = request.getParameter("startdate");
	String enddate = request.getParameter("enddate");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script type="text/javascript"
	src="http://code.jquery.com/jquery-3.2.0.min.js"></script>
<script type="text/javascript">
	$(function() {
		$('.checkbtn').click(function() {
			$.ajax({
				type : "GET",
				url : "/data",
				success : function(data) {
					$("#dataArea").html(data);
				},
				error : function(data) {
					alert("ERROR!!!");
				}
			});
		});
	});
</script>

<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Data</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>Crawling</title>
</head>
<body>
	<div class=container>
		<h1>Crawling Server is running...</h1>
		<p>
			START DATE :
			<%=startdate%></p>
		<p>
			END DATE :
			<%=enddate%></p>
		<button type="submit" id="checkbtn" name="checkbtn" class="checkbtn">check
			button</button>
		<div id="dataArea"></div>
	</div>
</body>
</html>

