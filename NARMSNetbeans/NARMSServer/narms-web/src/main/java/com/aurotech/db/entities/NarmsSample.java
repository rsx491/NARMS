package com.aurotech.db.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.util.CollectionUtils;

import com.aurotech.data.dto.SampleDTO;
import com.aurotech.data.serializer.CustomDateDeserializer;
import com.aurotech.data.serializer.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * @author jjvirani
 */
@Table(name="narms_sample")
@Entity
@NamedQueries({
     @NamedQuery(name = "getallsamplesbycenteridanddate",query = 
             "SELECT S FROM NarmsSample S INNER JOIN S.store SR INNER JOIN SR.narmsCenter NRC WHERE NRC.id=:centerid AND S.datePurchase >= :fromDate AND S.datePurchase <= :toDate ORDER BY s.id"),
    @NamedQuery(name = "getsamplecountbycenterandmeatname",query = 
            "SELECT count( S )  FROM NarmsSample S INNER JOIN S.store R INNER JOIN R.narmsCenter C "+
            "WHERE \n" +
            "R.id=S.store.id \n"+
            "and \n"+
            "R.narmsCenter.id=C.id \n"+
            "and \n"+
            "S.meat=:meatNameKeyCode \n" + 
            "and \n" + 
            "C.id=:centerid"),
    
    @NamedQuery(name = "getsamplecountbycenterandmeatnamebydate",query = 
            "SELECT count( S )  FROM NarmsSample S INNER JOIN S.store R INNER JOIN R.narmsCenter C "+
            "WHERE \n" +
            "R.id=S.store.id \n"+
            "and \n"+
            "R.narmsCenter.id=C.id \n"+
            "and \n"+
            "S.meat=:meatNameKeyCode \n" + 
            "and \n" + 
            "C.id=:centerid \n" + 
            "and \n" + 
            "S.datePurchase >=:fromDate \n"+
            "and \n" + 
            "S.datePurchase <=:toDate \n"),    
        
    @NamedQuery(name = "getsamplecountbycenterid",query = 
            "SELECT count(S) FROM NarmsSample S INNER JOIN S.store R INNER JOIN R.narmsCenter C "+
            "WHERE \n" +
            "R.id=S.store.id \n"+
            "and \n"+
            "R.narmsCenter.id=C.id \n"+
            "and \n" + 
            "C.id=:centerid")         
})
public class NarmsSample implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="store_id")
    @JsonIgnore
    private NarmsStore store;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="narms_user_id")
    @JsonIgnore
    private NarmsUser narmsUser;    
    
    @Column(name="meat", nullable=true)
    private String meat;
    
    @Column(name="type", nullable=true)
    private String type;
    
    @Column(name="organic", nullable=true)
    private String organic;
    
    @Column(name="packed_in_store", nullable=true)
    private String packedInStore;
    
    @Column(name="brand_code", nullable=true)
    private String brandCode;
    
    @Column(name="bar_code", nullable=true)
    private String barCode;
    
    @OneToMany(mappedBy="sample", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)   
    private List<Country> countries;
    
    @Column(name="note", nullable=true)
    private String note;
    
    @Column(name="sample_id", nullable=true)
    private String sampleid;
    
    @Column(name="genus", nullable=true)
    private String genus;    
    
    @Column(name="genus_name", nullable=true)
    private String genusName;
    
    @Column(name="ACCESSION_NUMBER", nullable=true)
    private String accessionNumber;    
    
    @Column(name="growth", nullable=true)
    private String growth;  
    
    @Column(name="SPECIES", nullable=true)
    private String species; 

    @Column(name="SEROTYPE", nullable=true)
    private String serotype; 
    
    @Column(name="ANTIGENIC_FORMULA", nullable=true)
    private String antigenicFormula; 
    
    @Column(name="ISOLATE_ID", nullable=true)
    private String isolateId;  
    
    @Column(name="SOURCE", nullable=true)
    private String source;
    
    @Column(name="SOURCE_SPECIES_INFO", nullable=true)
    private String sourceSpeciesInfo;       
    
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name="front_capture_id")
    private NarmsSampleImage frontCapture;
    
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name="back_capture_id")
    private NarmsSampleImage backCapture;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_purchase", nullable=true)
    @JsonSerialize(using=CustomDateSerializer.class)
    @JsonDeserialize(using=CustomDateDeserializer.class)
    private Date datePurchase;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_sale_by", nullable=true)
    @JsonSerialize(using=CustomDateSerializer.class)
    @JsonDeserialize(using=CustomDateDeserializer.class)
    private Date dateSaleBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_process", nullable=true)
    @JsonSerialize(using=CustomDateSerializer.class)
    @JsonDeserialize(using=CustomDateDeserializer.class)
    private Date dateProcess;
    
    public NarmsSample() {
		super();
    }
    
    public NarmsSample(SampleDTO sampleDTO) {
    	
    	if(sampleDTO != null) {
    		this.id = sampleDTO.getId();
	    	this.meat = sampleDTO.getMeat();
	    	this.type = sampleDTO.getType();
	    	this.organic = sampleDTO.getOrganic();
	    	this.packedInStore = sampleDTO.getPackedInStore();
	    	this.brandCode = sampleDTO.getBrandCode();
	    	this.barCode = sampleDTO.getBarCode();
            this.note = sampleDTO.getNote();
            this.datePurchase = sampleDTO.getDatePurchase();
            this.dateSaleBy = sampleDTO.getDateSaleBy();
            this.dateProcess = sampleDTO.getDateSaleBy();
            this.genus = sampleDTO.getGenus();
            this.genusName = sampleDTO.getGenusName();
            this.accessionNumber = sampleDTO.getAccessionNumber();
            this.growth = sampleDTO.getGrowth();
            this.species = sampleDTO.getSpecies();
            this.serotype = sampleDTO.getSerotype();
            this.antigenicFormula = sampleDTO.getAntigenicFormula();
            this.isolateId = sampleDTO.getIsolateId();
            this.source = sampleDTO.getSource();
            this.sourceSpeciesInfo = sampleDTO.getSourceSpeciesInfo();
    	}
    }

    public NarmsUser getNarmsUser() {
        return narmsUser;
    }

    public void setNarmsUser(NarmsUser narmsUser) {
        this.narmsUser = narmsUser;
    }

    public String getSampleid() {
        return sampleid;
    }

    public void setSampleid(String sampleid) {
        this.sampleid = sampleid;
    }
    
    public NarmsStore getStore() {
            return store;
    }

    public void setStore(NarmsStore store) {
            this.store = store;
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

    public String getPackedInStore() {
            return packedInStore;
    }

    public void setPackedInStore(String packedInStore) {
            this.packedInStore = packedInStore;
    }

    public String getBrandCode() {
            return brandCode;
    }

    public void setBrandCode(String brandCode) {
            this.brandCode = brandCode;
    }

    public String getBarCode() {
            return barCode;
    }

    public void setBarCode(String barCode) {
            this.barCode = barCode;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
    	if(!CollectionUtils.isEmpty(countries)) {
    		for (Country country : countries) {
				country.setSample(this);
			}
    	}
        this.countries = countries;
    }
    
    public String getNote() {
            return note;
    }

    public void setNote(String note) {
            this.note = note;
    }

    public Date getDatePurchase() {
            return datePurchase;
    }

    public void setDatePurchase(Date datePurchase) {
            this.datePurchase = datePurchase;
    }

    public Date getDateSaleBy() {
            return dateSaleBy;
    }

    public void setDateSaleBy(Date dateSaleBy) {
            this.dateSaleBy = dateSaleBy;
    }

    public Date getDateProcess() {
            return dateProcess;
    }

    public void setDateProcess(Date dateProcess) {
            this.dateProcess = dateProcess;
    }

    public Long getId() {
            return id;
    }

    public void setId(Long id) {
            this.id = id;
    }

    public NarmsSampleImage getFrontCapture() {
            return frontCapture;
    }

    public void setFrontCapture(NarmsSampleImage frontCapture) {
            this.frontCapture = frontCapture;
    }

    public NarmsSampleImage getBackCapture() {
            return backCapture;
    }

    public void setBackCapture(NarmsSampleImage backCapture) {
            this.backCapture = backCapture;
    }

    public String getGenus() {
		return genus;
	}

	public void setGenus(String genus) {
		this.genus = genus;
	}

	public String getGenusName() {
		return genusName;
	}

	public void setGenusName(String genusName) {
		this.genusName = genusName;
	}

	public String getAccessionNumber() {
		return accessionNumber;
	}

	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}

	public String getGrowth() {
		return growth;
	}

	public void setGrowth(String growth) {
		this.growth = growth;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getSerotype() {
		return serotype;
	}

	public void setSerotype(String serotype) {
		this.serotype = serotype;
	}

	public String getAntigenicFormula() {
		return antigenicFormula;
	}

	public void setAntigenicFormula(String antigenicFormula) {
		this.antigenicFormula = antigenicFormula;
	}

	public String getIsolateId() {
		return isolateId;
	}

	public void setIsolateId(String isolateId) {
		this.isolateId = isolateId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceSpeciesInfo() {
		return sourceSpeciesInfo;
	}

	public void setSourceSpeciesInfo(String sourceSpeciesInfo) {
		this.sourceSpeciesInfo = sourceSpeciesInfo;
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
        if (!(object instanceof NarmsSample)) {
            return false;
        }
        NarmsSample other = (NarmsSample) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.aurotech.db.entities.NarmsSample[ id=" + id + " ]";
    }
    
}
