package com.identity.service.impl;

import com.identity.dto.request.UserCreationRequest;
import com.identity.dto.request.UserUpdateRequest;
import com.identity.dto.response.UserResponse;
import com.identity.entity.UserEntity;
import com.identity.exception.AppException;
import com.identity.exception.ErrorCode;
import com.identity.mapper.UserMapper;
import com.identity.repository.UserRepository;
import com.identity.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class UserService implements IUserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public UserResponse createUser(UserCreationRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(
                userRepository.save(
                        userMapper.toUserEntity(request)
                )
        );
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserResponse> userResponses = userEntities.stream().map(userMapper::toUserResponse).toList();
        return userResponses;
    }

    @Override
    public UserResponse getUserById(String id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.toUserResponse(userEntity);
    }

    @Override
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        userMapper.updateUser(userEntity, request);
        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    @Override
    public void deleteUser(String id) {
        userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.deleteById(id);
    }
}
