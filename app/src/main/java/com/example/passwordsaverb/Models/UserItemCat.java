package com.example.passwordsaverb.Models;

import com.example.passwordsaverb.Common.Common;
import java.util.ArrayList;

public class UserItemCat {
    private String name;
    private Item items;

    public UserItemCat() {}

    public UserItemCat(String name, Item item) {
        this.name = name;
        this.items = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList getItems() {
        return Common.items;
    }

    public void setItems(Item items) {
        this.items = items;
    }

    public void addItems(Item item) {

        Common.items.add(item);
    }
}
