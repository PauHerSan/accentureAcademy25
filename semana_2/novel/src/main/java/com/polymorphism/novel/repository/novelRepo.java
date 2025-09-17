package com.polymorphism.novel.repository;

import com.polymorphism.novel.model.publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface novelRepo extends JpaRepository<publication,Long> {
    List<publication> findByTitle(String title);

    List<publication> findByTitleContainingIgnoreCase(String title);

    List<publication> findByWriterContainingIgnoreCase(String writer);

    void deleteByTitle(String title);
}
