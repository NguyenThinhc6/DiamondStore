package com.diamond.store.service.RoleService;

import com.diamond.store.dto.request.RoleRequest;
import com.diamond.store.exception.ResourceConflictException;
import com.diamond.store.exception.ResourceNotFoundException;
import com.diamond.store.model.Role;
import com.diamond.store.repository.RoleRepository;
import com.diamond.store.util.ApplicationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        log.info("Get all roles");
        return roleRepository.findAll();
    }

    @Override
    public Role findById(int id) {
        log.info("Get role by id: {}", id);


        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ApplicationMessage.ROLE_NOTFOUND));

    }

    @Override
    public void saveRole(RoleRequest roleRequest) {
        log.info("Save role: {}", roleRequest);

        roleRepository.findByRoleName(roleRequest.getRoleName()).ifPresentOrElse(role -> {
            throw new ResourceConflictException(ApplicationMessage.ROLE_CONFLICT);
        }, () -> {
            Role newRole = Role.builder()
                    .description(roleRequest.getDescription())
                    .roleName(roleRequest.getRoleName()).build();
            roleRepository.save(newRole);

            log.info("Save role success: {}", newRole);
        });
    }

    @Override
    public void deleteRole(int id) {
        log.info("Delete role by id: {}", id);

        roleRepository.findById(id).ifPresentOrElse(role -> {
            roleRepository.delete(role);
            log.info("Delete role success: {}", id);
        }, () -> {
            throw new ResourceNotFoundException(ApplicationMessage.ROLE_NOTFOUND);
        });
    }

    @Override
    public void updateRole(int roleId, RoleRequest roleRequest) {
        log.info("Update role: {}", roleRequest);

        roleRepository.findById(roleId).ifPresentOrElse(role -> {
            Role roleUpdate = Role.builder()
                    .id(roleId)
                    .roleName(roleRequest.getRoleName())
                    .description(roleRequest.getDescription())
                    .build();

            roleRepository.save(roleUpdate);
            log.info("Update role success: {}", roleUpdate);
        }, () -> {
            throw new ResourceNotFoundException(ApplicationMessage.ROLE_NOTFOUND);
        });
    }
}
