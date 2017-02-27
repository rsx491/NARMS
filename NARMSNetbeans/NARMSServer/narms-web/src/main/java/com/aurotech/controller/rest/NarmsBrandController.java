package com.aurotech.controller.rest;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aurotech.data.dto.RestResponseDTO;
import com.aurotech.db.dao.NarmsBrandDAO;
import com.aurotech.db.entities.NarmsBrand;

/**
 *
 * @author jjvirani
 */
@CrossOrigin
@RestController
@Transactional
public class NarmsBrandController {
    
    private static final Logger logger = LoggerFactory.getLogger(NarmsBrandController.class);    
    
    @Autowired
    private NarmsBrandDAO narmBrandDAO;
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getAllBrands", method=RequestMethod.GET)
    public RestResponseDTO getAllBrands() {
        logger.debug("getAllBrands(): START");
        RestResponseDTO result = new RestResponseDTO();
        
        try {
	        List<NarmsBrand> brands =  narmBrandDAO.findAll();
	        result.setData(brands);
	    } catch (Exception e) {
			result.setError("Unknown error occured while getting all brands.");
			logger.error("Unknown error occured while getting all brands.", e);
		}
        
        logger.debug("getAllBrands(): END");
        return result;
    }
    
    
}
