var tt = document.getElementById("c2");
tt.onkeydown = function(event){
	 var tab = String.fromCharCode(9);
	    var e = window.event || evt;
	    var t = e.target ? e.target : e.srcElement ? e.srcElement : e.which;
	    var scrollTop = t.scrollTop;
	    var k = e.keyCode ? e.keyCode : e.charCode ? e.charCode : e.which;
	    if (k == 9 && !e.ctrlKey && !e.altKey) {
	        if(t.setSelectionRange){
	            e.preventDefault();
	            var ss = t.selectionStart;
	            var se = t.selectionEnd;
	            // Multi line selection
	            if (ss != se && t.value.slice(ss,se).indexOf("\n") != -1) {
	                if(ss>0){
	                    ss = t.value.slice(0,ss).lastIndexOf("\n")+1;
	                }
	                var pre = t.value.slice(0,ss);
	                var sel = t.value.slice(ss,se);
	                var post = t.value.slice(se,t.value.length);
	                if(e.shiftKey){
	                    var a = sel.split("\n");
                 for (var i=0;i<a.length;i++){
                     if(a[i].slice(0,1)==tab||a[i].slice(0,1)==' ' ){
						a[i]=a[i].slice(1,a[i].length);
						}
	                    }
	                    sel = a.join("\n");
	                    t.value = pre.concat(sel,post);
	                    t.selectionStart = ss;
	                    t.selectionEnd = pre.length + sel.length;
	                }
	                else{
	                    sel = sel.replace(/\n/g,"\n"+tab);
	                    pre = pre.concat(tab);
	                    t.value = pre.concat(sel,post);
	                    t.selectionStart = ss;
	                    t.selectionEnd = se + (tab.length * sel.split("\n").length);
	                }
	            }
	            // Single line selection
	            else {
	                if(e.shiftKey){
	                    var brt = t.value.slice(0,ss);
	                    var ch = brt.slice(brt.length-1,brt.length);
	                    if(ch == tab||ch== ' '){
	                        t.value = brt.slice(0,brt.length-1).concat(t.value.slice(ss,t.value.length));
	                        t.selectionStart = ss-1;
	                        t.selectionEnd = se-1;
	                    }
	                }
	                else{
	                    t.value = t.value.slice(0,ss).concat(tab).concat(t.value.slice(ss,t.value.length));
	                    if (ss == se) {
	                        t.selectionStart = t.selectionEnd = ss + tab.length;
	                    }
	                    else {
	                        t.selectionStart = ss + tab.length;
	                        t.selectionEnd = se + tab.length;
	                    }
	                }
	            }
	        }
	        else{
	            e.returnValue=false;
	            var r = document.selection.createRange();
	            var br = document.body.createTextRange();
	            br.moveToElementText(t);
	            br.setEndPoint("EndToStart", r);
	            //Single line selection
	            if (r.text.length==0||r.text.indexOf("\n") == -1) {
	                if(e.shiftKey){
	                    var ch = br.text.slice(br.text.length-1,br.text.length);
	                    if(ch==tab||ch==' '){
	                        br.text = br.text.slice(0,br.text.length-1);
	                        r.setEndPoint("StartToEnd", br);
	                    }
	                }
	                else{
	                    var rtn = t.value.slice(br.text.length,br.text.length+1);
	                    if(rtn!=r.text.slice(0,1)){
	                        br.text = br.text.concat(rtn);
	                    }
	                    br.text = br.text.concat(tab);
	                }
	                var nr = document.body.createTextRange();
	                nr.setEndPoint("StartToEnd", br);
	                nr.setEndPoint("EndToEnd", r);
	                nr.select();
	            }
	            //Multi line selection
	            else{
	                if(e.shiftKey){
	                    var a = r.text.split("\r\n");
	                    var rt = t.value.slice(br.text.length,br.text.length+2);
	                    if(rt==r.text.slice(0,2)){
	                        var p = br.text.lastIndexOf("\r\n".concat(tab));
	                        if(p!=-1){
	                            br.text = br.text.slice(0,p+2).concat(br.text.slice(p+3,br.text.length));
	                        }
	                    }
	                    for (var i=0;i<a.length;i++){
	                        var ch = a[i].length>0&&a[i].slice(0,1);
	                        if(ch==tab||ch==' '){
	                            a[i]=a[i].slice(1,a[i].length);
	                        }
	                    }
	                    r.text = a.join("\r\n");
	                }
	                else{
	                    if(br.text.length>0){
	                        var rt = t.value.slice(br.text.length,br.text.length+2);
	                        if(rt!=r.text.slice(0,2)){
	                            r.text = tab.concat(r.text.split("\r\n").join("\r\n".concat(tab)));
	                        }
	                        else{
	                            var p = br.text.slice(0,ss).lastIndexOf("\r\n")+2;
	                            br.text = br.text.slice(0,p).concat(tab,br.text.slice(p,br.text.length));
	                            r.text = r.text.split("\r\n").join("\r\n".concat(tab));
	                        }
	                    }
	                    else{
	                        r.text = tab.concat(r.text).split("\r\n").join("\r\n".concat(tab));
	                    }
	                }
	                var nr = document.body.createTextRange();
	                nr.setEndPoint("StartToEnd", br);
	                nr.setEndPoint("EndToEnd", r);
	                nr.select();
	            }
	        }
	    }
	    t.scrollTop = scrollTop;
};

var num="";

function keyUp(){
	var obj=G("c2");
	var str=obj.value;
	str=str.replace(/\r/gi,"");
	str=str.split("\n");
	n=str.length;
	line(n);
}

function line(n){
	var lineobj=G("li");
	for(var i=1;i<=n;i++){
		if(document.all){
			num+=i+"\r\n";
		}else{
			num+=i+"\n";
		}
	}
	lineobj.value=num;
	num="";
}

function autoScroll(){
	var nV = 0;
	if(!document.all){
		nV=G("c2").scrollTop;
		G("li").scrollTop=nV;
		setTimeout("autoScroll()",20);
	}
}

if(!document.all){
	window.addEventListener("load",autoScroll,false);
}



function G(objid){
	return document.getElementById(objid);
};



