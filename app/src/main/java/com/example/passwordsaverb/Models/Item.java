package com.example.passwordsaverb.Models;

public class Item {
    //vars
    public String itemTitle, itemUsername, itemPwd, itemNote, itemUrl;

    //Constructors
    public Item() {}

    public Item(String itemTitle, String itemUsername, String itemPwd, String itemNote, String itemUrl) {
        this.itemTitle = itemTitle;
        this.itemUsername = itemUsername;
        this.itemPwd = itemPwd;
        this.itemNote = itemNote;
        this.itemUrl = itemUrl;
    }

    //Getters and setters
    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemUsername() {
        return itemUsername;
    }

    public void setItemUsername(String itemUsername) {
        this.itemUsername = itemUsername;
    }

    public String getItemPwd() {
        return itemPwd;
    }

    public void setItemPwd(String itemPwd) {
        this.itemPwd = itemPwd;
    }

    public String getItemNote() {
        return itemNote;
    }

    public void setItemNote(String itemNote) {
        this.itemNote = itemNote;
    }

    public String getItemUrl() {
        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemTitle='" + itemTitle + '\'' +
                ", itemUsername='" + itemUsername + '\'' +
                ", itemPwd='" + itemPwd + '\'' +
                ", itemNote='" + itemNote + '\'' +
                ", itemUrl='" + itemUrl + '\'' +
                '}';
    }
}
