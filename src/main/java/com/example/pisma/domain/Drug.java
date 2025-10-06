package com.example.pisma.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="drug")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Drug {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String externalId;
	
	@Column(nullable=false, unique=true, length=512)
	private String name;
	
	private String atc;
	private String manufacturer;
}
