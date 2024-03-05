package com.example.bitbookfinal.model;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private Long id=null;
    private String title;
    private String author;
    private List<Category> categories;

    public Book(){

    }
    public Book (String title, String author) {
        super();
        this.title = title;
        this.author = author;
        this.categories=new ArrayList<>();
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

    public void setCategories(List<Category>Categorys) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Libro [id=" + id + ", title=" + title + ", author=" + author + ", categories=" + categories + "]";
    }
    public void addCategory(Category category) {
        categories.add(category);
     //   category.getBooks().add(this);
    }

}
