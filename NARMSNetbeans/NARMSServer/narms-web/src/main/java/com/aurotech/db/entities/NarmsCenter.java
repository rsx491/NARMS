/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aurotech.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Muazzam
 */
@Entity
@NamedQueries({
     @NamedQuery(name = "findCenterByState",query = "SELECT C FROM NarmsCenter C WHERE C.centerState=:centerState"),
     @NamedQuery(name = "findAllCenters",query = "SELECT C FROM NarmsCenter C"),
     @NamedQuery(name = "getcenterbymonthandid",query = "SELECT C FROM NarmsCenter C WHERE C.id=:center_id")
})
public class NarmsCenter implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String centerName;
    private String centerState;
    
    @OneToMany( mappedBy = "narmsCenter", fetch = FetchType.LAZY)
    private List<CenterMeatTypeLimit> centerMeatTypelimit;
    
    @OneToMany(mappedBy = "narmsCenter", fetch = FetchType.LAZY)
    private List<NarmsUser> narmsUsers;
    
    @OneToMany(mappedBy = "narmsCenter", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JsonIgnore
    private List<NarmsStore> narmsStore;

    public List<NarmsStore> getNarmsStore() {
        return narmsStore;
    }

    public void setNarmsStore(List<NarmsStore> narmsStore) {
        this.narmsStore = narmsStore;
    }

    public List<NarmsUser> getNarmsUsers() {
        return narmsUsers;
    }

    public void setNarmsUsers(List<NarmsUser> narmsUsers) {
        this.narmsUsers = narmsUsers;
    }
    
    public String getCenterName() {
        return centerName;
    }

    public String getCenterState() {
        return centerState;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public void setCenterState(String centerState) {
        this.centerState = centerState;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public List<CenterMeatTypeLimit> getCenterMeatTypelimit() {
        return centerMeatTypelimit;
    }    
    
    public void setCenterMeatTypelimit(List<CenterMeatTypeLimit> centerMeatTypelimit) {
        this.centerMeatTypelimit = centerMeatTypelimit;
    }
    
    public void addCenterMeatTypelimits(List<CenterMeatTypeLimit> meatTypeLimits) {
            if(CollectionUtils.isEmpty(this.centerMeatTypelimit)) {
                    this.centerMeatTypelimit = new ArrayList<CenterMeatTypeLimit>();
            }

            this.centerMeatTypelimit.addAll(meatTypeLimits);
    }

    public void addCenterMeatTypelimit(CenterMeatTypeLimit meatTypeLimit) {
            if(meatTypeLimit == null) {
                    return;
            }

            if(CollectionUtils.isEmpty(this.centerMeatTypelimit)) {
                    this.centerMeatTypelimit = new ArrayList<CenterMeatTypeLimit>();
            }

            //sample.setStore(this);

            this.centerMeatTypelimit.add(meatTypeLimit);
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
        if (!(object instanceof NarmsCenter)) {
            return false;
        }
        NarmsCenter other = (NarmsCenter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.aurotech.db.entities.NarmsCenter[ id=" + id + " ]";
    }
    
}
