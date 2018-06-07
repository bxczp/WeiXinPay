<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="jquery.min.js"></script>
<title>支付页面测试</title>
</head>
<body>
商品信息：xxxxx<br/>
<img src="loadPayImage"/>

<script type="text/javascript">

	function orderStatus(){
		$.post("loadPayState",{},function(data){
			if(data==1){
				window.location.href="index.jsp";
			}
		})
	}
	
	setInterval("orderStatus()",3000);

</script>
</body>
</html>