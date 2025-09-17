package com.polymorphism.novel.model;

public class webToon extends publication{

    private String artist; // Atributo específico de Webtoon
    private boolean isFullColor; // Atributo específico de Webtoon

    public webToon(String title, String writer, String publisher, int yearOfRelease, String genre ,String artist, boolean isFullColor) {
        super(title, writer, publisher, yearOfRelease, genre);
        this.artist = artist;
        this.isFullColor = isFullColor;
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
