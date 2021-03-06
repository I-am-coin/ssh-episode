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

import com.ads.pojo.TEpisode;
import com.ads.pojo.TUser;
import com.ads.service.EpisodeService;
import com.ads.util.Page;
import com.ads.util.PageUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("json-default")
@Namespace("/episode")
public class EpisodeAction extends ActionSupport implements ModelDriven<TEpisode>, RequestAware, SessionAware{
	private static final long serialVersionUID = 1L;
	@Resource
	private EpisodeService episodeService;
	private Map<String, Object> requestMap;
	private Map<String, Object> sessionMap;
	
	private TEpisode episode;
	private int userId;
	private Page page;
	
	//getter and setter
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public void setPage(Page page) {
		this.page = page;
	}
	
	//实现接口方法
	@Override
	public TEpisode getModel() {
		this.episode = new TEpisode();
		return episode;
	}
	@Override
	public void setRequest(Map<String, Object> arg0) {
		this.requestMap = arg0;
	}
	@Override
	public void setSession(Map<String, Object> arg0) {
		this.sessionMap = arg0;
	}

	//action 开始
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
	 * 通过 episodeId 获取段子信息
	 * @return SUCCESS
	 */
	@Action(value="getEpisodeById",
			results={
					@Result(name=SUCCESS, location="/episode/content.html"),
					@Result(name=ERROR, location="/error.html")
			})
	public String getEpisodeById() {
		try {
			int userId = this.sessionMap.get("user") == null ? -1 : ((TUser) this.sessionMap.get("user")).getUserId();
			Integer episodeId = this.episode.getEpisodeId();
			TEpisode e = this.episodeService.getEpisodeById(episodeId);
			e.setTComments(null);
			e.setTUsers(null);
			e.setTUsers_1(null);
			
			if (userId != -1) {
				int collect = episodeService.isUserCollect(userId, episodeId) ? 1 : 0;
				int good = episodeService.isUserGood(userId, episodeId) ? 1 : 0;
				requestMap.put("collectFlag", collect);
				requestMap.put("goodFlag", good);
			}
			
			requestMap.put("episode", e);
			
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}
	
	/**
	 * 异步：点赞段子
	 */
	@Action(value="goodEpisode_ajax",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String goodEpisode_ajax() {
		episodeService.insertGoodEpisode(userId, episode.getEpisodeId());
		ActionContext.getContext().getValueStack().push(1);
		return SUCCESS;
	}
	
	/**
	 * 异步：收藏段子
	 */
	@Action(value="insertCollectEpisode_ajax",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String insertCollectEpisode_ajax() {
		episodeService.insertCollectEpisode(userId, episode.getEpisodeId());
		ActionContext.getContext().getValueStack().push(1);
		return SUCCESS;
	}
	
	/**
	 * 异步：取消收藏
	 */
	@Action(value="removeCollectEpisode_ajax",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String removeCollectEpisode_ajax() {
		episodeService.deleteCollectEpisode(userId, episode.getEpisodeId());
		ActionContext.getContext().getValueStack().push(1);
		return SUCCESS;
	}
	
	/**
	 * 异步：获取段子列表
	 */
	@Action(value="getEpisodes_ajax",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String getEpisodes_ajax() {
		List<TEpisode> episodes = episodeService
				.getEpisodes(page.getPageNum());//段子列表
		long total = episodeService.getEpisodeNum();//段子总数
		//设置分页数据
		this.page.setHasNextPage(PageUtil.hasNextPage(page.getPageNum(), 10, total));
		this.page.setTotal(total);
		this.page.setPerPageNum(10);
		
		//防止额外数据加载
		for (int i=0; i<episodes.size(); i++) {
			episodes.get(i).setTComments(null);
			episodes.get(i).setTUsers(null);
			episodes.get(i).setTUsers_1(null);
		}
		
		//把数据打包
		Map<String, Object> data = new HashMap<>();
		data.put("episodes", episodes);
		data.put("page", page);
		//把数据传送到前端
		ActionContext.getContext().getValueStack().push(data);
		return SUCCESS;
	}
	
	/**
	 * 通过 userId 获取收藏的段子列表
	 */
	@Action(value="getEpisodeByUserId_ajax",
			results={
					@Result(name=SUCCESS, type="json")
			})
	public String getEpisodeByUserId_ajax() {
		//获取段子列表
		List<TEpisode> episodes = episodeService
				.getEpisodesByUserId(userId, page.getPageNum());
		//段子总数
		long total = episodeService.getEpisodeNumByUserId(userId);
		
		//设置分页数据
		this.page.setHasNextPage(PageUtil.hasNextPage(page.getPageNum(), 10, total));
		this.page.setTotal(total);
		this.page.setPerPageNum(10);
		//防止额外数据加载
		for (int i=0; i<episodes.size(); i++) {
			episodes.get(i).setTComments(null);
			episodes.get(i).setTUsers(null);
			episodes.get(i).setTUsers_1(null);
		}
		//打包数据
		Map<String, Object> data = new HashMap<>();
		data.put("episodes", episodes);
		data.put("page", page);
		
		ActionContext.getContext().getValueStack().push(data);
		
		return SUCCESS;
	}
}
