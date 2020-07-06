package com.example.passwordsaverb.Models;

public class User {
    private String name;
    private String phone;
    private String password;
    private String hint;

    //constructor
    public User(){}
    public User(String name, String password, String hint) {
        this.name = name;
        this.password = password;
        this.hint = hint;
    }
    public User(String name, String password){
        this.name = name;
        this.password = password;
    }

    //getters and setter
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public String toString() {
        return "Users{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", hint='" + hint + '\'' +
                '}';
    }
}
