package com.aurotech.controller.rest;

import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aurotech.data.dto.RestResponseDTO;
import com.aurotech.db.dao.NarmsCenterDAO;
import com.aurotech.db.dao.NarmsStoreDAO;
import com.aurotech.db.entities.NarmsCenter;
import com.aurotech.db.entities.NarmsStore;

/**
 *
 * @author jjvirani
 */
@CrossOrigin
@RestController
@Transactional
public class NarmsCenterController {
    
    private static final Logger logger = LoggerFactory.getLogger(NarmsCenterController.class);    
    
    @Autowired
    private NarmsCenterDAO narmsCenterDAO;
    
    @Autowired
    private NarmsStoreDAO narmsStoreDAO;
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getAllCenters", method=RequestMethod.GET)
    public RestResponseDTO getAllCenters() {
        logger.debug("getAllCenters(): START");
        RestResponseDTO result = new RestResponseDTO();
        
        try {
	        List<NarmsCenter> centers =  narmsCenterDAO.getAllCenters();
	        result.setData(centers);
	    } catch (Exception e) {
			result.setError("Unknown error occured while getting all centers.");
			logger.error("Unknown error occured while getting all centers.", e);
		}
        
        logger.debug("getAllCenters(): END");
        return result;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getCenterLimits/{centerId}", method=RequestMethod.GET)
    public RestResponseDTO getCenterLimits(@PathVariable(value="centerId") Long centerId) {
        logger.debug("getCenterLimits(): START");
        logger.debug("getCenterLimits(): centerId="+centerId);
        RestResponseDTO result = new RestResponseDTO();
        
        try {
        	NarmsCenter center = narmsCenterDAO.getCenterById(centerId);
        	
	        if(center != null) {
	        	result.setData(center.getCenterMeatTypelimit());
	        } else {
	        	result.setError("Center Limits not found.");
	        }
	        
	    } catch (Exception e) {
			result.setError("Unknown error occured while getting center limits.");
			logger.error("Unknown error occured while getting center limits.", e);
		}
        
        logger.debug("getCenterLimits(): END");
        return result;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getCenterLimitsByStore/{storeId}", method=RequestMethod.GET)
    public RestResponseDTO getCenterLimitsByStore(@PathVariable(value="storeId") Long storeId) {
        logger.debug("getCenterLimitsByStore(): START");
        logger.debug("getCenterLimitsByStore(): storeId="+storeId);
        RestResponseDTO result = new RestResponseDTO();
        
        try {
        	NarmsStore store = narmsStoreDAO.getStoreById(storeId);
        	
	        if(store != null && store.getNarmsCenter() != null) {
	        	result.setData(store.getNarmsCenter().getCenterMeatTypelimit());
	        } else {
	        	result.setError("Center Limits not found.");
	        }
	        
	    } catch (Exception e) {
			result.setError("Unknown error occured while getting center limits.");
			logger.error("Unknown error occured while getting center limits.", e);
		}
        
        logger.debug("getCenterLimitsByStore(): END");
        return result;
    }
    
}
