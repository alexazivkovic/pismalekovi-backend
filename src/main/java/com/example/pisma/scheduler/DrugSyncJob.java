package com.example.pisma.scheduler;

import com.example.pisma.service.DrugService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class DrugSyncJob {
	private final DrugService service;
	
	@Value("${app.alims.url}") String alimsUrl;
	
	// jednom dnevno
	@Scheduled(cron = "0 0 3 * * *", zone = "Europe/Belgrade")
	public void run() {
		try {
			service.syncFromAlims(alimsUrl);
		} catch (Exception e) {
			// log, ali ne prekida aplikaciju
		}
	}
}
