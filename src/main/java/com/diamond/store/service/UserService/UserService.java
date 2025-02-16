package com.diamond.store.service.UserService;

import com.diamond.store.dto.request.FileRequest;
import com.diamond.store.dto.request.UserRequest;
import com.diamond.store.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    //User là account + profile
    void addUser(UserRequest userRequest);

    //User id là account id or profile id
    void updateUser(String userId, UserRequest userRequest);

    void deleteUser(String userId);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(String userId);

    UserResponse getUserByEmail(String email);

    UserResponse getUserByUsername(String username);

    void uploadAvatar(FileRequest fileRequest);

    /// Client method


}
