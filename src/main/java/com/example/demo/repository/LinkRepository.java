package com.example.demo.repository;

import com.example.demo.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> getLinkByUrl(String url);

    @Modifying
    @Transactional
    @Query("DELETE FROM Link e WHERE e.timeDelete < :now")
    void deleteByTimeDeleteBefore(LocalDate now);
}
