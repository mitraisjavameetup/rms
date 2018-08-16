package com.mitrais.rms.model;

import java.sql.Timestamp;

public class User
{
    private Long id = (long) 0;
    private String userName = "";
    private String password = "";
    private Timestamp lastLogin;
    
    
    public User() {}
    
    
    public User(Long id, String userName, String password) {
        this.setId(id);
        this.setUserName(userName);
        this.setPassword(password);
    }


	public User(Long id, String userName, String password, Timestamp lastLoginStamp) {
        this.setId(id);
        this.setUserName(userName);
        this.setPassword(password);
        this.setLastLogin(lastLoginStamp);
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
		this.id = id;
	}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public Timestamp getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Timestamp lastLoginStamp) {
		this.lastLogin = lastLoginStamp;
	}
}
