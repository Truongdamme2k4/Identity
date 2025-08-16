package com.identity.mapper;

import com.identity.dto.request.UserCreationRequest;
import com.identity.dto.request.UserUpdateRequest;
import com.identity.dto.response.UserResponse;
import com.identity.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toUserEntity(UserCreationRequest userCreationRequest);
    UserResponse toUserResponse(UserEntity userEntity);
    void updateUser(@MappingTarget UserEntity userEntity, UserUpdateRequest userUpdateRequest);
}
