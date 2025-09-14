package com.polymorphism.novel.model;

public class novels extends publication {

    private String editor;
    private int chapters;

    public novels(String title, String writer, String publisher, int yearOfRelease, String genre , String editor, int chapters) {
        super(title, writer, publisher, yearOfRelease, genre);
        this.editor = editor;
        this.chapters = chapters;
    }

    @Override
    public void printDetails() {
        System.out.println("Tipo: Novela Web");
        System.out.println("Título: " + this.title);
        System.out.println("Escritor: " + this.writer);
        System.out.println("Editor: " + this.editor);
        System.out.println("Casa de publicación: " + this.publisher);
        System.out.println("Año de lanzamiento: " + this.yearOfRelease);
        System.out.println("Número de capítulos: " + this.chapters);
        System.out.println("-------------------------");
    }

}
