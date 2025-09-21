package com.polymorphism.novel.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Novel")
public class novels extends publication {

    @Column(name = "Editor")
    private String editor;

    @Column(name = "Chapters")
    private int chapters;

    public novels() {}

    public novels(Long id, String title, String writer, String publisher, int yearOfRelease, String genre, String status , String editor, int chapters) {
        super(id, title, writer, publisher, yearOfRelease, genre, status);
        this.editor = editor;
        this.chapters = chapters;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public int getChapters() {
        return chapters;
    }

    public void setChapters(int chapters) {
        this.chapters = chapters;
    }



}
