<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="com.ads.util.ManyHasOneUtil"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.ads.pojo.*"%>
<%@page import="java.util.Set"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.ads.pojo.TUser,com.ads.pojo.TEpisode"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	//----获取用户数据
	TUser user = (TUser)session.getAttribute("user");
	int flag = 0;
	if (user != null) {
		flag = 1;
	}
	//----获取段子信息
	TEpisode episode = (TEpisode)request.getAttribute("episode");
	if (episode != null) {
		session.setAttribute("episodeId", episode.getEpisodeId());//保存段子id-给登录操作
	}
	else {
		response.sendRedirect("episode/"+session.getAttribute("episodeId")+".html");
	}
	
	int goodFlag = 0;
	int collectFlag = 0;
	if (user == null) {
		user = new TUser();
	}
	else {
		goodFlag = (Integer) request.getAttribute("goodFlag");
		collectFlag = (Integer) request.getAttribute("collectFlag");
	}
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>>"></base>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>爱段社</title>
<link href="favicon.ico" rel="icon" type="image/x-icon" />
<link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
<link href="css/content.css" rel="stylesheet" type="text/css" />
<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/index.js" type="text/javascript"></script>
</head>
<body class="page-article">
	<div class="header-channel-nav header-mini">
		<div class="content">
			<a href="episode/index.html" class="logo float-left iconfont icon-logo"></a>

			<div class="float-right header-links anim" style="display:<%= flag==0?"block":"none"%>;">
				<button class="btn btn-login js-to-login">QQ登录</button>
			</div>
		</div>
	</div>
	
	<div class="main">
		<div class="left-wrapper">
			<h2 class="joke-title">段子内容</h2>
			<div class="content-bd"><%= episode.getEpisodeContent()%></div>
			
			<div class="interact">
				<a class="article-like"><%= episode.getEpisodeGood()%>赞</a>
				<span class="line"></span>
				<a class="article-report">收藏</a>
			</div>
			
			<div class="comments">
				<div class="hd-comment">
					<a href="<%= flag==1?"episode/user.html":"javascript:void(0)"%>">
						<img src="<%= user.getUserImage()==null?"images/she.png":user.getUserImage()%>" />
					</a>
					<textarea placeholder="发表你的精彩评论，还可以输入140字" maxlength="140" class="comment-input"></textarea>
					<a class="add-comment-btn">发表</a>
				</div>
				
				<h5>
					<span>最新评论</span>
					<p></p>
				</h5>
				
				<ul class="comments-list">
				</ul>
				
				<div class="no-comment">
					<img src="images/noComment.png" />
					<p>暂无评论，快来抢沙发！</p>
				</div>
				
				<div class="error-get-comments comments-tip" style="display: none;">加载失败，请点击重试</div>
				<div class="no-more-comments" style="display: inline-block;">加载中……</div>
			</div>
		</div>
		
		<div class="right-wrapper">
			<div class="advertisement">
				<p>仅<br />供<br />学<br />习<br />之<br />用<br />，<br />侵<br />删<br />！</p>
			</div>
		</div>
	</div>
	
	<div class="footer-mini-wrapper">
		<div class="footer-mini">
			<p>
				<span>蜀ICP备18010012号</span>
			</p>
			<p>
				<a href="mailto:wlx.959@qq.com">与我联系：wlx.959@qq.com</a>
			</p>
		</div>
	</div>
	
	<div class="dialog login-register-dialog login-dialog" style="display: none;">
		<div class="close iconfont icon-close"></div>
		<div class="logo iconfont icon-logo"></div>
		<form name="login-form" class="login-form" action="user/userLogin_content" method="post">
			<div class="input-box input-username-box">
				<input id="login-userId" type="text" class="input-username" name="userId" placeholder="用户名" value="" />
			</div>
			<div class="input-box input-password-box">
				<input id="login-userPassword" type="password" class="input-password" name="userPassword" placeholder="密码" value="" />
			</div>
			<div class="error-msg-inner">
				<p class="error-msg"></p>
			</div>
			<input type="hidden" value="content" name="page"/>
			<button type="button" class="btn-login" id="click-to-login" style="pointer-events: auto;">登录</button>
		</form>
	</div>

	<div class="dialog information-dialog">
		<p id="info"></p>
	</div>
	<div class="mask"></div>
	<div class="widget-tool">
	 	<div class="item back-up icon iconfont icon-back-up anim" style="display:none;"></div>
	</div>
