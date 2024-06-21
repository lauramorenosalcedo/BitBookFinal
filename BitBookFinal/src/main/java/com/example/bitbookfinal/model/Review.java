package com.example.bitbookfinal.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import java.util.Optional;


@Entity
public class Review {
    public interface Basic{}
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
   @JsonView(Book.Basic.class)
    private Long id;
    @JsonView(Book.Basic.class)
    private String name;

    @JsonView(Book.Basic.class)
    private Long bookIdReview;
    @Column(columnDefinition = "TEXT")
    @JsonView(Book.Basic.class)
    private String description;

    @ManyToOne
    private User user;


    public Review(){

    }
    public Review( String name, String description){ //This is the category entity, its atributes are a name(author name) and a description.
        super();
        this.name=name;
        this.description=description;
       // this.book=book;
    }

    //Getters and setters of this entity.
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Long getBookIdReview() {
        return bookIdReview;
    }

    public void setBookIdReview(Long bookId) {
        this.bookIdReview = bookId;
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

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Review [id=" + id + ", name=" + name + ", description=" + description + "]";
    }

}