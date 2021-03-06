package com.ads.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ads.dao.UserDao;
import com.ads.service.UserService;
import com.ads.pojo.TUser;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {
	@Resource
	private UserDao userDao;
	
	@Override
	public TUser getUserById(int userId) {
		return this.userDao.getUserById(userId);
	}

	@Override
	public TUser getUserByCommentId(int commentId) {
		return userDao.getUserByCommentId(commentId);
	}

	@Override
	public void updateUserInfo(TUser user) {
		userDao.updateUser(user);
	}
	
	
	@Override
	public int insertUser(String user_nickname, String user_password){
		int userId = userDao.getNewUserId();
		userDao.insertUser(userId, user_nickname, user_password);
		
		return userId;
	}

	@Override
	public void deleteUser(Date loginTime) {
		userDao.deleteUser(loginTime);
	}

	@Override
	public int inserUser(TUser user) {
		int userId = userDao.getNewUserId();
		user.setUserId(userId);
		userDao.insertUser(user);
		
		return userId;
	}

	@Override
	public TUser getUserByOpenId(String openId) {
		return userDao.getUserByOpenId(openId);
	}
}
