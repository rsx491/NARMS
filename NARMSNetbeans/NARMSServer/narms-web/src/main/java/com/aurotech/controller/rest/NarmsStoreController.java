package com.aurotech.controller.rest;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aurotech.data.dto.RestResponseDTO;
import com.aurotech.data.dto.StoreDTO;
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
public class NarmsStoreController {
    
    private static final Logger logger = LoggerFactory.getLogger(NarmsStoreController.class);    
    
    @Autowired
    private NarmsStoreDAO narmsStoreDAO;
    
    @Autowired
    private NarmsCenterDAO narmsCenterDAO;
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getAllStores", method=RequestMethod.GET)
    public RestResponseDTO getAllStores() {
        logger.debug("getAllStores(): START");
        RestResponseDTO result = new RestResponseDTO();
        
        try {
	        List<NarmsStore> stores =  narmsStoreDAO.getAllStores();
	        result.setData(stores);
	    } catch (Exception e) {
			result.setError("Unknown error occured while getting all stores.");
			logger.error("Unknown error occured while getting all stores.", e);
		}
        
        logger.debug("getAllStores(): END");
        return result;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getAllStoresByCenterID", method=RequestMethod.GET)
    public RestResponseDTO getAllStoresByCenterID(@RequestParam(name="centerID", required=true) String centerID) {
        logger.debug("getAllStoresByCenterID(): START");
        RestResponseDTO result = new RestResponseDTO();
        
        try {
	        List<NarmsStore> stores =  narmsStoreDAO.getAllCenterStores(centerID);
	        result.setData(stores);
	    } catch (Exception e) {
			result.setError("Unknown error occured while getting all stores.");
			logger.error("Unknown error occured while getting all stores.", e);
		}
        
        logger.debug("getAllStoresByCenterID(): END");
        return result;
    }    
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getStoreByName", method=RequestMethod.GET)
    public RestResponseDTO getStoreByName(@RequestParam(name="storeName", required=true) String storeName) {
        logger.debug("getStoreByName(): START");
        logger.debug("storeName=" + storeName);
        RestResponseDTO result = new RestResponseDTO();
        
        try {
	        NarmsStore store =  narmsStoreDAO.getStoreByName(storeName.trim());
	        result.setData(store);
	    } catch (Exception e) {
			result.setError("Unknown error occured while getting store by name.");
			logger.error("Unknown error occured while getting store by name.", e);
		}
        
        logger.debug("getStoreByName(): END");
        return result;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getStoresByNameContains", method=RequestMethod.GET)
    public RestResponseDTO getStoresByNameContains(@RequestParam(name="storeName", required=true) String storeName) {
        logger.debug("getStoresByNameContains(): START");
        logger.debug("storeName=" + storeName);
        
        RestResponseDTO result = new RestResponseDTO();
        
        try {
	        List<NarmsStore> stores =  narmsStoreDAO.getStoresByNameContains(storeName.trim());
	        result.setData(stores);
	    } catch (Exception e) {
			result.setError("Unknown error occured while getting all stores.");
			logger.error("Unknown error occured while getting all stores.", e);
		}
        
        logger.debug("getStoresByNameContains(): END");
        return result;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/addStore", method=RequestMethod.POST)
    public RestResponseDTO addStore(@RequestBody NarmsStore store) {
    	 logger.debug("addStore(): START");
    	 logger.debug("Input=" + store);
    	 
    	 RestResponseDTO result = new RestResponseDTO();

		try {
			narmsStoreDAO.persist(store);
			result.setMessage("OK");
		} catch (Exception e) {
			result.setError("Unknown error occured while adding store.");
			logger.error("Unknown error occured while adding store.", e);
		}

		logger.debug("addStore(): END");
		return result;
	}
    
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/addStoreToCenter/{centerId}", method=RequestMethod.POST)
    public RestResponseDTO addStoreToCenter(@RequestBody NarmsStore store, @PathVariable(value="centerId") Long centerId) {
    	 logger.debug("addStoreToCenter(): START");
    	 logger.debug("Input=" + store);
    	 
    	 RestResponseDTO result = new RestResponseDTO();

		try {
			if(StringUtils.isEmpty(store.getStoreName()) || StringUtils.isEmpty(store.getStoreAddress())) {
				result.setError("Store Name and Address are required.");
				return result;
			}
			
			NarmsCenter center = narmsCenterDAO.getCenterById(centerId);
			
			if(center == null) {
				result.setError("Invalid Center.");
				return result;
			}
			
			store.setNarmsCenter(center);
			
			narmsStoreDAO.persist(store);
			 logger.debug("Store added. storeId="+ store.getId());
			result.setData(new StoreDTO(store));
		} catch (Exception e) {
			result.setError("Unknown error occured while adding store.");
			logger.error("Unknown error occured while adding store.", e);
		}

		logger.debug("addStoreToCenter(): END");
		return result;
	}
    
}
