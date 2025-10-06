package com.example.pisma.service;

import com.example.pisma.domain.SubscriberEmail;
import com.example.pisma.repo.SubscriberEmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class NotificationService {
	private final JavaMailSender mailSender;
	private final SubscriberEmailRepository subsRepo;
	
	@Value("${app.mail.from}") String from;
	
	public void notifyAll(String subject, String text) {
		for (SubscriberEmail s : subsRepo.findByActiveTrue()) {
			SimpleMailMessage m = new SimpleMailMessage();
			m.setFrom(from);
			m.setTo(s.getEmail());
			m.setSubject(subject);
			m.setText(text);
			try {
				mailSender.send(m);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
