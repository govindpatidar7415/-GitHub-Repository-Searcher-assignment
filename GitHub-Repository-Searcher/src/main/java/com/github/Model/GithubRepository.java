package com.github.Model;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GithubRepository {
    @Id
    private Long id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String owner;
    private String language;
    private Integer stars;
    private Integer forks;
    private ZonedDateTime lastUpdated;
}
