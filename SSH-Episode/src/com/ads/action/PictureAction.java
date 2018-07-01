package com.ads.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.ads.pojo.TPicture;
import com.ads.pojo.TUser;
import com.ads.service.PictureService;
import com.ads.util.Page;
import com.ads.util.PageUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("json-default")
@Namespace("/picture")
public class PictureAction extends ActionSupport implements ModelDriven<TPicture>, RequestAware, SessionAware {
	private static final long serialVersionUID = 1L;
	@Resource
	private PictureService pictureService;
	private TPicture picture;
	private Map<String, Object> requestMap;
	private Map<String, Object> sessionMap;
	private Page page;
	private int userId;
	
	//getter and setter
	public void setPage(Page page) {
		this.page = page;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	//implements
	@Override
	public TPicture getModel() {
		this.picture = new TPicture();
		return this.picture;
	}
	
	@Override
	public void setRequest(Map<String, Object> arg0) {
		this.requestMap = arg0;
	}
	
	@Override
	public void setSession(Map<String, Object> arg0) {
		this.sessionMap = arg0;
	}
	//action
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
	 * 异步：获取段子列表
	 */
	@Action(value="getPictures_ajax",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String getPictures_ajax() {
		List<TPicture> pictures = pictureService
				.getPictures(page.getPageNum());//段子列表
		long total = pictureService.getPictureNum();//段子总数
		//设置分页数据
		this.page.setHasNextPage(PageUtil.hasNextPage(page.getPageNum(), 10, total));
		this.page.setTotal(total);
		this.page.setPerPageNum(10);
		
		//防止额外数据加载
		for (int i=0; i<pictures.size(); i++) {
			pictures.get(i).setTPicComments(null);
			pictures.get(i).setTUsers(null);
			pictures.get(i).setTUsers_1(null);
		}
		
		//把数据打包
		Map<String, Object> data = new HashMap<>();
		data.put("pictures", pictures);
		data.put("page", page);
		//把数据传送到前端
		ActionContext.getContext().getValueStack().push(data);
		return SUCCESS;
	}
	
	/**
	 * 通过id获取图片
	 * @return String
	 */
	@Action(value="getPictureById",
			results={
					@Result(name=SUCCESS, location="/picture/content.html")
			})
	public String getPictureById() {
		int userId = this.sessionMap.get("user") == null ? -1 : ((TUser) this.sessionMap.get("user")).getUserId();
		int pictureId = picture.getPictureId();
		TPicture p = pictureService.getPictureById(pictureId);
		this.requestMap.put("picture", p);
		// 解决懒加载问题
		p.setTPicComments(null);
		p.setTUsers(null);
		p.setTUsers_1(null);
		
		if (userId != -1) {
			int collect = pictureService.isUserCollect(userId, pictureId) ? 1 : 0;
			int good = pictureService.isUserGood(userId, pictureId) ? 1 : 0;
			requestMap.put("collectFlag", collect);
			requestMap.put("goodFlag", good);
		}
		
		return SUCCESS;
	}
	
	/**
	 * 异步：点赞段子
	 */
	@Action(value="goodPicture_ajax",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String goodPicture_ajax() {
		pictureService.insertGoodPicture(userId, picture.getPictureId());
		ActionContext.getContext().getValueStack().push(1);
		return SUCCESS;
	}
	
	/**
	 * 异步：收藏段子
	 */
	@Action(value="insertCollectPicture_ajax",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String insertCollectPicture_ajax() {
		pictureService.insertCollectPicture(userId, picture.getPictureId());
		ActionContext.getContext().getValueStack().push(1);
		return SUCCESS;
	}
	
	/**
	 * 异步：取消收藏
	 */
	@Action(value="removeCollectPicture_ajax",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String removeCollectPicture_ajax() {
		pictureService.deleteCollectPicture(userId, picture.getPictureId());
		ActionContext.getContext().getValueStack().push(1);
		return SUCCESS;
	}
	
	/**
	 * 通过 userId 获取收藏的段子列表
	 */
	@Action(value="getPictureByUserId_ajax",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String getPictureByUserId_ajax() {
		//获取段子列表
		List<TPicture> pictures = pictureService
				.getPicturesByUserId(userId, page.getPageNum());
		//段子总数
		long total = pictureService.getPictureNumByUserId(userId);
		
		//设置分页数据
		this.page.setHasNextPage(PageUtil.hasNextPage(page.getPageNum(), 10, total));
		this.page.setTotal(total);
		this.page.setPerPageNum(10);
		//防止额外数据加载
		for (int i=0; i<pictures.size(); i++) {
			pictures.get(i).setTPicComments(null);
			pictures.get(i).setTUsers(null);
			pictures.get(i).setTUsers_1(null);
		}
		//打包数据
		Map<String, Object> data = new HashMap<>();
		data.put("pictures", pictures);
		data.put("page", page);
		
		ActionContext.getContext().getValueStack().push(data);
		
		return SUCCESS;
	}
}
