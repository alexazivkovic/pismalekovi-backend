package com.example.pisma.domain;

import lombok.*;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String pdfPath;
    private boolean active;

    @Builder.Default
    @OneToMany(mappedBy = "letter", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LetterDrug> letterDrugs = new HashSet<>();

    private Instant createdAt = Instant.now();
    private Instant updatedAt;
}