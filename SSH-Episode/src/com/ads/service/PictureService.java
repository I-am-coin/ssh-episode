package com.ads.service;

import java.util.Date;
import java.util.List;

import com.ads.pojo.TPicture;

public interface PictureService {
	/**
	 * 获取图片集合
	 * @return List<Picture> 
	 */
	public List<TPicture> getPictures(int pageNum);
	
	/**
	 * 获取用户收藏的图片
	 * @param pageNum
	 * @return
	 */
	public List<TPicture> getPicturesByUserId(int userId,int pageNum);
	
	/**
	 * 获取图片总数
	 * @return long 
	 */
	public long getPictureNum();
	
	/**
	 * 获取用户收藏图片的总页数
	 * @param userId
	 * @return long
	 */
	public long getPictureNumByUserId(int userId);
	
	/**
	 * 通过 userId 获取图片
	 * @param pictureId
	 * @return TPicture
	 */
	public TPicture getPictureById(int pictureId);
	
	/**
	 * 处理点赞图片操作
	 * @param userId
	 * @param picture
	 */
	public void insertGoodPicture(int userId, int pictureId);
	
	/**
	 * 添加收藏
	 * @param userId
	 * @param pictureId
	 */
	public void insertCollectPicture(int userId, int pictureId);
	
	/**
	 * 删除收藏
	 * @param userId
	 * @param pictureId
	 */
	public void deleteCollectPicture(int userId, int pictureId);
	
	/**
	 *删除图片
	 * @param add_time
	 */
	public void deletePicture(Date addTime);
	
	/**
	 * 新增图片内容
	 * @param imgPath 图片路径
	 * @param description 图片描述
	 */
	public void insertPicture(String imgPath, String description);
	
	/**
	 * 该用户是否收藏
	 * @param userId
	 * @param pictureId
	 * @return 是true，否false
	 */
	boolean isUserCollect(int userId, int pictureId);
	
	/**
	 * 该用户是否点赞
	 * @param userId
	 * @param pictureId
	 * @return 是true，否false
	 */
	boolean isUserGood(int userId, int pictureId);
}

	
