package com.naturegram.api.observation;

import com.naturegram.api.auth.UserAccount;
import com.naturegram.api.auth.UserAccountRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/observations")
public class ObservationController {

    private static final Set<String> VISIBILITIES = Set.of("public", "limited", "private");

    private final JdbcTemplate jdbc;
    private final UserAccountRepository users;

    public ObservationController(JdbcTemplate jdbc, UserAccountRepository users) {
        this.jdbc = jdbc;
        this.users = users;
    }

    @PostMapping
    public ResponseEntity<ObservationResponse> create(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody CreateObservationRequest request) {
        UserAccount owner = currentUser(principal);

        if (request.taxonId() != null && !taxonExists(request.taxonId())) {
            throw new IllegalArgumentException("Unknown taxon.");
        }

        String visibility = request.visibility() == null ? "private" : request.visibility();
        if (!VISIBILITIES.contains(visibility)) {
            throw new IllegalArgumentException("Invalid visibility.");
        }

        UUID id = UUID.randomUUID();
        jdbc.update("""
                INSERT INTO observations
                    (id, observer_id, taxon_id, title, description, observed_at, visibility)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """,
                id,
                owner.getId(),
                request.taxonId(),
                request.title(),
                request.description(),
                Timestamp.from(request.observedAt()),
                visibility);

        ObservationResponse response = findOwned(id, owner.getId())
                .orElseThrow(() -> new IllegalStateException("Created observation could not be read."));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<ObservationResponse> listMine(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        if (limit < 1 || limit > 100 || offset < 0) {
            throw new IllegalArgumentException("Invalid pagination values.");
        }

        UUID ownerId = currentUser(principal).getId();
        return jdbc.query("""
                SELECT id, observer_id, taxon_id, title, description,
                       observed_at, visibility, created_at, updated_at
                FROM observations
                WHERE observer_id = ?
                ORDER BY created_at DESC, id DESC
                LIMIT ? OFFSET ?
                """,
                (rs, rowNum) -> mapObservation(rs.getObject("id", UUID.class), rs),
                ownerId, limit, offset);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObservationResponse> getMine(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable UUID id) {
        UUID ownerId = currentUser(principal).getId();
        return findOwned(id, ownerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private UserAccount currentUser(UserDetails principal) {
        return users.findByUsernameIgnoreCase(principal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Authenticated account was not found."));
    }

    private boolean taxonExists(UUID taxonId) {
        Boolean exists = jdbc.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM taxa WHERE id = ?)",
                Boolean.class,
                taxonId);
        return Boolean.TRUE.equals(exists);
    }

    private java.util.Optional<ObservationResponse> findOwned(UUID id, UUID ownerId) {
        List<ObservationResponse> rows = jdbc.query("""
                SELECT id, observer_id, taxon_id, title, description,
                       observed_at, visibility, created_at, updated_at
                FROM observations
                WHERE id = ? AND observer_id = ?
                """,
                (rs, rowNum) -> mapObservation(id, rs),
                id, ownerId);
        return rows.stream().findFirst();
    }

    private ObservationResponse mapObservation(UUID id, java.sql.ResultSet rs)
            throws java.sql.SQLException {
        return new ObservationResponse(
                id,
                rs.getObject("observer_id", UUID.class),
                rs.getObject("taxon_id", UUID.class),
                rs.getString("title"),
                rs.getString("description"),
                rs.getTimestamp("observed_at").toInstant(),
                rs.getString("visibility"),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant());
    }

    public record CreateObservationRequest(
            @NotNull Instant observedAt,
            @Size(max = 200) String title,
            @Size(max = 5000) String description,
            UUID taxonId,
            String visibility) {
    }

    public record ObservationResponse(
            UUID id,
            UUID observerId,
            UUID taxonId,
            String title,
            String description,
            Instant observedAt,
            String visibility,
            Instant createdAt,
            Instant updatedAt) {
    }
}