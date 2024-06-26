package com.example.bitbookfinal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Book {

    public interface Basic{}
    public interface Categories{}
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Basic.class)
    private Long id=null;
    @JsonView(Basic.class)
    private String title;
    @JsonView(Basic.class)
    private int price;
    @JsonView(Basic.class)
    private String author;
    @JsonView(Basic.class)
    private String image;
    @JsonView(Basic.class)
    private String filename;
    @Lob
    @JsonIgnore
    @Column (nullable = true)
    private Blob imageFile;

    @JsonView(Categories.class)
    @ManyToMany
    private List<Category> categories;



   @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Basic.class)
    private List<Review> reviews;
    //private List<Review> reviews=new ArrayList<>();

    public Book(){

    }
    public Book (String title, String author, int price) { //This is the book entity, its attributes are a tittle, author, a list of the categories it belongs to, and a list of reviews it has.
        super();
        this.title = title;
        this.author = author;
        this.price = price;
        this.categories=new ArrayList<>();
        this.reviews=new ArrayList<>();
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    //Getters and setters of this entity.
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


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

    @Override
    public String toString() {
        return "Libro [id=" + id + ", title=" + title + ", author=" + author + ", categories=" + categories +", reviews="+reviews +  ", price=" + price +"]";
    }


    public void addCategory(Category category) { // Function used to add a category to the list of categories.
        categories.add(category);
        //   category.getBooks().add(this);
    }


    public void deleteCategory(Category category){ //Function used to delete a category from the list of categories.
        this.categories.remove(category);
    }

    public void deleteReview(Review review){
        this.reviews.remove(review);
    }
}