package com.example.pisma.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {
	private final Path root;
	
	public FileStorageService(@Value("${app.storage.root}") String rootDir) throws IOException {
		this.root = Paths.get(rootDir).toAbsolutePath().normalize();
		Files.createDirectories(this.root);
	}
	
	public String savePdf(MultipartFile file) throws IOException {
		String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
		if (ext == null || !ext.equalsIgnoreCase("pdf")) {
			throw new IllegalArgumentException("Samo PDF fajlovi su dozvoljeni.");
		}
		String name = UUID.randomUUID() + ".pdf";
		Path target = root.resolve(name);
		try (InputStream in = file.getInputStream()) {
			Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
		}
		// mapiramo kroz /files/{name}
		return "/files/" + name;
	}
	
	public Path resolveLocalPath(String publicPath) {
		String fname = Paths.get(publicPath).getFileName().toString();
		return root.resolve(fname);
	}
}
