package com.webservice.rest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ExceptionResolverImpl implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception exc) {

		String message = null;

		response.setContentType("application/text");
		if (exc instanceof FileSizeLimitExceededException) {
			response.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
			Long maxSizeInBytes = ((FileSizeLimitExceededException) exc)
					.getPermittedSize();
			message = "ERROR: Maximum upload size of " + maxSizeInBytes
					+ " Bytes per attachment exceeded";

		} else {
			message = "ERROR: " + exc.getMessage();
		}

		exc.printStackTrace();
		
		
		try {
			PrintWriter out = response.getWriter();
			out.write(message);
		} catch (IOException e) {
			System.out.println("Error while writing to response.");
			e.printStackTrace();
		}
		
		// for default behavior
		return null;
	}
}
