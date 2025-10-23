package com.store.technology.Repository;

import com.store.technology.Entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    @Query(value = "SELECT * FROM payment_methods", nativeQuery = true)
    List<PaymentMethod> findAllIncludingDeleted();

    @Query(value = "SELECT * FROM payment_methods WHERE id = ?1", nativeQuery = true)
    Optional<PaymentMethod> findByIdIncludingDeleted(Long id);
}
