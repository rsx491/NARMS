package com.aurotech.data.dto;

import java.io.Serializable;

import com.aurotech.db.entities.NarmsUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author jjvirani
 */
@JsonInclude(Include.NON_NULL)
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
   
    private Long id;
    
    private String email;
    
    private String firstName;
    
    private String lastName;
    
    
    public UserDTO() {
    	super();
    }
    
    public UserDTO(NarmsUser user) {
    	if(user != null) {
    		this.id = user.getId();
    		this.email = user.getEmail();
    		this.firstName = user.getFirstName();
    		this.lastName = user.getLastName();
    	}
    }
    
    public Long getId() {
		return id;
	}

    public void setId(Long id) {
		this.id = id;
	}

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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
        if (!(object instanceof UserDTO)) {
            return false;
        }
        UserDTO other = (UserDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDTO [id=").append(id).append(", email=")
				.append(email).append(", firstName=").append(firstName)
				.append(", lastName=").append(lastName).append("]");
		return builder.toString();
	}

	
    
	
    
    
}
