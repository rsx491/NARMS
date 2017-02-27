/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aurotech.init;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Muazzam
 */
public class AppContextListener implements ServletContextListener {

    private static ApplicationContext context;

    
    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);
    
    private static EntityManagerFactory emf;

    public static EntityManagerFactory getEmf() {
        return emf;
    }
    
    public static ApplicationContext getSpringContext() {
        return context;
    }    
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("AppContextListener contextInitialized");
        emf = Persistence.createEntityManagerFactory("MYSQL_PERSISTENCE_UNIT");
        sce.getServletContext().setAttribute("emf", emf);
        
       context = new ClassPathXmlApplicationContext("Spring-Module.xml");
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.debug("AppContextListener contextDestroyed");
        emf = (EntityManagerFactory)sce.getServletContext().getAttribute("emf");
        emf.close();
        
        context = null;
        
    }
    
    
    
}
