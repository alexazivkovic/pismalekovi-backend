package com.example.pisma.web;

import com.example.pisma.domain.SubscriberEmail;
import com.example.pisma.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/admin/subscribers")
@RequiredArgsConstructor
public class AdminSubscriberController {
	private final SubscriberService service;
	
	@GetMapping public List<SubscriberEmail> list(){ return service.all(); }
	
	@PostMapping public SubscriberEmail add(@RequestParam String email){ return service.add(email); }
	
	@PostMapping("/{id}/deactivate") public void deactivate(@PathVariable Long id){ service.deactivate(id); }
}
