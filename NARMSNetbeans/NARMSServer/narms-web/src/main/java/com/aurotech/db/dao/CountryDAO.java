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

import com.aurotech.db.entities.Country;
import com.aurotech.init.AppContextListener;



/**
 *
 * @author jjvirani
 */
@Component
public class CountryDAO {

    
    private EntityManager em;
    
	public List<Country> findAll() {
    	List<Country> result = null;
    	
    	em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Country> query = em.createNamedQuery( "findAllCountries", Country.class);
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
    
	public Country findByCountryCode(String countryCode) {
    	Country result = null;
    	
    	em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Country> query = em.createNamedQuery( "findCountryByCode", Country.class);
        query.setParameter("countryCode", countryCode);
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
	
	
	public Country findByCountryName(String countryName) {
    	Country result = null;
    	
    	em = AppContextListener.getEmf().createEntityManager();
        em.getTransaction().begin();
        TypedQuery<Country> query = em.createNamedQuery( "findCountryByName", Country.class);
        query.setParameter("countryName", countryName);
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
