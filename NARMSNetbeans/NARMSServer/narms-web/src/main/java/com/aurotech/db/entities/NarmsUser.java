/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aurotech.db.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Muazzam
 */
@Entity
@NamedQueries({
     @NamedQuery(name = "findUserBySessionToken",query = "SELECT U FROM NarmsUser U INNER JOIN NarmsSession S WHERE S.clientID=U.id AND S.uniqueID=:tokenid")
})
public class NarmsUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String usertype;

    
    @ManyToOne
    @JoinColumn(name = "narms_center_id")
    @JsonIgnore
    private NarmsCenter narmsCenter;
    
    @OneToMany(mappedBy="narmsUser", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JsonIgnore // Don't send samples with stores
    private List<NarmsSample> narmssamples;

    public List<NarmsSample> getNarmsSamples() {
        return narmssamples;
    }

    public void setNarmsSamples(List<NarmsSample> narmsSamples) {
        this.narmssamples = narmsSamples;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    

    public NarmsCenter getNarmsCenter() {
        return narmsCenter;
    }

    public void setNarmsCenter(NarmsCenter narmsCenter) {
        this.narmsCenter = narmsCenter;
    }
    
    
    
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Boolean getIsAdmin() {
        if(this.usertype=="admin"){
            return true;
        }
        return true;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public NarmsUser(){
    }
    
    public NarmsUser(String email,String password,String firstName,String lastName, String usertype){
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.usertype = usertype;
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
        if (!(object instanceof NarmsUser)) {
            return false;
        }
        NarmsUser other = (NarmsUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.aurotech.db.entities.NarmsUser[ id=" + id + " ]";
    }
    
}
