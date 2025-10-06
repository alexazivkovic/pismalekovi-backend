package com.example.pisma.repo;

import com.example.pisma.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface LetterDrugRepository extends JpaRepository<LetterDrug, LetterDrugId> {
	Set<LetterDrug> findByLetter_Id(Long letterId);
}
