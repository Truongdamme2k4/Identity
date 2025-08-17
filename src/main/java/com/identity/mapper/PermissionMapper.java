package com.identity.mapper;

import com.identity.dto.request.PermissionRequest;
import com.identity.dto.response.PermissionResponse;
import com.identity.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
