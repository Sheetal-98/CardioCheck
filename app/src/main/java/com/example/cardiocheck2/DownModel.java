package com.example.cardiocheck2;

public class DownModel {

    String Name, Link;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public DownModel(String name, String link){
        this.Link=link;
        this.Name=name;

    }
}
