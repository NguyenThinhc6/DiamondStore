package com.diamond.store.controller;

import com.diamond.store.dto.request.FileRequest;
import com.diamond.store.dto.request.UserRequest;
import com.diamond.store.dto.response.ApiResponse;
import com.diamond.store.dto.response.UserResponse;
import com.diamond.store.service.UploadService.UploadService;
import com.diamond.store.service.UserService.UserService;
import com.diamond.store.util.ApplicationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {

        return ApiResponse.<List<UserResponse>>builder()
                .message(ApplicationMessage.GET_DATA_SUCCESS)
                .data(userService.getAllUsers())
                .code(HttpStatus.OK.value())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable("id") String id) {

        return ApiResponse.<UserResponse>builder()
                .message(ApplicationMessage.GET_DATA_SUCCESS)
                .code(HttpStatus.OK.value())
                .data(userService.getUserById(id))
                .build();
    }

    @GetMapping("/find")
    public ApiResponse<UserResponse> getUserByUsername(@RequestParam(value = "username", required = false) String username, @RequestParam(value = "email", required = false) String email) {

        UserResponse userResponse = username != null ? userService.getUserByUsername(username) : userService.getUserByEmail(email);

        return ApiResponse.<UserResponse>builder()
                .message(ApplicationMessage.GET_DATA_SUCCESS)
                .code(HttpStatus.OK.value())
                .data(userResponse)
                .build();
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createUser(@RequestBody UserRequest userRequest) {

        userService.addUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<Void>builder()
                .code(HttpStatus.CREATED.value())
                .message(ApplicationMessage.CREATE_DATA_SUCCESS)
                .build());
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable("id") String id) {

        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<Void>builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message(ApplicationMessage.DELETE_DATA_SUCCESS)
                .build());
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse<Void>> updateUser(@PathVariable("id") String id, @RequestBody UserRequest userRequest) {

        userService.updateUser(id, userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(ApplicationMessage.UPDATE_DATA_SUCCESS)
                .build());
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Void>> uploadUser(@ModelAttribute FileRequest fileRequest) {

        userService.uploadAvatar(fileRequest);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(ApplicationMessage.UPLOAD_FILE_SUCCESS)
                .build());

    }

}
