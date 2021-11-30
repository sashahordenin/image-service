package com.example.imageservice.repository;

import com.example.imageservice.model.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("FROM Tag t WHERE t.name = ?1")
    Optional<Tag> findByName(String name);
}
