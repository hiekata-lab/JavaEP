// jQuery and Bootstrap are required

function calcStars(numOfDifficulty){
	var result = "";
	for (var i = 0; i < numOfDifficulty; i++) {
		result += "★";
	}
	return result;
}

function arrangeDate(ts) {
	var d = new Date(ts);

	var year = d.getFullYear();
	var month = d.getMonth() + 1;
	var day = d.getDate();
	var hour = (d.getHours() < 10) ? '0' + d.getHours() : d.getHours();
	var min = (d.getMinutes() < 10) ? '0' + d.getMinutes() : d.getMinutes();
	var sec = (d.getSeconds() < 10) ? '0' + d.getSeconds() : d.getSeconds();

	return year + '-' + month + '-' + day + ' ' + hour + ':' + min + ':' + sec;
}

function arrangeContents(id, td, content, maxLength) {
	if(content != null && content.length > maxLength){
	    var abbr = $("<span>");
    	td.append(abbr);
    	abbr.append(content.substr(0, maxLength) + "...");
    	abbr.attr("id", "abbr_" + id);
    	abbr.attr("style", "");

    	var full = $("<span>");
        td.append(full);
        full.append(content);
        full.attr("style", "display: none;");
        full.attr("id", "full_" + id);

        td.append("&nbsp;");

    	var a = $("<a>");
    	a.attr("href", "#");
    	a.attr("id", "toggle_" + id);
    	a.attr("onclick", "showDetail(" + id + "); return false;"); // 詳細の表示切替
    	td.append(a);
        a.append("詳細表示");
    } else {
    	td.append(content);
    }
}

function showDetail(id) {
	var abbr = $("#abbr_" + id);
    var full = $("#full_" + id);
    var toggle = $("#toggle_" + id); // リンクの表示
    if (toggle.text() == "詳細表示") {
        full.attr("style", "");
        abbr.attr("style", "display: none;");
        toggle.text("簡易表示");
    } else {
        abbr.attr("style", "");
        full.attr("style", "display: none;");
        toggle.text("詳細表示");
    }
}

// "Source Code"中に'<'と'>'で囲まれた部分があると、htmlタグと認識されるバグ対策
function escapeHTML(str) {
	return $('<div>').text(str).html();
}

function setPagerGet(id) {
	var ul = $("#pager");
	var currentId = Number(id);
	var preId = currentId - 1;

	if (preId > 0) {
		var li = $("<li>");
		ul.append(li);
		li.attr("class", "previous");

		var a = $("<a>");
		li.append(a);
		a.attr("href", "?questionId=" + preId);
		a.append("&larr; 前の設問");
	}

	var nextId = currentId + 1;

	var li = $("<li>");
	ul.append(li);
	li.attr("class", "next");

	var a = $("<a>");
	li.append(a);
	a.attr("href", "?questionId=" + nextId);
	a.append("次の設問 &rarr;");
}

function setPagerPost(id) {
	var ul = $("#pager");
	var currentId = Number(id);
	var preId = currentId - 1;

	if (preId > 0) {
		var li = $("<li>");
		ul.append(li);
		li.addClass("previous thumbnail").css({"float": "left"});
		
		var form = $('<form>');
		li.append(form);
		form.attr({
			"name": "question_" + preId, 
			"method": "post", 
			"action": "question"
		});
		form.css({"display": "inline"});
		
		var a = $('<a>');
		form.append(a);
		a.attr({
			"href": "javascript:void(0)", 
			"onclick": "document.question_" + preId + ".submit();return false;"
		});
		a.append("&larr; 前の設問");
		
		var input = $('<input>');
		form.append(input);
		input.attr({
			"type": "hidden", 
			"name": "questionId", 
			"value": preId
		});
	}

	var nextId = currentId + 1;

	var li = $("<li>");
	ul.append(li);
	li.addClass("next thumbnail").css({"float": "right"});

	var form = $('<form>');
	li.append(form);
	form.attr({
		"name": "question_" + nextId, 
		"method": "post", 
		"action": "question"
	});
	form.css({"display": "inline"});
	
	var a = $('<a>');
	form.append(a);
	a.attr({
		"href": "javascript:void(0)", 
		"onclick": "document.question_" + nextId + ".submit();return false;"
	});
	a.append("次の設問 &rarr;");
	
	var input = $('<input>');
	form.append(input);
	input.attr({
		"type": "hidden", 
		"name": "questionId", 
		"value": nextId
	});
}

function makeTemplate(id) {
	return "public class Question" + id + " {\n"
		+ "\tpublic static void main(String[] args) {\n"
		+ "\t\tSystem.out.println(\"Hello.\");\n"
		+ "\t}\n"
		+ "}";
}

function makeOopTemplate(id, className) {
	return "class " + className + " {\n"
		+ "\n"
		+ "}\n"
		+ "\n"
		+ "public class Question" + id + " {\n"
		+ "\tpublic static void main(String[] args) {\n"
		+ "\t\tSystem.out.println(\"Hello.\");\n"
		+ "\t}\n"
		+ "}";
}

// arrayをseedStrに従ってシャッフル
// seedStrが数値に変換できない場合はシャッフルしないで返却
function shuffle(array, seedStr) {
	var seed = Number(seedStr);
	var copied = [].concat(array);

	if (isNaN(seed)) {
		return array;
	}

	var ret = [];
	var keys = [];

	for (var i = 1; i <= copied.length; i++) {
		keys[i - 1] = i;
	}

	while (keys.length > 0) {
		var idx = seed % keys.length;
		ret.push(copied[idx]);
		copied.splice(idx, 1);
		keys.splice(idx, 1);
	}

	return ret;
}