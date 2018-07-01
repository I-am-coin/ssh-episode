package com.ads.dao;

import java.util.List;

import com.ads.pojo.TPicComment;

public interface PicCommentDao {
	
	/**
	 * 通过 pictureId 获取评论集
	 * @param pageNum
	 * @param pictureId
	 * @return List<TPicComment>
	 */
	public List<TPicComment> getPicCommentsByPictureId(int pageNum, int pictureId);
	
	/**
	 * 获取指定 userId 和 commentId 的点赞评论信息
	 * @param userId
	 * @param commentId
	 * @return int 0/1
	 */
	public int getGoodPicComment(int userId, int commentId);
	
	/**
	 * 获取评论数
	 * @param pictureId
	 */
	public long getPicCommentNum(int pictureId);
	
	/**
	 * 点赞评论
	 * @param commentId
	 * @param userId
	 */
	public void insertGoodPicComment(int commentId, int userId);
	
	/**
	 * 新增评论
	 * @param comment
	 */
	public void insertPicComment(TPicComment comment);
	
	/**
	 * 获取最新的评论id
	 * @return
	 */
	public int getNewPicCommentId();
	
	/**
	 * 删除评论
	 * @param commentId
	 * @return int 操作记录数
	 */
	public int deletePicComment(int commentId);
}
