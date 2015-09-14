<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
<title>回答画面</title>
<link rel="apple-touch-icon" href="<c:url value='/resources/images/apple-touch-icon.png' />" >
<link rel="shortcut icon" href="<c:url value='/resources/images/favicon.ico' />">
<link href="<c:url value='/resources/bootstrap3/css/bootstrap.min.css' />" rel="stylesheet">
<link href="<c:url value='/resources/css/styles.css' />" rel="stylesheet">
</head>

<body>
	<c:import url="/WEB-INF/views/include/header.jsp">
		<c:param name="activePage" value="qanda" />
	</c:import>

	<div class="container">

		<blockquote class="title red-font">
			<!-- 質問&nbsp;<span id="qandaTitle"></span>&nbsp;(問題&nbsp;<span id="questionTitle"></span>&nbsp;：&nbsp;<span id="userTitle"></span>&nbsp;さん)に対する回答-->
			質問&nbsp;<span id="qandaTitle"></span>&nbsp;(問題&nbsp;<span id="questionTitle"></span>)に対する回答
		</blockquote>

		<hr>

		<!-- Question & Answer code -->
		<div class="row">

			<!-- Question -->
  			<div class="col-md-6">
  				<p class="red-font">Question</p>
				<pre id="question" class="question"></pre>
			</div>

			<!-- Answer code -->
			<div class="col-md-6">
				<p class="red-font">Answer Code</p>
				<pre id="template"></pre>
			</div>
		</div>

		<hr>

		<!-- Student code & Console -->
		<div class="row">

			<!-- Student code -->
			<div class="col-md-6">
  				<p class="red-font">Student Code</p>

					<!-- line number -->
					<div class="ol" id="studentOl">
						<textarea cols="2" class="li" id="studentLi" disabled="disabled"></textarea>
					</div>

					<!-- textarea -->
					<textarea id="studentC2" class="c2 source" onkeyup="keyUp()" onscroll="G('li').scrollTop = this.scrollTop;" oncontextmenu="return false"></textarea>
			</div>

			<!-- Console -->
			<div class="col-md-6">
  				<p class="red-font">Console</p>
				<pre id="studentConsole" class="console"></pre>
			</div>

		</div>

		<hr>

		<!-- Question comment & Response comment -->
        <div class="row">

            <!-- Question comment -->
            <div class="col-md-6">
                <p class="red-font">Question Comment</p>

                <textarea style="width: 100%;" id="question_comment"></textarea>

                <div style="padding: 10px;">
                    <button onclick="edit()" type="button" class="btn btn-sm btn-warning"><span class="glyphicon glyphicon-pencil"></span>&nbsp;修正</button>
                    <div class="btn-group" data-toggle="buttons">
                        <label class="btn btn-default">
                            <input type="radio" name="options" id="option0">&nbsp;未解決
                       	</label>
                        <label class="btn btn-default">
                            <input type="radio" name="options" id="option1">
                            <span class="glyphicon glyphicon-flag"></span>&nbsp;解決済み
                        </label>
                    </div>
                </div>
            </div>

            <!-- Response comment -->
            <div class="col-md-6">
                <p class="red-font">Response Comment by " <span id="respondant"></span> "</p>
                <textarea style="width: 100%;" id="response_comment"></textarea>
                <div style="padding: 10px;">
                    <button onclick="response()" type="button" class="btn btn-sm btn-success"><span class="glyphicon glyphicon-ok-sign"></span>&nbsp;回答</button>
                </div>
            </div>

        </div>



        <sec:authorize ifAnyGranted="ROLE_ADMIN">

        <hr>

        <h2>TA確認用</h2>

        <div class="row">

            <!-- Source code -->
            <div class="col-md-6">
                <p class="red-font">Source Code</p>

                <!-- line numbers -->
                <div class="ol" id="ol">
                    <textarea cols="2" class="li" id="li" disabled="disabled"></textarea>
                </div>

                <!-- textarea -->
                <textarea id="c2" class="c2 source" onkeyup="keyUp()" onscroll="G('li').scrollTop = this.scrollTop;" oncontextmenu="return false"></textarea>

                <!-- args & buttons -->
                <div style="padding: 10px;">
                    ARGS:&nbsp;<input style="text" id="args">
                    <button id="save_btn" onclick="save()" type="button" class="btn btn-sm btn-warning"><span class="glyphicon glyphicon-save"></span>&nbsp;保存</button>
                    <button id="compile_btn" onclick="compile()" type="button" class="btn btn-sm btn-danger"><span class="glyphicon glyphicon-share-alt"></span>&nbsp;コンパイル</button>
                    <button id="execute_btn" onclick="execute()" type="button" class="btn btn-sm btn-info"><span class="glyphicon glyphicon-play"></span>&nbsp;実行</button>
                    <button id="mark_btn" onclick="mark()" id="opener2" type="button" class="btn btn-sm btn-success"><span class="glyphicon glyphicon-pencil"></span>&nbsp;採点</button>
                </div>
            </div>

            <!-- Console -->
            <div class="col-md-6">
                <p class="red-font">Console</p>
                <pre id="console" class="console"></pre>
            </div>

        </div>
        </sec:authorize>

        <hr>

		<!-- Pagination -->
		<div style="text-align: center;">
			<a href="/JavaEP/qanda" class="btn btn-primary">質問一覧に戻る</a>
		</div>

	</div>

	<c:import url="/WEB-INF/views/include/footer.jsp" ></c:import>

	<script src="<c:url value='/resources/js/jquery.1.11.1.min.js'/>"></script>
    <script src="<c:url value='/resources/bootstrap3/js/bootstrap.min.js'/>"></script>
    <script src="<c:url value='/resources/js/linenum.js'/>"></script>
    <script src="<c:url value='/resources/autosize/jquery.autosize-min.js'/>"></script>
    <script src="<c:url value='/resources/js/script.js'/>"></script>


	<script>
	var qandaId = "${qandaId}";
	var username = "<sec:authentication property="principal.username" />";
	var auth = "<sec:authentication property="authorities" />";
	var questionId;

	$(function() {
		// display question's information
		$.post("response/get", { qandaId : qandaId }, function(data) {
			$("#question").append(data.questionContent);

			if(auth.indexOf("ADMIN") != -1){
				$("#template").text(data.answerCode);
			} else if (data.objectOriented) {
				$('#template').text(makeOopTemplate(data.questionId, data.className));
			} else {
				$('#template').text(makeTemplate(data.questionId));
			}

			// ソース画面
			$("#studentC2").append(escapeHTML(data.savedCode));

			// コンソール
			$("#studentConsole").text(data.consoleContent);

			// 質問番号
			$("#qandaTitle").text(qandaId);

			// 問題番号
            $("#questionTitle").text(data.questionId);
            questionId = data.questionId;

          	// 投稿者
            $("#userTitle").text(data.username);

          	// 質問コメント
            $("#question_comment").text(data.questionComment);

			// 回答コメント
			if (data.responseComment != null) {
				$("#response_comment").text(data.responseComment);
			}

            // 回答者
            if (data.respondant != null) {
            	$("#respondant").text(data.respondant);
            }

          	// テキストエリアのリサイズ
            $("#question_comment").autosize();
            $("#response_comment").autosize();

            setFlag(data.solvedFlag);

         	// resize event setting at source code textarea
     		$(window).on("mouseup", function(e) {
        		$('#li').outerHeight($('#c2').outerHeight());
        	});
		});
	});

	function response() {
		var responseComment = $("#response_comment").val();
		$.post("response/answer", { qandaId : qandaId , responseComment : responseComment }, function(data) {
			alert(data);
		});
	}

	function edit() {
        var questionComment = $("#question_comment").val();
        $.post("response/comment", { qandaId : qandaId , questionComment : questionComment }, function(data) {
            alert(data);
        });
    }

	// 解決済みフラグの設定関係
	function setFlag(solvedFlag) {
		var radio = $("#option" + solvedFlag);
        radio.attr("checked", true);

        var div = radio.parent();
        div.attr("class", "btn btn-default active");

        $(".btn-group").click(function() {
            var checked = $("input[name='options']:not(:checked)").attr("id").replace("option", "");
            $.post("response/setFlag", { qandaId : qandaId , solvedFlag : checked });
        });
	}

	   // 保存
    function save(){
        var source = $('#c2').val();

        $.post("../question/save", { questionId: questionId, source: source }, function(data) {
            $("#console").empty();
            $("#console").append("Saved.");
        });
    }

    // コンパイル
    function compile(){
        var classname = "Question" + questionId;
        var source = $("#c2").val();

        $.post("../question/compile", { questionId: questionId, classname: classname, source: source }, function(data) {
            $("#console").empty();
            $("#console").append(data.message);
        });
    }

    // 実行
    function execute(){
        var classname = "Question" + questionId;
        var args = $("#args").val();

        $("#console").text("Please wait a moment...");
        disableButtons();

        $.post("../question/execute", { questionId: questionId, classname: classname, args: args }, function(data) {
            $("#console").empty();
            $("#console").append(data.message);

            enableButtons();
        });
    }

	// 採点
    function mark(){
        var classname = "Question"+questionId;
        var source = $("#c2").val();

        $("#console").text("Please wait a moment...");
        disableButtons();

        $.post("../question/mark", { questionId: questionId, classname: classname, source: source }, function(data) {
            $("#console").empty();
            $("#console").append(data.message);

            enableButtons();
        });
    }

    function disableButtons() {
        $("#execute_btn").addClass("disabled");
        $("#mark_btn").addClass("disabled");
    }

    function enableButtons() {
        $("#execute_btn").removeClass("disabled");
        $("#mark_btn").removeClass("disabled");
    }
	</script>

</body>
</html>