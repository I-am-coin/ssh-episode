package com.ads.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.ads.pojo.TUser;
import com.ads.service.UserService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;

@Namespace("/user")
@ParentPackage("json-default")
public class UserAction extends ActionSupport implements ModelDriven<TUser>, ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1L;
	@Resource
	private UserService userSerivce;
	private HttpServletRequest request;
	private HttpServletResponse response;
	
//	//截图相关属性
//	private File myFile;
////	private String myFileContentType;
//	private String myFileFileName;
//	private String destPath;
//	private double x1;
//	private double y1;
//	private double w;
//	private double h;
	
	private TUser user;
	private int type;

	//getter and setter
//	public File getMyFile() {
//		return myFile;
//	}
//	public void setMyFile(File myFile) {
//		this.myFile = myFile;
//	}
//	public String getMyFileContentType() {
//		return myFileContentType;
//	}
//	public void setMyFileContentType(String myFileContentType) {
//		this.myFileContentType = myFileContentType;
//	}
//	public String getMyFileFileName() {
//		return myFileFileName;
//	}
//	public void setMyFileFileName(String myFileFileName) {
//		this.myFileFileName = myFileFileName;
//	}
//	public String getDestPath() {
//		return destPath;
//	}
//	public void setDestPath(String destPath) {
//		this.destPath = destPath;
//	}
//	public double getX1() {
//		return x1;
//	}
//	public void setX1(double x1) {
//		this.x1 = x1;
//	}
//	public double getY1() {
//		return y1;
//	}
//	public void setY1(double y1) {
//		this.y1 = y1;
//	}
//	public double getW() {
//		return w;
//	}
//	public void setW(double w) {
//		this.w = w;
//	}
//	public double getH() {
//		return h;
//	}
//	public void setH(double h) {
//		this.h = h;
//	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	//实现接口方法
	@Override
	public TUser getModel() {
		this.user = new TUser();
		return user;
	}
	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}
	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	//action 开始
	/**
	 * 过滤随意请求
	 * @return
	 
	@Action(value="*",
			results={
					@Result(name=SUCCESS, type="redirect", location="/episode/index.html")
			})
	public String noActionFilter() {
		return SUCCESS;
	}
	
	/**
	 * 异步：登录
	 
//	@Action(value="userLogin_ajax_*",
//			results={
//					@Result(name=SUCCESS, type="json")
//			})
//	public String userLogin_ajax() {
//		int userId = this.user.getUserId();// 传递的用户id
//		String password = this.user.getUserPassword(); //  传递的用户密码
//		TUser u = this.userSerivce.getUserById(userId);// 从数据库中获取的用户信息
//
//		if (u != null
//				&& password.equals(u.getUserPassword())) {//登录成功
//			//保存 u
//			this.sessionMap.put("user", u);//保存 user 到 session 中
//			ActionContext.getContext().getValueStack().push(1);
//		}
//		else {//登录失败
//			ActionContext.getContext().getValueStack().push(0);// 在栈顶放一个 0, 标志登录失败
//		}
//		
//		return SUCCESS;
//	}
	
	/**
	 * 异步：修改个人信息
	 */
//	@Action(value="updateUserInfo_ajax",
//			results={
//					@Result(name=SUCCESS, type="json")
//			})
//	public String updateUserInfo_ajax() {
//		//获取对应 userId 的 User
//		TUser u = this.userSerivce.getUserById(this.user.getUserId());
//		
//		//修改指定信息
//		if (type == 1) {
//			u.setUserNickname(this.user.getUserNickname());
//		}
//		else if (type == 2) {
//			u.setUserGender(this.user.getUserGender());
//		}
//		else if (type == 3) {
//			u.setUserPassword(this.user.getUserPassword());
//		}
//		//更新数据库
//		this.userSerivce.updateUserInfo(u);
//		this.sessionMap.put("user", u);//保存 user 到 session 中
//		
//		return SUCCESS;
//	}
	
	/**
	 * 上传头像并截图
	 */
//	@Action(value="uploadImage",
//			results={
//					@Result(name=SUCCESS, type="redirect", location="/episode/user.html"),
//					@Result(name=ERROR, type="redirect", location="/episode/index.html")
//			})
//	public String uploadImage() {
//		//获取当前 user
//		TUser u = this.userSerivce.getUserById((int)this.requestMap.get("userId"));
//		destPath = ServletActionContext.getServletContext().getRealPath("/images");
//		
//		System.out.println("My file path: " + myFile);
//		System.out.println("My file name: " + myFileFileName);
//		System.out.println("DestPath: " + destPath);
//		File destFile = new File(destPath,myFileFileName);
//		
//		Date date = new Date(System.currentTimeMillis());  
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");  
//        String fileName = dateFormat.format(date) +"_"+ UUID.randomUUID()+myFileFileName.substring(myFileFileName.length()-4);
//        
//        File finalFile = new File(destPath,fileName);
//		try {
//			FileUtils.copyFile(myFile, destFile);
//			Boolean flag = ImageCutUtil.cutImage(destFile.getAbsolutePath(), finalFile.getAbsolutePath(), (int)x1, (int)y1, (int)w, (int)h);
//			if (flag) {
//				u.setUserImage(fileName);
//			}
//			else {
//				System.out.println("剪切未成功");
//			}
//
//			this.userSerivce.updateUserInfo(u);
//		} catch (IOException e) {
//			e.printStackTrace();
//			return ERROR;
//		}
//		
//		this.sessionMap.put("user", u);//保存 user 到 session 中
//		
//		return SUCCESS;
//	}
	
	/**
	 * 注册
	 * @return
	 */
