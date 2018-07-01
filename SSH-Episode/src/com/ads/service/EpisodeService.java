package com.ads.service;

import java.util.Date;
import java.util.List;

import com.ads.pojo.TEpisode;

public interface EpisodeService {
	/**
	 * 获取段子集合
	 * @return List<Episode> 
	 */
	List<TEpisode> getEpisodes(int pageNum);
	
	/**
	 * 获取用户收藏的段子
	 * @param pageNum
	 * @return
	 */
	List<TEpisode> getEpisodesByUserId(int userId,int pageNum);
	
	/**
	 * 通过获取段子总数
	 * @return long 
	 */
	long getEpisodeNum();
	
	/**
	 * 获取用户收藏段子的总页数
	 * @param userId
	 * @return long
	 */
	long getEpisodeNumByUserId(int userId);
	
	/**
	 * 通过 userId 获取段子
	 * @param episodeId
	 * @return TEpisode
	 */
	TEpisode getEpisodeById(int episodeId);
	
	/**
	 * 处理点赞段子操作
	 * @param userId
	 * @param episode
	 */
	void insertGoodEpisode(int userId, int episodeId);
	
	/**
	 * 添加收藏
	 * @param userId
	 * @param episodeId
	 */
	void insertCollectEpisode(int userId, int episodeId);
	
	/**
	 * 删除收藏
	 * @param userId
	 * @param episodeId
	 */
	void deleteCollectEpisode(int userId, int episodeId);
	
	/**
	 *删除段子
	 * @param add_time
	 */
	void deleteEpisode(Date addTime);
	
	/**
	 * 添加段子
	 * @param episode_content
	 */
	void insertEpisode(String episodeContent);
	
	/**
	 * 该用户是否收藏
	 * @param user_id
	 * @param episode_id
	 * @return 是true，否false
	 */
	boolean isUserCollect(int userId, int episodeId);
	
	/**
	 * 该用户是否点赞
	 * @param user_id
	 * @param episode_id
	 * @return 是true，否false
	 */
	boolean isUserGood(int userId, int episodeId);
}
