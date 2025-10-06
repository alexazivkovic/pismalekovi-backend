package com.example.pisma.service;

import com.example.pisma.domain.SubscriberEmail;
import com.example.pisma.repo.SubscriberEmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class SubscriberService {
	private final SubscriberEmailRepository repo;
	
	public List<SubscriberEmail> all() { return repo.findAll(); }
	
	public SubscriberEmail add(String email) {
		if (repo.existsByEmail(email)) {
			return repo.findAll().stream().filter(s->s.getEmail().equals(email)).findFirst().orElseThrow();
		}
		return repo.save(SubscriberEmail.builder().email(email).build());
	}
	
	public void deactivate(Long id) {
		var s = repo.findById(id).orElseThrow();
		s.setActive(false);
		repo.save(s);
	}
}
