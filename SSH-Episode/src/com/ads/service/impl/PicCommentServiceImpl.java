package com.ads.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ads.dao.PicCommentDao;
import com.ads.dao.PictureDao;
import com.ads.dao.UserDao;
import com.ads.pojo.TPicComment;
import com.ads.pojo.TPicture;
import com.ads.pojo.TUser;
import com.ads.service.PicCommentService;

@Service("picCommentService")
@Transactional
public class PicCommentServiceImpl implements PicCommentService {
	@Resource
	private PicCommentDao picCommentDao;
	@Resource
	private UserDao userDao;
	@Resource
	private PictureDao pictureDao;

	@Override
	public List<TPicComment> getPicCommentsByPictureId(int pageNum, int pictureId) {
		
		return picCommentDao.getPicCommentsByPictureId(pageNum, pictureId);
	}

	@Override
	public long getPicCommentNum(int pictureId) {
		return picCommentDao.getPicCommentNum(pictureId);
	}

	@Override
	public boolean isGoodPicComment(int userId, int commentId) {
		int count = picCommentDao.getGoodPicComment(userId, commentId);
		
		if (count != 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public void addGoodPicComment(int commentId, int userId) {
		picCommentDao.insertGoodPicComment(commentId, userId);
	}

	@Override
	public int insertPicComment(String commentContent, int userId, int pictureId) {
		TUser user = userDao.getUserById(userId);
		TPicture picture = pictureDao.getPictureById(pictureId);
		int commentId = picCommentDao.getNewPicCommentId();
		System.out.println("Picture Id : "+pictureId+" || Picture : "+picture);
		TPicComment comment = new TPicComment(picture, user, commentContent, 0);
		comment.setCommentId(commentId);
		picCommentDao.insertPicComment(comment);
		
		return commentId;
	}

	@Override
	public int deletePicComment(int commentId) {
		return picCommentDao.deletePicComment(commentId);
	}
}
