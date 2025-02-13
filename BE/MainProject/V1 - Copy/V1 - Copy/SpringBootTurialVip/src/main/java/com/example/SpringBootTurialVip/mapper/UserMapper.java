package com.example.SpringBootTurialVip.mapper;

import com.example.SpringBootTurialVip.dto.request.UserCreationRequest;
import com.example.SpringBootTurialVip.dto.request.UserUpdateRequest;
import com.example.SpringBootTurialVip.dto.response.UserResponse;
import com.example.SpringBootTurialVip.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")//Báo cho map structure là sẽ generate Mapper này sử dụng trong spring
//Theo kiểu Dependency Injection
public interface UserMapper {
    //Method này nhận vế 1 param request theo kiểu UserCreationRequest và trả về class User
    User toUser(UserCreationRequest request);
    //=> Có thể @Autowired vào service để sử dụng , VD bên UserService

    //Map data từ request dạng UserUpdateRequest vào user
    @Mapping(target = "roles",ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    //@Mapping(source = "username",target = "fullname")//map từ source vào target
    //@Mapping(target = "username",ignore = true)//ko map target này
    UserResponse toUserResponse(User user);
}
