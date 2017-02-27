
package com.aurotech.controller.rest;

import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aurotech.constants.NarmsConstants;
import com.aurotech.constants.SampleConstants;
import com.aurotech.data.dto.DocumentDTO;
import com.aurotech.data.dto.RestResponseDTO;
import com.aurotech.data.dto.SampleDTO;
import com.aurotech.data.dto.ShortSampleDTO;
import com.aurotech.data.dto.StoreDTO;
import com.aurotech.data.dto.UserDTO;
import com.aurotech.db.dao.NarmsCenterDAO;
import com.aurotech.db.dao.NarmsDocumentDAO;
import com.aurotech.db.dao.NarmsSampleDAO;
import com.aurotech.db.dao.NarmsStoreDAO;
import com.aurotech.db.dao.NarmsUserDAO;
import com.aurotech.db.entities.CenterMeatTypeLimit;
import com.aurotech.db.entities.Country;
import com.aurotech.db.entities.NarmsCenter;
import com.aurotech.db.entities.NarmsSample;
import com.aurotech.db.entities.NarmsSampleImage;
import com.aurotech.db.entities.NarmsStore;
import com.aurotech.db.entities.NarmsUser;
import com.aurotech.init.AppContextListener;
import com.aurotech.utils.DateUtils;
import com.aurotech.utils.FileUploadUtils;
import com.aurotech.utils.SampleLogUtils;

/**
 *
 * @author jjvirani
 */
@CrossOrigin
@RestController
@Transactional
public class NarmsSampleController {
    
    private static final Logger logger = LoggerFactory.getLogger(NarmsSampleController.class);    
    
    @Autowired
    private NarmsSampleDAO narmsSampleDAO;
    
    @Autowired
    private NarmsStoreDAO narmsStoreDAO;
    
    @Autowired
    private NarmsDocumentDAO narmsDocumentDAO;

    @Autowired
    private NarmsUserDAO narmsUserDAO;
    
    @Autowired
    private NarmsCenterDAO narmsCenterDAO;
    
