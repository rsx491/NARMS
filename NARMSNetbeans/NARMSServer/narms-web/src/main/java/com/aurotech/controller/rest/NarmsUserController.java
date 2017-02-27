/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aurotech.controller.rest;

import com.aurotech.constants.NarmsConstants;
import com.aurotech.data.dto.RestResponseDTO;
import com.aurotech.db.dao.NarmsCenterDAO;
import com.aurotech.db.dao.NarmsUserDAO;
import com.aurotech.db.dao.NarmsResetRequestDAO;
import com.aurotech.db.dao.SessionDAO;
import com.aurotech.db.entities.NarmsCenter;
import com.aurotech.db.entities.NarmsSession;
import com.aurotech.db.entities.NarmsUser;
import com.aurotech.db.entities.NarmsResetRequest;
import com.aurotech.init.AppContextListener;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 *
 * @author Muazzam
 */
@CrossOrigin
@RestController
public class NarmsUserController {
    
    private static final Logger logger = LoggerFactory.getLogger(NarmsUserController.class);    
    
    @Autowired
    private NarmsUserDAO narmsUserDAO;
    
    @Autowired
    private SessionDAO narmsSessionDAO;

    @Autowired
    private NarmsResetRequestDAO narmsResetRequestDAO;
       
    
    @CrossOrigin(origins = "*")
    @RequestMapping("/getallnarmusers")
    public List<NarmsUser> getAllNarmsUsers() {
        // Handle a new guest (if any):
        logger.debug("getallnarmusers called");
        List<NarmsUser> names =  narmsUserDAO.getAllUsers(); 
        return names;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping("/getactiveuserid")
    public String getActiveUserID(@RequestParam("tokenID") String tokenID) {
        // Handle a new guest (if any):
        logger.debug("getActiveUserID called");
        logger.debug("getActiveUserID called token: " + tokenID);
        
        try{
        
            Long narmsUserID =  narmsSessionDAO.getClientID(tokenID);
            if( narmsUserID != null ){

                logger.debug("getActiveUserID sucess {\"userid\":\""+narmsUserID+"\"}");
                return "{\"userid\":\""+narmsUserID+"\"}";


            }
        }catch(javax.persistence.NoResultException excp){
            logger.debug("Error occured " + excp.getMessage());
            
            logger.debug("getActiveUserID not sucess");
            return  "{\"message\":\"No Record Found\"}";
        }
        
        logger.debug("getActiveUserID not sucess return null");
        return null;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping("/getactiveuserbytoken")
    public RestResponseDTO getActiveUserByToken(@RequestParam("tokenID") String tokenID) {
        // Handle a new guest (if any):
        logger.debug("getActiveUserByToken called");
        logger.debug("getActiveUserByToken token: " + tokenID);
        RestResponseDTO dto = new RestResponseDTO();
        try{
            
            NarmsUser narmsUser =  narmsUserDAO.getActiveUserBySessionToken(tokenID);
            if( narmsUser != null ){
                //logger.debug("narmsUser.getNarmsCenter().getId() " + narmsUser.getNarmsCenter().getId());
                
                dto.setData(narmsUser);
                logger.debug("getActiveUserByToken sucess");
                return dto;
            }
        }catch(javax.persistence.NoResultException excp){
            logger.debug("Error occured " + excp.getMessage());
            
            logger.debug("getActiveUserByToken not sucess");
            dto.setError("Exception occured please check logs");
            return  dto;
        }
        
        dto.setMessage("No Record Found");
        logger.debug("getActiveUserByToken not sucess return null");
        return dto;
    }    
    
    @CrossOrigin(origins = "*")
    @RequestMapping("/getUserCenterIDBySessionToken")
    public String getUserCenterIDBySessionToken(@RequestParam("tokenID") String tokenID) {
        // Handle a new guest (if any):
        logger.debug("getUserCenterIDBySessionToken called");
        logger.debug("getUserCenterIDBySessionToken called token: " + tokenID);
        
        try{
        
            Long narmsUserCenterID =  narmsUserDAO.getUserCenterIDBySessionToken(tokenID);
            if( narmsUserCenterID != null ){

                logger.debug("getUserCenterIDBySessionToken sucess {\"usercenterid\":\""+narmsUserCenterID+"\"}");
                return "{\"usercenterid\":\""+narmsUserCenterID+"\"}";


            }
        }catch(javax.persistence.NoResultException excp){
            logger.debug("Error occured " + excp.getMessage());
            
            logger.debug("getUserCenterIDBySessionToken not sucess");
            return  "{\"message\":\"No Record Found\"}";
        }
        
        logger.debug("getUserCenterIDBySessionToken not sucess return null");
        
        return  "{\"message\":\"No Record Found\"}";
    }    
    
    @CrossOrigin(origins = "*")
    @RequestMapping("/getUserCenterBySession")
    public RestResponseDTO getUserCenterBySession(@RequestParam("tokenID") String tokenID) {
        // Handle a new guest (if any):
        logger.debug("getUserCenterBySession called");
        logger.debug("getUserCenterBySession called token: " + tokenID);
        RestResponseDTO dto = new RestResponseDTO();
        try{
        
            NarmsCenter narmsCenter =  narmsUserDAO.getUserCenterBySessionToken(tokenID);
            if( narmsCenter != null ){
                dto.setData(narmsCenter);
                logger.debug("getUserCenterBySession sucess ");
                return dto;


            }
        }catch(javax.persistence.NoResultException excp){
            logger.debug("Error occured " + excp.getMessage());
            
            logger.debug("getUserCenterBySession not sucess");
            
            dto.setError("Error occured " + excp.getMessage());
            return  dto;
        }
        
        logger.debug("getUserCenterBySession not sucess return null");
        dto.setMessage("Not Found");
        return  dto;
    }    
    
    @CrossOrigin(origins = "*")
    @RequestMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password) {
        // Handle a new guest (if any):
        logger.debug("login called");
        logger.debug("login called email: " + email);
        logger.debug("login called password: " + password);
        
        try{
        
            NarmsUser narmsUser =  narmsUserDAO.getUser(email, password);
            if( narmsUser != null ){

                String uniqueID = UUID.randomUUID().toString();
                SessionDAO sessionDAO = new SessionDAO();
                String usersType = narmsUser.getUsertype();
                sessionDAO.persist(new NarmsSession(narmsUser.getId() , uniqueID));

                logger.debug("login sucess {\"token\":\""+uniqueID+"\"}");
                return "{\"token\":\""+uniqueID+"\",\"usertype\":\""+narmsUser.getUsertype()+"\"}";


            }
        }catch(javax.persistence.NoResultException excp){
            logger.debug("Error occured " + excp.getMessage());
            
            logger.debug("login not sucess");
            return  "{\"message\":\"No Record Found\"}";
        }
        
        logger.debug("login not sucess return null");
        return null;
    }   
    
    @CrossOrigin(origins = "*")
    @RequestMapping("/register")
    public String register(@RequestParam("email") String email,
            @RequestParam("password") String password, @RequestParam("firstName") String firstName, 
            @RequestParam("lastName") String lastName , @RequestParam("stateName") String stateName,
            @RequestParam("usertype") String usertype) {
        // Handle a new guest (if any):
        logger.debug("register called");
        
        NarmsConstants narmsConstants = (NarmsConstants)AppContextListener.getSpringContext().getBean("narmsConstants");
        Map usertypes = (Map)narmsConstants.getUsertype();
        
        if( !usertypes.containsKey(usertype) ){
            return "{\"error\":\"Invalid user type\"}";
        }
        
        NarmsUser narmsUser =  narmsUserDAO.getUser(email);
        if( narmsUser != null ){
            
            return "{\"error\":\"This email is already registered\"}";
        }else{
            NarmsUser user = new NarmsUser(email, password, firstName, lastName,usertype);
            
            NarmsCenterDAO centerDAO = new NarmsCenterDAO();
            
            NarmsCenter center = centerDAO.getCenterWithState(stateName);
            user.setNarmsCenter(center);
            
            narmsUserDAO.persist(user);
            
            return "{\"message\":\"Ok\"}";
        }

    }
    

    @CrossOrigin(origins = "*")
    @RequestMapping("/request_reset")
    public String request_reset(@RequestParam("email") String email ) {
        // create a request for new password
        logger.debug("request reset called with email : " + email);
        
        
        NarmsUser narmsUser =  narmsUserDAO.getUser(email);
        if( narmsUser != null ){
            String uniqueID = UUID.randomUUID().toString();
            Long userID = narmsUser.getId();
            narmsResetRequestDAO.persist(new NarmsResetRequest(email, userID, uniqueID));

            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            //Set these values to a correct smtp server or gmail credentials
            /*
            mailSender.setHost("smtp.gmail.com");
            mailSender.setPort(465);
            mailSender.setUsername("");
            mailSender.setPassword("");
            mailSender.setProtocol("smtps");
            */
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("Password reset code");
            msg.setText("A password request for your account has just been requested. To reset it, go to http://52.26.209.16:8000/#/reset_code?"+uniqueID);
            mailSender.send(msg);

           return "{\"message\":\"Ok\"}";
        }else{
             return "{\"error\":\"Invalid email to send request to\"}";
            
        }

    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/update_login")
    public String update_login(@RequestParam("new_password") String new_password, @RequestParam("reset_code") String reset_code, @RequestParam("email") String email ) {
        // create a request for new password
        logger.debug("update login called");
        
        
        NarmsResetRequest narmsRequest =  narmsResetRequestDAO.getRequest(email, reset_code);
        if( narmsRequest != null ){
            NarmsUser narmsUser =  narmsUserDAO.getUser(email);
            narmsUser.setPassword(new_password);
            narmsUserDAO.persist(narmsUser);
            

           return "{\"message\":\"Ok\"}";
        }else{
             return "{\"error\":\"Invalid email or reset code to update\"}";
            
        }

    }
}
