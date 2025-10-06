package com.example.pisma.web;

import com.example.pisma.domain.Drug;
import com.example.pisma.domain.Letter;
import com.example.pisma.domain.SubscriberEmail;
import com.example.pisma.dto.DrugDto;
import com.example.pisma.dto.LetterDto;
import com.example.pisma.dto.PageResponse;
import com.example.pisma.repo.DrugRepository;
import com.example.pisma.repo.LetterRepository;
import com.example.pisma.service.DrugService;
import com.example.pisma.service.FileStorageService;
import com.example.pisma.service.LetterService;
import com.example.pisma.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController{
	private final LetterService letterService;
	private final LetterRepository letterRepo;
	private final DrugRepository repo;
	private final FileStorageService fileStorage;
	private final SubscriberService subscriberService;
	@Autowired
	private DrugService drugService;
	@Value("${app.alims.url}")
	private String alimsUrl;
	
	@GetMapping("/letters")
	public PageResponse<LetterDto> letters(@RequestParam(defaultValue="") String q,
	                                       @RequestParam(defaultValue="0") int page,
	                                       @RequestParam(defaultValue="10") int size) {
		var p = letterService.publicSearch(q, PageRequest.of(page, size, Sort.by("createdAt").descending()));
		
		List<LetterDto> letterDtos = p.getContent().stream()
				.map(l -> new LetterDto(
						l.getId(),
						l.getTitle(),
						l.getPdfPath(),
						l.isActive(),
						l.getCreatedAt(),
						l.getUpdatedAt(),
						l.getLetterDrugs().stream()
								.map(x -> DrugDto.builder().name(drugService.getNameFromId(x.getDrug().getId())).id(x.getDrug().getId()).build()).toList()))
				.collect(Collectors.toList());
		
		return new PageResponse<>(letterDtos, p.getTotalElements(), p.getTotalPages(), page, size);
	}
	
	@GetMapping("/letters/{id}")
	public Letter one(@PathVariable Long id) { return letterRepo.findById(id).orElseThrow(); }
	
	// Serviranje PDF-a preko /files/{fileName}
	@GetMapping(value = "/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<Resource> streamPdf(@PathVariable Long id) {
		Letter l = letterRepo.findById(id).orElseThrow();
		Path path = fileStorage.resolveLocalPath(l.getPdfPath());
		Resource r = new FileSystemResource(path);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(r);
	}
	
	/*@GetMapping("/admin/manual-sync")
	public String manualSync() {
		drugService.syncFromAlims(alimsUrl);
		return "OK";
	}*/
	
	@GetMapping("/drugs")
	public PageResponse<Drug> drugs(@RequestParam(defaultValue = "") String q,
	                                @RequestParam(defaultValue = "0") int page,
	                                @RequestParam(defaultValue = "10") int size) {
		Page<Drug> p = (q == null || q.isBlank())
				? repo.findAll(PageRequest.of(page, size, Sort.by("name").ascending()))
				: repo.findByNameContainingIgnoreCase(q, PageRequest.of(page, size, Sort.by("name").ascending()));
		return new PageResponse<>(p.getContent(), p.getTotalElements(), p.getTotalPages(), page, size);
	}
	
	@PostMapping("/subscribe")
	public SubscriberEmail add(@RequestParam String email){ return subscriberService.add(email); }
}
