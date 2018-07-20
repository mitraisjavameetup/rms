package com.mitrais.rms.model;

/**
 *
 * @author Febri_MW251@mitrais.com
 */
public class Books {

    private Integer book_id;
    private String title;
    private String author;
    private Float price;

    public Books() {
    }

    public Books(Integer book_id) {
        this.book_id = book_id;
    }

    public Books(Integer book_id, String title, String author, Float price) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public Integer getBookId() {
        return book_id;
    }

    public void setBookId(Integer book_id) {
        this.book_id = book_id;
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
