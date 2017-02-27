package com.aurotech.data.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.aurotech.data.serializer.CustomDateDeserializer;
import com.aurotech.data.serializer.CustomDateSerializer;
import com.aurotech.db.entities.Country;
import com.aurotech.db.entities.NarmsSample;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * @author jjvirani
 */
@JsonInclude(Include.NON_NULL)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class ShortSampleDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    private String meat;
    
    private String type;
    
    private String organic;
    
    private String sampleid;
      
    @JsonSerialize(using=CustomDateSerializer.class)
    @JsonDeserialize(using=CustomDateDeserializer.class)
    private Date datePurchase;
    
  
    public ShortSampleDTO() {
        super();
    }
    
    public ShortSampleDTO(NarmsSample sample) {
    	
    	if(sample != null) {
            this.id = sample.getId();
            this.meat = sample.getMeat();
            this.type = sample.getType();
            this.organic = sample.getOrganic(); 	
            this.datePurchase = sample.getDatePurchase();
            this.sampleid = sample.getSampleid();
    	}
    }
    
	
    public String getMeat() {
            return meat;
    }

    public void setMeat(String meat) {
            this.meat = meat;
    }

    public String getType() {
            return type;
    }

    public void setType(String type) {
            this.type = type;
    }

    public String getOrganic() {
            return organic;
    }

    public void setOrganic(String organic) {
            this.organic = organic;
    }


    public Date getDatePurchase() {
            return datePurchase;
    }

    public void setDatePurchase(Date datePurchase) {
            this.datePurchase = datePurchase;
    }

    public String getSampleid() {
        return sampleid;
    }

    public void setSampleid(String sampleid) {
        this.sampleid = sampleid;
    }
        
    public Long getId() {
            return id;
    }

    public void setId(Long id) {
            this.id = id;
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
        if (!(object instanceof ShortSampleDTO)) {
            return false;
        }
        ShortSampleDTO other = (ShortSampleDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
            return "SampleDTO [id=" + id + ", meat=" + meat
                            + ", type=" + type + ", organic=" + organic
                            + ", datePurchase=" + datePurchase
                            + "]";
    }

    
}
