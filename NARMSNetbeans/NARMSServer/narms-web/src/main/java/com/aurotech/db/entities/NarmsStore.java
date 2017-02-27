package com.aurotech.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author jjvirani
 */
@Table(name="narms_store")
@Entity
@NamedQueries({
    @NamedQuery(name = "findStoreByNameAndAddressAndCenter",
    		query = "SELECT S FROM NarmsStore S WHERE LOWER(S.storeName)=LOWER(:storeName) AND LOWER(S.storeAddress)=LOWER(:storeAddress) AND S.narmsCenter=:narmsCenter")
})
@JsonInclude(Include.NON_NULL)
public class NarmsStore implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name="store_name", nullable=false)
    private String storeName;
    
    
    @Column(name="store_address", nullable=true)
    private String storeAddress;
    
    @ManyToOne(cascade=CascadeType.REMOVE)    
    @JoinColumn(name = "narms_center_id")
    @JsonIgnore
    private NarmsCenter narmsCenter;
    
    
    
    @OneToMany(mappedBy="store", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JsonIgnore // Don't send samples with stores
    private List<NarmsSample> samples;
    
    public Long getId() {
		return id;
	}

    public NarmsCenter getNarmsCenter() {
        return narmsCenter;
    }

    public void setNarmsCenter(NarmsCenter narmsCenter) {
        this.narmsCenter = narmsCenter;
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

	public List<NarmsSample> getSamples() {
		return samples;
	}
	
	public void setSamples(List<NarmsSample> samples) {
		this.samples = samples;
	}
	
	public void addSamples(List<NarmsSample> samples) {
		if(CollectionUtils.isEmpty(this.samples)) {
			this.samples = new ArrayList<NarmsSample>();
		}
		
		if(!CollectionUtils.isEmpty(samples)) {
			for (NarmsSample sample : samples) {
				sample.setStore(this);
				this.samples.add(sample);
			}
		}
		
		this.samples.addAll(samples);
	}
	
	public void addSample(NarmsSample sample) {
		if(sample == null) {
			return;
		}
		
		if(CollectionUtils.isEmpty(this.samples)) {
			this.samples = new ArrayList<NarmsSample>();
		}
		
		sample.setStore(this);
		
		this.samples.add(sample);
	}
	
	public NarmsStore() {
		super();
	}
	
	public NarmsStore(String storeName,String storeAddress){
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
        if (!(object instanceof NarmsStore)) {
            return false;
        }
        NarmsStore other = (NarmsStore) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.aurotech.db.entities.NarmsStore[ id=" + id + " ]";
    }
    
}
