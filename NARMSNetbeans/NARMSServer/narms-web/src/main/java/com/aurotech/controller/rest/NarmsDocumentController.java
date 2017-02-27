package com.aurotech.controller.rest;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aurotech.db.dao.NarmsDocumentDAO;
import com.aurotech.db.entities.NarmsSampleImage;

/**
 * 
 * @author jjvirani
 */
@CrossOrigin
@RestController
@Transactional
public class NarmsDocumentController {

	private static final Logger logger = LoggerFactory
			.getLogger(NarmsDocumentController.class);

	@Autowired
	private NarmsDocumentDAO narmsDocumentDAO;

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/getDocument/{id}", method = RequestMethod.GET)
	public void getDocument(@PathVariable(value = "id") Long id,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("getDocument(): START");

		/*try {
			NarmsDocument document = narmsDocumentDAO.getDocumentById(id);

			if (document != null) {

				response.setContentType(document.getContentType());
				int docSize = document.getContent() != null ? document.getContent().length : 0;
				response.setContentLength(docSize);

				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("filename=\"%s\"", document.getName());
				response.setHeader(headerKey, headerValue);

				// get output stream of the response

				OutputStream outStream = response.getOutputStream();
				outStream.write(document.getContent());
				outStream.close();

			}
		} catch (IOException e) {
			logger.error("Error occured while downloading file.", e);
			e.printStackTrace();
		}*/

		logger.debug("getDocument(): END");
	}

}
