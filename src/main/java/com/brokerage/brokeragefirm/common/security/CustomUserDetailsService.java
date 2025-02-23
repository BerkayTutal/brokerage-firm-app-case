package com.brokerage.brokeragefirm.common.security;

import com.brokerage.brokeragefirm.service.CustomerService;
import com.brokerage.brokeragefirm.service.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private CustomerService customerService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerService.getCustomerByEmail(email);

        return new CustomUserDetails(customer);
    }

    @Autowired
    public void setCustomerService(@Lazy CustomerService customerService) {
        this.customerService = customerService;
    }
}