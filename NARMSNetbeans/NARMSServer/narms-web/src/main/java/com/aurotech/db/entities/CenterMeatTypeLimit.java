/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aurotech.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Muazzam
 */
//@TODO change this to CenterMeatNameLimit
@Entity
public class CenterMeatTypeLimit implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "narms_center_id")
    @JsonIgnore
    private NarmsCenter narmsCenter;    
    
    //@TODO check it later for auto
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    //private int countCompleted;
    private int totalLimit;
    private String type;
    //private int monthVal;//1 for Jan

    /*
    public int getMonthVal() {
        return monthVal;
    }

    public void setMonthVal(int monthVal) {
        this.monthVal = monthVal;
    }
    
    public int getCountCompleted() {
        return countCompleted;
    }
    
    public void setCountCompleted(int countCompleted) {
        this.countCompleted = countCompleted;
    }*/    

    public int getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(int totalLimit) {
        this.totalLimit = totalLimit;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
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
        if (!(object instanceof CenterMeatTypeLimit)) {
            return false;
        }
        CenterMeatTypeLimit other = (CenterMeatTypeLimit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.aurotech.db.entities.CenterMeatTypeLimit[ id=" + id + " ]";
    }
    
}
