package com.example.bitbookfinal.model;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;
import java.util.List;

public class Category {
    public interface Basic{}
    public interface Books{}
    @JsonView(Basic.class)
    private Long id=null;
    @JsonView(Basic.class)
    private String name;
    @JsonView(Books.class)
    private List<Book> books=new ArrayList<>();

    public Category(){
    }

    public Category( String name){ //This is the category entity, its atributes are a name and a list of books it has.
        super();
        this.name=name;
        this.books=new ArrayList<>();
    }

    //Getters and setters of this entity.
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
    public void deleteBook(Book book){
        this.books.remove(book);

    }
    @Override
    public String toString() {
        return "Category [id=" + id + ", name=" + name + ", listbooks=" + books + "]";
    }

}



