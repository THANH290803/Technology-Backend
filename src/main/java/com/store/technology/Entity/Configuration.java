package com.store.technology.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "configurations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    // Không unique
    @Column(nullable = false)
    private String name;

    // Cho phép null
    @Column(nullable = true)
    private String description;

    @OneToMany(mappedBy = "configuration", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"configuration"})
    private List<Specification> specifications;

    // Xoá mềm: lưu thời điểm bị xoá
    @Column(name = "deleted_at")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime deletedAt;
}
