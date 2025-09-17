package com.polymorphism.novel.repository;

import com.polymorphism.novel.model.publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface novelRepo extends JpaRepository<publication,Long> {
}
