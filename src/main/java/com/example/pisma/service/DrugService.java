package com.example.pisma.service;

import com.example.pisma.domain.Drug;
import com.example.pisma.repo.DrugRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service @RequiredArgsConstructor
public class DrugService {
	private final DrugRepository repo;
	
	public Page<Drug> search(String q, Pageable p) {
		if (q == null || q.isBlank()) return repo.findAll(p);
		return repo.findByNameContainingIgnoreCase(q, p);
	}
	
	public String getNameFromId(Long id){
		if(id == null) return "";
		return repo.getDrugsById(id).getName();
	}
	
	public void syncFromAlims(String url) {
		RestTemplate rt = new RestTemplate();
		ResponseEntity<Map> resp = rt.exchange(url, HttpMethod.GET, null, Map.class);
		Map<String, Object> json = resp.getBody();
		if (json == null) return;
		
		List<Map<String, Object>> lekovi = (List<Map<String, Object>>) json.get("lekovi");
		if (lekovi == null) {
			System.out.println("[ALIMS Sync] JSON ne sadrži 'lekovi' ključ: " + json.keySet());
			return;
		}
		
		for (Map<String, Object> m : lekovi) {
			String name = Objects.toString(m.getOrDefault("nazivLeka", ""), "").trim();
			if (name.isEmpty()) continue;
			
			if (!repo.existsByName(name)) {
				Drug d = Drug.builder()
						.name(name)
						.atc(Objects.toString(m.getOrDefault("atc", ""), ""))
						.manufacturer(Objects.toString(m.getOrDefault("proizvodjac", ""), ""))
						.externalId(Objects.toString(m.getOrDefault("sifraProizvoda", ""), ""))
						.build();
				repo.save(d);
			}
		}
	}
	
}