<script type="text/javascript">
	$(document).ready(function () {
		var page = null;
		var total = 0;//评论总数
		var loading = false;
		
		//--获取评论及用户信息
		$.ajax({
			type : "post",
			url : "comment/getCommentsAndUsersByEpisodeId_ajax",
			data : {
				"episodeId" : "<%= episode.getEpisodeId()%>",
				"pageNum" : 1
			},
			dataType:"json",
			success : function(data) {
				page = data.page;
				total = page.total;//保存评论总数
				fnCallbackComment(data);
			},
			error : function(msg) {
				$("#info").html("请求失败！");
				$(".information-dialog").css("display", "block");
				$(".information-dialog").delay(2000).hide(0);
			}
		});
		//----滚动加载数据
		$(window).scroll(function(){
            var srollPos = $(window).scrollTop();    //滚动条距顶部距离(页面超出窗口的高度)
            var totalheight = parseFloat($(window).height()) + parseFloat(srollPos);
            var content = $(".comments-list");
            
            if(($(document).height()) <= totalheight) {//执行添加一页数据!!滚动过快重复加载数据
            	if (page != null && page.hasNextPage) {//有下一页
            		loading = true;
            		//获取下一页段子数据
            		$.ajax({
            			type : "post",
            			url : "comment/getCommentsAndUsersByEpisodeId_ajax",
            			data : {
            				"episodeId" : "<%= episode.getEpisodeId()%>",
            				"pageNum" : page.pageNum+1
            			},
            			dataType:"json",
            			success : function(data) {
            				page = data.page;
            				fnCallbackComment(data);
            			},
            			error : function(msg) {
            				$("#info").html("请求失败！");
            				$(".information-dialog").css("display", "block");
            				$(".information-dialog").delay(2000).hide(0);
            			}
            		}).done(function() {
            			loading = false;
            		});
            	}
            	else if (!page.hasNextPage) {//数据加载完成
            		$(".no-more-comments").html("已加载全部内容");
            	}
            }
        });
		
        
        //登录后点赞、收藏标志
        if (<%= goodFlag%> == 1) {
			$(".article-like").addClass("hasliked");
		}
		if (<%= collectFlag%> == 1) {
			$(".article-report").html("已收藏");
			$(".article-report").addClass("article-hasliked");
		}
		
		//--处理点赞收藏评论
		//----点赞段子ajax
		$(".article-like").click(function () {
			var flag = '<%= flag%>';
		
			if (flag == '0') {
				$("#info").html("请先登录！");
				$(".information-dialog").css("display", "block");
				$(".information-dialog").delay(2000).hide(0);
			}
			else if (!$(".article-like").hasClass("hasliked")) {
				$.ajax({
					type : "post",
					url : "episode/goodEpisode_ajax",
					data : {
						"userId" : "<%= user.getUserId()%>",
						"episodeId" : "<%= episode.getEpisodeId()%>"
					},
					dataType:"json"
				});
				$(".article-like").html("<%= episode.getEpisodeGood()+1%>赞");
				$(".article-like").addClass("hasliked");
				$("#info").html("点赞成功！");
				$(".information-dialog").css("display", "block");
				$(".information-dialog").delay(2000).hide(0);
			}
		})
		//----收藏、取消收藏段子
		if (<%= flag%> == 1) {
			$(".article-report").click(function () {
				if ($(this).hasClass("article-hasliked")) {
					$.ajax({
						type : "post",
						url : "episode/removeCollectEpisode_ajax",
						data : {
							"userId" : "<%= user.getUserId()%>",
							"episodeId" : "<%= episode.getEpisodeId()%>"
						},
						dataType:"json"
					});
					$(this).removeClass("article-hasliked");
					$(this).html("收藏");
					$("#info").html("已取消收藏！");
					$(".information-dialog").css("display", "block");
					$(".information-dialog").delay(2000).hide(0);
				}
				else {
					$.ajax({
						type : "post",
						url : "episode/insertCollectEpisode_ajax",
						data : {
							"userId" : "<%= user.getUserId()%>",
							"episodeId" : "<%= episode.getEpisodeId()%>"
						},
						dataType:"json"
					});
					$(this).addClass("article-hasliked");
					$(this).html("已收藏");
					$("#info").html("收藏成功！");
					$(".information-dialog").css("display", "block");
					$(".information-dialog").delay(2000).hide(0);
				}
			})
		}
		else {
			$(".article-report").click(function() {
				$("#info").html("请先登录！");
				$(".information-dialog").css("display", "block");
				$(".information-dialog").delay(2000).hide(0);
			})
		}
		//----发表评论
		if (<%= flag%> == 1) {
			$(".add-comment-btn").click(function() {
				var text = $(".comment-input").val();
				var textLen = text.replace(/[\u0391-\uFFE5]/g,"aaa").length;
				$(".comment-input").val("");//清空输入
				if (textLen == 0 || textLen > 420) {
					$("#info").html("请输入140个字以内！");
					$(".information-dialog").css("display", "block");
					$(".information-dialog").delay(2000).hide(0);
				}
				else {
					var comment = new Object();
					comment.commentContent = text;
					comment.userId = '<%= user.getUserId()%>';
					comment.episodeId = '<%= episode.getEpisodeId()%>';
					
					$.ajax({
						type : "post",
						url : "comment/addComment_ajax",
						data : {
							"commentContent" : comment.commentContent,
							"userId" : comment.userId,
							"episodeId" : comment.episodeId
						},
						dataType:"json",
						success : function(data) {
							$("#info").html("评论成功！");
							$(".information-dialog").css("display", "block");
							$(".information-dialog").delay(2000).hide(0);
							var commentId = data;
							total += 1;//新增一条评论
							if ($(".no-comment").css("display") == "block") {//没有评论
								$(".comments-list").css("display", "block");
								$(".no-comment").css("display", "none");
								$(".no-more-comments").css("display", "inline-block");
							}
							$(".comments-list").prepend('<li><img src="<%= user.getUserImage()%>" />'
									+'<div class="comment-box">'
									+'<span class="comment-nickname"><%= user.getUserNickname()%></span>'
									+'<a class="float-right comment-delete">删除</a>'
									+'<a class="float-right comment-like"><p style="display:none;">'+commentId+'</p>0赞</a>'
									+'</div><p>'+comment.commentContent+'</p></li>');
						},
						error : function() {
							$("#info").html("请求失败！");
							$(".information-dialog").css("display", "block");
							$(".information-dialog").delay(2000).hide(0);
						}
					})
				}
			})
		}
		else {
			$(".add-comment-btn").click(function(){
				$("#info").html("请先登录！");
				$(".information-dialog").css("display", "block");
				$(".information-dialog").delay(2000).hide(0);
			});
		}
		//----删除评论ajax--on()绑定click事件
		$(".comments-list").on("click", ".comment-delete", function () {
			 $.ajax({
				type : "post",
				url : "comment/removeComment_ajax",
				data : {
					"commentId" : $(this).next().find("p").html()
				},
				dataType:"json",
				success : function(data) {
					if (data == 1) {
						$("#info").html("删除成功！");
						$(".information-dialog").css("display", "block");
						$(".information-dialog").delay(2000).hide(0);
					}
					else {
						$("#info").html("请求失败！");
						$(".information-dialog").css("display", "block");
						$(".information-dialog").delay(2000).hide(0);
					}
				},
				error : function(msg) {
					$("#info").html("请求失败！"+msg);
					$(".information-dialog").css("display", "block");
					$(".information-dialog").delay(2000).hide(0);
				}
			}); 
			 $(this).parent().parent().slideUp("fast");
			 total -= 1;//减少一条评论
			 if (total == 0) {
					$(".comments-list").css("display", "none");
					$(".no-comment").css("display", "block");
					$(".no-more-comments").css("display", "none");
			}
		});
		//----点赞评论ajax--on()绑定click事件
		$(".comments-list").on("click", ".comment-like", function () {
			var flag = '<%= flag%>';

			if (flag == '0') {
				$("#info").html("请先登录！");
				$(".information-dialog").css("display", "block");
				$(".information-dialog").delay(2000).hide(0);
			}
			else if (!$(this).hasClass("hasliked")) {
				//已登录未点赞
				$.ajax({//异步保存点赞评论数据
					type : "post",
					url : "comment/addGoodComment_ajax",
					data : {
						"commentId" : $(this).find("p").html(),
						"userId" : "<%= user.getUserId()%>"
					},
					dataType:"json"
				});
				$(this).addClass("hasliked");
				var str = $(this).html();
				var p_str = str.substring(0, str.lastIndexOf('>')+1)
				var zan_str = (Number(str.substring(str.lastIndexOf('>')+1,str.length-1))+1)+"赞";
				$(this).html(p_str + zan_str);
				$("#info").html("点赞成功！");
				$(".information-dialog").css("display", "block");
				$(".information-dialog").delay(2000).hide(0);
			}
		})
	})
	
	//***************
	function fnCallbackComment(data) {//评论回调函数
		var page = data.page;
		
		if (page == null) {
			$("#info").html("请求失败！");
			$(".information-dialog").css("display", "block");
			$(".information-dialog").delay(2000).hide(0);
		}
		else if (page.total == 0) {
			$(".no-comment").css("display", "block");
			$(".no-more-comments").css("display", "none");
		}
		else {
			var comments = data.comments;
			var content = $(".comments-list");
			
			//已经登录获取点赞评论数据
			if (<%=flag%> == 1) {
				var commentIds = "";
				
				for (var i=0; i<comments.length; i++) {//保存评论id
					commentIds += comments[i].commentId+',';
				}
				
				$.ajax({//异步获取点赞段子信息
					type : "post",
					url : "comment/getGoodCommentInfo_ajax",
					dataType:"json",
					data : {
						"commentIds" : commentIds,
						"userId" : "<%= user.getUserId()%>"
					},
					success : function(data) {//不能访问comments和users
						var good = data;
						
						for (var i=0; i<comments.length; i++) {//遍历添加组件
							var hasLiked = "";
							var textDelete = "";
							var user = comments[i].TUser;
							<%-- 设置点赞信息 --%>
							if (good[i] ==1) {
								hasLiked = "hasliked";
							}
							<%-- 设置删除信息 --%>
							if (<%= user.getUserId()%> == user.userId) {
								textDelete = '<a class="float-right comment-delete">删除</a>';
							}
							
							content.append('<li><img src="images/'+user.userImage+'" />'
									+'<div class="comment-box">'
									+'<span class="comment-nickname">'+user.userNickname+'</span>'
									+textDelete
									+'<a class="float-right comment-like '+hasLiked+'"><p style="display:none;">'+comments[i].commentId+'</p>'
									+comments[i].commentGood+'赞</a>'
									+'</div><p>'+comments[i].commentContent+'</p></li>');
						}
					},
					error : function(msg) {
						$("#info").html("请求失败！");
						$(".information-dialog").css("display", "block");
						$(".information-dialog").delay(2000).hide(0);
					}
				});
			}
			else {//没有登录
				for (var i=0; i<comments.length; i++) {
					var user = comments[i].TUser;
					content.append('<li><img src="images/'+user.userImage+'" />'
							+'<div class="comment-box">'
							+'<span class="comment-nickname">'+user.userNickname+'</span>'
							+'<a class="float-right comment-like"><p style="display:none;">'+comments[i].commentId+'</p>'
							+comments[i].commentGood+'赞</a>'
							+'</div><p>'+comments[i].commentContent+'</p></li>');
				}
			}
		}
	}
	/* //登录回调函数
	function fnCallback_login(data) {
		if (data == 0) {
			$(".login-dialog .error-msg-inner").css("display", "block");
			$(".login-dialog .error-msg").html('用户名或密码错误！');
		}
		else {
			window.location.reload();
		}
	}
	//处理登录AJAX
 	$("#click-to-login").click(function() {
 		if ($("#login-userId").val() == ''
 				|| $("#login-userPassword").val() == '') {
 			$(".login-dialog .error-msg-inner").css("display", "block");
 			$(".login-dialog .error-msg").html('用户名和密码不能为空！');
 		}
 		else if ($("#login-userId").val().length != 8) {
 			$(".login-dialog .error-msg-inner").css("display", "block");
 			$(".login-dialog .error-msg").html('用户名必须为8个字符！');
 		}
 		else if ($("#login-userPassword").val().length > 16
 				|| $("#login-userPassword").val().length < 6) {
 			$(".login-dialog .error-msg-inner").css("display", "block");
 			$(".login-dialog .error-msg").html('密码必须为6-16个字符！');
 		}
 		else {
			$.ajax({
				type : "post",
				url : "user/userLogin_ajax_content",
				data : {
					"userId" : $("#login-userId").val(),
					"userPassword" : $("#login-userPassword").val()
				},
				dataType:"json",
				success : function(data) {
					fnCallback_login(data);
				},
				error : function(msg) {
					$("#info").html("请求失败！");
					$(".information-dialog").css("display", "block");
					$(".information-dialog").delay(2000).hide(0);
				}
			});
 		}
	}) */
	
	//处理退出登录
	$(".logout-btn").click(function() {
		window.location.replace("episode/index.html");
	});
	
</script>
</body>
</html>