    private static final String FRONT = "front";
    private static final String BACK = "back";
    
    
    private List<ShortSampleDTO> convertToShortSampleDTO(List<NarmsSample> samples) {
    	
    	List<ShortSampleDTO> sampleDTOList = null;
    	
    	if(!CollectionUtils.isEmpty(samples)) {
    		sampleDTOList = new ArrayList<ShortSampleDTO>(samples.size());
    		
    		for (NarmsSample sample : samples) {

                    ShortSampleDTO sampleDTO = new ShortSampleDTO(sample);
                    sampleDTOList.add(sampleDTO);
                }
    	}
    	
    	
    	return sampleDTOList;
    }    
    
    
    private List<SampleDTO> convertToSampleDTO(List<NarmsSample> samples) {
    	
    	List<SampleDTO> sampleDTOList = null;
    	
    	if(!CollectionUtils.isEmpty(samples)) {
    		sampleDTOList = new ArrayList<SampleDTO>(samples.size());
    		
    		for (NarmsSample sample : samples) {
    			
    			SampleDTO sampleDTO = new SampleDTO(sample);
    			
    			if(sample.getFrontCapture() != null) {
    				sampleDTO.setFrontCapture(new DocumentDTO(sample.getFrontCapture()));
    			}
    			
    			if(sample.getBackCapture() != null) {
    				sampleDTO.setBackCapture(new DocumentDTO(sample.getBackCapture()));
    			}
    			
    			// Add store details for sample
    			StoreDTO storeDTO = new StoreDTO(sample.getStore());
    			sampleDTO.setStore(storeDTO);
    			
    			sampleDTOList.add(sampleDTO);
			}
    	}
	
    	return sampleDTOList;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getAllSamples", method=RequestMethod.GET)
    public RestResponseDTO getAllSamples() {
        logger.debug("getAllSamples(): START");
        RestResponseDTO result = new RestResponseDTO();
        
        List<NarmsSample> samples = null;
		try {
			samples = narmsSampleDAO.getAllSamples();
			result.setData(convertToSampleDTO(samples));
		} catch (Exception e) {
			result.setError("Unknown error occured while getting all samples.");
			logger.error("Unknown error occured while getting all samples.", e);
		}
        
        logger.debug("getAllSamples(): END");
        return result;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getAllSamples/{id}", method=RequestMethod.GET)
    public RestResponseDTO getAllSamplesWithID(@PathVariable(value="id") String id) {
        logger.debug("getAllSamplesWithID(): START");
        RestResponseDTO result = new RestResponseDTO();
        
        try {
            Long sampleID = Long.parseLong(id);
            
            NarmsSample sample = narmsSampleDAO.getSampleById( sampleID );
            SampleDTO sampleDTO = new SampleDTO(sample);
            sampleDTO.setStore(new StoreDTO(sample.getStore()));
            
            if(sample.getFrontCapture() != null) {
				sampleDTO.setFrontCapture(new DocumentDTO(sample.getFrontCapture()));
			}
			
			if(sample.getBackCapture() != null) {
				sampleDTO.setBackCapture(new DocumentDTO(sample.getBackCapture()));
			}
            
            result.setData(sampleDTO);
            
        } catch (Exception e) {
            result.setError("Unknown error occured while getting all samples.");
            logger.error("Unknown error occured while getting all samples.", e);
        }

        logger.debug("getAllSamplesWithID(): END");
        return result;
    }    
    
    /**
     * Send all samples for user with user token and send ShortSampleDTO in rest response
     * 
     * @param usertoken
     * @param month
     * @return RestResponseDTO
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getAllSamples/usertoken/{usertoken}/month/{month}", method=RequestMethod.GET)
    @Transactional
    public RestResponseDTO getAllSamplesByUserID(@PathVariable(value="usertoken") String usertoken , @PathVariable(value="month") String month) {
        logger.debug("/getAllSamples/usertoken/ START");
        logger.debug("usertoken=" + usertoken);
        
        logger.debug("month entered := " + month );
        
        if(month == null){
            month = "" + LocalDate.now().getMonthValue();
        }
        
        RestResponseDTO result = new RestResponseDTO();
        
        try {
        	
        	NarmsUser currentUser = narmsUserDAO.getActiveUserBySessionToken(usertoken);
        	
        	if(currentUser == null) {
        		result.setError("Invalid User Session Token.");
        		return result;
        	}       
                
                List<NarmsSample> returnSamples = new ArrayList();

                for( NarmsSample sample : currentUser.getNarmsSamples() ){
                    
                    //GET COUNT FOR CURRENT MONTH ONLY

                    logger.debug("Date month := " + month);
                    logger.debug("LocalDate.now().getMonthValue() month := " + LocalDate.now().getMonthValue());
                    
                    if( month != null && Integer.parseInt(month) != 0 && Integer.parseInt(month) != LocalDate.now().getMonthValue() ){
                        continue;
                    }                    
                    
                    returnSamples.add( sample);

                    
                }
                
			
	        result.setData(convertToShortSampleDTO(returnSamples));
	        
	    } catch (Exception e) {
			result.setError("Unknown error occured while adding store sample.");
			logger.error("Unknown error occured while adding store sample.", e);
            }
        
        logger.debug("/getAllSamples/usertoken/ END");
        return result;
    }    
    
    /**
     * Get all samples for the center by dates
     * send ShortSampleDTO object in RestResponseDTO object
     * 
     * @param centerid
     * @param fromdate
     * @param todate
     * @return RestResponseDTO
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getAllSamplesByCenterAndDate", method=RequestMethod.GET)
    public RestResponseDTO getAllSamplesByCenterAndDate( @RequestParam("centerid") String centerid , @RequestParam("fromdate") String fromdate, @RequestParam("todate") String todate) {
        logger.debug("getAllSamplesByCenterAndDate(): START");
        RestResponseDTO result = new RestResponseDTO();
        
        List<NarmsSample> samples = null;
        try {

            Date fromDate = DateUtils.getFormatedDate(fromdate);
            Date toDate = DateUtils.getFormatedDate(todate);

            logger.debug("fromDate := " + fromDate);
            logger.debug("toDate := " + toDate);

            samples = narmsSampleDAO.getAllSamplesByCenterIDAndDate(centerid, fromDate, toDate);
            result.setData(convertToShortSampleDTO(samples));

        } catch (Exception e) {
                result.setError("Unknown error occured while getting all samples.");
                logger.error("Unknown error occured while getting all samples.", e);
        }
        
        logger.debug("getAllSamplesByCenterAndDate(): END");
        return result;
    }    
    
    /**
     * get all samples by center, send ShortSampleDTO in RestResponseDTO
     * @param centerid
     * @return ShortSampleDTO
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getAllSamplesByCenter", method=RequestMethod.GET)
    public RestResponseDTO getAllSamplesByCenter( @RequestParam("centerid") String centerid ) {
        logger.debug("getAllSamplesByCenter(): START");
        RestResponseDTO result = new RestResponseDTO();
        
        List<NarmsSample> samples = null;
		try {
                    
                    YearMonth yearMonth = YearMonth.of( LocalDate.now().getYear(), LocalDate.now().getMonth()  );  // eg January of 2015.
                    LocalDate firstOfMonth = yearMonth.atDay( 1 );
                    LocalDate lastOfMonth = yearMonth.atEndOfMonth();

                    logger.debug("firstOfMonth := " + firstOfMonth);
                    logger.debug("lastOfMonth := " + lastOfMonth);
                    
                    Date fromDate = Date.from(firstOfMonth.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                    Date toDate = Date.from(lastOfMonth.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                    
                    logger.debug("fromDate := " + fromDate);
                    logger.debug("toDate := " + toDate);
                    
                    samples = narmsSampleDAO.getAllSamplesByCenterIDAndDate(centerid, fromDate, toDate);
                    
                    result.setData(convertToShortSampleDTO(samples));
                        
		} catch (Exception e) {
			result.setError("Unknown error occured while getting all samples.");
			logger.error("Unknown error occured while getting all samples.", e);
		}
        
        logger.debug("getAllSamplesByCenter(): END");
        return result;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getStoreSamples/{storeId}", method=RequestMethod.GET)
    @Transactional
    public RestResponseDTO getStoreSamples(@PathVariable(value="storeId") Long storeId) {
        logger.debug("getStoreSamples(): START");
        logger.debug("storeId=" + storeId);
        
        RestResponseDTO result = new RestResponseDTO();
        
        try {
        	
        	NarmsStore store = narmsStoreDAO.getStoreById(storeId);
	       
        	if(store != null) {
        		 result.setData(convertToSampleDTO(store.getSamples()));
        	} else {
        		result.setError("Store doesn't exist.");
        	}
	        
	        
	    } catch (Exception e) {
			result.setError("Unknown error occured while getting store samples.");
			logger.error("Unknown error occured while getting store samples.", e);
		}
        
        logger.debug("getStoreSamples(): END");
        return result;
    }
    
    
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getsamplecountbycenterid", method=RequestMethod.GET)
    @Consumes({"application/json"})
    public RestResponseDTO getsamplecountbycenterid( @RequestParam("centerid") String centerid) {
        logger.debug("getsamplecountbycenterid(): START");
        

        RestResponseDTO result = new RestResponseDTO();
        
        try {

        	SampleConstants constants = (SampleConstants)AppContextListener.getSpringContext().getBean("sampleConstants");
                //NarmsCenter narmsCenter = narmsCenterDAO.getCenterByMonthAndID( centerid, LocalDate.now().getMonthValue() );
                NarmsCenter narmsCenter = narmsCenterDAO.getCenterByID( centerid );
                
        	if(narmsCenter == null) {
        		result.setError("No result found");
        		return result;
        	}       
                
                Map<String,Long> meatCount = new TreeMap();

                
                for( CenterMeatTypeLimit limit : narmsCenter.getCenterMeatTypelimit() ){
                     logger.debug("limit.getType() := " + limit.getType());
                     String meatNameKey = (String)constants.getMeatNameKey().get(limit.getType());
                     logger.debug("meatNameKey := " + meatNameKey );
                     
                     Long limitCountCompleted = narmsSampleDAO.getSampleCountByMeatNameAndCenterID(meatNameKey , narmsCenter.getId());
                     meatCount.put(limit.getType(), Long.valueOf( limitCountCompleted ));

                }
                
			
	        result.setData(meatCount);
	        
	    } catch (Exception e) {
			result.setError("Unknown error occured while adding store sample.");
			logger.error("Unknown error occured while adding store sample.", e);
            }
        
        logger.debug("getsamplecountbycenterid(): END");
        return result;
    }    
    
    
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getcurrentsamplecountbycenterid", method=RequestMethod.GET)
    @Consumes({"application/json"})
    public RestResponseDTO getcurrentsamplecountbycenterid( @RequestParam("centerid") String centerid) {
        logger.debug("getcurrentsamplecountbycenterid(): START");
        

        RestResponseDTO result = new RestResponseDTO();
        
        try {
      
                Date fromDate = DateUtils.getCurrentMonthStartDate();
                Date toDate = DateUtils.getCurrentMonthEndDate();

                logger.debug("fromDate := " + fromDate);
                logger.debug("toDate := " + toDate);


        	SampleConstants constants = (SampleConstants)AppContextListener.getSpringContext().getBean("sampleConstants");
                //NarmsCenter narmsCenter = narmsCenterDAO.getCenterByMonthAndID( centerid, LocalDate.now().getMonthValue() );
                NarmsCenter narmsCenter = narmsCenterDAO.getCenterByID( centerid );
                
        	if(narmsCenter == null) {
        		result.setError("No result found");
        		return result;
        	}       
                
                Map<String,Long> meatCount = new TreeMap();

                
                for( CenterMeatTypeLimit limit : narmsCenter.getCenterMeatTypelimit() ){
                     logger.debug("limit.getType() := " + limit.getType());
                     String meatNameKey = (String)constants.getMeatNameKey().get(limit.getType());
                     logger.debug("meatNameKey := " + meatNameKey );
                     
                     Long limitCountCompleted = narmsSampleDAO.getSampleCountByMeatNameAndCenterID(meatNameKey , narmsCenter.getId() , fromDate , toDate );
                     meatCount.put(limit.getType(), Long.valueOf( limitCountCompleted ));

                }
                
			
	        result.setData(meatCount);
	        
	    } catch (Exception e) {
			result.setError("Unknown error occured while adding store sample.");
			logger.error("Unknown error occured while adding store sample.", e);
            }
        
        logger.debug("getcurrentsamplecountbycenterid(): END");
        return result;
    }

    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getSampleStoresByUser", method=RequestMethod.GET)
    @Consumes({"application/json"})
    public RestResponseDTO getSampleStoresByUser( @RequestParam("tokenID") String tokenID) {
        logger.debug("getSampleStoresByUser(): START");
        
        RestResponseDTO result = new RestResponseDTO();
        
        try {
        	
        	NarmsUser currentUser = narmsUserDAO.getActiveUserBySessionToken(tokenID);
        	
        	if(currentUser == null) {
        		result.setError("Invalid User Session Token.");
        		return result;
        	}       
                
                Map<String,NarmsStore> stores = new TreeMap();

                for( NarmsSample sample : currentUser.getNarmsSamples() ){
                    
                    //GET COUNT FOR CURRENT MONTH ONLY
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sample.getDatePurchase());
                    int month = cal.get(Calendar.MONTH);
                    month = month+1;
                    logger.debug("Date month := " + month);
                    logger.debug("LocalDate.now().getMonthValue() month := " + LocalDate.now().getMonthValue());
                    
                    if( month != LocalDate.now().getMonthValue() ){
                        continue;
                    }                    
                    
                    
                    NarmsStore narmsStore = sample.getStore();
                    stores.put( ""+narmsStore.getId(), narmsStore );
                    
                }
                
			
	        result.setData(stores);
	        
	    } catch (Exception e) {
			result.setError("Unknown error occured while adding store sample.");
			logger.error("Unknown error occured while adding store sample.", e);
            }
        
        logger.debug("getSampleStoresByUser(): END");
        return result;
    }    
    
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/getSampleCountByUser", method=RequestMethod.GET)
    @Consumes({"application/json"})
    public RestResponseDTO getSampleCountByUser( @RequestParam("tokenID") String tokenID) {
        logger.debug("getSampleCountByUser(): START");
        
        RestResponseDTO result = new RestResponseDTO();
        
        try {
        	
        	NarmsUser currentUser = narmsUserDAO.getActiveUserBySessionToken(tokenID);
        	
                logger.debug("currentUser.getFirstName() " + currentUser.getFirstName());
                logger.debug("currentUser.getNarmsSamples().size() " + currentUser.getNarmsSamples().size());
                
        	if(currentUser == null) {
        		result.setError("Invalid User Session Token.");
        		return result;
        	}       
                
                SampleConstants constants = (SampleConstants)AppContextListener.getSpringContext().getBean("sampleConstants");
                
                Iterator keys = constants.getMeatName().keySet().iterator();
                Map<String,Long> meatCount = new TreeMap();

                for( NarmsSample sample : currentUser.getNarmsSamples() ){
                    logger.debug("user sample is " + sample.getSampleid());
                    //GET COUNT FOR CURRENT MONTH ONLY
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sample.getDatePurchase());
                    int month = cal.get(Calendar.MONTH);
                    month = month + 1;
                    if( month != LocalDate.now().getMonthValue() ){
                        continue;
                    }
                     String value = (String)constants.getMeatName().get(sample.getMeat());
                     
                     logger.debug("value := " + value);
                     
                     if( meatCount.get(value) != null ){
                         Long count = meatCount.get(value);
                         count = count + 1;
                         logger.debug("in count++ := " + count);
                         meatCount.put(value, count);
                     }else{
                         meatCount.put(value, 1L);
                     }
                }
                
			
	        result.setData(meatCount);
	        
	    } catch (Exception e) {
			result.setError("Unknown error occured while adding store sample.");
			logger.error("Unknown error occured while adding store sample.", e);
            }
        
        logger.debug("getSampleCountByUser(): END");
        return result;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/addStoreSamples/{storeId}", method=RequestMethod.POST)
    @Consumes({"application/json"})
    public RestResponseDTO addStoreSamples(@RequestBody SampleDTO sampleInput, @PathVariable(value="storeId") Long storeId, HttpServletRequest request) {
        logger.debug("addStoreSamples(): START");
        logger.debug("storeId=" + storeId);
        logger.debug("sampleInput=" + sampleInput);
        
        NarmsConstants narmsConstants = (NarmsConstants)AppContextListener.getSpringContext().getBean("narmsConstants");
        String TOKEN_KEY = (String)narmsConstants.getCONSTANTS().get("TOKEN_KEY");
        
        logger.debug("TOKEN_KEY from spring conf := " + TOKEN_KEY);
        
        String userSessionToken = request.getHeader(TOKEN_KEY);
        
        RestResponseDTO result = new RestResponseDTO();
        
        try {
        	
        	NarmsUser currentUser = narmsUserDAO.getActiveUserBySessionToken(userSessionToken);
        	
        	if(currentUser == null) {
        		result.setError("Invalid User Session Token.");
        		return result;
        	}
        	 
        	NarmsStore store = narmsStoreDAO.getStoreById(storeId);
        	
        	if(store == null) {
        		result.setError("Store doesn't exist");
        		return result;
        	}
        	
        	NarmsCenter center = store.getNarmsCenter();
        	if(center == null) {
        		result.setError("Store doesn't have any center.");
        		return result;
        	}
        	
        	List<CenterMeatTypeLimit> limits = center.getCenterMeatTypelimit();
        	
        	if(CollectionUtils.isEmpty(limits)) {
        		result.setError("Center doesn't have any limits defined.");
        		return result;
        	}
        	
        	
        	String sampleMeatNameCode = sampleInput.getMeat();
        	SampleConstants constants = (SampleConstants)AppContextListener.getSpringContext().getBean("sampleConstants");
                String meatName = (String)constants.getMeatName().get(sampleMeatNameCode);
                
                /*
                Iterator keys = constants.getMeatName().keySet().iterator();
                
                while( keys.hasNext() ){
                    String key = (String)keys.next();
                    logger.debug("key from sample := " + key);
                }

                Iterator values = constants.getMeatName().values().iterator();
                
                while( values.hasNext() ){
                    String value = (String)values.next();
                    logger.debug("values from sample := " + value);
                }
                */
                
                //meatType = MeatType.findByValue(meatTypeValue);
                logger.debug("sampleMeatNameCode from sample := " + sampleMeatNameCode);
        	logger.debug("meatName from sample := " + meatName);
                
        	String sampleMeatTypeCode = sampleInput.getType();
                String meatType = (String)constants.getMeatType().get(sampleMeatTypeCode);
                //meatType = MeatType.findByValue(meatTypeValue);
                logger.debug("sampleMeatTypeCode from sample := " + sampleMeatTypeCode);
        	logger.debug("meatType from sample := " + meatType);                
                
                
        	CenterMeatTypeLimit limit = null;
        	
        	for (CenterMeatTypeLimit centerMeatTypeLimit : limits) {
        		if( meatName.equalsIgnoreCase(centerMeatTypeLimit.getType())){
        			limit = centerMeatTypeLimit;
        			break;
        		}
        	}
                
                logger.debug("limit.getType() := " + limit.getType());
                String meatNameKey = (String)constants.getMeatNameKey().get(limit.getType());
                logger.debug("meatNameKey := " + meatNameKey );

                Long limitCountCompleted = narmsSampleDAO.getSampleCountByMeatNameAndCenterID( meatNameKey , center.getId());
                
        	if( limitCountCompleted >= limit.getTotalLimit()) {
        		result.setError("Sample limit of "+limit.getTotalLimit()+" already reached.");
        		return result;
        	}
        	
        	// Set countries for sample
        	Map countryList = constants.getCountriesList();
        	
        	List<String> inputCountryList = sampleInput.getCountries();
        	List<Country> countries = null;
        	if(!CollectionUtils.isEmpty(inputCountryList)) {
        		countries = new ArrayList<Country>(inputCountryList.size());
        		for (String countryCode : inputCountryList) {
        			
        			String countryName = (String)countryList.get(countryCode);
        			
        			countries.add(new Country(countryCode, countryName));
				}
        	}
        	
        		
        	NarmsSample sample = new NarmsSample(sampleInput);
                sample.setStore(store);
                sample.setNarmsUser(currentUser);
                String sampleID = SampleLogUtils.getSampleLogID(meatName, meatType,center.getCenterState() , limitCountCompleted.intValue() + 1);
                sample.setSampleid(sampleID);
                sample.setCountries(countries);
                
                sample = narmsSampleDAO.persist(sample);
			
                // Update Limits
                /*
                int index = limits.indexOf(limit);
                limit.setCountCompleted(limit.getCountCompleted() + 1);
                limits.set(index, limit);
                 */
                        
        	center.setCenterMeatTypelimit(limits);
        	narmsCenterDAO.update(center);
	        
        	SampleDTO resultDTO = new SampleDTO(sample);
        	resultDTO.setUser(new UserDTO(sample.getNarmsUser()));
        	
	        result.setData(resultDTO);
	        
	    } catch (Exception e) {
			result.setError("Unknown error occured while adding store sample.");
			logger.error("Unknown error occured while adding store sample.", e);
            }
        
        logger.debug("addStoreSamples(): END");
        return result;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/updateSample/{sampleId}", method=RequestMethod.PUT)
    @Consumes({"application/json"})
    public RestResponseDTO updateSample(@RequestBody SampleDTO sampleInput, @PathVariable(value="sampleId") Long sampleId, HttpServletRequest request) {
        logger.debug("updateSample(): START");
        logger.debug("sampleId=" + sampleId);
        logger.debug("sampleInput=" + sampleInput);
        
        NarmsConstants narmsConstants = (NarmsConstants)AppContextListener.getSpringContext().getBean("narmsConstants");
        String TOKEN_KEY = (String)narmsConstants.getCONSTANTS().get("TOKEN_KEY");
        
        logger.debug("TOKEN_KEY from spring conf := " + TOKEN_KEY);
        
        String userSessionToken = request.getHeader(TOKEN_KEY);
        
        RestResponseDTO result = new RestResponseDTO();
        
        try {
        	
        	NarmsUser currentUser = narmsUserDAO.getActiveUserBySessionToken(userSessionToken);
        	
        	// Get persisted sample data
        	NarmsSample sample = narmsSampleDAO.getSampleById(sampleId);
        	
        	if(currentUser == null) {
        		result.setError("Invalid User Session Token.");
        		return result;
        	}
        	
        	
        	NarmsStore store = sample.getStore();
        	
        	if(store == null) {
        		result.setError("Store doesn't exist");
        		return result;
        	}
        	
        	NarmsCenter center = store.getNarmsCenter();
        	if(center == null) {
        		result.setError("Store doesn't have any center.");
        		return result;
        	}
        	
        	
        	List<CenterMeatTypeLimit> limits = center.getCenterMeatTypelimit();
        	
        	if(CollectionUtils.isEmpty(limits)) {
        		result.setError("Center doesn't have any limits defined.");
        		return result;
        	}
        	
        	if(sample == null) {
        		result.setError("Sample doesn't exist.");
        		return result;
        	}
        	
        	
        	String sampleMeatNameCode = sampleInput.getMeat();
        		SampleConstants sampleConstants = (SampleConstants)AppContextListener.getSpringContext().getBean("sampleConstants");
                String meatName = (String)sampleConstants.getMeatName().get(sampleMeatNameCode);
                
                
                //meatType = MeatType.findByValue(meatTypeValue);
                logger.debug("sampleMeatNameCode from sample := " + sampleMeatNameCode);
        	logger.debug("meatName from sample := " + meatName);
                
        	String sampleMeatTypeCode = sampleInput.getType();
                String meatType = (String)sampleConstants.getMeatType().get(sampleMeatTypeCode);
                //meatType = MeatType.findByValue(meatTypeValue);
                logger.debug("sampleMeatTypeCode from sample := " + sampleMeatTypeCode);
        	logger.debug("meatType from sample := " + meatType);                
                
                
        	CenterMeatTypeLimit limit = null;
        	
        	for (CenterMeatTypeLimit centerMeatTypeLimit : limits) {
        		if( meatName.equalsIgnoreCase(centerMeatTypeLimit.getType())){
        			limit = centerMeatTypeLimit;
        			break;
        		}
        	}
        	
               Long limitCountCompleted = narmsSampleDAO.getSampleCountByMeatNameAndCenterID(limit.getType() , center.getId());
                
        	if( limitCountCompleted >= limit.getTotalLimit()) {
        		result.setError("Sample limit of "+limit.getTotalLimit()+" already reached.");
        		return result;
        	}
        	
        	// Set countries for sample
        	Map countryList = sampleConstants.getCountriesList();
        	
        	List<String> inputCountryList = sampleInput.getCountries();
        	List<Country> countries = null;
        	if(!CollectionUtils.isEmpty(inputCountryList)) {
        		countries = new ArrayList<Country>(inputCountryList.size());
        		for (String countryCode : inputCountryList) {
        			
        			String countryName = (String)countryList.get(countryCode);
        			
        			countries.add(new Country(countryCode, countryName));
				}
        	}
        	
        	
        	//sample.setId(sampleId);
        	sample.setMeat(sampleInput.getMeat());
        	sample.setType(sampleInput.getType());
        	sample.setOrganic(sampleInput.getOrganic());
        	sample.setPackedInStore(sampleInput.getPackedInStore());
        	sample.setBrandCode(sampleInput.getBrandCode());
        	sample.setBarCode(sampleInput.getBarCode());
        	sample.setNote(sampleInput.getNote());
        	sample.setDatePurchase(sampleInput.getDatePurchase());
        	sample.setDateSaleBy(sampleInput.getDateSaleBy());
        	sample.setDateProcess(sampleInput.getDateProcess());
        	sample.setGenus(sampleInput.getGenus());
        	sample.setGenusName(sampleInput.getGenusName());
        	sample.setAccessionNumber(sampleInput.getAccessionNumber());
        	sample.setGrowth(sampleInput.getGrowth());
        	sample.setSpecies(sampleInput.getSpecies());
        	sample.setSerotype(sampleInput.getSerotype());
        	sample.setAntigenicFormula(sampleInput.getAntigenicFormula());
        	sample.setIsolateId(sampleInput.getIsolateId());
        	sample.setSource(sampleInput.getSource());
        	sample.setSourceSpeciesInfo(sampleInput.getSourceSpeciesInfo());
        	
            sample.setStore(store);
            sample.setNarmsUser(currentUser);
            String sampleID = SampleLogUtils.getSampleLogID(meatName, meatType,center.getCenterState() , limitCountCompleted.intValue() + 1);
            sample.setSampleid(sampleID);
            sample.setCountries(countries);
            
            sample = narmsSampleDAO.update(sample);
	        
        	SampleDTO resultDTO = new SampleDTO(sample);
        	resultDTO.setUser(new UserDTO(sample.getNarmsUser()));
        	
        	resultDTO.setStore(new StoreDTO(store));
        	
	        result.setData(resultDTO);
	        
	    } catch (Exception e) {
			result.setError("Unknown error occured while adding store sample.");
			logger.error("Unknown error occured while adding store sample.", e);
            }
        
        logger.debug("updateSample(): END");
        return result;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/addStoreSamples", method=RequestMethod.POST)
    @Consumes({"application/json"})
    public RestResponseDTO addStoreSamplesByStoreName(@RequestBody StoreDTO storeInput, HttpServletRequest request) {
        logger.debug("addStoreSamplesByStoreName(): START");
        logger.debug("storeInput=" + storeInput);
        
        
        NarmsConstants narmsConstants = (NarmsConstants)AppContextListener.getSpringContext().getBean("narmsConstants");
        String TOKEN_KEY = (String)narmsConstants.getCONSTANTS().get("TOKEN_KEY");
        
        logger.debug("TOKEN_KEY from spring conf := " + TOKEN_KEY);
        
        String userSessionToken = request.getHeader(TOKEN_KEY);
        
        RestResponseDTO result = new RestResponseDTO();
        
        try {
        	
        	NarmsUser currentUser = narmsUserDAO.getActiveUserBySessionToken(userSessionToken);
        	
        	if(currentUser == null) {
        		result.setError("Invalid User Session Token.");
        		return result;
        	}
        	
        	String storeName = storeInput.getStoreName();
        	String storeAddress = storeInput.getStoreAddress();
        	
        	if(StringUtils.isEmpty(storeName) || StringUtils.isEmpty(storeAddress)
        			|| StringUtils.isEmpty(storeName.trim()) || StringUtils.isEmpty(storeAddress.trim())) {
        		result.setError("Store Name and Store Address is required.");
        		return result;
        	}
        	
        	storeName = storeName.trim();
        	storeAddress = storeAddress.trim();
        	NarmsCenter userCenter = currentUser.getNarmsCenter();
        	
        	if(userCenter == null) {
        		result.setError("User does not have any center.");
        		return result;
        	}
        	 
        	NarmsStore store = narmsStoreDAO.findStoreByNameAndAddressAndCenter(
        			storeInput.getStoreName(), storeInput.getStoreAddress(), currentUser.getNarmsCenter());
        	
        	//If store doesn't exist, create it.
        	if(store == null) {
        		logger.debug("store does not exist. Creating new one.");
        		
        		store = new NarmsStore();
        		store.setStoreName(storeName);
        		store.setStoreAddress(storeAddress);
        		narmsStoreDAO.persist(store);
        	}
        	store.setNarmsCenter(currentUser.getNarmsCenter());
        	NarmsCenter center = store.getNarmsCenter();
        	if(center == null) {
        		result.setError("Store doesn't have any center.");
        		return result;
        	}
        	
        	List<CenterMeatTypeLimit> limits = center.getCenterMeatTypelimit();
        	
        	if(CollectionUtils.isEmpty(limits)) {
        		result.setError("Center doesn't have any limits defined.");
        		return result;
        	}
        	
        	if(CollectionUtils.isEmpty(storeInput.getSamples())) {
        		result.setError("No Samples to add.");
        		return result;
        	}
        	
        	List<NarmsSample> samples = new ArrayList<NarmsSample>(storeInput.getSamples().size());
        	
        	for (SampleDTO sampleDTO : storeInput.getSamples()) {
        	
                        int sampleMeatNameValue = Integer.parseInt(sampleDTO.getMeat());
                        SampleConstants constants = (SampleConstants)AppContextListener.getSpringContext().getBean("sampleConstants");
                        String meatName = (String)constants.getMeatName().get(sampleMeatNameValue);
                        //meatType = MeatType.findByValue(meatTypeValue);
                        logger.debug("meatName from sample := " + meatName);

                        CenterMeatTypeLimit limit = null;

                        for (CenterMeatTypeLimit centerMeatTypeLimit : limits) {
                                if( meatName.equalsIgnoreCase(centerMeatTypeLimit.getType())){
                                        limit = centerMeatTypeLimit;
                                        break;
                                }
                        }
                        
                     logger.debug("limit.getType() := " + limit.getType());
                     String meatNameKey = (String)constants.getMeatNameKey().get(limit.getType());
                     logger.debug("meatNameKey := " + meatNameKey );

                        if( narmsSampleDAO.getSampleCountByMeatNameAndCenterID( meatNameKey , center.getId()) >= limit.getTotalLimit()) {
                                continue;
                        }
            		
                        NarmsSample sample = new NarmsSample(sampleDTO);
                        sample.setNarmsUser(currentUser);
    			sample.setStore(store);
    			narmsSampleDAO.persist(sample);
    			
    			samples.add(sample);
    			
                        /*
                        int index = limits.indexOf(limit);
    			limit.setCountCompleted(limit.getCountCompleted() + 1);
                        limits.set(index, limit);
                        */
		}
        	
                
                
        	// Update Limits
        	center.setCenterMeatTypelimit(limits);
        	narmsCenterDAO.update(center);
	        
	        result.setData(convertToSampleDTO(samples));
	        
	    } catch (Exception e) {
			result.setError("Unknown error occured while adding store samples.");
			logger.error("Unknown error occured while adding store samples.", e);
	}
        
        logger.debug("addStoreSamplesByStoreName(): END");
        return result;
    }
    
    @CrossOrigin(origins = "*")
    @RequestMapping(value="/uploadSampleCapture/{captureType}/{id}", method=RequestMethod.POST)
    @Consumes({"multipart/form-data"})
    public RestResponseDTO uploadSampleCapture(
    		@PathVariable(value="captureType") String captureType,
    		@PathVariable(value="id") Long id,
    		@RequestPart(value="file", required=false) MultipartFile file) {
    	
    	logger.debug("uploadSampleCapture(): START");
    	
    	logger.debug("captureType=" + captureType);
    	logger.debug("id=" + id);
    	
    	RestResponseDTO result = new RestResponseDTO();
    	
    	try {
	    	
	    	NarmsSample sample = narmsSampleDAO.getSampleById(id);
	    	
	    	if(sample == null) {
	    		result.setError("Sample not found.");
	    		return result;
	    	}
	    	
	    	if(FRONT.equals(captureType) || BACK.equals(captureType)) {
	    		
	    		if(file == null) {
	    			result.setError("No files to upload");
	        		return result;
	    		}
	    		
	    		
	    		NarmsSampleImage doc = null;
	    		
	    		if(FRONT.equals(captureType)) { 
	    			doc = sample.getFrontCapture();
	    		} else {
	    			doc = sample.getBackCapture();
	    		}
	    		
	    		if(doc == null) {
	    			doc = new NarmsSampleImage();
	    		}
	    		String originalFileName = file.getOriginalFilename();
				doc.setExtention(FilenameUtils.getExtension(originalFileName));
				doc.setContentType(file.getContentType());
				doc.setName(originalFileName);
				
				if(doc.getId() == null) {
					narmsDocumentDAO.persist(doc);
				}
				
				
				// Store file into file system
				String newFileName = doc.getId() + "." + doc.getExtention();
				
				File destFile = FileUploadUtils.createFileInRepo(null, newFileName);
				
				file.transferTo(destFile);
				
				doc.setFileServerPath("/" + newFileName);
				//narmsDocumentDAO.update(doc);
				
				
				if(FRONT.equals(captureType)) {
					
					sample.setFrontCapture(doc);
				} else {
					sample.setBackCapture(doc);
				}
	    		
	    		narmsSampleDAO.update(sample);
	    		
	    		result.setMessage("OK");
	    		
	    	} else {
	    		result.setError("Invalid capture type. It should be either 'front' or 'back'.");
	    		return result;
	    	}
    	
    	} catch (Exception e) {
    		logger.error("Error while uploading capture.", e);
    		result.setError(e.getMessage());
    		return result;
    	}
    	
    	logger.debug("uploadSampleCapture(): END");
    	
    	return result;
    }
    
/*    @CrossOrigin(origins = "*")
    @RequestMapping(value="/addStoreSamplesWithUpload", method=RequestMethod.POST, consumes={"multipart/form-data"})
    public RestResponseDTO addStoreSamplesWithUpload(
    		@RequestParam(value="data", required=true) String jsonData,
    		@RequestPart(value="file", required=false) MultipartFile[] files) {
        logger.debug("addStoreSamplesWithUpload(): START");
        
        RestResponseDTO result = new RestResponseDTO();
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
        	StoreDTO storeInput = objectMapper.readValue(jsonData, StoreDTO.class);
        	 
	        String storeName = storeInput.getStoreName();
	        String storeAddress = storeInput.getStoreAddress();
	        List<SampleDTO> inputSamples = storeInput.getSamples();
	        
	        NarmsStore store = narmsStoreDAO.getStoreByName(storeName);
	        
	        if(StringUtils.isEmpty(storeName)) {
	        	result.setError("Store Name is required.");
	        	return result;
	        }
	        
	        // If store not found then create it or update address
	        if(CollectionUtils.isEmpty(inputSamples)) {
	        	result.setError("No Samples to add.");
	        	return result;
	        }
	        
	        
	        Map<String, NarmsDocument> fileMap = new HashMap<String, NarmsDocument>();
	        
	        if(files != null && files.length > 0) {
	        	for (MultipartFile file : files) {
	        		
	        		System.out.println("File: " + file.getOriginalFilename());
	        		NarmsDocument doc = new NarmsDocument();
					doc.setContent(file.getBytes());
					doc.setContentType(file.getContentType());
					doc.setName(file.getOriginalFilename());
					
					fileMap.put(doc.getName(), doc);
				}
	        }
	        
	        
	        List<NarmsSample> samples = new ArrayList<NarmsSample>();
	        
	        if(!CollectionUtils.isEmpty(storeInput.getSamples())) {
        		for (SampleDTO sampleDTO : storeInput.getSamples()) {
        			
        			NarmsSample sample = new NarmsSample(sampleDTO);
        			sample.setFrontCapture(fileMap.get(sampleDTO.getFrontCapture()));
        			sample.setBackCapture(fileMap.get(sampleDTO.getBackCapture()));
					
        			samples.add(sample);
				}
        	}
        	
	        
	        if(store == null){
	        	// If store is not present, add it
	        	store = new NarmsStore(storeName, storeAddress);
	        	store.addSamples(samples);
	        	narmsStoreDAO.persist(store);
	        } else {
	        	store.addSamples(samples);
	        	narmsStoreDAO.update(store);
	        }
	        
	        result.setMessage("OK");
	    } catch (Exception e) {
			result.setError("Unknown error occured while adding store samples.");
			logger.error("Unknown error occured while adding store samples.", e);
		}
        
        logger.debug("addStoreSamplesWithUpload(): END");
        return result;
    }*/
    
}
