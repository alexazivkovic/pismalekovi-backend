package com.example.pisma.dto;

import lombok.Data;
import java.util.List;

@Data
public class LetterCreateDto {
	private String title;
	private List<Long> drugIds;
}
