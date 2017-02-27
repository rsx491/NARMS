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

import com.aurotech.constants.SampleConstants;
import com.aurotech.data.dto.RestResponseDTO;
import com.aurotech.db.dao.CountryDAO;
import com.aurotech.db.entities.Country;
import com.aurotech.init.AppContextListener;

/**
 *
 * @author jjvirani
 */
@CrossOrigin
@RestController
@Transactional
public class CountryController {
    
    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);    
    
    @Autowired
    private CountryDAO countryDAO;
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getAllCountries", method=RequestMethod.GET)
    public RestResponseDTO getAllCountries() {
        logger.debug("getAllCountries(): START");
        RestResponseDTO result = new RestResponseDTO();
        
        try {
        	
        	SampleConstants constants = (SampleConstants)AppContextListener.getSpringContext().getBean("sampleConstants");
        	
	        result.setData(constants.getCountriesList());
	    } catch (Exception e) {
			result.setError("Unknown error occured while getting all countries.");
			logger.error("Unknown error occured while getting all countries.", e);
		}
        
        logger.debug("getAllCountries(): END");
        return result;
    }
    
    
}
