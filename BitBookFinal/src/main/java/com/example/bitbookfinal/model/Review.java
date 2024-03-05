package com.example.bitbookfinal.model;

import java.util.ArrayList;
import java.util.List;

public class Review {
    private Long id;
    private String name;
    private String description;
     public Review(){

     };
    public Review( String name, String description){
        super();
        this.name=name;
        this.description=description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    @Override
    public String toString() {
        return "Review [id=" + id + ", name=" + name + ", description=" + description + "]";
    }

}
