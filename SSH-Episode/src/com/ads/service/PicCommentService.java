package com.ads.service;

import java.util.List;

import com.ads.pojo.TPicComment;


public interface PicCommentService {
	/**
	 * 通过图片编号获取相应的评论数据
	 * @param pageNum 页号
	 * @param pictureId 图片编号
	 * @return 评论信息
	 */
	public List<TPicComment> getPicCommentsByPictureId(int pageNum, int pictureId);
	
	/**
	 * 获取评论数
	 * @param pictureId
	 */
	public long getPicCommentNum(int pictureId);
	
	/**
	 * 指定 userId 用户是否点赞 commentId 评论
	 * @param userId
	 * @param commentId
	 * @return boolean
	 */
	public boolean isGoodPicComment(int userId, int commentId);
	
	/**
	 * 增加点赞评论数
	 * @param commentId 评论编号
	 * @param userId 用户编号
	 */
	public void addGoodPicComment(int commentId, int userId);

	/**
	 * 新增评论
	 * @param PicComment对象
	 * @return 新增评论id
	 */
	public int insertPicComment(String commentContent, int userId, int pictureId);
	
	/**
	 * 删除评论
	 * @param userId 用户编号
	 * @param pictureId 图片编号
	 * @return int 操作记录数
	 */
	public int deletePicComment(int commentId);
}
