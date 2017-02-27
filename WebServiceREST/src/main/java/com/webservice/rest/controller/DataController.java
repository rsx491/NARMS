package com.webservice.rest.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.webservice.rest.dao.Data;

@RestController
public class DataController {
	
	@RequestMapping(value = "/", method= {RequestMethod.GET,RequestMethod.POST})
	public String home() {
		return "SUCCEESS: App Version= 0.0.1";
	}
	
	@RequestMapping(value = "/api/data/addData", method= RequestMethod.POST, headers="content-type=multipart/form-data")
	public @ResponseBody String addUserMultiPart(@RequestParam(value="name", required=false) String name,
			@RequestParam(value="address", required=false) String address,
			@RequestParam(value="age", required=false) String age,
			@RequestParam(value="fileName", required=false) String fileName,
			@RequestParam(value="file", required=false) MultipartFile file){
		
		int fileSize = 0;
    	if (file != null && !file.isEmpty()) {
            try {
                fileName = fileName==null ? file.getOriginalFilename() : fileName;
                byte[] bytes = file.getBytes();
                fileSize = bytes.length;
            } catch (Exception e) {
               return "ERROR:" + e.getMessage();
            }
        }
		
		Data data = new Data();
		data.setName(name);
		data.setAddress(address);
		data.setAge(age);
		data.setFileName(fileName);
		data.setFileSize(fileSize);
		
		System.out.println("**** Request Received : " + new Date() +" ****");
		System.out.println(data);
		System.out.println("********");
		
		return "SUCCESS: " + data;
	}
	
}
