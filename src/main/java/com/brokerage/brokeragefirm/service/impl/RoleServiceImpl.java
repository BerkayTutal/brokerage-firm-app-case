package com.brokerage.brokeragefirm.service.impl;

import com.brokerage.brokeragefirm.common.mapper.RoleMapper;
import com.brokerage.brokeragefirm.repository.RoleRepository;
import com.brokerage.brokeragefirm.service.RoleService;
import com.brokerage.brokeragefirm.service.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role get(String name) {
        log.info("Fetching role with name: {}", name);
        return RoleMapper.toModel(roleRepository.getByName(name));
    }
}
