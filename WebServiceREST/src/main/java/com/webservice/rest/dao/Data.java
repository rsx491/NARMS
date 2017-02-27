package com.webservice.rest.dao;

public class Data {

	private String name;
	private String address;
	private String age;
	private String fileName;
	private int fileSize;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Data [");
		if (name != null)
			builder.append("name=").append(name).append(", ");
		if (address != null)
			builder.append("address=").append(address).append(", ");
		if (age != null)
			builder.append("age=").append(age).append(", ");
		if (fileName != null)
			builder.append("fileName=").append(fileName).append(", ");
		if (fileSize > 0)
			builder.append("fileSize=").append(fileSize).append(" Bytes");
		
		builder.append("]");
		
		
		return builder.toString();
	}
	
	
}
