package com.diamond.store.service.RoleService;

import com.diamond.store.dto.request.RoleRequest;
import com.diamond.store.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();
    Role findById(int id);
    void saveRole(RoleRequest roleRequest);
    void deleteRole(int id);
    void updateRole(int role, RoleRequest roleRequest);
}
