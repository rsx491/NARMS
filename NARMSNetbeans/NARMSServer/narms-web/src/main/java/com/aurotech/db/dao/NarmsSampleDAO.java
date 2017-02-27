package com.aurotech.db.dao;


import java.util.List;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.aurotech.db.entities.NarmsSample;
import com.aurotech.init.AppContextListener;
import java.util.Date;



/**
 *
 * @author jjvirani
 */
@Component
@Transactional
public class NarmsSampleDAO {

    private static final Logger logger = LoggerFactory.getLogger(NarmsSampleDAO.class);    
    
    private EntityManager em;
    
    public <T> T persist(T t) {
        
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        em.persist(t);
        em.getTransaction().commit();
        
        if(em != null && em.isOpen()){
            em.close();
        }
        
        return t;
    }
    
    public <T> T update(T t) {
        
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        em.merge(t);
        em.getTransaction().commit();
        if(em != null && em.isOpen()){
            em.close();
        }
        
        return t;
    }

    public void deleteSampleByID(Long sampleID) {
        em = AppContextListener.getEmf().createEntityManager();
        NarmsSample sample = em.find(NarmsSample.class, sampleID);
        if( sample != null) {
            em.getTransaction().begin();
            em.remove(sample);
            em.getTransaction().commit();
        }
    }
    
    public NarmsSample getSampleById(Long sampleId) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsSample> query = em.createQuery(
            "SELECT s FROM NarmsSample s WHERE s.id=:sampleId" , NarmsSample.class);
        query.setParameter("sampleId", sampleId);
        em.getTransaction().commit();
        NarmsSample sample = null;
        
        try {
        	sample = query.getSingleResult();
		} catch (NoResultException e) {
		}
        
        if(em != null && em.isOpen()){
            em.close();
        }
        return sample;
    }
  
  
    public Long getSampleCountByMeatNameAndCenterID( String meatNameKeyCode ,  Long centerid) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery query = em.createNamedQuery("getsamplecountbycenterandmeatname" , NarmsSample.class);
        
        logger.debug("meatNameKeyCode := " + meatNameKeyCode);
        logger.debug("centerid := " + centerid);
        
        query.setParameter("meatNameKeyCode", meatNameKeyCode);
        query.setParameter("centerid", centerid);

        em.getTransaction().commit();

        Long count = null;
        try {
        	count = (Long)query.getSingleResult();
	} catch (NoResultException e) {
        
        }
        
        logger.debug("count := " + count);
        
        if(em != null && em.isOpen()){
            em.close();
        }
        return count;
    } 
    
    public Long getSampleCountByMeatNameAndCenterID( String meatNameKeyCode ,  Long centerid , Date fromDate , Date toDate) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery query = em.createNamedQuery("getsamplecountbycenterandmeatnamebydate" , NarmsSample.class);
        
        logger.debug("meatNameKeyCode := " + meatNameKeyCode);
        logger.debug("centerid := " + centerid);
        logger.debug("fromDate := " + fromDate);
        logger.debug("toDate := " + toDate);
        
        query.setParameter("meatNameKeyCode", meatNameKeyCode);
        query.setParameter("centerid", centerid);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);

        em.getTransaction().commit();

        Long count = null;
        try {
        	count = (Long)query.getSingleResult();
	} catch (NoResultException e) {
        
        }
        
        logger.debug("count := " + count);
        
        if(em != null && em.isOpen()){
            em.close();
        }
        return count;
    }    
    
    public Long getTotalSampleCountByCenterID(Long centerid) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        
        TypedQuery query = em.createNamedQuery("getsamplecountbycenterid" , NarmsSample.class);
        
        query.setParameter("centerid", centerid);

        em.getTransaction().commit();

        Long count = null;
        try {
        	count = (Long)query.getSingleResult();
	} catch (NoResultException e) {
        
        }
        
        if(em != null && em.isOpen()){
            em.close();
        }
        return count;
    }    
    
    
    public Long getUserSampleCountByMeat(Long userID , String meatNameKey) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery query = em.createQuery(
            "SELECT COUNT(S) FROM NarmsSample S WHERE s.narmsUser.id=:userID AND S.meat=:meatNameKey" , 
                NarmsSample.class);
        query.setParameter("userID", userID);
        query.setParameter("meatNameKey", meatNameKey);
        em.getTransaction().commit();

        Long count = null;
        try {
        	count = (Long)query.getSingleResult();
	} catch (NoResultException e) {
        
        }
        
        if(em != null && em.isOpen()){
            em.close();
        }
        return count;
    }    
    
    public List<NarmsSample> getAllSamples() {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsSample> query = em.createQuery(
            "SELECT s FROM NarmsSample s ORDER BY s.id", NarmsSample.class);
        em.getTransaction().commit();
        
        List<NarmsSample> samples = query.getResultList();
        if(em != null && em.isOpen()){
            em.close();
        }
        
        return samples;
    
    }
    
    public List<NarmsSample> getAllSamplesByCenterIDAndDate(String centerid , Date fromDate , Date toDate) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsSample> query = em.createNamedQuery( "getallsamplesbycenteridanddate" , NarmsSample.class);
        query.setParameter("centerid", Long.parseLong(centerid));
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        
        em.getTransaction().commit();
        
        List<NarmsSample> samples = query.getResultList();
        
        
        
        if(em != null && em.isOpen()){
            em.close();
        }
        
        return samples;
    }
    
    public List<NarmsSample> getSamplesByStore(String storeName) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsSample> query = em.createQuery(
            "SELECT s FROM NarmsSample s WHERE s.store.storeName=:storeName" , NarmsSample.class);
        query.setParameter("storeName", storeName);
        em.getTransaction().commit();
        
        List<NarmsSample> list = query.getResultList();
                
        if(em != null && em.isOpen()){
            em.close();
        }
        return list;
    }    
    
    @PreDestroy
    public void beforeGoing() {
        logger.debug("NarmsSampleDAO going good bye");
        if(em != null && em.isOpen()){
            em.close();
        }
    }
    
}
