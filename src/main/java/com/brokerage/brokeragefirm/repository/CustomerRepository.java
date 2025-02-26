package com.brokerage.brokeragefirm.repository;

import com.brokerage.brokeragefirm.repository.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Optional<CustomerEntity> findCustomerByEmail(String email);

    boolean existsById(Long id);

    Page<CustomerEntity> findAll(Pageable pageable);

}
