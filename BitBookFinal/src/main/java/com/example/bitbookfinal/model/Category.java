package com.example.bitbookfinal.model;

import com.example.bitbookfinal.model.Book;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private Long id=null;
    private String name;
    private List<Book> books;

    public Category(){
    }

    public Category( String name){
        super();
        this.name=name;
        this.books=new ArrayList<>();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBook(List<Book> books){
        this.books=books;
    }
    @Override
    public String toString() {
        return "Category [id=" + id + ", name=" + name + ", listbooks=" + books + "]";
    }

}


