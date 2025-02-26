package com.brokerage.brokeragefirm.service.impl;

import com.brokerage.brokeragefirm.common.constants.Constants;
import com.brokerage.brokeragefirm.common.enums.Error;
import com.brokerage.brokeragefirm.common.exception.DuplicateEntryException;
import com.brokerage.brokeragefirm.common.exception.NotFoundException;
import com.brokerage.brokeragefirm.common.mapper.CustomerMapper;
import com.brokerage.brokeragefirm.common.mapper.RoleMapper;
import com.brokerage.brokeragefirm.repository.CustomerRepository;
import com.brokerage.brokeragefirm.repository.entity.CustomerEntity;
import com.brokerage.brokeragefirm.service.AssetService;
import com.brokerage.brokeragefirm.service.CustomerService;
import com.brokerage.brokeragefirm.service.RoleService;
import com.brokerage.brokeragefirm.service.model.Asset;
import com.brokerage.brokeragefirm.service.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final RoleService roleService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AssetService assetService;


    @Override
    public Customer register(Customer customer) {
        log.info("Registering customer: {}", customer);
        CustomerEntity customerEntity = CustomerEntity.builder()
                .email(customer.getEmail())
                .password(passwordEncoder.encode(customer.getPassword()))
                .roles(Set.of(RoleMapper.toEntity(roleService.get(Constants.ROLE_CUSTOMER))))
                .build();

        try {
            customerEntity = customerRepository.save(customerEntity);
            assetService.create(Asset.builder().customerId(customerEntity.getId()).assetName(Constants.ASSET_TRY).build());
        } catch (Exception e) {
            throw new DuplicateEntryException(Error.EMAIL_ALREADY_EXISTS, customer.getEmail());
        }
        return CustomerMapper.toModel(customerEntity);
    }

    @Override
    public Customer get(String email) {
        log.info("Fetching customer with email: {}", email);
        return customerRepository.findCustomerByEmail(email)
                .map(CustomerMapper::toModel)
                .orElseThrow(() -> new NotFoundException(Error.CUSTOMER_NOT_FOUND_EMAIL, email));
    }

    @Override
    public Customer get(Long id) {
        log.info("Fetching customer with ID: {}", id);
        return customerRepository.findById(id)
                .map(CustomerMapper::toModel)
                .orElseThrow(() -> new NotFoundException(Error.CUSTOMER_NOT_FOUND_ID, id));
    }

    @Transactional
    @Override
    public Customer updateEmailPassword(Customer customer) {
        log.info("Updating customer: {}", customer);
        CustomerEntity customerEntity = customerRepository.findById(customer.getId()).orElseThrow(() -> new NotFoundException(Error.CUSTOMER_NOT_FOUND_ID, customer.getId()));
        customerEntity.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerEntity.setEmail(customer.getEmail());
        customerEntity = customerRepository.save(customerEntity);

        return CustomerMapper.toModel(customerEntity);
    }

    @Override
    public Page<Customer> getAll(Pageable pageable) {
        return customerRepository.findAll(pageable).map(CustomerMapper::toModel);
    }

    @Override
    public boolean exists(Long id) {
        return customerRepository.existsById(id);
    }

}
