package com.example.pisma.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "letter_drug")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(LetterDrugId.class)
public class LetterDrug {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "letter_id")
    private Letter letter;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_id")
    private Drug drug;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LetterDrug that = (LetterDrug) o;

        if (!letter.equals(that.letter)) return false;
        return drug.equals(that.drug);
    }

    @Override
    public int hashCode() {
        int result = letter.hashCode();
        result = 31 * result + drug.hashCode();
        return result;
    }
}