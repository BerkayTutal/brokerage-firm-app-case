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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private static final String ROLE_CUSTOMER = "CUSTOMER";
    private final RoleService roleService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AssetService assetService;


    @Override
    public Customer register(Customer customer) {

        CustomerEntity customerEntity = CustomerEntity.builder()
                .email(customer.getEmail())
                .password(passwordEncoder.encode(customer.getPassword()))
                .roles(Set.of(RoleMapper.toEntity(roleService.get(ROLE_CUSTOMER))))
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
        return customerRepository.findCustomerByEmail(email)
                .map(CustomerMapper::toModel)
                .orElseThrow(() -> new NotFoundException(Error.CUSTOMER_NOT_FOUND_EMAIL, email));
    }

    @Override
    public Customer get(Long id) {
        return customerRepository.findById(id)
                .map(CustomerMapper::toModel)
                .orElseThrow(() -> new NotFoundException(Error.CUSTOMER_NOT_FOUND_ID, id));
    }

    @Transactional
    @Override
    public Customer updateEmailPassword(Customer customer) {

        CustomerEntity customerEntity = customerRepository.findById(customer.getId()).orElseThrow(() -> new NotFoundException(Error.CUSTOMER_NOT_FOUND_ID, customer.getId()));
        customerEntity.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerEntity.setEmail(customer.getEmail());
        customerEntity = customerRepository.save(customerEntity);

        return CustomerMapper.toModel(customerEntity);
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll().stream().map(CustomerMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public boolean exists(Long id) {
        return customerRepository.existsById(id);
    }

}
