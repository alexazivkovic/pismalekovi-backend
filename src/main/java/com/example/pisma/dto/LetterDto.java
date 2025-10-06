package com.example.pisma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LetterDto {
    private Long id;
    private String title;
    private String pdfPath;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private List<DrugDto> drugs;
}