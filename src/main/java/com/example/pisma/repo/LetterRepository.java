package com.example.pisma.repo;

import com.example.pisma.domain.Letter;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LetterRepository extends JpaRepository<Letter, Long> {
	Page<Letter> findByActiveTrueAndTitleContainingIgnoreCase(String q, Pageable p);
	Page<Letter> findByTitleContainingIgnoreCase(String q, Pageable p);
}
