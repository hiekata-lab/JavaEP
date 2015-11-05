<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
<title>問題一覧</title>
<link rel="apple-touch-icon" href="<c:url value='/resources/images/apple-touch-icon.png' />" >
<link rel="shortcut icon" href="<c:url value='/resources/images/favicon.ico' />">
<link href="<c:url value='/resources/bootstrap3/css/bootstrap.min.css' />" rel="stylesheet">
<link href="<c:url value='/resources/datatables/css/jquery.dataTables.css' />" rel="stylesheet">
<link href="<c:url value='/resources/css/styles.css' />" rel="stylesheet">
</head>

<body>
	<c:import url="/WEB-INF/views/include/header.jsp">
		<c:param name="activePage" value="index" />
	</c:import>

	<div class="container">

       	<blockquote>問題一覧画面</blockquote>

		<!-- 合計点数と順位 -->
		<div style="margin: 0 20 20 20;">
			<div style="float: left;">
				合計点数： <span id="total"></span>点<br>
				(現在の上位) <span id="highscore"></span>
			</div>
			<div style="float: right;">
				<button onclick="showHighScore()" type="button" class="btn btn-info">点数を再計算</button>
			</div>
		</div>

		<!-- 問題一覧 -->
		<div class="list" style="clear: both; margin-top: 80px;">
			<div style="padding : 10px;">
				<table id="table" class="table table-hover">
					<c:if test="${mode == 'exam'}">
						<sec:authorize ifAnyGranted="ROLE_USER">
							<colgroup>
								<col style="width: 15%;">
							</colgroup>
							<colgroup>
								<col style="width: 10%;">
							</colgroup>
							<colgroup>
								<col style="width: 10%;">
							</colgroup>
							<colgroup>
								<col style="width: 65%;">
							</colgroup>
						</sec:authorize>
					</c:if>
					<thead>
						<tr>
							<th style="display: none;">ID</th>
							<th>Number</th>
							<th>Score</th>
							<th>難易度</th>
							<th>問題</th>
							<sec:authorize ifAnyGranted="ROLE_ADMIN">
								<th>テストで表示</th>
							</sec:authorize>
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
	var username = "<sec:authentication property="principal.username" />";
	var auth = "<sec:authentication property="authorities" />";
	var maxLength = 30;
	var mode = "${mode}";
	var isAdmin = auth.indexOf("ADMIN") != -1; 

	$(function() {
		showList();
		showHighScore();
	});

	// 問題一覧の作成
	function showList() {
		var tbody = $("#tbody");
		var path;
		if (mode == "exam") {
			path = "examList";
		} else {
			path = "list";
		}

		// 問題一覧情報の取得
		$.get(path, function(data) {
			var total = 0;
			data = shuffle(data);

			for (var i in data) {
				var question = data[i];
				var id = question.id;
				var score = question.score;
				var difficulty = question.difficulty;
				var content = question.content; // 問題文
				var shownInExam = question.shownInExam;

				var tr = $("<tr>");
				tbody.append(tr);

				// ID
				var td = $("<td>");
				td.attr("style", "display: none;");
				tr.append(td);
				td.append(id);

				// Number
				td = $("<td>");
				tr.append(td);
				
				var form = $('<form>');
				td.append(form);
				form.attr({
					"name": "question_" + id, 
					"method": "post", 
					"action": "question"
				}).css({"display": "inline"});
				
				var a = $('<a>');
				var num = Number(i) + 1;
				form.append(a);
				a.attr({
					"href": "javascript:void(0)", 
					"onclick": "document.question_" + id + ".submit();return false;"
				});
				
				if (mode == "exam") {
					a.append("テスト問題 " + ("000" + num).slice(-3));
				} else {
					a.append("Question " + ("000" + id).slice(-3));
				}
				
				var input = $('<input>');
				form.append(input);
				input.attr({
					"type": "hidden", 
					"name": "questionId", 
					"value": id
				});

				// スコア
				td = $("<td>");
				tr.append(td);
				td.append(score);
				total += score;

				// 難易度
				td = $("<td>");
				tr.append(td);
				td.append(calcStars(difficulty)); // 難易度の★を計算

				// 問題文
				td = $("<td>");
				tr.append(td);
				arrangeContents(id, td, content, maxLength);
				
				// テストで表示
				if (auth.indexOf("ADMIN") != -1) {
					td = $("<td>");
					tr.append(td);
					var input = $('<input>').attr({
						"type": "checkbox",
						"id": "shown_in_exam_" + id,
						"value": id,
						"checked": shownInExam
					}); 
					td.append(input);
					var label = $('<label>').attr({
						"for": "shown_in_exam_" + id,
					});
					input.after(label);
					if (shownInExam) {
						label.text("表示中");	
					} else {
						label.text("非表示中");
					}
	            }
			}

			$("#total").text(total);
			
			// checkbox click event
			if (isAdmin) {
				setCheckboxEvent();
			}
			
			if (mode == "exam" && !isAdmin) {
				// mode == "exam"のときはdatatableを切る
			} else {
				$('#table').dataTable({
					"iDisplayLength": 50,
			        "pagingType": "full_numbers",
			        "order": [[ 0, "asc" ]]
			    });
			}
		});
	}

	function showHighScore() {
		$("#highscore").empty();

		$.get("score", function(data) {
			for (var i = 0; i < data.length; i++) {
				$("#highscore").append((i + 1) + "位: " + data[i] + "点 ");
			}
		});
	}
	
	function setCheckboxEvent() {
		$('input[type="checkbox"]').change(function() {
			var checked = $(this).prop('checked');
			
			$.ajax({
				type: "POST",
				url: "setShownInExam",
				data: {
					questionId: Number($(this).val()),
					shownInExam: checked
				}
			});
			
			if (checked) {
				$(this).next().text('表示中');
			} else {
				$(this).next().text('非表示中');
			}
		});
	}
	</script>

</body>
</html>