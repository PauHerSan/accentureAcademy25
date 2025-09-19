package com.polymorphism.novel.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Webtoon")
public class webToon extends publication{

    @Column(name = "Artist")
    private String artist;

    @Column(name = "Color")
    private boolean isFullColor;

    public webToon() {
    }

    public webToon(Long id, String title, String writer, String publisher, int yearOfRelease, String genre, String status ,String artist, boolean isFullColor) {
        super(id, title, writer, publisher, yearOfRelease, genre, status);
        this.artist = artist;
        this.isFullColor = isFullColor;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public boolean isFullColor() {
        return isFullColor;
    }

    public void setFullColor(boolean fullColor) {
        isFullColor = fullColor;
    }

    @Override
    public void printDetails() {
        System.out.println("Tipo: Webtoon");
        System.out.println("Título: " + this.title);
        System.out.println("Escritor: " + this.writer);
        System.out.println("Artista: " + this.artist);
        System.out.println("Casa de publicación: " + this.publisher);
        System.out.println("Año de lanzamiento: " + this.yearOfRelease);
        System.out.println("A todo color: " + (isFullColor ? "Sí" : "No"));
        System.out.println("-------------------------");
    }
}
