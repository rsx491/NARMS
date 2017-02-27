/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aurotech.utils;

import com.aurotech.constants.SampleConstants;
import com.aurotech.controller.rest.NarmsSampleController;
import com.aurotech.init.AppContextListener;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Muazzam
 */
public class SampleLogUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(SampleLogUtils.class);    
    
    public static String getSampleLogID(String meatName, String meatType, String centerState ,int nextCount){
        
        SampleConstants constants = (SampleConstants)AppContextListener.getSpringContext().getBean("sampleConstants");
        String meatNameShortAbbr = (String)constants.getMeatNameShort().get(meatName);
        //meatType = MeatType.findByValue(meatTypeValue);
        logger.debug("meatNameShortAbbr := " + meatNameShortAbbr);        

        String meatTypeShortAbbr = (String)constants.getMeatTypeShort().get(meatType);
        //meatType = MeatType.findByValue(meatTypeValue);
        logger.debug("meatTypeShortAbbr := " + meatTypeShortAbbr); 
        
        String stateCode = (String)constants.getStateCode().get(centerState);
        //meatType = MeatType.findByValue(meatTypeValue);
        logger.debug("meatTypeShortAbbr := " + meatTypeShortAbbr);         
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( ""+( LocalDate.now().getYear() % 100 ) )
                .append(stateCode)
                .append( ( (""+LocalDate.now().getMonthValue()).length() < 2 ? "0"+LocalDate.now().getMonthValue() : ""+LocalDate.now().getMonthValue()  ) )
                .append(meatNameShortAbbr)
                .append( meatTypeShortAbbr)
                .append( (""+nextCount).length() < 2 ? "0"+nextCount : ""+nextCount );
                
        
        return stringBuilder.toString();
    }
    
}
