<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
<title>質問一覧画面</title>
<link rel="apple-touch-icon" href="<c:url value='/resources/images/apple-touch-icon.png' />" >
<link rel="shortcut icon" href="<c:url value='/resources/images/favicon.ico' />">
<link href="<c:url value='/resources/bootstrap3/css/bootstrap.min.css' />" rel="stylesheet">
<link href="<c:url value='/resources/datatables/css/jquery.dataTables.css' />" rel="stylesheet">
<link href="<c:url value='/resources/css/styles.css' />" rel="stylesheet">
<link href="<c:url value='/resources/hook/hook.css' />" rel="stylesheet">
</head>

<body>

    <div class="hook" id="hook"></div>

	<c:import url="/WEB-INF/views/include/header.jsp">
		<c:param name="activePage" value="qanda" />
	</c:import>

	<div class="container" >

		<blockquote>質問一覧画面</blockquote>

		<!-- Q&A一覧 -->
        <div>
            <div style="padding: 10px;">
	            <table class="table table-hover table-hover table-bordered table-striped" id="table">
	                <thead>
		                <tr class="info">
		                    <th>ID</th>
		                    <th>問題番号</th>
		                     <sec:authorize ifAnyGranted="ROLE_ADMIN">
		                      <th>投稿者</th>
		                     </sec:authorize>
		                    <th>質問</th>
		                    <th>エラーコード</th>
		                    <th>投稿日時</th>
		                    <th>回答済フラグ</th>
		                    <th>コメント</th>
	                     	<th>回答者</th>
		                    <th>解決済フラグ</th>
		                    <th>いいね</th>
	                     	<th></th>
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
    <script src="<c:url value='/resources/js/jquery.periodicalupdater.js'/>"></script>
    <script src="<c:url value='/resources/js/favico-0.3.5.min.js'/>"></script>
    <script src="<c:url value='/resources/hook/hook.min.js'/>"></script>
    <script src="<c:url value='/resources/hook/mousewheel.js'/>"></script>
	<script>
	var maxLength = 20;

	var initFlag = true;

	var initDataLength;

	var auth = "<sec:authentication property="authorities" />";

	jQuery(function ($) {

		$('#hook').hook();

		getQandas();

	    automaticUpdate();
	});

	function automaticUpdate(){
		$.PeriodicalUpdater('qanda/list',{
	        //  オプション設定
	            method: 'POST',      // 送信リクエストURL
	            minTimeout: 1000,  // 送信インターバル(ミリ秒)
	            type: 'json',       // xml、json、scriptもしくはhtml (jquery.getやjquery.postのdataType)
	            multiplier:1,       // リクエスト間隔の変更
	            maxCalls: 0         //　リクエスト回数（0：制限なし）
	        },
	        function (data){
	            //dataは上記URLから引き渡され、変更があったか自動で判別される。
	            //dataに変更があった場合のみ実行

	            if(initFlag){
	                initFlag = false;
	            } else {
	                $('#table').dataTable().fnDestroy();

	                show(data);

	                //更新後のデータ数との差分=新しいコメント数
	                var num = data.length - initDataLength;

	                //console.log("num="+num);

	                if(num != 0){
	                	var favicon = new Favico({
	                        animation:'pop'
	                            });
	                    favicon.badge(num);
	                }
	            }


	        });
	}

	function getQandas(){


		$.ajax({
			type: "POST",
			url: "qanda/list",
			success: function(data) {
				initDataLength = data.length;
				show(data);
			},
			error : function(data) {
			    console.log(data);
			}
		});
	};

	function show(data){
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

            // 投稿者
            if(auth.indexOf("ADMIN") != -1){
                td = $("<td>");
                tr.append(td);
                td.append(qanda.username);
            }

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

            if(qanda.solvedFlag == 1){
                span.attr("class", "glyphicon glyphicon-ok");
                td.append("<br>");
                td.append(arrangeDate(qanda.solved));
            }

         // いいね
            td = $("<td>");
            tr.append(td);
            td.append(qanda.likeCount);

            // 回答リンク
            td = $("<td>");
            tr.append(td);

            var a = $("<a>");
            td.append(a);

            a.attr("href", "qanda/response?qandaId=" + qanda.id);
            a.attr("target", "ans");
            a.append("詳細");
        }

        $('#table').dataTable({
            "iDisplayLength": 50,
            "pagingType": "full_numbers",
            "order": [[ 9, "desc" ]]
        });
	}

	</script>

</body>
</html>