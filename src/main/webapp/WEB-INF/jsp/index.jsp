<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Index</title>
<script src="https://code.jquery.com/jquery-3.2.1.js"></script>
	<link rel="stylesheet" href="/css/jquery-ui.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>
    <script src="http://netdna.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
    <script type='text/javascript' src='//code.jquery.com/jquery-1.8.3.js'></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.5.0/css/bootstrap-datepicker3.min.css">
    
    <script type='text/javascript' src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.5.0/js/bootstrap-datepicker.min.js"></script>
    <script src="/js/bootstrap-datepicker.kr.js" charset="UTF-8"></script>
    <script type='text/javascript'>
    $(function(){
        $('.input-group.date').datepicker({
            calendarWeeks: false,
            todayHighlight: true,
            autoclose: true,
            format: "yyyy-mm-dd",
            language: "kr"
        });
    });
    </script>
    
    <script>
    function check_form() {
    	if(fr.startdate.value =="") {
    		alert("시작일자를 입력해 주세요.");
    		fr.startdate.focus();
    		return false;
    	}
    	else if(fr.enddate.value == "") {
    		alert("종료일자를 입력해 주세요.");
    		fr.enddate.focus();
    		return false;
    	}
    	else if(fr.startdate.value > fr.enddate.value) {
    		alert("시작일자가 종료일자보다 빨라야 합니다.");
    		fr.startdate.focus();
    		return false;
    	}
    	else if(fr.facebook_id.value =="") {
    		alert("아이디를 입력해 주세요.");
    		fr.facebook_id.focus();
    		return false;
    	}
    	else if(fr.facebook_pw.value == "") {
    		alert("비밀번호를 입력해 주세요.");
    		fr.facebook_pw.focus();
    		return false;
    	}
    	else if(fr.maxcnt.value == "") {
    		alert("Max count를 입력해 주세요.");
    		fr.startdate.focus();
    		return false;
    	}
    	else
    		return true;
    }
    </script>
</head>
<body>
	<div class="container">
		<h1 style="margin: 15px 25px 15px 0px; padding: 10px;">Write data and press submit button</h1>

		<form action="/crawling" method="POST" onsubmit = "return check_form()" name="fr">
		
			<div class="form-group">
			<label for="start">Start Date:</label>
				<div class="input-group date">
					<input type="text" class="form-control" 
						placeholder="YYYY-MM-DD"
						id = "startdate"
						name = "startdate"><span
						class="input-group-addon"><i
						class="glyphicon glyphicon-calendar"></i></span>
				</div>
			</div>

			<div class="form-group">
				<label for="end">End Date:</label>
				<div class="input-group date">
					<input type="text" class="form-control" 
						placeholder="YYYY-MM-DD"
						id = "enddate"
						name = "enddate"><span
						class="input-group-addon"><i
						class="glyphicon glyphicon-calendar"></i></span>
				</div>
			</div>

			<div class="form-group">
				<label for="id">Facebook ID:</label> <input type="text" class="form-control"
					id="facebook_id" placeholder="Enter ID" name="facebook_id">
			</div>

			<div class="form-group">
				<label for="pwd">Facebook Password:</label> <input type="password"
					class="form-control" id="facebook_pw" placeholder="Enter password" name="facebook_pw">
			</div>
			
			<div class="form-group">
				<label for="Maxcnt">MAX FanPage count from one keyword:</label> <input type="text"
					class="form-control" id="maxcnt" placeholder="Enter Max count" name="maxcnt" value="Infinite">
					* Infinite : 팬 페이지 무한 스크롤
					* 그 외 숫자 : MaxCount로 설정됨 
			</div>
			
			<button type="submit" class="btn btn-primary">submit</button>
		</form>
		
		<button style="margin-top: 15px;" id="button for check Data" class ="btn btn-info"
			onclick="location='/data'">Check Data</button>
	</div>
</body>
</html>