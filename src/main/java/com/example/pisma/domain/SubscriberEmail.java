// domain/SubscriberEmail.java
package com.example.pisma.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="subscriber_email")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SubscriberEmail {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false, unique=true, length=320)
	private String email;
	@Builder.Default
	private Boolean active = true;
	@Builder.Default
	private Instant createdAt = Instant.now();
}
