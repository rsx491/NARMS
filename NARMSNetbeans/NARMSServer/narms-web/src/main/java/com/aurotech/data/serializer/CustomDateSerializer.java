/**
 * 
 */
package com.aurotech.data.serializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.aurotech.constants.NarmsConstants;
import com.aurotech.init.AppContextListener;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author jjvirani
 *
 */
@Component
public class CustomDateSerializer extends JsonSerializer<Date> {

	@Override
	public void serialize(Date value, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {
            
                NarmsConstants narmsConstants = (NarmsConstants)AppContextListener.getSpringContext().getBean("narmsConstants");
                String DATE_FORMAT = (String)narmsConstants.getCONSTANTS().get("DATE_FORMAT");
                
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		generator.writeString(formatter.format(value));
		
	}

}
