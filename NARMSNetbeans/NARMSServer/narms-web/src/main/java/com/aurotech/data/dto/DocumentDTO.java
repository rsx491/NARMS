package com.aurotech.data.dto;

import java.io.Serializable;
import java.util.Date;

import com.aurotech.constants.NarmsConstants;
import com.aurotech.data.serializer.CustomDateDeserializer;
import com.aurotech.data.serializer.CustomDateSerializer;
import com.aurotech.db.entities.NarmsSampleImage;
import com.aurotech.init.AppContextListener;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * @author jjvirani
 */
public class DocumentDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String name;
    private String contentType;
    private String extention;
    private String path;
    
    @JsonSerialize(using=CustomDateSerializer.class)
    @JsonDeserialize(using=CustomDateDeserializer.class)
    private Date dateCreated = new Date();
    
    @JsonSerialize(using=CustomDateSerializer.class)
    @JsonDeserialize(using=CustomDateDeserializer.class)
    private Date dateModified = new Date();
    
    public DocumentDTO() {
		super();
	}
    
    public DocumentDTO(NarmsSampleImage document) {
    	
    	if(document != null) {
            
                NarmsConstants narmsConstants = (NarmsConstants)AppContextListener.getSpringContext().getBean("narmsConstants");
                String FILE_PROTOCOL = (String)narmsConstants.getCONSTANTS().get("FILE_PROTOCOL");
                String FILE_HOST = (String)narmsConstants.getCONSTANTS().get("FILE_HOST");
                String REPO_ROOT = (String)narmsConstants.getCONSTANTS().get("REPO_ROOT");
            
    		this.id = document.getId();
    		this.name = document.getName();
    		this.contentType = document.getContentType();
    		this.extention =document.getExtention();
    		this.path = FILE_PROTOCOL + "://" +FILE_HOST 
    				+ REPO_ROOT + document.getFileServerPath();
    	}
    	
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getExtention() {
		return extention;
	}

	public void setExtention(String extention) {
		this.extention = extention;
	}

	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentDTO other = (DocumentDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
    public String toString() {
        return "com.aurotech.db.entities.NarmsSample[ id=" + id + " ]";
    }
    
}
