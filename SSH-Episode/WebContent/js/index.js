$(document).ready(function() {
	// QQ 登录处理
	$('.js-to-login').click(function() {
		var url = "";
		// 请求 QQ 登录 url
		$.get("user/qqlogin", function(data) {
			console.log("获取数据data：%o", data);
			url = data.url;

			var loginWindow = window.open(url);
			// 轮询
			var timer = setInterval(function() {
				$.ajax({
					type : "post",
					url : "user/qqLoginStatus",
					dataType : "json",
					success : function(data) {
						console.log("qqLoginStatus: %o", data);

						if (data == 2) {// 授权成功
							loginWindow.close();
							clearInterval(timer);

							$.ajax({
								type : "post",
								url : "user/getUserInfo",
								dataType : "json",
								success : function(data) {
									console.log("qqLoginStatus: %o", data);
									if (data == "success") {
										console.log("登录成功");
										window.location.reload();
									} else if (data == "error") {
										console.log("登录错误");
									} else {
										console.log("未知错误");
									}
								}
							});
						} else if (data == 1) {
							loginWindow.close();
							clearInterval(timer);
						} else if (loginWindow.closed) {
							clearInterval(timer);
						}
					}
				});
			}, 1000);// 轮询间隔1s
		});
	});

	// $('.js-to-login').click(function() {//登录弹出
	// $('.mask').fadeIn(100);
	// $('.login-dialog').slideDown(200);
	// });
	// $('.js-to-register').click(function() {//注册弹出
	// $('.login-dialog').slideUp(200);
	// $('.register-dialog').slideDown(200);
	// });
	// $('.close').click(function() {//关闭登录注册
	// $('.mask').fadeOut(100);
	// $('.login-dialog').slideUp(200);
	// $('.register-dialog').slideUp(200);
	// });
	// $(".register-ok-dialog .close").click(function() {
	// $(".mask").css("display", "none");
	// $(".register-ok-dialog").css("display", "none");
	// });
	$('.refresh').click(function() {// 刷新页面
		window.location.reload();
	});
	$(window).scroll(function() {// 设置滚动显示
		var sc = $(window).scrollTop();
		if (sc > 20) {
			$(".back-up").css("display", "block");
		} else {
			$(".back-up").css("display", "none");
		}
	});

	$(".back-up").click(function() {
		scrollTop();
	});// 点击回滚顶部
	// 用户信息js实现显示与隐藏
	$(".user-info").click(function() {
		$(".user-info-menu").slideToggle("fast");
	});
});

function scrollTop() {// 实现回滚函数
	var sc = $(window).scrollTop();
	$('body,html').animate({
		scrollTop : 0
	}, 500);
}

/**
 * 计算 text 的长度 汉字为3 ，字符为 1
 * 
 * @param text
 * @returns int 长度
 */
function textLength(text) {
	if (text == null) {
		return 0;
	} else {
		var realLength = 0;
		var len = text.length;
		var charCode = -1;
		for (var i = 0; i < len; i++) {
			charCode = text.charCodeAt(i);
			if (charCode >= 0 && charCode <= 128)
				realLength += 1;
			else
				realLength += 3;
		}
		return realLength;
	}
}

function toQQLogin(page) {
	var url = "https://graph.qq.com/oauth2.0/authorize?"
			+ "response_type=code&" + "client_id=101475887&"
			+ "redirect_uri=http://www.wlx959.cn/qqlogin.jsp&"
			+ "state=qqlogin&" + "scope=get_user_info";
	window.open(url);
}
//set css
function setActiveStyleSheet(filename) {
	var link = document.getElementsByTagName('link')[0];
    link.setAttribute('href',filename);
}
//判断访问设备
function isMobile() {
	console.log("Navi : %o", navigator);
	if((navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|wOSBrowser|BrowserNG|WebOS)/i))) {
		console.log("Mobile");
		return true;
		//document.write('<link href="shouji.css" rel="stylesheet" type="text/css" media="screen" />');
	}
	else {
		console.log("PC");
		return false;
		//document.write('<link href="pc.css" rel="stylesheet" type="text/css" media="screen" />');
	}
}
