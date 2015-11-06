<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
<title>管理者画面</title>
<link rel="apple-touch-icon" href="<c:url value='/resources/images/apple-touch-icon.png' />" >
<link rel="shortcut icon" href="<c:url value='/resources/images/favicon.ico' />">
<link href="<c:url value='/resources/bootstrap3/css/bootstrap.min.css' />" rel="stylesheet">
</head>

<body>
	<c:import url="/WEB-INF/views/include/header.jsp">
		<c:param name="activePage" value="admin" />
	</c:import>

	<div class="container">

		<blockquote>管理者画面</blockquote>

		<div class="row">
			<div class="col-sm-6 col-md-4">
				<div class="thumbnail">
					<div class="caption">
						<h3>問題一括登録</h3>
						<div class="form-group">
							<label for="dstPath">フォルダパス</label>
							<input type="text" class="form-control" id="dstPath" value="">
						</div>
						<p><a href="#" class="btn btn-primary" onclick="generateQuestions()">登録</a></p>
					</div>
				</div>
  			</div>
  			<div class="col-sm-6 col-md-4">
				<div class="thumbnail">
					<div class="caption">
						<h3>ユーザ一括登録</h3>
						<form method="post" action="admin/generateUsers" enctype="multipart/form-data">
							<div class="form-group">
								<label for="filePath">ファイル選択</label>
								<input type="file" name="file">
							</div>
							<p><button type="submit" class="btn btn-primary">登録</button></p>
						</form>
					</div>
				</div>
  			</div>
  			<div class="col-sm-6 col-md-4">
				<div class="thumbnail">
					<div class="caption">
						<h3>ユーザ登録・更新</h3>
						<form method="post" action="admin/generateUsers" enctype="multipart/form-data">
							<div class="form-group">
								<label for="username">ユーザ名</label>
								<input type="text" class="form-control" id="username">
							</div>
							<p style="color: red;" id="searchResult"></p>
							<div class="form-group">
								<label for="password">パスワード</label>
								<input type="password" class="form-control" id="password">
							</div>
							<div class="form-group">
								<label for="authority">権限</label>
								<select class="form-control" id="authority">
									<option value="ROLE_USER">ユーザ</option>
									<option value="ROLE_ADMIN">管理者</option>
								</select>
							</div>

							<p>
								<button type="button" class="btn btn-info" onclick="search()">検索</button>
								&nbsp;
								<button type="button" class="btn btn-danger" onclick="set()">設定</button>
							</p>
						</form>
					</div>
				</div>
  			</div>
  		</div>
  		
  		<div class="row">
  			<div class="col-sm-6 col-md-4">
				<div class="thumbnail">
					<div class="caption">
						<h3>新規問題の作成</h3>
						<p><a href="question/create" class="btn btn-primary">作成</a></p>
					</div>
				</div>
  			</div>
			<div class="col-sm-6 col-md-4">
				<div class="thumbnail">
					<div class="caption">
						<h3>ユーザ一覧</h3>
						<p><a href="admin/users" class="btn btn-primary">表示</a></p>
					</div>
				</div>
  			</div>
  			<div class="col-sm-6 col-md-4">
				<div class="thumbnail">
					<div class="caption">
						<h3>テスト結果のダウンロード</h3>
						<p><a href="admin/exportExamScore" class="btn btn-primary">ダウンロード</a></p>
					</div>
				</div>
  			</div>
		</div>
	
		<div class="row">
  			<div class="col-sm-6 col-md-4">
				<div class="thumbnail">
					<div class="caption">
						<h3>自動採点</h3>
						<p>
							<button type="button" class="btn btn-info" onclick="markAll()">採点</button>
							&nbsp;
							<button type="button" class="btn btn-danger" onclick="calcExamScore()">テスト点数再計算</button>
						</p>
					</div>
				</div>
  			</div>
  		</div>
	</div>
	
	<c:import url="/WEB-INF/views/include/footer.jsp"></c:import>

	<script src="<c:url value='/resources/js/jquery.1.11.1.min.js'/>"></script>
    <script src="<c:url value='/resources/bootstrap3/js/bootstrap.min.js'/>"></script>
	<script>
	function generateQuestions() {
		var dstPath = $("#dstPath").val();

		$.ajax({
			type: "POST",
			url: "admin/generate",
			data: { dstPath : dstPath },
			success: function(data) {
				alert(data);
			}
		});
	};

	// 登録済みユーザか未登録ユーザかの検索
	function search() {
		var username = $("#username").val();

		if (!username) {
			$("#searchResult").text("ユーザ名が空です。");
			return;
		}

		$.ajax({
			type: "POST",
			url: "admin/search",
			data: { username : username },
			success: function(data) {
				if (data == "new") {
					$("#searchResult").text("未登録ユーザです。");
				} else {
					$("#searchResult").text("登録済みユーザです。");
				}
			}
		});

	};

	// 新規登録またはパスワードの再設定
	function set(){
		var username = $("#username").val();
		var password = $("#password").val();
		var authority = $("#authority").val();

		$("#searchResult").empty();

		if (!username) {
			$("#searchResult").text("ユーザ名が空です。");
			return;
		}

		if (!password) {
			$("#searchResult").text("パスワードが空です。");
			return;
		}

		$.ajax({
			type: "POST",
			url: "admin/set",
			data: { username : username, password : password, authority : authority },
			success: function(data) {
				alert(data);
			}
		});
	};
	
	// 自動採点
	function markAll(){
			$.ajax({
			type: "POST",
			url: "admin/markAll",
			success: function(data) {
				alert(data);
			}
		});
	};
	
	// テスト点数再計算
	function calcExamScore(){
			$.ajax({
			type: "POST",
			url: "admin/calcExamScore",
			success: function(data) {
				alert(data);
			}
		});
	};
	</script>

</body>
</html>