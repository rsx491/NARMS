/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aurotech.db.dao;

import com.aurotech.db.entities.NarmsCenter;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.aurotech.db.entities.NarmsUser;
import com.aurotech.init.AppContextListener;



/**
 *
 * @author Muazzam
 */
@Component
public class NarmsUserDAO {

    private static final Logger logger = LoggerFactory.getLogger(NarmsUserDAO.class);    
    
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

    public <T> void update(T t) {
        
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        em.merge(t);
        em.getTransaction().commit();
        if(em != null && em.isOpen()){
            em.close();
        }
    }

    public void deleteUserByID(Long userID) {
        em = AppContextListener.getEmf().createEntityManager();
        NarmsUser user = em.find(NarmsUser.class, userID);
        if( user != null) {
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
        }
    }

    public NarmsUser getUserById(String userID) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsUser> query = em.createQuery(
            "SELECT n FROM NarmsUser n WHERE n.id="+userID , NarmsUser.class);
        em.getTransaction().commit();
        NarmsUser user = null;
        try {
             user = query.getSingleResult();
        } catch(NoResultException e){
        }
        if(em != null && em.isOpen()){
            em.close();
        }
        return user;
    } 
    
    
    public List<NarmsUser> getAllUsers() {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsUser> query = em.createQuery(
            "SELECT n FROM NarmsUser n ORDER BY n.id", NarmsUser.class);
        em.getTransaction().commit();
        List<NarmsUser> users = query.getResultList();
        if(em != null && em.isOpen()){
            em.close();
        }
        
        return users;
    
    }
    
    public Long getUserCenterIDBySessionToken(String userSessionToken) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        
        TypedQuery <NarmsUser> query = em.createNamedQuery("findUserBySessionToken" , NarmsUser.class).setParameter("tokenid", userSessionToken);
        //em.getTransaction().commit();
        Long userCenterID = null;
        try {
            NarmsUser user = query.getSingleResult();
             userCenterID = user.getNarmsCenter().getId();
        } catch(NoResultException e){
            if(em!=null){
                em.close();
            }
            return null;
        }

        if(em != null && em.isOpen()){
            em.close();
        }
        
        return userCenterID;
    
    }
    
    public NarmsCenter getUserCenterBySessionToken(String userSessionToken) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        
        TypedQuery <NarmsUser> query = em.createNamedQuery("findUserBySessionToken" , NarmsUser.class).setParameter("tokenid", userSessionToken);
        //em.getTransaction().commit();
        NarmsCenter userCenter = null;
        try {
            NarmsUser user = query.getSingleResult();
            userCenter = user.getNarmsCenter();
        } catch(NoResultException e){
        }

        if(em != null && em.isOpen()){
            em.close();
        }
        
        return userCenter;
    
    }    
    
    public NarmsUser getActiveUserBySessionToken(String userSessionToken) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        
        TypedQuery <NarmsUser> query = em.createNamedQuery("findUserBySessionToken" , NarmsUser.class).setParameter("tokenid", userSessionToken);
        //em.getTransaction().commit();
        
        NarmsUser user = null;
        try {
             user = query.getSingleResult();
             
        } catch(NoResultException e){
        }

        if(em != null && em.isOpen()){
            em.close();
        }
        
        return user;
    
    }
    
    
    
    
    public NarmsUser getUser(String email, String password) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        
        logger.debug("SELECT n FROM NarmsUser n WHERE n.email='"+email+"' and n.password='"+password+"'");
        
        TypedQuery <NarmsUser> query = em.createQuery(
            "SELECT n FROM NarmsUser n WHERE n.email='"+email+"' and n.password='"+password+"'" , NarmsUser.class);
        //em.getTransaction().commit();
        NarmsUser user = null;
        try {
             user = query.getSingleResult();
        } catch(NoResultException e){
            
            logger.debug("Error occured := " + e );
            
        } 
        if(em != null && em.isOpen()){
            em.close();
        }
        
        return user;
    }  
    
    public NarmsUser getUser(String email) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery <NarmsUser> query = em.createQuery(
            "SELECT n FROM NarmsUser n WHERE n.email='"+email+"'" , NarmsUser.class);
        em.getTransaction().commit();
        NarmsUser user = null;
        try {
             user = query.getSingleResult();
        } catch(NoResultException e){
        }
        if(em != null && em.isOpen()){
            em.close();
        }
        return user;
    }



    public Boolean validateAdminToken(String userSessionToken) {
        
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        
        TypedQuery <NarmsUser> query = em.createNamedQuery("findUserBySessionToken" , NarmsUser.class).setParameter("tokenid", userSessionToken);
        em.getTransaction().commit();
        
        NarmsUser user = null;
        try {
             user = query.getSingleResult();
             
        } catch(NoResultException e){
            return false;
        }

        if(em != null && em.isOpen()){
            em.close();
        }

        return true;
    }    
    
    @PreDestroy
    public void beforeGoing() {
        logger.debug("NarmsUserDAO going good bye");
        if(em != null && em.isOpen()){
            em.close();
        }
    }
    
}
