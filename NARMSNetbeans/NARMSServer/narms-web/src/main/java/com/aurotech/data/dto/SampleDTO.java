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
public class SampleDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
   // @JsonManagedReference
    private StoreDTO store;
    
    private UserDTO user;
    
    private String meat;
    
    private String type;
    
    private String organic;
    
    private String packedInStore;
    
    private String brandCode;
    
    private String barCode;
    
    private List<String> countries;
    
    private String note;
    
    private String sampleid;
    
    private DocumentDTO frontCapture;
    
    private DocumentDTO backCapture;
    
    @JsonSerialize(using=CustomDateSerializer.class)
    @JsonDeserialize(using=CustomDateDeserializer.class)
    private Date datePurchase;
    
    @JsonSerialize(using=CustomDateSerializer.class)
    @JsonDeserialize(using=CustomDateDeserializer.class)
    private Date dateSaleBy;
    
    @JsonSerialize(using=CustomDateSerializer.class)
    @JsonDeserialize(using=CustomDateDeserializer.class)
    private Date dateProcess;
    
    private String genus;    
    
    private String genusName;
    
    private String accessionNumber;    
    
    private String growth;  
    
    private String species; 

    private String serotype; 
    
    private String antigenicFormula; 
    
    private String isolateId;  
    
    private String source;
    
    private String sourceSpeciesInfo; 
    
    public SampleDTO() {
		super();
	}
    
    public SampleDTO(NarmsSample sample) {
    	
    	if(sample != null) {
	    	this.id = sample.getId();
	    	this.meat = sample.getMeat();
	    	this.type = sample.getType();
	    	this.organic = sample.getOrganic();
	    	this.packedInStore = sample.getPackedInStore();
	    	this.brandCode = sample.getBrandCode();
	    	this.barCode = sample.getBarCode();
            this.note = sample.getNote();
            
            List<Country> countryList = sample.getCountries();
            if(!CollectionUtils.isEmpty(countryList)) {
            	List<String> countries = new ArrayList<String>(countryList.size());
            	
            	for (Country country : countryList) {
            		countries.add(country.getCountryCode());
				}
            	
            	this.countries = countries;
            }
            	
            this.datePurchase = sample.getDatePurchase();
            this.dateSaleBy = sample.getDateSaleBy();
            this.dateProcess = sample.getDateSaleBy();
            this.sampleid = sample.getSampleid();
            this.genus = sample.getGenus();
            this.genusName = sample.getGenusName();
            this.accessionNumber = sample.getAccessionNumber();
            this.growth = sample.getGrowth();
            this.species = sample.getSpecies();
            this.serotype = sample.getSerotype();
            this.antigenicFormula = sample.getAntigenicFormula();
            this.isolateId = sample.getIsolateId();
            this.source = sample.getSource();
            this.sourceSpeciesInfo = sample.getSourceSpeciesInfo();
            
    	}
    }
    
	public StoreDTO getStore() {
		return store;
	}

	public void setStore(StoreDTO store) {
		this.store = store;
	}

	public UserDTO getUser() {
		return user;
	}
	
	public void setUser(UserDTO user) {
		this.user = user;
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

	public List<String> getCountries() {
		return countries;
	}
	
	public void setCountries(List<String> countries) {
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

	public DocumentDTO getFrontCapture() {
		return frontCapture;
	}
	
	public void setFrontCapture(DocumentDTO frontCapture) {
		this.frontCapture = frontCapture;
	}
	
	public DocumentDTO getBackCapture() {
		return backCapture;
	}
	
	public void setBackCapture(DocumentDTO backCapture) {
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
        if (!(object instanceof SampleDTO)) {
            return false;
        }
        SampleDTO other = (SampleDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SampleDTO [id=").append(id).append(", store=")
				.append(store).append(", user=").append(user).append(", meat=")
				.append(meat).append(", type=").append(type)
				.append(", organic=").append(organic)
				.append(", packedInStore=").append(packedInStore)
				.append(", brandCode=").append(brandCode).append(", barCode=")
				.append(barCode).append(", countries=").append(countries)
				.append(", note=").append(note).append(", sampleid=")
				.append(sampleid).append(", frontCapture=")
				.append(frontCapture).append(", backCapture=")
				.append(backCapture).append(", datePurchase=")
				.append(datePurchase).append(", dateSaleBy=")
				.append(dateSaleBy).append(", dateProcess=")
				.append(dateProcess).append(", genus=").append(genus)
				.append(", genusName=").append(genusName)
				.append(", accessionNumber=").append(accessionNumber)
				.append(", growth=").append(growth).append(", species=")
				.append(species).append(", serotype=").append(serotype)
				.append(", antigenicFormula=").append(antigenicFormula)
				.append(", isolateId=").append(isolateId).append(", source=")
				.append(source).append(", sourceSpeciesInfo=")
				.append(sourceSpeciesInfo).append("]");
		return builder.toString();
	}

	

    
}
