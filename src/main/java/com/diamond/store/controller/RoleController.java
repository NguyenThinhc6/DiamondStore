package com.diamond.store.controller;

import com.diamond.store.dto.request.RoleRequest;
import com.diamond.store.dto.response.ApiResponse;
import com.diamond.store.model.Role;
import com.diamond.store.service.RoleService.RoleService;
import com.diamond.store.util.ApplicationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ApiResponse<List<Role>> getAllRoles() {

        return ApiResponse.<List<Role>>builder()
                .code(HttpStatus.OK.value())
                .message(ApplicationMessage.GET_DATA_SUCCESS)
                .data(roleService.findAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Role> getRoleById(@PathVariable Integer id) {

        return ApiResponse.<Role>builder()
                .code(HttpStatus.OK.value())
                .message(ApplicationMessage.GET_DATA_SUCCESS)
                .data(roleService.findById(id))
                .build();
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createRole(@RequestBody RoleRequest roleRequest) {

        roleService.saveRole(roleRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Void>builder()
                        .code(HttpStatus.CREATED.value())
                        .message(ApplicationMessage.CREATE_DATA_SUCCESS)
                        .build());
    }

    @DeleteMapping("/{id}/delete")
    public ApiResponse<Void> deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(ApplicationMessage.DELETE_DATA_SUCCESS)
                .build();
    }

    @PutMapping("/{id}/update")
    public ApiResponse<Void> updateRole(@PathVariable Integer id, @RequestBody RoleRequest roleRequest) {
        roleService.updateRole(id, roleRequest);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(ApplicationMessage.UPDATE_DATA_SUCCESS)
                .build();
    }
}
