package com.store.technology.Repository;

import com.store.technology.Entity.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpecificationRepository extends JpaRepository<Specification, Long> {
    // ✅ Lấy tất cả (kể cả đã xoá)
    @Query("SELECT specification FROM Specification specification")
    List<Specification> findAllIncludingDeleted();

    // ✅ Lấy tất cả chưa xoá
    @Query("SELECT specification FROM Specification specification WHERE specification.deletedAt IS NULL")
    List<Specification> findAllNotDeleted();

    // ✅ Lấy 1 (kể cả đã xoá)
    @Query("SELECT specification FROM Specification specification WHERE specification.id = :id")
    Specification findAnyById(Long id);

    // ✅ Lấy 1 (chưa xoá)
    @Query("SELECT specification FROM Specification specification WHERE specification.id = :id AND specification.deletedAt IS NULL")
    Specification findNotDeletedById(Long id);

    // 🟢 Lấy tất cả Specification theo configurationId (không bị xóa)
    @Query("SELECT s FROM Specification s WHERE s.configuration.id = :configurationId AND s.deletedAt IS NULL")
    List<Specification> findByConfigurationId(Long configurationId);

    // 🟣 (Tuỳ chọn) Bao gồm cả những cái đã bị xóa mềm
    @Query("SELECT s FROM Specification s WHERE s.configuration.id = :configurationId")
    List<Specification> findAllByConfigurationIdIncludingDeleted(Long configurationId);
}
