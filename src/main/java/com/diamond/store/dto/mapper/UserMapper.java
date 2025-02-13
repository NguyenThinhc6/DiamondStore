package com.diamond.store.dto.mapper;

import com.diamond.store.dto.response.UserResponse;
import com.diamond.store.model.Account;
import com.diamond.store.model.Profile;

public class UserMapper {

    public static UserResponse toUserResponse(Account account, Profile profile) {

        return UserResponse.builder()
                .userId(account.getAccountId())
                .email(profile.getEmail())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .phone(profile.getPhone())
                .username(account.getUsername())
                .avatar(profile.getProfileImage() != null ? profile.getProfileImage().getImageUrl() : "default")
                .build();
    }
}
