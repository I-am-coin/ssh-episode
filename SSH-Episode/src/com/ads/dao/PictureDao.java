package com.ads.dao;

import java.util.Date;
import java.util.List;

import com.ads.pojo.TPicture;

public interface PictureDao {
	/**
	 * 获取指定 pageNum 的图片列表
	 * @return Set<TPicture>
	 */
	public List<TPicture> getPictures(int pageNum);
	
	/**
	 * 获取用户收藏图片
	 * @param pageNum
	 * @return
	 */
	public List<TPicture> getPictureByUserId(int userId,int pageNum);
	
	/**
	 * 通过 pictureId 获取图片信息
	 * @param pictureId
	 * @return TPicture
	 */
	public TPicture getPictureById(int pictureId);
	
	/**
	 * 通过 picture 更新图片信息
	 * @param picture
	 */
	public void updatePicture(TPicture picture);
	
	/**
	 * 按时间删除图片
	 * @param addTime
	 */
	public void deletePicture(Date addTime);
	
	/**
	 * 新增图片
	 * @param pictureContent
	 */
	public void insertPicture(String imgPath, String pictureDesc);
	
	/**
	 * 获取图片总数
	 */
	public long getPictureNum();
	
	/**
	 * 获取用户收藏图片总页数
	 * @param userId
	 * @return
	 */
	public long getPictureNumByUserId(int userId);
	
	/**
	 * 查询该用户是否收藏
	 * @param userId
	 * @param pictureId
	 * @return 收藏1/未收藏0
	 */
	long countUserCollect(int userId, int pictureId);
	
	/**
	 * 查询该用户是否点赞
	 * @param userId
	 * @param pictureId
	 * @return 点赞1/未点赞0
	 */
	long countUserGood(int userId, int pictureId);

}
