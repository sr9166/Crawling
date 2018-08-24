<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Data</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
		<div class="container">
		
			<h4 class = naver>naver keywords crawling : ${navercnt} / ${loopingnumber}</h4>
			<h4 class = fanpages>facebook fanpages crawling : ${fanpagecnt} / ${naver}</h4>
			<h4 class = filtering>filtering fanpages : ${filteringcnt} / ${fanpage}</h4>
			
			<c:if test="${fanpagecnt eq '-1'}">
				<script type="text/javascript">
					alert("Facebook Login ERROR!!! Retry with correct ID and PW");
				</script>
			</c:if>
			
		    <table class="table table-striped">
		     	<tr>
		        	<th>NO</th>
		            <th>NAME</th>
		            <th>URL</th>
		            <th>NUMBER</th>
		        </tr>
				<c:forEach var="item" items="${list}" varStatus="status">
		              <tr style="margin-top:300px" class=font-big>
		                  <td>${status.count }</td>
		                  <td>${item.name}</td>
		                  <td>${item.url}</td>
		                  <td>${item.number}</td>
		              </tr>
				</c:forEach>
			</table>
		</div>
</body>
</html>