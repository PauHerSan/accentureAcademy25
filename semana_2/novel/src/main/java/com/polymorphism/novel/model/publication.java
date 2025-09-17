package com.polymorphism.novel.model;

public abstract class publication {

    protected String title;
    protected String writer;
    protected String publisher;
    protected int yearOfRelease;
    protected String genre;

    public publication(String title, String writer, String publisher, int yearOfRelease, String genre) {
        this.title = title;
        this.writer = writer;
        this.publisher = publisher;
        this.yearOfRelease = yearOfRelease;
        this.genre = genre;
    }

    public static void add(novels novel) {
    }

    public static void add(webToon webtoon1) {
    }

    public abstract void printDetails();
}
