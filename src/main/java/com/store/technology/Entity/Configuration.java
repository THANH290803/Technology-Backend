package com.store.technology.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "configurations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SQLDelete(sql = "UPDATE configurations SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String configKey;

    @Column(nullable = false)
    private String configValue;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
