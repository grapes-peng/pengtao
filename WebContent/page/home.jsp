<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>home</title>
<script type="text/javascript">
	function go() {
		window.open("page/test.jsp");
		form.submit();
	}
</script>
</head>
<body>
	<h2>spring mvc 实例</h2>
	<form id="form" action="<%=request.getContextPath()%>/go">
		<td><input type="button" onclick="go()" /></td>
	</form>
</body>
</html>
</html>