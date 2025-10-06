package com.example.pisma.repo;

import com.example.pisma.domain.SubscriberEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriberEmailRepository extends JpaRepository<SubscriberEmail, Long> {
	List<SubscriberEmail> findByActiveTrue();
	boolean existsByEmail(String email);
}
