package com.example.summer10;

public class Credentials {
    private String usermail;
    private String password;

    public Credentials(){

    }

    public Credentials(String usermail, String password){
        this.usermail=usermail;
        this.password=password;
    }

    public String getUsermail() {
        return usermail;
    }

    public String getPassword() {
        return password;
    }

    public void setUsermail(String usermail) {
        this.usermail = usermail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
