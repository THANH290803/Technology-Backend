package com.store.technology.Entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// Khi gọi entityManager.remove() hoặc repo.delete(entity) -> sẽ chạy UPDATE thay vì DELETE
@SQLDelete(sql = "UPDATE brands SET deleted_at = NOW() WHERE id = ?")
// Mặc định các truy vấn sẽ thêm điều kiện "deleted_at IS NULL"
@Where(clause = "deleted_at IS NULL")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    // Trường soft-delete: null => chưa xoá, có giá trị => đã xoá
    @Column(name = "deleted_at")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime deletedAt;
}
