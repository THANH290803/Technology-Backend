package com.store.technology.Repository;

import com.store.technology.Entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    @Query(value = "SELECT * FROM configurations", nativeQuery = true)
    List<Configuration> findAllIncludingDeleted();

    @Query(value = "SELECT * FROM configurations WHERE id = ?1", nativeQuery = true)
    Optional<Configuration> findByIdIncludingDeleted(Long id);
}
