package com.example.pisma.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig {
	
	@Value("${app.storage.root}")
	private String rootDir;
	
	@PostConstruct
	public void initStorageFolder() throws IOException {
		Path storagePath = Paths.get(rootDir).toAbsolutePath().normalize();
		if (Files.notExists(storagePath)) {
			Files.createDirectories(storagePath);
			System.out.println("[Storage] Kreiran folder za PDF fajlove: " + storagePath);
		} else {
			System.out.println("[Storage] Folder za PDF fajlove već postoji: " + storagePath);
		}
	}
	
	@Bean
	public WebMvcConfigurer filesHandler(@Value("${app.storage.root}") String root) {
		return new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/files/**")
						.addResourceLocations("file:" + Paths.get(root).toAbsolutePath().normalize().toString() + "/");
			}
		};
	}
}
