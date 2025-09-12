package com.info_anime.anime_fullPrograms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "anime_data")
public class animeModel {

    @Id
    private String id;
    private String titulo;
    private String escritor;
    private String director;
    private int anoLanzamiento;
    private int numeroCapitulos;
    private List<String> genero;

    public animeModel() {
    }

    public animeModel(String id, String titulo, String escritor, String director, int anoLanzamiento, int numeroCapitulos, List<String> genero) {
        this.id = id;
        this.titulo = titulo;
        this.escritor = escritor;
        this.director = director;
        this.anoLanzamiento = anoLanzamiento;
        this.numeroCapitulos = numeroCapitulos;
        this.genero = genero;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEscritor() {
        return escritor;
    }

    public void setEscritor(String escritor) {
        this.escritor = escritor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getAnoLanzamiento() {
        return anoLanzamiento;
    }

    public void setAnoLanzamiento(int anoLanzamiento) {
        this.anoLanzamiento = anoLanzamiento;
    }

    public int getNumeroCapitulos() {
        return numeroCapitulos;
    }

    public void setNumeroCapitulos(int numeroCapitulos) {
        this.numeroCapitulos = numeroCapitulos;
    }

    public List<String> getGenero() {
        return genero;
    }

    public void setGenero(List<String> genero) {
        this.genero = genero;
    }
}

