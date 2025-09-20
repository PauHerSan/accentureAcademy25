package com.polymorphism.novel.repository;

import com.polymorphism.novel.model.publication;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface publicationRepo extends JpaRepository<publication,Long> {

    List<publication> findByTitleIgnoreCase(String title);
    List<publication> findByWriterIgnoreCase(String author);
    // Buscar por título
    List<publication> findByTitle(String title);

    // Buscar por escritor/autor
    List<publication> findByWriter(String writer);

    // Buscar por género (para todos los tipos)
    List<publication> findByGenre(String genre);

    // Query para obtener solo Novels usando discriminator
    @Query("SELECT p FROM publication p WHERE TYPE(p) = novels")
    List<publication> findAllNovels();

    // Query para obtener solo WebToons usando discriminator
    @Query("SELECT p FROM publication p WHERE TYPE(p) = webToon")
    List<publication> findAllWebToons();

    // Query para obtener novels por género específicamente
    @Query("SELECT p FROM publication p WHERE TYPE(p) = novels AND p.genre = :genre")
    List<publication> findNovelsByGenre(@Param("genre") String genre);

    // Eliminar por título
    @Transactional
    void deleteByTitle(String title);

    // Verificar si existe por título (útil para validaciones)
    boolean existsByTitle(String title);

}
