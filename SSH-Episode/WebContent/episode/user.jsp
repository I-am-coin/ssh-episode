<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.ads.pojo.TUser, java.util.Enumeration"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	//----获取用户数据
	TUser user = (TUser)session.getAttribute("user");
	int flag = 0;
	if (user != null) {
		flag = 1;
	}
	else {
		user = new TUser();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%= basePath %>>"></base>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>爱段社</title>
<link href="favicon.ico" rel="icon" type="image/x-icon" />
<link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
<link href="css/personal_center.css" rel="stylesheet" type="text/css" />
<link href="css/main.css" rel="stylesheet" type="text/css" />
<link href="css/jquery.Jcrop.min.css" rel="stylesheet" type="text/css" />
<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/jquerysession.js" type="text/javascript"></script>
<script src="js/index.js" type="text/javascript"></script>
<script src="js/profile.js" type="text/javascript"></script>
<script src="js/jquery.Jcrop.min.js"></script>
<script src="js/script.js"></script>
<style>
</style>
</head>
<body>
	<div class="header-channel-nav header-mini">
		<div class="content">
			<a href="episode/index.html" class="logo float-left iconfont icon-logo"></a>

		</div>
	</div>
	
	<div class="main">
		<div class="left-wrapper">
			<div class="profile-wrapper">
				<img src="<%= user.getUserImage()==null?"images/she.png":user.getUserImage()%>" />
				<p id="p-nick" class="nickname"><%= user.getUserNickname()==null?"":user.getUserNickname()%></p>
				<p><%= user.getUserId()==0?"":user.getUserId()%></p>
			</div>
			<ul class="nav-list">
				<li id="li-profile" class="active">账号信息</li>
				<li id="li-collect">段子收藏</li>
				<li id="li-picture">图片收藏</li>
				<span id="span-item" class="span-item1" style="top: 0px;"></span>
				<span id="span-item" class="span-item2" style="top: 47px;display: none;"></span>
				<span id="span-item" class="span-item3" style="top: 94px;display: none;"></span>
			</ul>
			<div class="web-info">
				<span>蜀ICP备18010012号</span><br />
				<a href="mailto:wlx-959@qq.com">Email:wlx.959@qq.com</a>
			</div>
		</div>
		
		<div class="right-wrapper">
			<div id="profile" class="nav-show profile-edit-wrapper active">
				<div class="profile-account">
					<div class="account-box">
						<label class="img-label">头像</label>
						<div class="account-op">
							<img id="profile_url" src="<%= user.getUserImage()==null?"images/she.png":user.getUserImage()%>" />
							<!-- <input id="js-to-upload" type="button" value="上传头像" class="btn" style="display:block;margin:0;margin-bottom: 20px;"/>
							<p style="font-size: 12px;color: #999;line-height: 18px;">支持.jpg .jepg .png格式照片，大小不超过512K</p> -->
						</div>
					</div>
					<div class="account-box">
						<label>ID</label>
						<div class="account-op">
							<input id="user-id" type="text" readonly="readonly"
								value="<%= user.getUserId()==0?"":user.getUserId()%>" class="input user-name" />
						</div>
					</div>
					<div class="account-box">
						<label>昵称</label>
						<div class="account-op">
							<form class="change-nickname">
								<p>
									<input id="input-nickname" type="text" readonly="readonly"
										value="<%= user.getUserNickname()==null?"":user.getUserNickname()%>" class="input user-name" />
								</p>
								<p class="hint">
									<span class="icon-delete iconfont"></span>1-10个字符，支持汉字、数字、英文、“-”、“_”等
								</p>
								<p class="save-name">
									<button id="button-nickname" type="button" class="btn" style="display: none;">保存</button>
								</p>
							</form>
						</div>
					</div>
					<div class="account-box">
						<label>性别</label>
						<div class="account-op">
							<form class="change-gender">
								<p>
									<input id="input-gender" type="text" readonly="readonly"
									 value="<%= user.getUserGender()==1?"男":"女"%>"	class="input user-name"/>
								</p>
								<p class="save-name">
									<button id="button-gender" type="button" class="btn" style="display: none;">保存</button>
								</p>
							</form>
						</div>
					</div>
					<%-- <div class="account-box">
						<label>密码</label>
						<div class="account-op">
							<form class="change-password">
								<p>
									<input id="input-password" type="password" value="<%= user.getUserPassword()==null?"********":user.getUserPassword()%>" class="input" />
								</p>
								<p class="hint">
									<span class="icon-delete iconfont"></span>6-16个字符，支持数字、英文、“-”、“_”等
								</p>
								<p class="save-name">
									<button id="button-password" type="button" class="btn" style="display: none;">保存</button>
								</p>
							</form>
						</div>
					</div> --%>
				</div>
			</div>
			
			<div id="collect" class="nav-show">
				<ul id="episode-list" class="like-list" style="display:<%= flag==1?"block":"none"%>;"></ul>
				<div style="display: <%= flag==0?"block":"none"%>" class="no-like" id="no-like-episode">
					<img src="images/noCollect.png" /><p>暂无收藏，赶紧去看看吧</p>
				</div>
			</div>
			
			<div id="picture" class="nav-show">
				<ul id="picture-list" class="like-list" style="display:<%= flag==1?"block":"none"%>;"></ul>
				<div style="display: <%= flag==0?"block":"none"%>" class="no-like" id="no-like-picture">
					<img src="images/noCollect.png" /><p>暂无收藏，赶紧去看看吧</p>
				</div>
			</div>
			
		</div>
	</div>
	<!-- 上传图片box -->
	<%-- <div class="upload-box" style="display:none;">
		<div class="close iconfont icon-close"></div>
		<div class="box-header"><h2 style="color:white;">上传图片</h2></div>
		<div class="box-body">
			<form id="upload_form" enctype="multipart/form-data" method="post" action="user/uploadImage" onsubmit="return checkForm()">
				<!-- hidden crop params -->
				<input type="hidden" id="x1" name="x1" />
				<input type="hidden" id="y1" name="y1" />
				<input type="hidden" id="x2" />
				<input type="hidden" id="y2" />

				<div>
					<input type="file" id="image_file" name="myFile" onchange="fileSelectHandler()" />
				</div>

				<div class="error"></div>

				<div class="step2">
					<h2>请选择需要截图的部位,然后按上传</h2>
					<img id="preview" />

					<div class="info">
						<label>文件大小</label>
						<input type="text" id="filesize" class="file-info" />
						<label>类型</label>
						<input type="text" id="filetype" class="file-info" />
						<label>图像尺寸</label>
						<input type="text" id="filedim" class="file-info" />
						<label>宽度</label>
						<input type="text" id="w" name="w" class="file-info" />
						<label>高度</label>
						<input type="text" id="h" name="h" class="file-info" />
					</div>
					<input type="hidden" name="userId" value="<%= user.getUserId()%>"/>
					<input type="submit" class="upload-btn" value="上传" />
				</div>
			</form>
		</div>
	</div> --%>
	<!-- <div class="footer-mini-wrapper">
		<div class="footer-mini">
			<p>
				<span>蜀ICP备18010012号</span>
			</p>
			<p>
				<a href="mailto:wlx-959@qq.com">与我联系：wlx.959@qq.com</a>
			</p>
		</div>
	</div> -->
	<div class="mask"></div> 
	<div class="widget-tool">
	 	<div class="item back-up icon iconfont icon-back-up anim" style="display:none;"></div>
	</div>
<script type="text/javascript">
	var totalEpisode = 0;
	var loadingEpisode = false;
	var pageEpisode = null;
	var totalPicture = 0;
	var loadingPicture = false;
	var pagePicture = null;
	$(document).ready(function() {
		//滚动加载数据
		$(window).scroll(function(){
            var srollPos = $(window).scrollTop();    //滚动条距顶部距离(页面超出窗口的高度)
            var totalheight = parseFloat($(window).height()) + parseFloat(srollPos);
            
            if(($(document).height()) <= totalheight) {//执行添加一页数据!!滚动过快重复加载数据
            	if ($("#collect").css("display") == "block") {//段子收藏被激活
            		if (totalEpisode != 0 && pageEpisode.hasNextPage) {//有下一页
                		loadingEpisode = true;
                		//获取下一页段子数据
                		$.ajax({
                			type : "post",
                			url : "episode/getEpisodeByUserId_ajax",
                			data : {
            					"page.pageNum" : pageEpisode.pageNum+1,
            					"userId" : <%= user.getUserId()%>
                			},
                			dataType:"json",
                			success : function(data) {
                				pageEpisode = data.page;//更新 page 数据
                				fnCallback_collect(data);
                			},
                			error : function(msg) {
                				$("#info").html("请求失败！");
                				$(".information-dialog").css("display", "block");
                				$(".information-dialog").delay(2000).hide(0);
                			}
                		}).done(function() {
                			loadingEpisode = false;
                		});
                	}
            	}
            	else if ($("#picture").css("display") == "block") {//图片收藏被激活
            		if (totalPicture != 0 && pagePicture.hasNextPage) {//有下一页
                		loadingPicture = true;
                		//获取下一页段子数据
                		$.ajax({
                			type : "post",
                			url : "picture/getPictureByUserId_ajax",
                			data : {
            					"page.pageNum" : pagePicture.pageNum+1,
            					"userId" : <%= user.getUserId()%>
                			},
                			dataType:"json",
                			success : function(data) {
                				pagePicture = data.page;//更新 page 数据
                				fnCallback_picture(data);
                			},
                			error : function(msg) {
                				$("#info").html("请求失败！");
                				$(".information-dialog").css("display", "block");
                				$(".information-dialog").delay(2000).hide(0);
                			}
                		}).done(function() {
                			loadingPicture = false;
                		});
                	}
            	}
            	
            }
        });

		//取消收藏--on()绑定click()事件
		$("#episode-list").on("click", ".like-remove", function() {
			var text = $(this).parent().prev().attr("href");
			var episodeId = text.substring(8, text.lastIndexOf("."));
			$.ajax({
				type : "post",
				url : "episode/removeCollectEpisode_ajax",
				data : {
					"userId" : "<%= user.getUserId()%>",
					"episodeId" : episodeId
				},
				dataType:"json",
				success : function(data) {
    			},
    			error : function(msg) {
    				$("#info").html("请求失败！"+msg);
    				$(".information-dialog").css("display", "block");
    				$(".information-dialog").delay(2000).hide(0);
    			}
			});
			totalEpisode -= 1;//收藏减少
			$(this).parents("li").slideUp("fast");
			if (totalEpisode == 0) {
				$("#episode-list").css("display", "none");
				$("#no-like-episode").css("display","block");
			}
		});
		//取消收藏--on()绑定click()事件
		$("#picture-list").on("click", ".like-remove", function() {
			var text = $(this).parent().prev().attr("href");
			var pictureId = text.substring(8, text.lastIndexOf("."));
			$.ajax({
				type : "post",
				url : "picture/removeCollectPicture_ajax",
				data : {
					"userId" : "<%= user.getUserId()%>",
					"pictureId" : pictureId
				},
				dataType:"json",
				success : function(data) {
    			},
    			error : function(msg) {
    				$("#info").html("请求失败！"+msg);
    				$(".information-dialog").css("display", "block");
    				$(".information-dialog").delay(2000).hide(0);
    			}
			});
			totalPicture -= 1;//收藏减少
			$(this).parents("li").slideUp("fast");
			if (totalPicture == 0) {
				$("#picture-list").css("display", "none");
				$("#no-like-picture").css("display","block");
			}
		});
	});



	$("#li-collect").click(function() {
		if (pageEpisode != null) {
			return;
		}
		if (<%=flag%> == 1) {//若已经登录
			//获取段子数据
			$.ajax({
				type : "post",
				url : "episode/getEpisodeByUserId_ajax",
				data : {
					"page.pageNum" : 1,
					"userId" : <%= user.getUserId()%>
				},
				dataType:"json",
				success : function(data) {
					if (data.page.total == 0) {
						$("#episode-list").css("display", "none");
						$("#no-like-episode").css("display","block");
					}
					else {
						pageEpisode = data.page;
						totalEpisode = pageEpisode.total;
						fnCallback_collect(data);
					}
				},
				error : function(msg) {
    				$("#info").html("请求失败！");
    				$(".information-dialog").css("display", "block");
    				$(".information-dialog").delay(2000).hide(0);
				}
			});
			
		}
	});
	$("#li-picture").click(function() {
		if (pagePicture != null) {
			return;
		}
		if (<%=flag%> == 1) {//若已经登录
			//获取段子数据
			$.ajax({
				type : "post",
				url : "picture/getPictureByUserId_ajax",
				data : {
					"page.pageNum" : 1,
					"userId" : <%= user.getUserId()%>
				},
				dataType:"json",
				success : function(data) {
					if (data.page.total == 0) {
						$("#picture-list").css("display", "none");
						$("#no-like-picture").css("display","block");
					}
					else {
						pagePicture = data.page;
						totalPicture = pagePicture.total;
						fnCallback_picture(data);
					}
				},
				error : function(msg) {
    				$("#info").html("请求失败！");
    				$(".information-dialog").css("display", "block");
    				$(".information-dialog").delay(2000).hide(0);
				}
			});
			
		}
	});
	
	function fnCallback_collect(data) {
		var list = data.episodes;//段子分页数据
		var content = $("#episode-list");
		
		if (list != null) {
			for (var i=0; i<list.length; i++) {
				content.append('<li class="hasnoimg"><div class="doc-items-wrapper"><div class="doc-items">'
						+'<a href="episode/'+list[i].episodeId+'.html">'
						+'<h3>'+list[i].episodeContent.substring(0,70)+'…</h3></a>'
						+'<p><span class="like-remove">取消收藏<i class="iconfont icon-close-half"></i></span></p>'
						+'</div></div></li>');
			}
		}
		else {
			$("#info").html("请求失败！");
			$(".information-dialog").css("display", "block");
			$(".information-dialog").delay(2000).hide(0);
		}
	}
	
	function fnCallback_picture(data) {
		var list = data.pictures;//图片分页数据
		var content = $("#picture-list");
		
		if (list != null) {
			for (var i=0; i<list.length; i++) {
				content.append('<li class="hasnoimg"><div class="doc-items-wrapper"><div class="doc-items">'
						+'<a href="picture/'+list[i].pictureId+'.html">'
						+'<h3>'+list[i].pictureDesc.substring(0,70)+'…</h3></a>'
						+'<p><span class="like-remove">取消收藏<i class="iconfont icon-close-half"></i></span></p>'
						+'</div></div></li>');
			}
		}
		else {
			$("#info").html("请求失败！");
			$(".information-dialog").css("display", "block");
			$(".information-dialog").delay(2000).hide(0);
		}
	}


	/* //处理修改昵称信息AJAX
 	$("#button-nickname").click(function() {
 		if ($("#input-nickname").val() == ''
 				|| $("#input-nickname").val().length < 1
 				|| $("#input-nickname").val().length > 10) {
 			$(".change-nickname .hint").css("display","block");
 		}
 		else {
 			$(".change-nickname .hint").css("display","none");
			$.ajax({
				type : "post",
				url : "user/updateUserInfo_ajax",
				data : {
					"type" : "1",
					"userId" : $("#user-id").html(),
					"userNickname" : $("#input-nickname").val()
				},
				dataType:"json",
				success : function(data) {
					$("#p-nick").html($("#input-nickname").val());
    				$("#info").html("修改成功！");
    				$(".information-dialog").css("display", "block");
    				$(".information-dialog").delay(2000).hide(0);
				},
				error : function(msg) {
    				$("#info").html("请求失败！");
    				$(".information-dialog").css("display", "block");
    				$(".information-dialog").delay(2000).hide(0);
				}
			});
 		}
	})
	//处理修改性别信息AJAX
 	$("#button-gender").click(function() {
		$.ajax({
			type : "post",
			url : "user/updateUserInfo_ajax",
			data : {
				"type" : "2",
				"userId" : $("#user-id").html(),
				"userGender" : $("input[type=radio]:checked").val()
			},
			dataType:"json",
			success : function(data) {
				$("#info").html("修改成功！");
				$(".information-dialog").css("display", "block");
				$(".information-dialog").delay(2000).hide(0);
			},
			error : function(msg) {
				$("#info").html("请求失败！");
				$(".information-dialog").css("display", "block");
				$(".information-dialog").delay(2000).hide(0);
			}
		});
	})
	//处理修改密码信息AJAX
 	$("#button-password").click(function() {
 		if ($("#input-password").val() == ''
 				|| $("#input-password").val().length < 6
 				|| $("#input-password").val().length > 16) {
 			$(".change-password .hint").css("display","block");
 		}
 		else {
 			$(".change-password .hint").css("display","none");
			$.ajax({
				type : "post",
				url : "user/updateUserInfo_ajax",
				data : {
					"type" : "3",
					"userId" : $("#user-id").html(),
					"userPassword" : $("#input-password").val()
				},
				dataType:"json",
				success : function(data) {
    				$("#info").html("修改成功！");
    				$(".information-dialog").css("display", "block");
    				$(".information-dialog").delay(2000).hide(0);
				},
				error : function(msg) {
    				$("#info").html("请求失败！");
    				$(".information-dialog").css("display", "block");
    				$(".information-dialog").delay(2000).hide(0);
				}
			});
 		}
	}); */
</script>
</body>
</html>