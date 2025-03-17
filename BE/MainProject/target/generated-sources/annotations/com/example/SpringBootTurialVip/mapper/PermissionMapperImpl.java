package com.example.SpringBootTurialVip.mapper;

import com.example.SpringBootTurialVip.dto.request.PermissionRequest;
import com.example.SpringBootTurialVip.dto.response.PermissionResponse;
import com.example.SpringBootTurialVip.entity.Permission;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-18T02:00:35+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class PermissionMapperImpl implements PermissionMapper {

    @Override
    public Permission toPermission(PermissionRequest request) {
        if ( request == null ) {
            return null;
        }

        Permission.PermissionBuilder permission = Permission.builder();

        permission.name( request.getName() );
        permission.description( request.getDescription() );

        return permission.build();
    }

    @Override
    public PermissionResponse toPermissionResponse(Permission permission) {
        if ( permission == null ) {
            return null;
        }

        PermissionResponse.PermissionResponseBuilder permissionResponse = PermissionResponse.builder();

        permissionResponse.name( permission.getName() );
        permissionResponse.description( permission.getDescription() );

        return permissionResponse.build();
    }
}
