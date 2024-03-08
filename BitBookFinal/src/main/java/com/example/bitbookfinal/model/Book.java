package com.example.bitbookfinal.model;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;
import java.util.List;

public class Book {
    public interface Basic{};
    public interface Categories{};
    @JsonView(Basic.class)
    private Long id=null;
    @JsonView(Basic.class)
    private String title;
    @JsonView(Basic.class)
    private String author;
    @JsonView(Basic.class)
    private String image;
    @JsonView(Categories.class)
    private List<Category> categories;
    @JsonView(Basic.class)
    private List<Review> reviews;

    public Book(){

    }
    public Book (String title, String author) {
        super();
        this.title = title;
        this.author = author;
        this.categories=new ArrayList<>();
        this.reviews=new ArrayList<>();
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public List <Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category>categories) {
        this.categories = categories;
    }

    public List <Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review>reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "Libro [id=" + id + ", title=" + title + ", author=" + author + ", categories=" + categories +", reviews="+reviews +"]";
    }
    public void addCategory(Category category) {
        categories.add(category);
        //   category.getBooks().add(this);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void deleteCategory(Category category){
        this.categories.remove(category);

    }
}