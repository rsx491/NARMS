package com.aurotech.controller.rest;

import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.PathParam;
import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.aurotech.utils.DateUtils;
import java.util.Date;

import com.aurotech.data.dto.RestResponseDTO;
import com.aurotech.db.dao.NarmsBrandDAO;

import com.aurotech.db.dao.NarmsUserDAO;
import com.aurotech.db.dao.NarmsCenterDAO;
import com.aurotech.db.dao.NarmsStoreDAO;
import com.aurotech.data.dto.SampleDTO;
import com.aurotech.db.dao.NarmsSampleDAO;

import com.aurotech.db.entities.CenterMeatTypeLimit;
import com.aurotech.db.entities.NarmsCenter;
import com.aurotech.db.entities.NarmsSample;
import com.aurotech.db.entities.NarmsSampleImage;
import com.aurotech.db.entities.NarmsStore;
import com.aurotech.db.entities.NarmsUser;
import com.aurotech.db.entities.NarmsBrand;

import com.aurotech.constants.NarmsConstants;
import com.aurotech.constants.SampleConstants;


/**
 * Provide basic CRUD functions for major object types
 * 
 */
@CrossOrigin
@RestController
@Transactional
public class NarmsAdminController {
    
    private static final Logger logger = LoggerFactory.getLogger(NarmsBrandController.class);    
    
    @Autowired
    private NarmsUserDAO narmsUserDAO;


    @Autowired
    private NarmsBrandDAO narmBrandDAO;

    @Autowired
    private NarmsStoreDAO narmsStoreDAO;
    
    @Autowired
    private NarmsCenterDAO narmsCenterDAO;

     @Autowired
    private NarmsSampleDAO narmsSampleDAO;

    /** Centers **/
    @CrossOrigin(origins = "*")
    @RequestMapping(method={RequestMethod.DELETE}, value={"/admin/centers"})
    public String adminDeleteCenter(@RequestParam("tokenID") String tokenID, @RequestParam("centerID") String recordID) {
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
        narmsCenterDAO.deleteByID(Long.valueOf(recordID));
        return "{\"result\":\"ok\"}";
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method={RequestMethod.POST}, value={"/admin/centers"})
    public String adminAddCenter(@RequestParam("tokenID") String tokenID,
        @RequestParam("centerState") String centerState,
        @RequestParam("centerMeatTypeLimit") String centerMeatTypeLimit,
        @RequestParam("centerName") String centerName) {
        
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
        NarmsCenter record = new NarmsCenter();
        record.setCenterName(centerName);
        record.setCenterState(centerState);
        narmsCenterDAO.persist(record);
        return "{\"result\":\"ok\"}";
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method={RequestMethod.PUT}, value={"/admin/centers"})
    public String adminUpdateCenter(@RequestParam("tokenID") String tokenID,
        @RequestParam("centerID") String recordID,
        @RequestParam("centerState") String centerState,
        @RequestParam("centerMeatTypeLimit") String centerMeatTypeLimit,
        @RequestParam("centerName") String centerName) {
        
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
        NarmsCenter record = narmsCenterDAO.getCenterById(Long.valueOf(recordID));
        if(record!=null) {
            record.setCenterName(centerName);
            record.setCenterState(centerState);
            narmsCenterDAO.update(record);
        } else {
           return "{\"result\":\"no record\"}"; 
        }
        return "{\"result\":\"ok\"}";
    }

