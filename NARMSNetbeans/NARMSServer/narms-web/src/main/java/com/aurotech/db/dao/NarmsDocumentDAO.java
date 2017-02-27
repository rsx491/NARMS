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

import com.aurotech.db.entities.NarmsSampleImage;
import com.aurotech.init.AppContextListener;



/**
 *
 * @author jjvirani
 */
@Component
@Transactional
public class NarmsDocumentDAO {

    private static final Logger logger = LoggerFactory.getLogger(NarmsDocumentDAO.class);    
    
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
    
    public <T> void delete(T t) {
        
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        em.remove(t);
        em.getTransaction().commit();
        
        if(em != null && em.isOpen()){
            em.close();
        }
        
    }
    
    
    public List<NarmsSampleImage> getAllDocuments() {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsSampleImage> query = em.createQuery("SELECT d FROM NarmsDocument d ORDER BY d.id", NarmsSampleImage.class);
        em.getTransaction().commit();
        
        List<NarmsSampleImage> samples = query.getResultList();
        if(em != null && em.isOpen()){
            em.close();
        }
        
        return samples;
    
    }
    
    public NarmsSampleImage getDocumentById(Long id) {
    	NarmsSampleImage document = null;
    	
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsSampleImage> query = em.createQuery("SELECT d FROM NarmsDocument d WHERE d.id=:id" , NarmsSampleImage.class);
        query.setParameter("id", id);
        em.getTransaction().commit();
        
        try {
        	document = query.getSingleResult();
		} catch (NoResultException e) {
		}
                
        if(em != null && em.isOpen()){
            em.close();
        }
        return document;
    }    
    
    @PreDestroy
    public void beforeGoing() {
        logger.debug("NarmsDocumentDAO going good bye");
        if(em != null && em.isOpen()){
            em.close();
        }
    }
    
}
