package com.store.technology.Repository;

import com.store.technology.Entity.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecificationRepository extends JpaRepository<Specification, Long> {

    @Query(value = "SELECT * FROM specifications", nativeQuery = true)
    List<Specification> findAllIncludingDeleted();

    @Query(value = "SELECT * FROM specifications WHERE id = ?1", nativeQuery = true)
    Optional<Specification> findByIdIncludingDeleted(Long id);
}
