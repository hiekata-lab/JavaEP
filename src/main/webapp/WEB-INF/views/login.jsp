<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="apple-touch-icon" href="<c:url value='/resources/images/apple-touch-icon.png' />" >
<link rel="shortcut icon" href="<c:url value='/resources/images/favicon.ico' />">
<link href="<c:url value='/resources/bootstrap3/css/bootstrap.min.css' />" rel="stylesheet">
<link href="<c:url value='/resources/css/signin.css' />" rel="stylesheet">
<title>Login</title>
</head>

<body>
	<div class="container">

		<form action="j_spring_security_check" method="post" class="form-signin">
			<h2 class="form-signin-heading">Please sign in</h2>
			<input type="text" class="form-control" name="j_username" placeholder="User ID" required autofocus>
			<input type="password" class="form-control" name="j_password" placeholder="Password" required>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Login</button>
		</form>

		<c:if test="${not empty param.login_error}">
			<font color="red">${SPRING_SECURITY_LAST_EXCEPTION.message}</font>
		</c:if>

	</div> <!-- /container -->
</body>
</html>