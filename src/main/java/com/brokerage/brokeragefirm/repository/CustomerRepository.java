package com.brokerage.brokeragefirm.repository;

import com.brokerage.brokeragefirm.repository.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository {

    Optional<CustomerEntity> findCustomerByEmail(String email);

    boolean existsById(Long id);

}
