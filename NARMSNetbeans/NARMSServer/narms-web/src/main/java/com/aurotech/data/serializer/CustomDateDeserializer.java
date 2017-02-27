/**
 * 
 */
package com.aurotech.data.serializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aurotech.constants.NarmsConstants;
import com.aurotech.init.AppContextListener;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author jjvirani
 *
 */
public class CustomDateDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		
            NarmsConstants narmsConstants = (NarmsConstants)AppContextListener.getSpringContext().getBean("narmsConstants");
            String DATE_FORMAT = (String)narmsConstants.getCONSTANTS().get("DATE_FORMAT");  
            
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            String date = parser.getText();
            try {
                return format.parse(date);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
	}

}
