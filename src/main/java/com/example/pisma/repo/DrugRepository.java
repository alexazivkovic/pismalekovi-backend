package com.example.pisma.repo;

import com.example.pisma.domain.Drug;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface DrugRepository extends JpaRepository<Drug, Long> {
	boolean existsByName(String name);
	Page<Drug> findByNameContainingIgnoreCase(String q, Pageable p);
	Drug getDrugsById(Long id);
}
