package com.aurotech.data.dto;

import java.io.Serializable;
import java.util.List;

import com.aurotech.db.entities.NarmsStore;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 *
 * @author jjvirani
 */
@JsonInclude(Include.NON_NULL)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class StoreDTO implements Serializable {
    private static final long serialVersionUID = 1L;
   
    private Long id;
    
    private String userID;
    
    private String storeName;
    
    private String storeAddress;

    private List<SampleDTO> samples;
    
    public StoreDTO() {
    	super();
    }
    
    public StoreDTO(NarmsStore store) {
    	if(store != null) {
    		this.id = store.getId();
    		this.storeName = store.getStoreName();
    		this.storeAddress = store.getStoreAddress();
    	}
    }
    
    public Long getId() {
		return id;
	}

    public void setId(Long id) {
		this.id = id;
	}

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    
    
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public List<SampleDTO> getSamples() {
		return samples;
	}
	
	public void setSamples(List<SampleDTO> samples) {
		this.samples = samples;
	}
	
	public StoreDTO(String storeName,String storeAddress){
		super();
        this.storeName = storeName;
        this.storeAddress = storeAddress;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StoreDTO)) {
            return false;
        }
        StoreDTO other = (StoreDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "StoreDTO [id=" + id + ", storeName=" + storeName
				+ ", storeAddress=" + storeAddress
				+ "]";
	}

    
    
}
