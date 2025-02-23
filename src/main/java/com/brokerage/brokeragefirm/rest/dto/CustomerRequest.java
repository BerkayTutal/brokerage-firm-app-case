package com.brokerage.brokeragefirm.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String password;

}
