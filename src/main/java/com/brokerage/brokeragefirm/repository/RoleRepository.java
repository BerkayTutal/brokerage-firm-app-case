package com.brokerage.brokeragefirm.repository;

import com.brokerage.brokeragefirm.repository.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity getByName(String name);
}