    /** Samples **/
    @CrossOrigin(origins = "*")
    @RequestMapping(method={RequestMethod.DELETE}, value={"/admin/samples"})
    public String adminDeleteSample(@RequestParam("tokenID") String tokenID, @RequestParam("sampleID") String sampleID) {
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
        narmsSampleDAO.deleteSampleByID(Long.valueOf(sampleID));
        return "{\"result\":\"ok\"}";
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method={RequestMethod.POST}, value={"/admin/samples"})
    public String adminAddSample(@RequestParam("tokenID") String tokenID,@RequestParam("barCode") String barCode,
        @RequestParam("brandCode") String brandCode,
        @RequestParam("datePurchase") String datePurchase,
        @RequestParam("meat") String meat,
        @RequestParam("note") String note,
        @RequestParam("organic") String organic,
        @RequestParam("packedInStore") String packedInStore,
        @RequestParam("type") String type, HttpServletRequest request) {
        
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
        NarmsSample sample = new NarmsSample();
        sample.setMeat(meat);
        sample.setType(type);
        sample.setOrganic(organic);
        sample.setPackedInStore(packedInStore);
        sample.setBrandCode(brandCode);
        sample.setBarCode(barCode);   
        sample.setCountries(null);
        sample.setNote(note);
        Date datePurchaseDate = new Date( Long.valueOf(datePurchase));
        sample.setDatePurchase( datePurchaseDate);  
        narmsSampleDAO.persist(sample);
        return "{\"result\":\"ok\"}";
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method={RequestMethod.PUT}, value={"/admin/samples"})
    public String adminUpdateSample(@RequestParam("tokenID") String tokenID,
        @RequestParam("sampleID") String sampleID,
        @RequestParam("barCode") String barCode,
        @RequestParam(value = "brandCode", required = false, defaultValue = "") String brandCode,
        @RequestParam(value = "datePurchase", required = false, defaultValue = "0") String datePurchase,
        @RequestParam(value = "meat", required = false, defaultValue = "") String meat,
        @RequestParam(value = "note", required = false, defaultValue = "") String note,
        @RequestParam("organic") String organic,
        @RequestParam("packedInStore") String packedInStore,
        @RequestParam("type") String type, HttpServletRequest request) {
        
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
        NarmsSample sample = narmsSampleDAO.getSampleById(Long.valueOf(sampleID));
        sample.setMeat(meat);
        sample.setType(type);
        sample.setOrganic(organic);
        sample.setPackedInStore(packedInStore);
        sample.setBrandCode(brandCode);
        sample.setBarCode(barCode);   
        sample.setCountries(null);
        sample.setNote(note);
        if( datePurchase != "0") {
            try {
                Date datePurchaseDate = new Date( Long.valueOf(datePurchase));
                sample.setDatePurchase( datePurchaseDate);
            } catch (Exception e) {
                logger.error("Could not convert date of "+datePurchase);
            }
        }  
        narmsSampleDAO.update(sample);
        return "{\"result\":\"ok\"}";
    }

    /** Stores **/
    @CrossOrigin(origins = "*")
    @RequestMapping(method={RequestMethod.DELETE}, value={"/admin/stores"})
    public String adminDeleteStore(@RequestParam("tokenID") String tokenID, @RequestParam("storeID") String recordID) {
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
        narmsStoreDAO.deleteByID(Long.valueOf(recordID));
        return "{\"result\":\"ok\"}";
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value="/admin/stores", method=RequestMethod.POST)
    public RestResponseDTO adminAddStore(@RequestParam("tokenID") String tokenID,@RequestParam("storeName") String storeName,@RequestParam("storeAddress") String storeAddress) {
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
         RestResponseDTO result = new RestResponseDTO();

         NarmsStore store = new NarmsStore(storeName, storeAddress);
        try {
            narmsStoreDAO.persist(store);
            result.setMessage("OK");
        } catch (Exception e) {
            result.setError("Unknown error occured while adding store.");
            logger.error("Unknown error occured while adding store.", e);
        }

        return result;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value="/admin/stores", method=RequestMethod.PUT)
    public RestResponseDTO adminUpdateStore(@RequestParam("tokenID") String tokenID,@RequestParam("storeID") String recordID,@RequestParam("storeName") String storeName,@RequestParam("storeAddress") String storeAddress) {
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
         RestResponseDTO result = new RestResponseDTO();

         NarmsStore store =  narmsStoreDAO.getStoreById(Long.valueOf(recordID));
        
        if(store != null) {
            try {
                store.setStoreName(storeName);
                store.setStoreAddress(storeAddress);
                //need something set samples
                narmsStoreDAO.persist(store);
                result.setMessage("OK");
            } catch (Exception e) {
                result.setError("Unknown error occured while adding store.");
                logger.error("Unknown error occured while adding store.", e);
            }
        } else {
            result.setError("No store found with that id");
        }

        return result;
    }

