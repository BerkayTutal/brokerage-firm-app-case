package com.brokerage.brokeragefirm.service.impl;

import com.brokerage.brokeragefirm.common.mapper.RoleMapper;
import com.brokerage.brokeragefirm.repository.RoleRepository;
import com.brokerage.brokeragefirm.service.RoleService;
import com.brokerage.brokeragefirm.service.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByName(String name) {
        return RoleMapper.toModel(roleRepository.getByName(name));
    }
}
