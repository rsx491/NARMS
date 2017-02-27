/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aurotech.db.dao;

import java.util.List;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.aurotech.db.entities.NarmsBrand;
import com.aurotech.init.AppContextListener;



/**
 *
 * @author jjvirani
 */
@Component
public class NarmsBrandDAO {

    
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
        NarmsBrand record = em.find(NarmsBrand.class, recordID);
        if( record != null) {
            em.getTransaction().begin();
            em.remove(record);
            em.getTransaction().commit();
        }
    }

    public NarmsBrand getById(Long id) {
        em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        NarmsBrand record = em.find(NarmsBrand.class, id);
        em.getTransaction().commit();
        if(em != null && em.isOpen()){
            em.close();
        }
        return record;
    }

	public List<NarmsBrand> findAll() {
    	List<NarmsBrand> result = null;
    	
    	em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<NarmsBrand> query = em.createNamedQuery( "findAllBrands", NarmsBrand.class);
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
    
	public NarmsBrand findByBrandCode(String brandCode) {
    	NarmsBrand result = null;
    	
    	em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<NarmsBrand> query = em.createNamedQuery( "findBrandByCode", NarmsBrand.class);
        query.setParameter("brandCode", brandCode);
        em.getTransaction().commit();
        try {
        	result = query.getSingleResult();
        } catch(NoResultException e){
        }
        if(em != null && em.isOpen()){
            em.close();
        }
        return result;
    	
    }
	
	
	public NarmsBrand findByBrandName(String brandName) {
    	NarmsBrand result = null;
    	
    	em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<NarmsBrand> query = em.createNamedQuery( "findBrandByName", NarmsBrand.class);
        query.setParameter("brandName", brandName);
        em.getTransaction().commit();
        try {
        	result = query.getSingleResult();
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
