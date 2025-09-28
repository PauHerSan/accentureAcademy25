package com.info_anime.anime_fullPrograms.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

//Data cubre el Getter y setter
@Data
@AllArgsConstructor
@NoArgsConstructor
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


}

