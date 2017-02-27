/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aurotech.utils;

import com.aurotech.constants.NarmsConstants;
import com.aurotech.init.AppContextListener;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author Muazzam
 */
public class DateUtils {
    
    	public static Date getFormatedDate(String dateText) throws ParseException{
		
            NarmsConstants narmsConstants = (NarmsConstants)AppContextListener.getSpringContext().getBean("narmsConstants");
            String DATE_FORMAT = (String)narmsConstants.getCONSTANTS().get("DATE_FORMAT");  
            
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            String date = dateText;
            return format.parse(date);

	}
        
        
        public static Date getCurrentMonthStartDate(){
        
            YearMonth yearMonth = YearMonth.of( LocalDate.now().getYear(), LocalDate.now().getMonth()  );  // eg January of 2015.
            LocalDate firstOfMonth = yearMonth.atDay( 1 );
            LocalDate lastOfMonth = yearMonth.atEndOfMonth();


            Date fromDate = Date.from(firstOfMonth.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            //Date toDate = Date.from(lastOfMonth.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            
            return fromDate;

        }
        
        
        public static Date getCurrentMonthEndDate(){
        
            YearMonth yearMonth = YearMonth.of( LocalDate.now().getYear(), LocalDate.now().getMonth()  );  // eg January of 2015.
            LocalDate firstOfMonth = yearMonth.atDay( 1 );
            LocalDate lastOfMonth = yearMonth.atEndOfMonth();


            //Date fromDate = Date.from(firstOfMonth.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            Date toDate = Date.from(lastOfMonth.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            
            return toDate;

        }
    
}
