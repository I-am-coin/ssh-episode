package com.ads.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.ads.dao.PicCommentDao;
import com.ads.pojo.TPicComment;
import com.ads.pojo.TUser;

@Repository("picCommentDao")
public class PicCommentDaoImpl extends HibernateDaoSupport implements PicCommentDao {
	@Resource
	public void setSessionFactory0(SessionFactory sessionFactory){  
		super.setSessionFactory(sessionFactory); 
	}
	
	@Override
	public List<TPicComment> getPicCommentsByPictureId(int pageNum, int pictureId) {
		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(TPicComment.class);
		//设置查询条件
		Criterion criterion = Restrictions.eq("TPicture.pictureId", pictureId);
		criteria.add(criterion);
		criteria.addOrder(Order.desc("commentGood"));//设置点赞数降序
		criteria.setFirstResult((pageNum-1) * 10);
		criteria.setMaxResults(10);
		
		@SuppressWarnings("unchecked")
		List<TPicComment> picComments = criteria.list();
		return picComments;
	}

	@Override
	public int getGoodPicComment(int userId, int commentId) {
		Session session = this.getSessionFactory().getCurrentSession();
		String hql = "SELECT count(*) FROM TPicComment c left outer join c.TUsers u "
				+"WHERE u.userId = ? AND c.commentId = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, userId);
		query.setParameter(1, commentId);
		int count = ((Long) query.list().get(0)).intValue();
		return count;
	}

	@Override
	public long getPicCommentNum(int pictureId) {
		Session session = this.getSessionFactory().getCurrentSession();
		String hql = "SELECT count(*) FROM TPicComment WHERE TPicture.pictureId=?";
		Query query = session.createQuery(hql);
		query.setParameter(0, pictureId);
		Long count = (Long) query.list().get(0);
		if (count != null) {
			return count;
		}
		return 0;
	}

	@Override
	public void insertGoodPicComment(int commentId, int userId) {
		Session session = this.getSessionFactory().getCurrentSession();
		TUser user = session.get(TUser.class, userId);
		TPicComment comment = session.get(TPicComment.class, commentId);
		//设置关联
		user.getTPicComments().add(comment);
		comment.getTUsers().add(user);
		comment.setCommentGood(comment.getCommentGood()+1);
		//保存数据
		session.update(comment);
		session.update(user);
	}

	@Override
	public void insertPicComment(TPicComment comment) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.save(comment);
	}

	@Override
	public int getNewPicCommentId() {
		Session session = this.getSessionFactory().getCurrentSession();
		//创建 criteria 对象
		Criteria criteria = session.createCriteria(TPicComment.class);		
		criteria.addOrder(Order.desc("commentId"));
		criteria.setMaxResults(1);//只返回一条数据
		
		TPicComment comment = (TPicComment) criteria.uniqueResult();
		int commentId = 1000000000;
		if (comment != null) {
			commentId = comment.getCommentId() + 1;
		}
		
		return commentId;
	}

	@Override
	public int deletePicComment(int commentId) {
		Session session = this.getSessionFactory().getCurrentSession();
		String hql = "DELETE FROM TPicComment c WHERE c.commentId = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, commentId);
		
		return query.executeUpdate();
	}
	
}
