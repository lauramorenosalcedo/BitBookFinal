package com.example.bitbookfinal.model;

import com.fasterxml.jackson.annotation.JsonView;

public class Review {
    public interface Basic{}
   @JsonView(Book.Basic.class)
    private Long id;
    @JsonView(Book.Basic.class)
    private String name;
    @JsonView(Book.Basic.class)
    private String description;
    public Review(){

    }
    public Review( String name, String description){ //This is the category entity, its atributes are a name(author name) and a description.
        super();
        this.name=name;
        this.description=description;
    }

    //Getters and setters of this entity.
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