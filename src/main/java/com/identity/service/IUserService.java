package com.identity.service;

import com.identity.dto.request.UserCreationRequest;
import com.identity.dto.request.UserUpdateRequest;
import com.identity.dto.response.UserResponse;

import java.util.List;

public interface IUserService {
    UserResponse createUser(UserCreationRequest request);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(String id);
    UserResponse updateUser(String id, UserUpdateRequest request);
    void deleteUser(String id);
}
