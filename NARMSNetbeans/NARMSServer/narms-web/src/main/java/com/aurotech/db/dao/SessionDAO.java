/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aurotech.db.dao;

import com.aurotech.db.entities.NarmsUser;
import com.aurotech.init.AppContextListener;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;



/**
 *
 * @author Muazzam
 */
@Component
public class SessionDAO {

    
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
    
    public Long getClientID(String tokenId) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNamedQuery( "findUserIDByToken").setParameter("tokenid", tokenId);
        em.getTransaction().commit();
        Long userID = null;
        try {
             userID = (Long)query.getSingleResult();
        } catch(NoResultException e){
            if(em!=null){
                em.close();
            }
            return null;
        }
        if(em != null && em.isOpen()){
            em.close();
        }
        return userID;
    }    
    
    public Long getClient(String tokenId) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createNamedQuery( "findUserIDByToken").setParameter("tokenid", tokenId);
        em.getTransaction().commit();
        Long userID = null;
        try {
             userID = (Long)query.getSingleResult();
        } catch(NoResultException e){
            if(em!=null){
                em.close();
            }
            return null;
        }
        if(em != null && em.isOpen()){
            em.close();
        }
        return userID;
    }    
    
    @PreDestroy
    public void beforeGoing() {
        if(em != null && em.isOpen()){
            em.close();
        }
    }
    
}
