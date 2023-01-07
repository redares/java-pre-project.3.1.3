package org.example.pp.repository;

import org.example.pp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAll();
    Role findRoleByName(String name);
}