    /** Users **/
    @CrossOrigin(origins = "*")
    @RequestMapping(method={RequestMethod.GET}, value={"/admin/users"})
    public List<NarmsUser> adminGetUsers(@RequestParam("tokenID") String tokenID) {
        //RestResponseDTO dto = new RestResponseDTO();
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        logger.debug("calling admin/users/get with token: "+tokenID+" and is admin: "+String.valueOf(isAdmin) );
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
        List<NarmsUser> names =  narmsUserDAO.getAllUsers();
        return names;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method={RequestMethod.DELETE}, value={"/admin/users"})
    public String adminGetUsers(@RequestParam("tokenID") String tokenID, @RequestParam("userID") String userID) {
        //RestResponseDTO dto = new RestResponseDTO();
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        logger.debug("calling admin/users/delete with token: "+tokenID+" and is admin: "+String.valueOf(isAdmin) );
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
        narmsUserDAO.deleteUserByID(Long.valueOf(userID));
        return "{\"result\":\"ok\"}";
    }


    @CrossOrigin(origins = "*")
    @RequestMapping(method={RequestMethod.POST}, value={"/admin/users"})
    public String adminCreateUser( 
        @RequestParam("tokenID") String tokenID,
        @RequestParam("email") String email,
        @RequestParam("password") String password, 
        @RequestParam("firstName") String firstName, 
        @RequestParam("lastName") String lastName,
        @RequestParam("userType") String userType) {
        //RestResponseDTO dto = new RestResponseDTO();
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        logger.debug("calling admin/users/get with token: "+tokenID+" and is admin: "+String.valueOf(isAdmin) );
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
        NarmsUser narmsUser =  narmsUserDAO.getUser(email);
        if( narmsUser != null ){
            
            return "{\"error\":\"This email is already registered\"}";
        }else{
            NarmsUser user = new NarmsUser(email, password, firstName, lastName, userType);
            
            /* skip this for users added via admin panel
            NarmsCenterDAO centerDAO = new NarmsCenterDAO();
            NarmsCenter center = centerDAO.getCenterWithState(stateName);
            user.setNarmsCenter(center);
            */
            
            narmsUserDAO.persist(user);
            
            return "{\"message\":\"Ok\"}";
        }
    }


    @CrossOrigin(origins = "*")
    @RequestMapping(method={RequestMethod.PUT}, value={"/admin/users"})
    public String adminUpdateUser(@RequestParam("tokenID") String tokenID,@RequestParam("userID") String userID,@RequestParam("email") String email,@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        //RestResponseDTO dto = new RestResponseDTO();
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        logger.debug("calling admin/users/get with token: "+tokenID+" and is admin: "+String.valueOf(isAdmin) );
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
        NarmsUser narmsUser =  narmsUserDAO.getUserById(userID);
        if( narmsUser != null ){
            narmsUser.setEmail(email);
            narmsUser.setFirstName(firstName);
            narmsUser.setLastName(lastName);
            narmsUserDAO.update(narmsUser);
            return "{\"message\":\"Ok\"}";
            
        }else{
            return "{\"error\":\"This user ID is not registered\"}";
            
        }
    }
    


    /** Brands **/
     @CrossOrigin(origins = "*")
    @RequestMapping(method={RequestMethod.DELETE}, value={"/admin/brands"})
    public String adminDeleteBrand(@RequestParam("tokenID") String tokenID, @RequestParam("brandID") String recordID) {
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
        narmBrandDAO.deleteByID(Long.valueOf(recordID));
        return "{\"result\":\"ok\"}";
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method={RequestMethod.POST}, value={"/admin/brands"})
    public String adminAddBrand(@RequestParam("tokenID") String tokenID,
        @RequestParam("brandCode") String brandCode,
        @RequestParam("brandName") String brandName) {
        
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
        NarmsBrand record = new NarmsBrand();
        record.setBrandName(brandName);
        record.setBrandCode(brandCode);
        narmBrandDAO.persist(record);
        return "{\"result\":\"ok\"}";
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method={RequestMethod.PUT}, value={"/admin/brands"})
    public String adminUpdateBrand(@RequestParam("tokenID") String tokenID,
        @RequestParam("brandID") String recordID,
        @RequestParam("brandCode") String brandCode,
        @RequestParam("brandName") String brandName) {
        
        Boolean isAdmin = narmsUserDAO.validateAdminToken(tokenID); //validation is broken throwing null errors 
        if(isAdmin != true){
            logger.debug("not allowed!");
            return null;
        }
        NarmsBrand record = narmBrandDAO.getById(Long.valueOf(recordID));
        if(record!=null) {
            record.setBrandName(brandName);
            record.setBrandCode(brandCode);
            narmBrandDAO.update(record);
        } else {
           return "{\"result\":\"no record\"}"; 
        }
        return "{\"result\":\"ok\"}";
    }
    
    
}
