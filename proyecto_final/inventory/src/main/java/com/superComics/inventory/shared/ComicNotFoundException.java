package com.superComics.inventory.shared;

public class ComicNotFoundException extends BusinessException{

    public ComicNotFoundException(String message) {
        super(message);
    }

}
