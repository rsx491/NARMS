package com.aurotech.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author jjvirani
 */
@Table(name="country")
@Entity
@NamedQueries({
    @NamedQuery(name = "findAllCountries",query = "SELECT C FROM Country C"),
    @NamedQuery(name = "findCountryByCode",query = "SELECT C FROM Country C WHERE C.countryCode = :countryCode"),
    @NamedQuery(name = "findCountryByName",query = "SELECT C FROM Country C WHERE C.countryName = :countryName")
})

@JsonInclude(Include.NON_NULL)
public class Country implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="sample_id", nullable=false)
    @JsonIgnore
    private NarmsSample sample;    
    
    @Column(name="country_code", nullable=false)
    private String countryCode;
    
    @Column(name="country_name", nullable=false)
    private String countryName;
    
    
    public Country(){}
    
    public Country(String countryCode, String countryName){
    	this.countryCode = countryCode;
    	this.countryName = countryName;
    }
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

    public NarmsSample getSample() {
        return sample;
    }

    public void setSample(NarmsSample sample) {
        this.sample = sample;
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
		Country other = (Country) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
    public String toString() {
        return "com.aurotech.db.entities.Country[ id=" + id + " ]";
    }
    
    
    
}
