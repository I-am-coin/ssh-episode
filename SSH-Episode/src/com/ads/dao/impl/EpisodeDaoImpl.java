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

import com.ads.dao.EpisodeDao;
import com.ads.pojo.TEpisode;

@Repository("episodeDao")
public class EpisodeDaoImpl extends HibernateDaoSupport implements EpisodeDao {
	@Resource
	public void setSessionFactory0(SessionFactory sessionFactory){  
		super.setSessionFactory(sessionFactory); 
	}
	
	@Override
	public List<TEpisode> getEpisodes(int pageNum) {
		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(TEpisode.class);
		criteria.addOrder(Order.desc("episodeId"));
		criteria.setFirstResult((pageNum-1) * 10);
		criteria.setMaxResults(10);
		
		@SuppressWarnings("unchecked")
		List<TEpisode> TEpisodes = criteria.list();
		return TEpisodes;
	}
	
	@Override
	public TEpisode getEpisodeById(int episodeId) {
		Session session = this.getSessionFactory().getCurrentSession();
		return session.get(TEpisode.class, episodeId);
	}

	@Override
	public void updateEpisode(TEpisode episode) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.merge(episode);
	}

	@Override
	public void deleteEpisode(Date addTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		
		Session session = this.getSessionFactory().getCurrentSession();
		
		String hql = "DELETE FROM TEpisode e WHERE e.addDate < ?";
		
		Query query2 = session.createQuery(hql);//删除段子
		query2.setParameter(0, addTime);
		int count = query2.executeUpdate();
		
		System.out.println(sdf.format(new Date())+" : 删除段子"+count+"条");
	}

	@Override
	public void insertEpisode(String episodeContent) {
		Session session = this.getSessionFactory().getCurrentSession();
		
		TEpisode episode = new TEpisode();
		episode.setEpisodeContent(episodeContent);
		episode.setEpisodeGood(0);
		episode.setAddDate(new Date());
		
		session.merge(episode);
	}
	
	public long getEpisodeNum(){
		Session session = this.getSessionFactory().getCurrentSession();
		String hql = "SELECT count(*) FROM TEpisode";
		Query query = session.createQuery(hql);
		Long count = (Long) query.uniqueResult();
		if (count != null) {
			return count;
		}
		return 0;
	}

	@Override
	public List<TEpisode> getEpisodeByUserId(int userId, int pageNum) {
		Session session = this.getSessionFactory().getCurrentSession();
		String sql =  
                "SELECT e.episode_id as episodeId,e.episode_content as episodeContent,e.add_date as addDate,e.episode_good as episodeGood"
                    + " FROM t_episode e LEFT JOIN t_collect c ON e.episode_id=c.episode_id where c.user_id = "+userId;
		Query query = session.createSQLQuery(sql).setResultTransformer(Transformers.aliasToBean(TEpisode.class));
		query.setFirstResult((pageNum-1) * 10);
		query.setMaxResults(10);
				
		@SuppressWarnings("unchecked")
		List<TEpisode> TEpisodes = query.list();
		return TEpisodes;
	}

	@Override
	public long getEpisodeNumByUserId(int userId) {
		Session session = this.getSessionFactory().getCurrentSession();
		String sql =  
                "SELECT count(*) "  
                    + "FROM t_episode e LEFT JOIN t_collect c ON e.episode_id=c.episode_id where c.user_id = "+userId;
		Query query = session.createSQLQuery(sql);
		Long count = ((BigInteger) query.uniqueResult()).longValue();
		
		return count;
	}

	@Override
	public long countUserCollect(int userId, int episodeId) {
		Session session = this.getSessionFactory().getCurrentSession();
		String sql = "SELECT count(*) FROM t_collect c WHERE c.user_id=? AND c.episode_id=?";
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, userId);
		query.setParameter(1, episodeId);
		Long count = ((BigInteger) query.uniqueResult()).longValue();
		
		return count;
	}

	@Override
	public long countUserGood(int userId, int episodeId) {
		Session session = this.getSessionFactory().getCurrentSession();
		String sql = "SELECT count(*) FROM t_good_episode e WHERE e.user_id=? AND e.episode_id=?";
		Query query = session.createSQLQuery(sql);
		query.setParameter(0, userId);
		query.setParameter(1, episodeId);
		Long count = ((BigInteger) query.uniqueResult()).longValue();
		
		return count;
	}
	
	
}
