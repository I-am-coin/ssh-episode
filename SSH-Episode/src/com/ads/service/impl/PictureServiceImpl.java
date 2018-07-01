package com.ads.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ads.dao.PictureDao;
import com.ads.dao.UserDao;
import com.ads.service.PictureService;
import com.ads.pojo.TPicture;
import com.ads.pojo.TUser;

@Transactional
@Service("pictureService")
public class PictureServiceImpl implements PictureService {
	@Resource
	private PictureDao pictureDao;
	@Resource
	private UserDao userDao;
	
	@Override
	public List<TPicture> getPictures(int pageNum) {
		return pictureDao.getPictures(pageNum);
	}

	@Override
	public TPicture getPictureById(int pictureId) {
		return pictureDao.getPictureById(pictureId);
	}

	@Override
	public void insertGoodPicture(int userId, int pictureId) {
		//获取图片
		TPicture picture = pictureDao.getPictureById(pictureId);
		//设置图片点赞数+1
		picture.setPictureGood(picture.getPictureGood()+1);
		//设置关联关系
		TUser user = userDao.getUserById(userId);
		picture.getTUsers_1().add(user);
		user.getTPictures_1().add(picture);
		//更新数据
		pictureDao.updatePicture(picture);
		userDao.updateUser(user);
	}

	@Override
	public void insertCollectPicture(int userId, int pictureId) {
		//获取图片和用户
		TPicture picture = pictureDao.getPictureById(pictureId);
		TUser user = userDao.getUserById(userId);
		//设置关联
		picture.getTUsers().add(user);
		user.getTPictures().add(picture);
		//保存数据
		pictureDao.updatePicture(picture);
		userDao.updateUser(user);
	}

	@Override
	public void deleteCollectPicture(int userId, int pictureId) {
		//获取数据
		TPicture picture = pictureDao.getPictureById(pictureId);
		TUser user = userDao.getUserById(userId);
		//删除关联
		picture.getTUsers().remove(user);
		user.getTPictures().remove(picture);
		//保存数据
		pictureDao.updatePicture(picture);
		userDao.updateUser(user);
	}

	@Override
	public void deletePicture(Date addTime) {
		pictureDao.deletePicture(addTime);
	}

	@Override
	public void insertPicture(String imgPath, String description) {
		pictureDao.insertPicture(imgPath, description);
	}


	@Override
	public long getPictureNum() {
		return pictureDao.getPictureNum();
	}

	@Override
	public List<TPicture> getPicturesByUserId(int userId, int pageNum) {
		return pictureDao.getPictureByUserId(userId, pageNum);
	}

	@Override
	public long getPictureNumByUserId(int userId) {
		return pictureDao.getPictureNumByUserId(userId);
	}

	@Override
	public boolean isUserCollect(int userId, int pictureId) {
		return pictureDao.countUserCollect(userId, pictureId) == 1L ? true : false;
	}

	@Override
	public boolean isUserGood(int userId, int pictureId) {
		return pictureDao.countUserGood(userId, pictureId) == 1L ? true : false;
	}
}



