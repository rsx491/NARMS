package com.aurotech.db.entities;

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

/**
 *
 * @author jjvirani
 */
@Table(name="narms_brand")
@Entity
@NamedQueries({
    @NamedQuery(name = "findAllBrands",query = "SELECT B FROM NarmsBrand B"),
    @NamedQuery(name = "findBrandByCode",query = "SELECT B FROM NarmsBrand B WHERE B.brandCode = :brandCode"),
    @NamedQuery(name = "findBrandByName",query = "SELECT B FROM NarmsBrand B WHERE B.brandName = :brandName")
})

@JsonInclude(Include.NON_NULL)
public class NarmsBrand implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name="brand_code", nullable=false, unique=true)
    private String brandCode;
    
    @Column(name="brand_name", nullable=false)
    private String brandName;  
    
    public NarmsBrand(){}
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
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
		NarmsBrand other = (NarmsBrand) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
    public String toString() {
        return "com.aurotech.db.entities.NarmsBrand[ id=" + id + " ]";
    }
    
}
