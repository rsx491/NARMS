/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aurotech.db.dao;

import com.aurotech.controller.rest.NarmsSampleController;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.aurotech.db.entities.NarmsCenter;
import com.aurotech.init.AppContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author Muazzam
 */
@Component
public class NarmsCenterDAO {

    private static final Logger logger = LoggerFactory.getLogger(NarmsCenterDAO.class);    
    
    private EntityManager em;
    
    public <T> void persist(T t) {
        
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        em.persist(t);
        em.getTransaction().commit();
        
        if(em != null && em.isOpen()){
            em.close();
        }
    }
    public <T> T update(T t) {
        
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        t = em.merge(t);
        em.getTransaction().commit();
        
        if(em != null && em.isOpen()){
            em.close();
        }
        
        return t;
    }

    public void deleteByID(Long recordID) {
        em = AppContextListener.getEmf().createEntityManager();
        NarmsCenter record = em.find(NarmsCenter.class, recordID);
        if( record != null) {
            em.getTransaction().begin();
            em.remove(record);
            em.getTransaction().commit();
        }
    }
    
    public NarmsCenter getCenterById(Long id) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        NarmsCenter narmsCenter = em.find(NarmsCenter.class, id);
        em.getTransaction().commit();
        if(em != null && em.isOpen()){
            em.close();
        }
        return narmsCenter;
    }
    
    public NarmsCenter getCenterWithState(String narmsState) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNamedQuery( "findCenterByState").setParameter("centerState", narmsState);
        em.getTransaction().commit();
        NarmsCenter narmsCenter = null;
        try {
             narmsCenter = (NarmsCenter)query.getSingleResult();
        } catch(NoResultException e){
        }
        if(em != null && em.isOpen()){
            em.close();
        }
        return narmsCenter;
    }
    
    public NarmsCenter getCenterByID(String centerid) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNamedQuery( "getcenterbymonthandid");
        //query.setParameter("monthVal", monthVal  );
        query.setParameter("center_id", Long.parseLong(centerid) );
        em.getTransaction().commit();
        NarmsCenter narmsCenter = null;
        try {
             narmsCenter = (NarmsCenter)query.getSingleResult();
            /*
            List<NarmsCenter> result = query.getResultList();
            
            for(NarmsCenter center : result){
                logger.debug("centercound := " + center.getCenterName());
            }*/
            
        } catch(NoResultException e){
        }
        if(em != null && em.isOpen()){
            em.close();
        }
        return narmsCenter;
    }
    
    public List<NarmsCenter> getAllCenters() {
    	List<NarmsCenter> result = null;
    	
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<NarmsCenter> query = em.createNamedQuery( "findAllCenters", NarmsCenter.class);
        em.getTransaction().commit();
        try {
        	result = query.getResultList();
        } catch(NoResultException e){
        }
        if(em != null && em.isOpen()){
            em.close();
        }
        return result;
    }  
    
    @PreDestroy
    public void beforeGoing() {
        if(em != null && em.isOpen()){
            em.close();
        }
    }
    
}
