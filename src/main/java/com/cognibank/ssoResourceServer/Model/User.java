package com.cognibank.ssoResourceServer.Model;

public class User {

    private String userName;

    private String password;

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

    public User withUserName (final String userName) {
        setUserName (userName);
        return this;
    }

    public User withPassword (final String password) {
        setPassword (password);
        return this;
    }
}
