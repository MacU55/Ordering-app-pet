package org.example.company.repository;

import java.util.Optional;
import org.example.company.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByUserName(String userName);

    boolean existsByEmail(String email);
}
