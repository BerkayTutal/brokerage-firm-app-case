package com.brokerage.brokeragefirm.common.aspect.annotations;

import com.brokerage.brokeragefirm.common.enums.Error;
import com.brokerage.brokeragefirm.common.exception.NotFoundException;
import com.brokerage.brokeragefirm.common.exception.PermissionException;
import com.brokerage.brokeragefirm.common.security.CustomUserDetails;
import com.brokerage.brokeragefirm.service.AssetService;
import com.brokerage.brokeragefirm.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ValidateOwnershipComponent {

    private final CustomerService customerService;
    private final AssetService assetService;

    @Around("@annotation(validateOwnershipCustomer)")
    public Object validate(ProceedingJoinPoint joinPoint, ValidateOwnershipCustomer validateOwnershipCustomer) throws Throwable {
        final Object[] args = joinPoint.getArgs();

        if (args != null && args[0] != null) {
            final var customUserDetails = ((CustomUserDetails) args[0]);
            final var userId = ((Long) args[1]);

            checkOwnershipUser(customUserDetails, userId);
        }
        return joinPoint.proceed();
    }

    private void checkCustomerExists(Long userId) {
        if (!customerService.existsById(userId)) {
            throw new NotFoundException(Error.CUSTOMER_NOT_FOUND_ID, userId);
        }
    }

    private void checkOwnershipUser(CustomUserDetails loggedUser, Long customerId) {
        checkCustomerExists(customerId);
        if (!loggedUser.isAdmin() && (customerId != null && !loggedUser.getCustomer().getId().equals(customerId))) {
            throw new PermissionException(Error.NO_PERMISSION_CUSTOMER);
        }
    }
}