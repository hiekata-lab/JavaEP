<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
<title>設定画面</title>
<link rel="apple-touch-icon" href="<c:url value='/resources/images/apple-touch-icon.png' />" >
<link rel="shortcut icon" href="<c:url value='/resources/images/favicon.ico' />">
<link href="<c:url value='/resources/bootstrap3/css/bootstrap.min.css' />" rel="stylesheet">
</head>

<body>
	<c:import url="/WEB-INF/views/include/header.jsp">
		<c:param name="activePage" value="setting" />
	</c:import>

	<div class="container" >

		<blockquote>設定画面</blockquote>

		<div class="row">
			<div class="col-sm-6 col-md-4">
				<div class="thumbnail">
					<div class="caption">
						<h3>パスワードの更新</h3>
						<div class="form-group">
							<label for="password">パスワード</label>
							<input type="password" class="form-control" id="password" />
						</div>
						<p><a href="#" class="btn btn-primary" onclick="updatePassword()">更新</a></p>
					</div>
				</div>
  			</div>
  		</div>

	</div>

	<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>

	<script src="<c:url value='/resources/js/jquery.1.11.1.min.js'/>"></script>
    <script src="<c:url value='/resources/bootstrap3/js/bootstrap.min.js'/>"></script>
	<script>
	var username = "<sec:authentication property="principal.username" />";

	function updatePassword() {
		var password = $("#password").val();
		if (!password) {
			alert("Your password is empty!");
			return;
		}

		$.ajax({
			type: "POST",
			url: "setting/updatePassword",
			data: { username : username, password : password },
			success: function(data) {
				alert(data);
			}
		});
	};
	</script>

</body>
</html>