<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
<title>解答画面</title>
<link rel="apple-touch-icon" href="<c:url value='/resources/images/apple-touch-icon.png' />" >
<link rel="shortcut icon" href="<c:url value='/resources/images/favicon.ico' />">
<link href="<c:url value='/resources/bootstrap3/css/bootstrap.min.css' />" rel="stylesheet">
<link href="<c:url value='/resources/css/styles.css' />" rel="stylesheet">
<!-- Magnific Popup core CSS file -->
<link rel="stylesheet" href="<c:url value='/resources/popUp/magnific-popup.css' />">

<style type="text/css">
    .white-popup {
        position: relative;
        background: #FFF;
        padding: 20px;
        width: auto;
        max-width: 500px;
        margin: 20px auto;
    }
</style>
</head>

<body>
	<c:import url="/WEB-INF/views/include/header.jsp">
		<c:param name="activePage" value="index" />
	</c:import>

	<div class="container">

		<blockquote class="title red-font">Question&nbsp;<span id="questionTitle"></span></blockquote>

		<h5 class="red-font">難易度：<span id="difficulty"></span>&nbsp;<span id="numOfStudent"></span>人が正解</h5>

		<sec:authorize ifAnyGranted="ROLE_ADMIN">
			<c:if test="${mode != 'exam'}">
				<h5><a href="<c:url value='/question/edit?questionId=${questionId}' />">編集</a></h5>
			</c:if>
		</sec:authorize>

		<hr>

		<!-- Question & Template -->
		<div class="row">

			<!-- Question -->
  			<div class="col-md-6">
  				<p class="red-font">Question</p>
				<pre id="question" class="question"></pre>
			</div>

			<!-- Template -->
			<div class="col-md-6">
				<p class="red-font">Template</p>
				<pre id="template"></pre>
			</div>

		</div>

		<hr>

		<!-- Source code & Console -->
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

				<!-- Q&A -->
				<c:if test="${mode != 'exam'}">
					<div style="padding: 10px;">
				        <table>
							<tr>
                                <td>
                                    <a href="#" id="popupBtn" class="btn btn-danger" onclick="getSimilarQandas(); return false;"><span class="glyphicon glyphicon-send"></span> TAに質問する</a>
                                </td>
							</tr>
				        </table>
			        </div>
		        </c:if>
			</div>

		</div>



		<!-- Pagination -->
		<div style="text-align : center">
			<ul class="pager" id="pager"></ul>
			<a href="/JavaEP/" class="btn btn-primary">問題一覧に戻る</a>
		</div>

		<c:if test="${mode != 'exam'}">
			<hr>
	
			<button class="btn btn-primary" id="jQueryPush">質問済み内容の表示&nbsp;<span class="caret"></span></button>

        	<!-- Q&A一覧 -->
	        <div id="qanda" style="display: none;">
	               <br>
		            <table class="table table-hover table-bordered table-striped" id="table">
		                <thead>
			                <tr class="info">
			                    <th>ID</th>
			                    <th>問題番号</th>
			                    <th>質問</th>
			                    <th>エラーコード</th>
			                    <th>投稿日時</th>
			                    <th>回答済フラグ</th>
			                    <th>コメント</th>
			                    <th>回答者</th>
			                    <th>解決済フラグ</th>
			                    <th></th>
			                </tr>
		                </thead>
		                <tbody id="tbody"></tbody>
		            </table>
	        </div>
        </c:if>

	</div>

	<!-- 自動回答の結果表示ＵＩ -->
    <div id="popup" class="white-popup mfp-hide">
        <p class="red-font">参考となる回答はありますか？</p>
        <table class="table table-hover table-bordered table-striped">
        <thead>
            <tr class="info">
                <th>回答</th>
                <th>類似度</th>
            </tr>
        </thead>
        <tbody id="tbody4popup"></tbody>
        </table>

        <hr>
        <p class="red-font">続けて質問する</p>
        <div class="form-group">
            <label for="comment" class="col-sm-3 control-label">質問内容</label>
            <div class="col-sm-9">
                <textarea class="form-control" id="comment" placeholder="コメントする..."></textarea>
            </div>
        </div>

        <br><br>
        <a class="btn btn-primary" onclick="ask(); return false"><span class="glyphicon glyphicon-send"></span> TAに質問する</a>

	</div>

	<!-- 自動採点結果のアンケートＵＩ -->
        <div id="dialog" class="white-popup mfp-hide">
            <p class="red-font">正解おめでとうございます。<br>参考となった回答はありましたか？</p>
            <table class="table table-hover table-bordered table-striped" style="word-break:break-all;word-wrap:break-word;">
                <thead>
                    <tr>
                        <th>回答</th>
                        <th>類似度</th>
                        <th>選択</th>
                    </tr>
                </thead>
                <tbody id="tbody4dialog">
                </tbody>
            </table>
            <button class="btn btn-primary" onclick="getInquiry()"><span class="glyphicon glyphicon-send"></span> 送信</button>

        </div>

	<c:import url="/WEB-INF/views/include/footer.jsp" ></c:import>

	<script src="<c:url value='/resources/js/jquery.1.11.1.min.js'/>"></script>
    <script src="<c:url value='/resources/bootstrap3/js/bootstrap.min.js'/>"></script>
	<script src="<c:url value='/resources/js/linenum.js'/>"></script>
    <script src="<c:url value='/resources/autosize/jquery.autosize-min.js'/>"></script>
    <script src="<c:url value='/resources/js/script.js'/>"></script>
    <script src="<c:url value='resources/popUp/jquery.magnific-popup.min.js'/>"></script>
    <script src="<c:url value='/resources/js/jquery.periodicalupdater.js'/>"></script>
    <script src="<c:url value='/resources/js/favico-0.3.5.min.js'/>"></script>

	<script>
	var maxLength = 20;
	var mode = "${mode}";
	var askedFlag = false;//アンケート表示用の変数
	var initFlag = true;//自動更新用の変数
	var commentNum;//自動更新用の変数

	var questionId = "${questionId}";
	var username = "<sec:authentication property="principal.username" />";
	var auth = "<sec:authentication property="authorities" />";

	// From an element with ID #popup
	$('#popupBtn').magnificPopup({
	    items: {
	        src: '#popup',
            type: 'inline'
        }
	});

	$(function() {
		$("#comment").autosize();

		//Q&Aの表示切替
		$("#jQueryPush").click(function(){
            $("#qanda").toggle();
        });

		// 質問の取得
		if (mode != "exam") {
			getQandas();
		}

		// display question's information
		$.get("question/show", { questionId : questionId }, function(data) {
			if (data.question) {
				$("#question").append(data.question);
			} else {
				window.location.href = "/JavaEP";
			}

			// テンプレート表示欄、もし管理者であれば回答を表示
			if(auth.indexOf("ADMIN") != -1){
                $("#template").text(data.source);
            } else if (data.objectOriented) {
            	$('#template').text(makeOopTemplate(questionId, data.className));
            } else {
            	$('#template').text(makeTemplate(questionId));
            }

			// コンソール画面
			if (data.saved) {
				$("#c2").append(escapeHTML(data.saved));
			} else {
				// ソースコードが保存されていない場合は、テンプレートを表示
				var template = $("#template").text();
				$("#c2").append(escapeHTML(template));
			}

			// 解答済みであれば
			if (data.passed) {
				$("#console").text("Passed.");
			}

			var stars = calcStars(data.difficulty);
			$("#difficulty").text(stars);

			$("#numOfStudent").text(data.numOfPassed);
		});

		// タイトルの設定
		$("#questionTitle").append(questionId);

		// ページ遷移設定
		if (mode != "exam") {
			setPagerPost(questionId);
		}

		// resize event setting at source code textarea
		$(window).on("mouseup", function(e) {
    		$('#li').outerHeight($('#c2').outerHeight());
    	});

		automaticUpdate();
		
		// forbid paste
		if (mode == "exam" && auth.indexOf("ADMIN") == -1) {
			$('#c2').attr('onpaste', 'return false;');
		}
	});

	// 保存
	function save(){
		var source = $('#c2').val();

		$.post("question/save", { questionId: questionId, source: source }, function(data) {
			$("#console").empty();
			$("#console").append("Saved.");
		});
	}

	// コンパイル
	function compile(){
		var classname = "Question" + questionId;
		var source = $("#c2").val();

		$.post("question/compile", { questionId: questionId, classname: classname, source: source }, function(data) {
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

		$.post("question/execute", { questionId: questionId, classname: classname, args: args }, function(data) {
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

		setTimeout(function (){
			$.post("question/mark", { questionId: questionId, classname: classname, source: source }, function(data) {
				$("#console").empty();
				$("#console").append(data.message);

				if(data.message == "Passed." && askedFlag){
					$.magnificPopup.open({
			            items: {
			                src: '#dialog',
			                type: 'inline'
			            }
			        });
				}

				enableButtons();
			});
		},3000 * Math.random());
	}


	function disableButtons() {
		$("#execute_btn").addClass("disabled");
		$("#mark_btn").addClass("disabled");
	}

	function enableButtons() {
		$("#execute_btn").removeClass("disabled");
		$("#mark_btn").removeClass("disabled");
	}

	// TAに質問
	function ask(){

		var consoleText = $("#console").text();

		if(consoleText == "" || consoleText == "Passed." || consoleText == "Saved." || consoleText == "Compiled."){
			alert("エラーコードが適切ではありません。");
		} else {
			var source = $('#c2').val();
	        var comment = $("#comment").val();

	        if(comment == ""){
	        	alert("質問内容を入力してください。");
	        } else {
	            $.post("question/setQanda", { questionId: questionId, username: username, console : consoleText, source: source, comment : comment }, function(data) {
	                alert("saved");
	                $.magnificPopup.close();
	                $("#comment").val("");//初期化
	                getQandas();
	            });
	        }
		}
    }

	//QandAの初期取得
	function getQandas(){


        $.ajax({
            type: "POST",
            url: "question/getQandaList",
            data: { questionId : questionId },
            success: function(data) {
            	if(initFlag){
            		commentNum = data.length;//更新との違い
            	}

            	createQATable(data);
            },
            error : function(data) {
                console.log(data);
            }
        });
    };

    function createQATable(data){
    	var tbody = $("#tbody");
        tbody.empty();

    	for (var i in data) {
            var qanda = data[i];

            var tr = $("<tr>");
            tbody.append(tr);

            // ID
            var td = $("<td>");
            tr.append(td);
            td.append(qanda.id);

            // 問題番号
            td = $("<td>");
            tr.append(td);
            td.append(qanda.questionId);

            // コメント
            td = $("<td>");
            tr.append(td);
            td.append(qanda.questionComment);

            // エラーコード
            td = $("<td>");
            tr.append(td);
            arrangeContents(qanda.id, td, qanda.console, maxLength);

            // 投稿日時
            td = $("<td>");
            tr.append(td);
            td.append(arrangeDate(qanda.created));

            // 回答済みFlag
            td = $("<td>");
            tr.append(td);

            var span = $("<span>");
            td.append(span);

            if (qanda.responsedFlag == 1) {
                span.attr("class", "glyphicon glyphicon-ok");
                td.append("<br>");
                td.append(arrangeDate(qanda.responsed));
            }

            // 回答コメント
            td = $("<td>");
            tr.append(td);
            td.append(qanda.responseComment);

            // 回答者
            td = $("<td>");
            tr.append(td);
            td.append(qanda.respondant);

            // 解決済みFlag
            td = $("<td>");
            tr.append(td);

            var span = $("<span>");
            td.append(span);

            if (qanda.solvedFlag == 1) {
                span.attr("class", "glyphicon glyphicon-ok");
                td.append("<br>");
                td.append(arrangeDate(qanda.solved));
            }

            // 回答リンク
            td = $("<td>");
            tr.append(td);

            var a = $("<a>");
            td.append(a);

            a.attr("href", "qanda/response?qandaId=" + qanda.id);
            a.attr("target", "ans");
            a.append("詳細");
        }
    }

    var updateCount = 1;

    function automaticUpdate(){
        //回答の自動チェック
        $.PeriodicalUpdater('question/getQandaList',{
            //  オプション設定
                method: 'POST',      // 送信リクエストURL
                minTimeout: 10000,  // 送信インターバル(ミリ秒)
                type: 'json',       // xml、json、scriptもしくはhtml (jquery.getやjquery.postのdataType)
                multiplier:1,       // リクエスト間隔の変更
                maxCalls: 0,         //　リクエスト回数（0：制限なし）
                data: { questionId : questionId }
            },
            function (data){
                //dataは上記URLから引き渡され、変更があったか自動で判別される。
                //dataに変更があった場合のみ実行

                //console.log("cNum="+commentNum);
                //console.log("dNum="+data.length);

                if(initFlag || commentNum != data.length){
                    initFlag = false;
                    commentNum = data.length;
                } else {

                    var favicon = new Favico({
                        animation:'pop'
                            });
                    favicon.badge(updateCount);
                    updateCount += 1;

                    createQATable(data);


                }


            });
    }


 // 自動回答
    function getSimilarQandas(){
        var tbody = $("#tbody4popup");
        var tbody2 = $("#tbody4dialog");
        tbody.empty();
        tbody2.empty();
        var consoleText = $("#console").text();
        $.ajax({
            type: "POST",
            url: "question/getSimilarQandas",
            data: { consoleText : consoleText },
            success: function(data) {

            	if(data.length == 0){
            		var tr = $("<tr>");
                    tbody.append(tr);

                    td = $("<td>");
                    td.attr("colspan", 2);
                    td.attr("style", "text-align : center");
                    tr.append(td);
                    var span = $("<span>");
                    td.append(span);
                    span.attr("class", "red-font");
                    span.append("No related answers.");
            	} else {
            		//質問済みＦｌａｇを真に
            		if(consoleText == "" || consoleText == "Passed." || consoleText == "Saved." || consoleText == "Compiled."){

                    } else {
                    	askedFlag = true;
                    }


                    //並び替え
                    data = data.sort(sort_by('value', true, function(a){return a.toUpperCase()}));

                    var max = 10;

                    if(data.length < 10){
                        max = data.length;
                    }

                    for(var i = 0; i < max; i++){
                        var tr = $("<tr>");
                        tbody.append(tr);

                        var td = $("<td>");
                        tr.append(td);
                        td.append(data[i].comment);

                        td = $("<td>");
                        tr.append(td);
                        td.append(data[i].value);

                        //採点後の表示ＵＩ用
                        var tr2 = $("<tr>");
                        tbody2.append(tr2);

                        var td2 = $("<td>");
                        tr2.append(td2);
                        td2.append(data[i].comment);

                        td2 = $("<td>");
                        tr2.append(td2);
                        td2.append(data[i].value);

                        td2 = $("<td>");
                        tr2.append(td2);

                        var check = $("<input type='checkbox'>");
                        td2.append(check);
                        check.attr("class", "form-control");
                        check.attr("value", data[i].id);
                        check.attr("name", "ans");
                    }
            	}


            }
        });


    }


    //アンケート結果の集計
    function getInquiry(){
        var query = "";
        $('[name="ans"]:checked').each(function(){
          query += "," + $(this).val();
        });
        query = query.substring(1);

        $.ajax({
            type: "POST",
            url: "question/setInquiry",
            data: { questionIds : query },
            success: function(data) {
            	askedFlag = false;
                alert("ご協力ありがとうございます！");
                $.magnificPopup.close();
            }
        });
    }

    //JsonArrayの並び替え
    var sort_by = function(field, reverse, primer){
        reverse = (reverse) ? -1 : 1;
        return function(a,b){
            a = a[field];
            b = b[field];
            if (typeof(primer) != 'undefined'){
                a = primer(a);
                b = primer(b);
                }
            if (a<b) return reverse * -1;
            if (a>b) return reverse * 1;
            return 0;
            }
        }
</script>

</body>
</html>