package com.example.pisma.dto;

import lombok.*;
import java.util.List;

@Data @AllArgsConstructor
public class PageResponse<T> {
	private List<T> content;
	private long totalElements;
	private int totalPages;
	private int page;
	private int size;
}
