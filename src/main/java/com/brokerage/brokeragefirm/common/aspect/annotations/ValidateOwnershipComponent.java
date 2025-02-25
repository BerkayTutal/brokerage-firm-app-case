package com.brokerage.brokeragefirm.common.aspect.annotations;

import com.brokerage.brokeragefirm.common.enums.Error;
import com.brokerage.brokeragefirm.common.exception.NotFoundException;
import com.brokerage.brokeragefirm.common.exception.PermissionException;
import com.brokerage.brokeragefirm.common.security.CustomUserDetails;
import com.brokerage.brokeragefirm.rest.dto.OrderRequest;
import com.brokerage.brokeragefirm.service.AssetService;
import com.brokerage.brokeragefirm.service.CustomerService;
import com.brokerage.brokeragefirm.service.OrderService;
import com.brokerage.brokeragefirm.service.model.Asset;
import com.brokerage.brokeragefirm.service.model.Order;
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
    private final OrderService orderService;

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

    @Around("@annotation(validateOwnershipAsset)")
    public Object validate(ProceedingJoinPoint joinPoint, ValidateOwnershipAsset validateOwnershipAsset) throws Throwable {
        final Object[] args = joinPoint.getArgs();

        if (args != null && args[0] != null) {
            final var customUserDetails = ((CustomUserDetails) args[0]);
            final var assetId = ((Long) args[1]);

            checkOwnershipAsset(customUserDetails, assetId);
        }
        return joinPoint.proceed();
    }


    @Around("@annotation(validateOwnershipOrder)")
    public Object validate(ProceedingJoinPoint joinPoint, ValidateOwnershipOrder validateOwnershipOrder) throws Throwable {
        final Object[] args = joinPoint.getArgs();

        if (args != null && args[0] != null) {
            final var customUserDetails = ((CustomUserDetails) args[0]);
            if (args[1] instanceof Long) {
                final var orderId = ((Long) args[1]);
                checkOwnershipOrder(customUserDetails, orderId);
            }
            else if(args[1] instanceof OrderRequest) {
                final var order = ((OrderRequest) args[1]);
                checkOwnershipUser(customUserDetails, order.customerId());
            }
        }
        return joinPoint.proceed();
    }

    private void checkCustomerExists(Long userId) {
        if (!customerService.exists(userId)) {
            throw new NotFoundException(Error.CUSTOMER_NOT_FOUND_ID, userId);
        }
    }

    private void checkOwnershipUser(CustomUserDetails loggedUser, Long customerId) {
        checkCustomerExists(customerId);
        if (!loggedUser.isAdmin() && (customerId != null && !loggedUser.getCustomer().getId().equals(customerId))) {
            throw new PermissionException(Error.NO_PERMISSION_CUSTOMER);
        }
    }

    private void checkOwnershipAsset(CustomUserDetails loggedUser, Long assetId) {
        Asset asset = assetService.get(assetId);
        if (!loggedUser.isAdmin() && (assetId != null && !loggedUser.getCustomer().getId().equals(asset.getCustomerId()))) {
            throw new PermissionException(Error.NO_PERMISSION_ASSET);
        }
    }

    private void checkOwnershipOrder(CustomUserDetails loggedUser, Long orderId) {
        Order order = orderService.get(orderId);
        if (!loggedUser.isAdmin() && (orderId != null && !loggedUser.getCustomer().getId().equals(order.getCustomerId()))) {
            throw new PermissionException(Error.NO_PERMISSION_ORDER);
        }
    }
}