package com.brokerage.brokeragefirm.common.security;

import com.brokerage.brokeragefirm.service.model.Customer;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    @Getter
    private final Customer customer;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(Customer customer) {
        this.customer = customer;
        this.authorities = new ArrayList<>();
        customer.getRoles().forEach(item -> authorities.add(new SimpleGrantedAuthority("ROLE_" + item.getName())));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    public String getUsername() {
        return customer.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isAdmin() {
        return authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
