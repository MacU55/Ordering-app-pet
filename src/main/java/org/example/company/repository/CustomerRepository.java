package org.example.company.repository;

import java.util.Optional;
import org.example.company.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);
    Optional<Long> findIdByEmail(String email);

    Optional<Customer> findByUserName(String userName);

    boolean existsByEmail(String email);
    Optional<Long> findIdByUserName(String userName);
}
