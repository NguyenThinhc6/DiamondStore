package com.diamond.store.service.UserService;

import com.diamond.store.dto.mapper.UserMapper;
import com.diamond.store.dto.request.FileRequest;
import com.diamond.store.dto.request.UserRequest;
import com.diamond.store.dto.response.FileResponse;
import com.diamond.store.dto.response.UserResponse;
import com.diamond.store.exception.ResourceConflictException;
import com.diamond.store.exception.ResourceNotFoundException;
import com.diamond.store.model.Account;
import com.diamond.store.model.Image;
import com.diamond.store.model.Profile;
import com.diamond.store.repository.AccountRepository;
import com.diamond.store.repository.ImageRepository;
import com.diamond.store.repository.ProfileRepository;
import com.diamond.store.service.UploadService.UploadService;
import com.diamond.store.util.ApplicationMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final ImageRepository imageRepository;
    private final UploadService uploadService;

    @Transactional
    @Override
    public void addUser(UserRequest userRequest) {
        log.info("Create user: {}", userRequest);

        accountRepository.findByUsername(userRequest.getUsername())
                .ifPresentOrElse(account -> {
                    throw new ResourceConflictException(ApplicationMessage.USER_CONFLICT);
                }, () -> {
                    Account newAccount = Account.builder()
                            .username(userRequest.getUsername())
                            .password(userRequest.getPassword())
                            .build();
                    accountRepository.save(newAccount);//vì có transaction nên lưu trước để nó quản lý

                    Profile newProfile = Profile.builder()
                            .account(newAccount)
                            .email(userRequest.getEmail())
                            .firstName(userRequest.getFirstName())
                            .lastName(userRequest.getLastName())
                            .phone(userRequest.getPhone())
                            .build();

                    newAccount.setProfile(newProfile);//khi set profile thì account có sự thay đổi nên đc transaction nó đánh dấu và cập nhật sau kkhi kêt thúc

                    log.info("Create user success!");
                });
    }

    @Transactional
    @Override
    public void updateUser(String userId, UserRequest userRequest) {
        log.info("Update user: {}", userRequest);

        accountRepository.findById(userId)
                .ifPresentOrElse(account ->
                {
                    account.setUsername(userRequest.getUsername());
                    account.setPassword(userRequest.getPassword());

                    // Kiểm tra nếu Profile đã tồn tại thì update, nếu chưa thì tạo mới
                    Profile profile = account.getProfile();
                    if (profile == null) {
                        profile = new Profile();
                        profile.setAccount(account);
                    }

                    profile.setEmail(userRequest.getEmail());
                    profile.setFirstName(userRequest.getFirstName());
                    profile.setLastName(userRequest.getLastName());
                    profile.setPhone(userRequest.getPhone());

                    // Không cần gọi save() riêng, vì @Transactional sẽ tự cập nhật
                    log.info("Update user success!");

                }, () -> {
                    throw new ResourceNotFoundException(ApplicationMessage.USER_NOTFOUND);
                });
    }

    @Override
    public void deleteUser(String userId) {
        log.info("Delete user: {}", userId);

        accountRepository.findById(userId)
                .ifPresentOrElse(account -> {
                    accountRepository.delete(account);
                    log.info("Delete user success");
                }, () -> {
                    throw new ResourceNotFoundException(ApplicationMessage.USER_NOTFOUND);
                });
    }

    @Override
    public List<UserResponse> getAllUsers() {
        log.info("Get all users");
        return accountRepository.findAll().stream().map(account -> UserMapper.toUserResponse(account, account.getProfile())).collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(String userId) {
        log.info("Get user by id: {}", userId);

        Account account = accountRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(ApplicationMessage.USER_NOTFOUND));
        Profile profile = profileRepository.findById(account.getAccountId()).orElseThrow(() -> new ResourceNotFoundException(ApplicationMessage.USER_NOTFOUND));
        return UserMapper.toUserResponse(account, profile);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        log.info("Get user by email: {}", email);

        Profile profile = profileRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(ApplicationMessage.USER_NOTFOUND));
        Account account = accountRepository.findById(profile.getProfileId()).orElseThrow(() -> new ResourceNotFoundException(ApplicationMessage.USER_NOTFOUND));

        return UserMapper.toUserResponse(account, profile);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        log.info("Get user by username: {}", username);

        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(ApplicationMessage.USER_NOTFOUND));
        Profile profile = profileRepository.findById(account.getAccountId()).orElseThrow(() -> new ResourceNotFoundException(ApplicationMessage.USER_NOTFOUND));

        return UserMapper.toUserResponse(account, profile);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void uploadAvatar(FileRequest fileRequest) {

        profileRepository.findById(fileRequest.getOwnerId()).ifPresentOrElse(
                p -> {



                    FileResponse fileUpload = uploadService.uploadAvatar(fileRequest);
//                    if(imageRepository.findById(fileRequest.getOwnerId()).isPresent()) {
//                        Image imageToUpdate = imageRepository.findById(fileRequest.getOwnerId()).get();
//                        imageToUpdate.setImageUrl(fileUpload.getUrl());
//                        imageToUpdate.setTag(String.join(",", fileRequest.getTags()));
//                        // Bạn có thể cập nhật các trường khác nếu cần
//
//                        imageRepository.save(imageToUpdate);
//                    }
//
//                    Image image = Image.builder()
//                            .imageId(fileUpload.getFileId())
//                            .imageUrl(fileUpload.getUrl())
//                            .tag(String.join(",", fileRequest.getTags()))
//                            .build();
//
//                    p.setProfileImage(image);

                    imageRepository.findById(fileRequest.getOwnerId())
                            .ifPresentOrElse(existingImage -> {
                                // Cập nhật image đã tồn tại
                                existingImage.setImageUrl(fileUpload.getUrl());
                                existingImage.setTag(String.join(",", fileRequest.getTags()));
                                imageRepository.save(existingImage);
                            },() -> {
                                // Tạo mới image nếu chưa tồn tại
                                Image newImage = Image.builder()
                                        .imageId(fileUpload.getFileId())
                                        .imageUrl(fileUpload.getUrl())
                                        .tag(String.join(",", fileRequest.getTags()))
                                        .build();
                                p.setProfileImage(newImage);
                            });
                },
                () -> {
                    throw new ResourceNotFoundException(ApplicationMessage.USER_NOTFOUND);
                }
        );
    }
}
