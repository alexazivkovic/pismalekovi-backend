package com.example.pisma.domain;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LetterDrugId implements Serializable {
	private Long letter;
	private Long drug;
}
