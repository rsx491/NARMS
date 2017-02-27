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
import org.springframework.util.StringUtils;

import com.aurotech.db.entities.NarmsCenter;
import com.aurotech.db.entities.NarmsStore;
import com.aurotech.init.AppContextListener;



/**
 *
 * @author jjvirani
 */
@Component
@Transactional
public class NarmsStoreDAO {

    private static final Logger logger = LoggerFactory.getLogger(NarmsStoreDAO.class);    
    
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

    public void deleteByID(Long recordID) {
        em = AppContextListener.getEmf().createEntityManager();
        NarmsStore record = em.find(NarmsStore.class, recordID);
        if( record != null) {
            em.getTransaction().begin();
            em.remove(record);
            em.getTransaction().commit();
        }
    }
    
    public List<NarmsStore> getAllCenterStores(String centerID) {
    	List<NarmsStore> stores = null;
    	
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsStore> query = em.createQuery(
            "SELECT S FROM NarmsStore S WHERE S.narmsCenter.id="+centerID , NarmsStore.class);
        
        stores = query.getResultList();
        
        em.getTransaction().commit();
        
        if(em != null && em.isOpen()){
            em.close();
        }
        return stores;
    
    }    
    
    public List<NarmsStore> getAllStores() {
    	List<NarmsStore> stores = null;
    	
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsStore> query = em.createQuery(
            "SELECT s FROM NarmsStore s ORDER BY s.storeName", NarmsStore.class);
        
        stores = query.getResultList();
        
        em.getTransaction().commit();
        
        if(em != null && em.isOpen()){
            em.close();
        }
        return stores;
    
    }
    
    public List<NarmsStore> getStoresByNameContains(String nameString) {
    	// If string is empty, return all stores
    	if(!StringUtils.hasText(nameString)) {
    		 return getAllStores();
    	 }
    	
    	List<NarmsStore> stores = null;
    	
    	nameString = StringUtils.trimWhitespace(nameString);
    	 
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        
        TypedQuery <NarmsStore> query = em.createQuery(
            "SELECT s FROM NarmsStore s WHERE lower(s.storeName) like :storeName  ORDER BY s.storeName", NarmsStore.class);
        query.setParameter("storeName", "%" + nameString.toLowerCase() + "%");
        
        stores = query.getResultList();
        
        em.getTransaction().commit();
        
        
        if(em != null && em.isOpen()){
            em.close();
        }
        return stores;
    
    }
    
    public NarmsStore getStoreById(Long storeId) {
    	
    	 NarmsStore store = null;
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsStore> query = em.createQuery(
            "SELECT s FROM NarmsStore s WHERE s.id=:storeId" , NarmsStore.class);
        query.setParameter("storeId", storeId);
        em.getTransaction().commit();
        
        try {
        	store = query.getSingleResult();
		} catch (NoResultException e) {
		}
        
        if(em != null && em.isOpen()){
            em.close();
        }
        return store;
    }   
    
    public NarmsStore getStoreByName(String storeName) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsStore> query = em.createQuery(
            "SELECT s FROM NarmsStore s WHERE s.storeName=:storeName" , NarmsStore.class);
        query.setParameter("storeName", storeName);
        em.getTransaction().commit();
        
        NarmsStore store = null;
        
        try {
        	store = query.getSingleResult();
		} catch (NoResultException e) {
		}
        if(em != null && em.isOpen()){
            em.close();
        }
        return store;
    }
    
    public NarmsStore findStoreByNameAndAddressAndCenter(String storeName, String storeAddress, NarmsCenter narmsCenter) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsStore> query = em.createNamedQuery("findStoreByNameAndAddressAndCenter", NarmsStore.class);
        query.setParameter("storeName", storeName);
        query.setParameter("storeAddress", storeAddress);
        query.setParameter("narmsCenter", narmsCenter);
        em.getTransaction().commit();
        
        NarmsStore store = null;
        
        try {
        	store = query.getSingleResult();
		} catch (NoResultException e) {
		}
        if(em != null && em.isOpen()){
            em.close();
        }
        return store;
    }    
    
    @PreDestroy
    public void beforeGoing() {
        logger.debug("NarmsStoreDAO going good bye");
        if(em != null && em.isOpen()){
            em.close();
        }
    }
    
}
