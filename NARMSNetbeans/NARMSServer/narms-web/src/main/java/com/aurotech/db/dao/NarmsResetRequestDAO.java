/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aurotech.db.dao;

import com.aurotech.db.entities.NarmsResetRequest;
import com.aurotech.init.AppContextListener;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;



/**
 *
 * @author Muazzam
 */
@Component
public class NarmsResetRequestDAO {

    private static final Logger logger = LoggerFactory.getLogger(NarmsResetRequestDAO.class);    
    
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
    
    
    public List<NarmsResetRequest> getAllRequests() {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsResetRequest> query = em.createQuery(
            "SELECT n FROM NarmsResetRequests n ORDER BY n.id", NarmsResetRequest.class);
        em.getTransaction().commit();
        List<NarmsResetRequest> requests = query.getResultList();
        if(em != null && em.isOpen()){
            em.close();
        }
        
        return requests;
    
    }
    
    public NarmsResetRequest getRequest(String email, String code) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsResetRequest> query = em.createQuery(
            "SELECT n FROM NarmsResetRequest n WHERE n.email='"+email+"' and n.code='"+code+"'" , NarmsResetRequest.class);
        em.getTransaction().commit();
        NarmsResetRequest request = null;
        try {
             request = query.getSingleResult();
        } catch(NoResultException e){
            if(em!=null){
                em.close();
            }
            return null;
        }
        if(em != null && em.isOpen()){
            em.close();
        }
        
        return request;
    }  
       
    
    @PreDestroy
    public void beforeGoing() {
        if(em != null && em.isOpen()){
            em.close();
        }
    }
    
}
