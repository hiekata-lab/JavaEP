<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
<title>ユーザ一覧</title>
<link rel="apple-touch-icon" href="<c:url value='/resources/images/apple-touch-icon.png' />" >
<link rel="shortcut icon" href="<c:url value='/resources/images/favicon.ico' />">
<link rel="stylesheet" href="<c:url value='/resources/bootstrap3/css/bootstrap.min.css' />">
<link rel="stylesheet" href="<c:url value='/resources/datatables/css/jquery.dataTables.css' />">
<link rel="stylesheet" href="<c:url value='/resources/css/styles.css' />">
</head>
<body>
	<c:import url="/WEB-INF/views/include/header.jsp">
		<c:param name="activePage" value="admin" />
	</c:import>

	<div class="container">

       	<blockquote>ユーザ一覧画面</blockquote>

		<div class="list">
			<div style="padding: 10px;">
				<table id="table" class="table table-hover">
					<thead>
						<tr>
							<th>User ID</th>
							<th>Authority</th>
							<th>Score</th>
							<th>Exam Score</th>
						</tr>
					</thead>
					<tbody id="tbody"></tbody>
				</table>
			</div>
		</div>

	</div>

	<c:import url="/WEB-INF/views/include/footer.jsp" ></c:import>

	<script src="<c:url value='/resources/js/jquery.1.11.1.min.js'/>"></script>
	<script src="<c:url value='/resources/bootstrap3/js/bootstrap.min.js'/>"></script>
	<script src="<c:url value='/resources/datatables/js/jquery.dataTables.js'/>"></script>
	<script src="<c:url value='/resources/js/script.js'/>"></script>
	<script>
	$(function() {
		$.ajax({
			type: "get",
			url: "showUsers",
			success: function(data) {
				for (var i in data) {
					var user = data[i];
					
					var tr = $('<tr>');
					$('#tbody').append(tr);
					
					tr.append($('<td>').text(user.username));
					tr.append($('<td>').text(user.authority));
					tr.append($('<td>').text(user.score));
					tr.append($('<td>').text(user.examScore));
				}
				
				$('#table').dataTable({
					"iDisplayLength": 50,
			        "pagingType": "full_numbers",
			        "order": [[ 0, "asc" ]]
			    });
			}
		});
	});
	</script>

</body>
</html>