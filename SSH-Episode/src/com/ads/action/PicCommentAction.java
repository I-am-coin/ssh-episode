package com.ads.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import com.ads.service.PicCommentService;
import com.ads.service.UserService;
import com.ads.pojo.TPicComment;
import com.ads.util.Page;
import com.ads.util.PageUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("json-default")
@Namespace("/picComment")
public class PicCommentAction extends ActionSupport implements ModelDriven<Page> {
	private static final long serialVersionUID = 1L;
	@Resource
	private PicCommentService picCommentService;
	@Resource
	private UserService userService;
	
	private Page page;
	private int pictureId;
	private String commentIds;
	private int userId;
	private int commentId;
	private String commentContent;
	
	//getter and setter
	public void setPictureId(int pictureId) {
		this.pictureId = pictureId;
	}
	public void setCommentIds(String commentIds) {
		this.commentIds = commentIds;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	
	//ModelDriven 
	@Override
	public Page getModel() {
		this.page = new Page();
		
		return page;
	}

	//Action 开始
	/**
	 * 过滤随意请求
	 * @return
	 */
	@Action(value="*",
			results={
					@Result(name=SUCCESS, type="redirect", location="/episode/index.html")
			})
	public String noActionFilter() {
		return SUCCESS;
	}
	
	/**
	 * 加载评论分页及相应用户信息
	 */
	@Action(value="getPicCommentsAndUsersByPictureId_ajax",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String getPicCommentsAndUsersByPictureId_ajax() {
		List<TPicComment> comments = picCommentService
				.getPicCommentsByPictureId(page.getPageNum(), pictureId);//评论s
		long total = picCommentService.getPicCommentNum(pictureId);//评论总数
		//设置page属性
		this.page.setHasNextPage(PageUtil.hasNextPage(page.getPageNum(), 10, total));
		this.page.setTotal(total);
		this.page.setPerPageNum(10);
		
		//防止加载额外数据
		for (int i=0; i<comments.size(); i++) {
			comments.get(i).setTPicture(null);
			comments.get(i).setTUsers(null);
			comments.get(i).getTUser().setTComments(null);
			comments.get(i).getTUser().setTComments_1(null);
			comments.get(i).getTUser().setTPicComments(null);
			comments.get(i).getTUser().setTPicComments_1(null);
			comments.get(i).getTUser().setTEpisodes(null);
			comments.get(i).getTUser().setTEpisodes_1(null);
			comments.get(i).getTUser().setTPictures(null);
			comments.get(i).getTUser().setTPictures_1(null);
		}
		
		//将配置好的数据打包
		Map<String, Object> data = new HashMap<>();
		data.put("comments", comments);
		data.put("page", page);
		//将数据压入栈顶
		ActionContext.getContext().getValueStack().push(data);
		return SUCCESS;
	}
	
	/**
	 * 异步：登录加载点赞评论数据
	 */
	@Action(value="getGoodPicCommentInfo_ajax",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String getGoodPicCommentInfo_ajax() {
		String[] commentsId = this.commentIds.split(",");//评论id集合
		List<Integer> good = new ArrayList<>(10);//点赞对应关系
		
		for (int i=0; i<commentsId.length; i++) {
			if (picCommentService.isGoodPicComment(userId, Integer.parseInt(commentsId[i]))) {
				good.add(1);
			}
			else {
				good.add(0);
			}
		}
		
		//保存数据
		ActionContext.getContext().getValueStack().push(good);
		return SUCCESS;
	}
	
	/**
	 * 异步：删除评论
	 */
	@Action(value="removePicComment_ajax",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String removePicComment_ajax() {
		int record = picCommentService.deletePicComment(commentId);
		if (record == 0) {
			ActionContext.getContext().getValueStack().push(0);
		}
		else {
			ActionContext.getContext().getValueStack().push(1);
		}
		return SUCCESS;
	}
	
	/**
	 * 异步：点赞评论
	 */
	@Action(value="addGoodPicComment_ajax",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String addGoodPicComment_ajax() {
		picCommentService.addGoodPicComment(commentId, userId);
		return SUCCESS;
	}
	
	/**
	 * 异步：发表评论
	 * @return
	 */
	@Action(value="addPicComment_ajax",
			results={
					@Result(name=SUCCESS, type="json")
	})
	public String addPicComment_ajax() {
		System.out.println("Action PictureId ： "+pictureId);
		int commentId = picCommentService.insertPicComment(commentContent, userId, pictureId);
		ActionContext.getContext().getValueStack().push(commentId);
		return SUCCESS;
	}
}