//	@Action(value="userRegister",
//			results={
//					@Result(name=SUCCESS, type="redirect", location="/episode/index.html")
//			})
//	public String userRegister() {
//		
//		String userNickname = this.user.getUserNickname();
//		String userPassword = this.user.getUserPassword();
//		int userId = userSerivce.insertUser(userNickname, userPassword);
//		sessionMap.put("userId", userId);
//		
//		return SUCCESS;
//	}
	
	/**
	 * 异步：QQ登录-1
	 * @return
	 */
	@Action(value="qqlogin",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String qqLogin() {
	        try {
	            String url = new Oauth().getAuthorizeURL(request);
	            Map<String, String> data = new HashMap<String, String>();
	            data.put("url", url);
	            ActionContext.getContext().getValueStack().push(data);
	        } catch (QQConnectException e) {
	            e.printStackTrace();
	        }
		
		return SUCCESS;
	}
	
	
	/**
	 * QQ登录回调
	 * @return
	 */
	@Action(value="qqcallback",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String qqCallback() {
		response.setContentType("text/html; charset=utf-8");

        try {
			PrintWriter out = response.getWriter();
			AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
            String accessToken = null;
            String openID = null;
            long tokenExpireIn = 0L;

            if (accessTokenObj.getAccessToken().equals("")) {
//                我们的网站被CSRF攻击了或者用户取消了授权
//                做一些数据统计工作
            	System.out.print("没有获取到响应参数");
            	request.getSession().setAttribute("qq_login_status", 1);
            } else {
                accessToken = accessTokenObj.getAccessToken();
                tokenExpireIn = accessTokenObj.getExpireIn();

                request.getSession().setAttribute("access_token", accessToken);
                request.getSession().setAttribute("token_expirein", String.valueOf(tokenExpireIn));

                // 利用获取到的accessToken 去获取当前用的openid -------- start
                OpenID openIDObj =  new OpenID(accessToken);
                openID = openIDObj.getUserOpenID();

                out.println("登录跳转中，请等待……<br />");
                out.println("等待过久请关闭网页");
                request.getSession().setAttribute("openid", openID);
//                out.println("<a href=" + "/shuoshuoDemo.html" +  " target=\"_blank\">去看看发表说说的demo吧</a>");
                // 利用获取到的accessToken 去获取当前用户的openid --------- end
            	request.getSession().setAttribute("qq_login_status", 2);
        		ActionContext.getContext().getValueStack().push(1);
            }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (QQConnectException e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	/**
	 * 获取 QQ 登录状态
	 * @return
	 */
	@Action(value="qqLoginStatus",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String qqLoginStatus() {
		Object status = request.getSession().getAttribute("qq_login_status");
		ActionContext.getContext().getValueStack().push(status);
		if (status != null) {
			request.getSession().removeAttribute("qq_login_status");
		}
		return SUCCESS;
	}
	
	
	/**
	 * 获取用户信息
	 * @return
	 */
	@Action(value="getUserInfo",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String getUserInfo() {
		Object accessTokenObj = request.getSession().getAttribute("access_token");
		Object openIdObj = request.getSession().getAttribute("openid");
		String status = "success";
		TUser user = null;
		
		if (accessTokenObj == null || openIdObj == null) {
			status = "error";
		}
		else {
			String accessToken = accessTokenObj.toString();
			String openId = openIdObj.toString();
			
			// 根据 openid 获取 user
			user = userSerivce.getUserByOpenId(openId);
			if (user == null) {//尚未注册
				UserInfo qzoneUserInfo = new UserInfo(accessToken, openId);
	            try {
					UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
					user = new TUser();
					if (userInfoBean.getRet() == 0) {
						user.setOpenId(openId);
	                    user.setUserNickname(userInfoBean.getNickname());
	                    user.setUserGender(userInfoBean.getGender().equals("男")?1:0);
	                    user.setUserImage(userInfoBean.getAvatar().getAvatarURL100());
	                    user.setLoginTime(new Date());
	                    user.setUserPassword("default");
	                    int userId = userSerivce.inserUser(user);
	                    user.setUserId(userId);
	                    
	                } else {
	        			status = "error";
	                }
				} catch (QQConnectException e) {
        			status = "error";
					e.printStackTrace();
				}
			} else {//已注册，更新信息
				UserInfo qzoneUserInfo = new UserInfo(accessToken, openId);
	            try {
					UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
					if (userInfoBean.getRet() == 0) {
	                    user.setUserNickname(userInfoBean.getNickname());
	                    user.setUserGender(userInfoBean.getGender().equals("男")?1:0);
	                    user.setUserImage(userInfoBean.getAvatar().getAvatarURL100());
	                    user.setLoginTime(new Date());
	                    userSerivce.updateUserInfo(user);
	                } else {
	        			status = "error";
	                }
				} catch (QQConnectException e) {
        			status = "error";
					e.printStackTrace();
				}
				
			}
		}
		
		request.getSession().setAttribute("user", user);
		ActionContext.getContext().getValueStack().push(status);
		return SUCCESS;
	}
}