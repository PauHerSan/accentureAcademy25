package com.polymorphism.novel.model;

import jakarta.persistence.*;

@Entity
@Table(name = "publications")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "publication_type", discriminatorType = DiscriminatorType.STRING)
public abstract class publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Title")
    protected String title;

    @Column(name = "Writer")
    protected String writer;

    @Column(name = "Publisher")
    protected String publisher;

    @Column(name = "Year of Release")
    protected int yearOfRelease;

    @Column(name = "Genre")
    protected String genre;

    @Column(name = "Status")
    protected String status;

    public publication() {
    }

    public publication(Long id, String title, String writer, String publisher, int yearOfRelease, String genre, String status) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.publisher = publisher;
        this.yearOfRelease = yearOfRelease;
        this.genre = genre;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(int yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public abstract void printDetails();
}
