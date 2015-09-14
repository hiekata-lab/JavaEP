<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
<title>編集画面</title>
<link rel="apple-touch-icon" href="<c:url value='/resources/images/apple-touch-icon.png' />" >
<link rel="shortcut icon" href="<c:url value='/resources/images/favicon.ico' />">
<link href="<c:url value='/resources/bootstrap3/css/bootstrap.min.css' />" rel="stylesheet">
<link href="<c:url value='/resources/css/styles.css' />" rel="stylesheet">
</head>

<body>
	<c:import url="/WEB-INF/views/include/header.jsp">
		<c:param name="activePage" value="index" />
	</c:import>

	<div class="container" id="main">
		<blockquote class="title red-font">Question&nbsp;<span id="qid"></span>&nbsp;編集画面</blockquote>

		<span class="red-font">難易度：&nbsp;</span>
		<select id="difficulty">
			<option value="1">1</option>
			<option value="2">2</option>
			<option value="3">3</option>
			<option value="4">4</option>
			<option value="5">5</option>
		</select>
		
		<hr>
		
		<div style="margin-top: 5px;">
			<input id="object_oriented" name="object_oriented" type="checkbox">
			<label for="object_oriented" class="red-font">オブジェクト指向</label><br>
			<label id="class_name_label" for="class_name" class="red-font" style="color: #dddddd;">クラス名</label>
			<input id="class_name_box" type="text" disabled="disabled">
		</div>
		
		<hr>

		<!-- Question -->
		<div class="row">

  			<div class="col-md-6">
  				<p class="red-font">Question</p>
				<textarea id="question" class="c2" style="padding: 0;"></textarea>
			</div>

		</div>

		<hr>

		<!-- Source Code & Args -->
		<div class="row">

			<!-- Source Code -->
			<div class="col-md-6">
  				<p class="red-font">Source Code</p>
				<!-- line number -->
				<div class="ol" id="ol">
					<textarea cols="2" class="li" id="li" disabled="disabled"></textarea>
				</div>

				<!-- source code -->
				<textarea id="c2" class="c2 source" onkeyup="keyUp()" onscroll="G('c2').scrollTop = this.scrollTop;" oncontextmenu="return false"></textarea>
			</div>

			<!-- Args -->
			<div class="col-md-6">
  				<p class="red-font">Args</p>
				<input type="text" id="args_1"><br>
				<input type="button" value="追加" id="add_btn" class="btn btn-primary" style="margin: 5 0 0 0;">
				<input type="button" value="削除" id="remove_btn" class="btn btn-danger" style="margin: 5 0 0 0;">
			</div>

		</div>

		<!-- Pagination -->
		<div style="text-align : center">
			<ul class="pager" id="pager"></ul>
			<button class="btn btn-warning" onclick="save()">保存</button>
			<a href="/JavaEP/" class="btn btn-primary">問題一覧に戻る</a>
		</div>

	</div>

	<c:import url="/WEB-INF/views/include/footer.jsp" ></c:import>

    <script src="<c:url value='/resources/js/jquery.1.11.1.min.js'/>"></script>
    <script src="<c:url value='/resources/bootstrap3/js/bootstrap.min.js'/>"></script>
    <script src="<c:url value='/resources/js/linenum.js'/>"></script>
    <script src="<c:url value='/resources/autosize/jquery.autosize-min.js'/>"></script>
    <script src="<c:url value='/resources/js/script.js'/>"></script>
	<script>
	var argsCount = 1;

	var questionId = "${questionId}";
	var username = "<sec:authentication property="principal.username" />";
	var auth = "<sec:authentication property="authorities" />";

	$(function() {
		// for OOP question
		$('#object_oriented').change(function() {
			if ($(this).is(':checked')) {
				$('#class_name_label').removeAttr('style');
				$('#class_name_box').removeAttr('disabled').focus();
			} else {
				$('#class_name_label').css('color', '#dddddd');
				$('#class_name_box').attr('disabled', 'disabled');
			}
		});
		
		// display question's information
		$.get("show", { questionId: questionId }, function(data) {
			if (data.question) {
				$("#question").text(data.question);
				$("#c2").text(data.source);
				$("#difficulty").val(data.difficulty);
				var args = JSON.parse(data.args);
				for (var i = 1; i <= args.length; i++) {
					if (i != 1) {
						addTextbox();
					}
					$("#args_" + i).val(args[i - 1]);
				}
				setPagerGet(questionId);
				
				// for OOP
				if (data.objectOriented) {
					$('#object_oriented').attr("checked", true);
					$('#class_name_box').removeAttr('disabled').val(data.className);
					$('#class_name_label').removeAttr('style');
				}
			} else {
				$('#c2').text(makeTemplate(questionId));
			}
		});

		$("#qid").append(questionId);
		
		// resize event setting at source code textarea
 		$(window).on("mouseup", function(e) {
    		$('#li').outerHeight($('#c2').outerHeight());
    	});
		
		// textbox of args
		$('#add_btn').click(function() {
			addTextbox();
		});
		
		$('#remove_btn').click(function() {
			if (argsCount >= 2) {
				var box = $('#args_' + argsCount--);
				box.next().remove();
				box.remove();
			}
		});
	});

	function addTextbox() {
		var preBox = $('#args_' + argsCount);
		var br = $('<br>').insertAfter(preBox);
		var newBox =
			$('<input>')
			.attr('type', 'text')
			.attr('id', 'args_' + ++argsCount)
			.insertAfter(br);
	}

	function save() {
		var question = $('#question').val();
		var source = $('#c2').val();
		var difficulty = $('#difficulty').val();
		var args = [];

		for (var i = 1; i <= argsCount; i++) {
			args.push($('#args_' + i).val());
		}
		
		var objectOriented = false;
		var className = "";
		if ($('#object_oriented').is(':checked')) {
			objectOriented = true;
			className = $('#class_name_box').val();
			
			if (!className) {
				alert("Please input the class name in source code.");
				return;
			}
		}

		$.post("edit/save", {
				questionId: questionId,
				question: question,
				source: source,
				difficulty: difficulty,
				args: JSON.stringify(args),
				objectOriented: objectOriented,
				className: className
			},
			function(data) {
				alert("Saved.");
				window.location.href = '/JavaEP/';
			}
		);
	}
	</script>

</body>
</html>