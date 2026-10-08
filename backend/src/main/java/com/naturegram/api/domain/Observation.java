package com.naturegram.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.OffsetDateTime;

@Entity
@Table(name = "observations")
@Getter @Setter
public class Observation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "taxon_id")
    private Long taxonId;

    @Column(name = "observed_at", nullable = false)
    private OffsetDateTime observedAt;

    @Column(nullable = false, columnDefinition = "geometry(Point,4326)")
    private Point geom;

    @Column(name = "location_accuracy")
    private Double locationAccuracy;

    @Column(name = "place_guess")
    private String placeGuess;

    private String description;

    @Column(name = "quality_grade", nullable = false)
    private String qualityGrade = "NEEDS_ID";

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();
}
