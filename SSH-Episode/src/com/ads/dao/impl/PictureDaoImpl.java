package com.ads.dao.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.ads.dao.PictureDao;
import com.ads.pojo.TPicture;

@Repository("pictureDao")
public class PictureDaoImpl extends HibernateDaoSupport implements PictureDao {
	@Resource
	public void setSessionFactory0(SessionFactory sessionFactory){  
		super.setSessionFactory(sessionFactory); 
	}
	
	@Override
	public List<TPicture> getPictures(int pageNum) {
		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(TPicture.class);
		criteria.addOrder(Order.desc("pictureId"));
		criteria.setFirstResult((pageNum-1) * 10);
		criteria.setMaxResults(10);
		
		@SuppressWarnings("unchecked")
		List<TPicture> TPictures = criteria.list();
		return TPictures;
	}
	
	@Override
	public TPicture getPictureById(int pictureId) {
		Session session = this.getSessionFactory().getCurrentSession();
		return session.get(TPicture.class, pictureId);
	}

	@Override
	public void updatePicture(TPicture picture) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.merge(picture);
	}

	@Override
	public void deletePicture(Date addTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		
		Session session = this.getSessionFactory().getCurrentSession();
		
		String hql = "DELETE FROM TPicture e WHERE e.addDate < ?";
		
		Query query2 = session.createQuery(hql);//删除图片
		query2.setParameter(0, addTime);
		int count = query2.executeUpdate();
		
		System.out.println(sdf.format(new Date())+" : 删除图片"+count+"条");
	}

	@Override
	public void insertPicture(String imgPath, String pictureDesc) {
		Session session = this.getSessionFactory().getCurrentSession();
		
		TPicture picture = new TPicture();
		picture.setPictureDesc(pictureDesc);
		picture.setPictureImg(imgPath);
		picture.setPictureGood(0);
		picture.setAddDate(new Date());
		
		session.merge(picture);
	}
	
	public long getPictureNum(){
		Session session = this.getSessionFactory().getCurrentSession();
		String hql = "SELECT count(*) FROM TPicture";
		Query query = session.createQuery(hql);
		Long count = (Long) query.uniqueResult();
		if (count != null) {
			return count;
		}
		return 0;
	}

	@Override
	public List<TPicture> getPictureByUserId(int userId, int pageNum) {
		
		Session session = this.getSessionFactory().getCurrentSession();
		String sql =  
                "SELECT e.picture_id as pictureId,e.picture_img as pictureImg,e.picture_desc as pictureDesc,e.add_date as addDate,e.picture_good as pictureGood "  
                    + "FROM t_picture e LEFT JOIN t_pic_collect c ON e.picture_id=c.picture_id where c.user_id = "+userId;
		Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(TPicture.class));
		query.setFirstResult((pageNum-1) * 10);
		query.setMaxResults(10);
				
		@SuppressWarnings("unchecked")
		List<TPicture> TPictures = query.list();
		return TPictures;
	}

	@Override
	public long getPictureNumByUserId(int userId) {
		
		Session session = this.getSessionFactory().getCurrentSession();
		String sql =  
                "SELECT count(*) "  
                    + "FROM t_picture e LEFT JOIN t_pic_collect c ON e.picture_id=c.picture_id where c.user_id = "+userId;
		Query query = session.createSQLQuery(sql);
		Long count = ((BigInteger) query.uniqueResult()).longValue();
		
		return count;
	}

	@Override
	public long countUserCollect(int userId, int pictureId) {
		Session session = this.getSessionFactory().getCurrentSession();
		String sql = "SELECT count(*) FROM t_pic_collect c WHERE c.user_id=? AND c.picture_id=?";
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, userId);
		query.setParameter(1, pictureId);
		Long count = ((BigInteger) query.uniqueResult()).longValue();
		
		return count;
	}

	@Override
	public long countUserGood(int userId, int pictureId) {
		Session session = this.getSessionFactory().getCurrentSession();
		String sql = "SELECT count(*) FROM t_good_picture p WHERE p.user_id=? AND p.picture_id=?";
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, userId);
		query.setParameter(1, pictureId);
		Long count = ((BigInteger) query.uniqueResult()).longValue();
		
		return count;
	}
}
