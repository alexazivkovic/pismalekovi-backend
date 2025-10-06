package com.example.pisma.web;

import com.example.pisma.domain.Letter;
import com.example.pisma.dto.*;
import com.example.pisma.service.DrugService;
import com.example.pisma.service.FileStorageService;
import com.example.pisma.service.LetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/letters")
@RequiredArgsConstructor
public class AdminLetterController {
	private final LetterService service;
	private final DrugService drugService;
	private final FileStorageService storage;
	
	@GetMapping
	public PageResponse<LetterDto> list(@RequestParam(defaultValue="") String q,
                                     @RequestParam(defaultValue="0") int page,
                                     @RequestParam(defaultValue="10") int size) {
    var p = service.adminSearch(q, PageRequest.of(page, size, Sort.by("createdAt").descending()));

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
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Letter create(@RequestPart("meta") LetterCreateDto meta,
	                     @RequestPart("file") MultipartFile file) throws Exception {
		String pdfPath = storage.savePdf(file);
		return service.create(meta, pdfPath);
	}
	
	@PutMapping(value="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Letter update(@PathVariable Long id,
	                     @RequestPart("meta") LetterUpdateDto meta,
	                     @RequestPart(value="file", required=false) MultipartFile file) throws Exception {
		String newPath = (file!=null)? storage.savePdf(file) : null;
		return service.update(id, meta, newPath);
	}
	
	@PostMapping("/{id}/deactivate")
	public void deactivate(@PathVariable Long id) {
		service.deactivate(id);
	}
}