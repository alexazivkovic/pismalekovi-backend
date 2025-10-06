package com.example.pisma.dto;

import lombok.Data;
import java.util.List;

@Data
public class LetterUpdateDto {
	private String title;
	private List<Long> drugIds;
	private Boolean active;
}
