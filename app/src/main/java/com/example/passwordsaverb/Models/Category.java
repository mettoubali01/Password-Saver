package com.example.passwordsaverb.Models;

public class Category {
    //vars
    private String name;
    private String icon;

   //Constructors
    public Category() {}
    public Category(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    //Getter and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}