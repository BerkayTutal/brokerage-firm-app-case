package com.brokerage.brokeragefirm.rest;

import com.brokerage.brokeragefirm.common.enums.Error;
import com.brokerage.brokeragefirm.common.exception.PermissionException;
import com.brokerage.brokeragefirm.common.mapper.CustomerRequestMapper;
import com.brokerage.brokeragefirm.common.mapper.CustomerResponseMapper;
import com.brokerage.brokeragefirm.common.security.CustomUserDetails;
import com.brokerage.brokeragefirm.common.security.auth.JwtUtil;
import com.brokerage.brokeragefirm.rest.dto.AuthResponse;
import com.brokerage.brokeragefirm.rest.dto.CustomerRequest;
import com.brokerage.brokeragefirm.rest.dto.CustomerResponse;
import com.brokerage.brokeragefirm.service.CustomerService;
import com.brokerage.brokeragefirm.service.model.Customer;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
@RequiredArgsConstructor
public class AuthRestController {

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final CustomerService customerService;


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@RequestBody @Valid CustomerRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new PermissionException(Error.INCORRECT_EMAIL_PASSWORD);
        }
        final CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String token = jwtUtil.generateToken(userDetails);

        return AuthResponse.builder().userId(userDetails.getCustomer().getId()).token(token).build();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse register(@RequestBody @Valid CustomerRequest customerRequest) {
        Customer customer = customerService.registerCustomer(CustomerRequestMapper.toModel(customerRequest, null));
        return CustomerResponseMapper.toResponse(customer);
    }
}
