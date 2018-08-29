<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<base href="<%= basePath %>>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>爱段社</title>
<link href="#" rel="stylesheet" type="text/css"/>
<link href="favicon.ico" rel="icon" type="image/x-icon" />
<link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/index.js" type="text/javascript"></script>
<script type="text/javascript">
	if (isMobile()) {
		setActiveStyleSheet("css/index-style-mobile.css");
	}
	else {
		setActiveStyleSheet("css/index-style.css");
	}
</script>
</head>
<body class="page-channel" style="background-color:white;">
	<!-- <div class="header-top"></div> -->
	<div class="header-search">
		<div class="content">
			<a href="episode/index.html" class="logo iconfont icon-logo"></a>
			<div class="channel" >
				 <a class="a-hover active" href="episode/index.html">段子</a>
				 <!-- <a class="a-hover" href="picture/index.html">图片</a> -->
				 <a class="a-hover" href="episode/index.html">图片</a>
				 <a class="a-hover" href="episode/index.html">视频</a>
			</div>
		</div>
	</div>
	
	<div class="main index-main">
		<div style="width:100%;height:800px;">
			<a href="episode/index.html">
				<img style="margin:auto;padding:0;max-height:800px;" src="images/404.png" alt="404" />
			</a>
		</div>
	</div>
	
	<div class="footer-mini-wrapper">
		<div class="footer-mini">
			<p>
				<span>蜀ICP备18010012号</span>
			</p>
			<p>
				<a href="mailto:wlx-959@qq.com">与我联系：wlx.959@qq.com</a>
			</p>
		</div>
	</div>
</body>
</html>